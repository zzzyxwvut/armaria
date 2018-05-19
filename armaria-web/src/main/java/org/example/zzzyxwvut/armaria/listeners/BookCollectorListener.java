package org.example.zzzyxwvut.armaria.listeners;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.zzzyxwvut.armaria.beans.BookBean;
import org.example.zzzyxwvut.armaria.beans.LoanBean;
import org.example.zzzyxwvut.armaria.beans.TicketBean;
import org.example.zzzyxwvut.armaria.domain.naming.Constants.BOOKS;
import org.example.zzzyxwvut.armaria.domain.naming.Constants.LOANS;
import org.example.zzzyxwvut.armaria.events.MaturedTicketEvent;
import org.example.zzzyxwvut.armaria.service.BookService;
import org.example.zzzyxwvut.armaria.service.LoanService;
import org.example.zzzyxwvut.armaria.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * This class collects the books that fall due.
 */
/* Note: All non-TimeUnit temporal values are converted to milliseconds. */
@Component
public final class BookCollectorListener
{
	@Autowired
	private BookService bookService;

	@Autowired
	private LoanService loanService;

	@Autowired
	private TicketService ticketService;

	@Autowired
	private ApplicationEventPublisher publisher;

	private final Logger logger	= LogManager.getLogger();
	private final ScheduledExecutorService scheduler;
	private final AtomicLong workers;
	private final CountDownLatch latchPoll;
	private final Object lock	= new Object();
	private final Random random	= new Random();
	private volatile boolean dirty	= true;
	private volatile boolean alive	= true;
	private Thread collector, registrar;

	public BookCollectorListener()
	{
		final int size	= 32;
		workers		= new AtomicLong(size);
		scheduler	= Executors.newScheduledThreadPool(size);
		latchPoll	= new CountDownLatch(1);
	}

