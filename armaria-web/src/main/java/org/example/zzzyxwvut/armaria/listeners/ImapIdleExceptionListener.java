package org.example.zzzyxwvut.armaria.listeners;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.integration.mail.ImapIdleChannelAdapter;
import org.springframework.integration.mail.ImapIdleChannelAdapter.ImapIdleExceptionEvent;
import org.springframework.stereotype.Component;

@Component
public final class ImapIdleExceptionListener
{
	@Autowired
	ImapIdleChannelAdapter customAdapter;

	private volatile boolean fired	= false;

	@EventListener(condition = "#event.cause != null")
	public void handleImapIdleExceptionEvent(ImapIdleExceptionEvent event)
	{
		if (fired)
			return;

		fired	= true;
		customAdapter.setReconnectDelay(TimeUnit.MINUTES.toMillis(8L));
	}
}
