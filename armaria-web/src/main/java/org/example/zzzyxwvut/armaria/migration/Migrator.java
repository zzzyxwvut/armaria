package org.example.zzzyxwvut.armaria.migration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

/**
 * This class employs the Flyway tool to migrate data to a database.
 */
public class Migrator extends HttpServlet
{
	private static final long serialVersionUID	= 2777568529926894959L;
	private final Logger logger	= LogManager.getLogger();

	public Migrator() { }

	/*
	 * From https://flywaydb.org/documentation/migrations
	 *
	 * The file name consists of the following parts:
	 *
	 * Prefix: V for versioned migrations,
	 *	U for undo migrations,
	 *	R for repeatable migrations
	 *
	 * Version: Underscores (automatically replaced by dots at runtime)
	 *	separate as many parts as you like (not for repeatable migrations)
	 *
	 * Separator: __ (two underscores)
	 *
	 * Description: Underscores (automatically replaced by spaces at runtime)
	 *	separate the words
	 */
	@Override
	public void init() throws ServletException
	{
		try {
			Context initContext	= new InitialContext();
			Context envContext	= (Context) initContext.lookup("java:comp/env");
			DataSource s	= (DataSource) envContext.lookup("jdbc/poachers");
			Flyway flyway	= new Flyway();
			flyway.setDataSource(s);
			flyway.baseline();
			int score	= flyway.migrate();

			if (score > 0) {
				logger.debug(new StringBuilder(24)
					.append("Applied ").append(score)
					.append(" migrations.").toString());
			} else {
				logger.debug("Database is up to date.");
			}
		} catch (NamingException | FlywayException e) {
			logger.error(Migrator.class.getCanonicalName(), e);
		}
	}
}