	/* Put the book on the shelf. */
	private void shelve(LoanBean loan)
	{
		BookBean book	= loan.getBook();
		book.setStatus(BOOKS.AVAILABLE);

		try {
			synchronized (lock) {
				if (loanService.hasLoan(loan.getId())) {
					loanService.deleteLoan(loan.getId());
					bookService.saveBook(book);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/* Collect a borrowed book. */
	private void collect(LoanBean loan)
	{
		workers.incrementAndGet();

		try {	/* Could be pre-empted by the client. */
			if (!loanService.hasLoan(loan.getId()))
				return;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		if (System.currentTimeMillis() < loan.getTerm().getTime()) {
			logger.warn(new StringBuilder(128)
				.append("Book-collector: reject premature collection: [")
				.append(loan.getId()).append("]: ")
				.append(loan.getTerm().getTime()
						- System.currentTimeMillis()));
			loan.setStatus(LOANS.DEFAULT);	/* Make it eligible again. */

			try {
				synchronized (lock) {
					loanService.saveLoan(loan);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			return;
		}

		logger.debug(new StringBuilder(128)
			.append("Book-collector: commence collecting: [")
			.append(loan.getId()).append("]: ").append(loan.getTerm())
			.append("\n").append(loan.getBook()));
		Iterable<TicketBean> tickets	= null;

		try {
			synchronized (lock) {
				tickets	= ticketService.getAllTickets();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			shelve(loan);
			return;
		}

		for (TicketBean ticket : tickets) {
			if (!alive) {
				return;
			} else if (!ticket.getBook().equals(loan.getBook())) {
				continue;
			}

			/*
			 * Note: We assume that this book is the only copy
			 *	and that the user has met the ticket quota.
			 */
			try {
				/* Lend the book to the oldest ticket-holder. */
				publisher.publishEvent(new MaturedTicketEvent(
					loan.getId(), ticket, lock, "en_US"));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				shelve(loan);
			}

			return;
		}

		shelve(loan);
	}

	/* Manage the loans. */
	private void poll(long pause)
	{
		try {
			if (!latchPoll.await(pause, TimeUnit.MILLISECONDS))
				logger.debug("Book-collector: make the latch time out: "
						+ latchPoll.getCount());
		} catch (InterruptedException e) {
			if (!alive)
				return;
		}

		do {
			Iterable<LoanBean> loans	= null;

			try {
				synchronized (lock) {
					loans	= loanService.getAllLoans();
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

			if (loans != null) {
				for (LoanBean loan : loans) {
					if (!alive) {
						return;
					} else if (workers.get() == 0L) {
						break;		/* All workers are occupied. */
					} else if (loan.getStatus().isManaged()) {
						continue;	/* Let the workers sleep. */
					}

					loan.setStatus(LOANS.MANAGED);

					try {
						synchronized (lock) {
							loanService.saveLoan(loan);
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						continue;
					}

					/* Bestow a 2-hour (and 16 minutes more) grace. */
					long grace	= ((random.nextLong() & 15L) << 16L)
										+ 7200000L;
					assert grace < 9000000L;	/* 150 minutes */

					long delay	= loan.getTerm().getTime() + grace
								- System.currentTimeMillis();
					delay	= (delay < 120000L) ? 120000L : delay;

					/* Assign a worker to manage this loan. */
					scheduler.schedule(new Runnable() {
						@Override
						public void run()
						{
							collect(loan);
						}
					}, delay, TimeUnit.MILLISECONDS);

					logger.debug(new StringBuilder(64)
						.append("Book-collector: register: [")
						.append(loan.getId()).append("]: ")
						.append(loan.getTerm()).toString());
					workers.decrementAndGet();
				}
			}

			try {
				TimeUnit.HOURS.sleep(5L
					+ (random.nextLong() & 3L));

				if (dirty)
					reclaim();
			} catch (InterruptedException e) {
				if (!alive)
					return;
			}
		} while (true);
	}

	/*
	 * Restore all managed loans to the default state when the server is up,
	 * so as to be able to register them again.
	 */
	private void reclaim()
	{
		Iterable<LoanBean> loans	= null;

		try {
			synchronized (lock) {
				loans	= loanService.getAllLoans();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			latchPoll.countDown();
			return;
		}

		for (LoanBean loan : loans) {
			if (!alive) {
				return;
			} else if (!loan.getStatus().isManaged()) {
				continue;
			}

			loan.setStatus(LOANS.DEFAULT);

			try {
				synchronized (lock) {
					loanService.saveLoan(loan);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

		dirty	= false;
		latchPoll.countDown();
	}

	@EventListener
	@Order
	public void handleContextRefreshedEvent(ContextRefreshedEvent event)
	{
		if (event.getApplicationContext().getParent() != null)
			return;

		final long delay	= TimeUnit.MINUTES.toMillis(
						5L + (random.nextLong() & 7L));
		registrar	= new Thread() {
			@Override
			public void run()
			{
				long start	= delay;
				long stop	= System.currentTimeMillis() + start;

				do {
					try {	/* Consider migration dependencies. */
						TimeUnit.MILLISECONDS.sleep(start);
					} catch (InterruptedException e) {
						if (!alive)
							return;
					}

					start	= stop - System.currentTimeMillis();

					if (start < 1L) {
						reclaim();
						return;
					}
				} while (true);
			}
		};

		registrar.setDaemon(true);
		registrar.setName("book-registrar");
		registrar.start();

		collector	= new Thread() {
			@Override
			public void run()
			{
				poll(delay << 1);
			}
		};

		collector.setDaemon(true);
		collector.setName("book-collector");
		collector.start();

		logger.debug(new StringBuilder(128)
			.append("Book-collector: initialised\n")
			.append(registrar.toString()).append("\n")
			.append(collector.toString()).toString());
	}

	@EventListener
	@Order
	public void handleContextClosedEvent(ContextClosedEvent event)
	{
		if (event.getApplicationContext().getParent() != null)
			return;

		alive	= false;
		logger.debug("Book-collector: shutting down...");

		try {
			registrar.interrupt();
			collector.interrupt();
			scheduler.shutdown();
			TimeUnit.SECONDS.sleep(8L);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
