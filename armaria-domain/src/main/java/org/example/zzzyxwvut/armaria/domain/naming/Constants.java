package org.example.zzzyxwvut.armaria.domain.naming;

import java.util.concurrent.TimeUnit;

/* NOTE: Some of the QUOTA constants are referenced in the profile view. */
public enum Constants
{
	INSTANCE;

	////CREATE TABLE IF NOT EXISTS books(
	////	`id`		SMALLINT(5) UNSIGNED	NOT NULL AUTO_INCREMENT,
	////	`author`	VARCHAR(255)		NOT NULL,
	////	`title`		VARCHAR(511)		NOT NULL,
	////	`subtitle`	VARCHAR(511)		DEFAULT NULL,
	////	`language`	ENUM('GREEK','LATIN')	NOT NULL,
	////	`pages`		SMALLINT(4) UNSIGNED	NOT NULL,
	////	`year`		SMALLINT(4) UNSIGNED	NOT NULL,
	////	`status`	ENUM('AVAILABLE','BORROWED') NOT NULL,
	////	PRIMARY KEY(`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	public enum BOOKS
	{
		GREEK		("Greek"),
		LATIN		("Latin"),
		AVAILABLE	("available"),
		BORROWED	("borrowed");

		private final String value;
		private BOOKS(String value)	{ this.value	= value; }

		@Override
		public String toString()	{ return value; }
		public boolean isLatin()	{ return this == LATIN; }
		public boolean isBorrowed()	{ return this == BORROWED; }
	}

	////CREATE TABLE IF NOT EXISTS loans(
	////	`id`		INT UNSIGNED		NOT NULL AUTO_INCREMENT,
	////	`book_id`	SMALLINT(5) UNSIGNED	NOT NULL,
	////	`user_id`	INT UNSIGNED		NOT NULL,
	////	`term`		TIMESTAMP DEFAULT CURRENT_TIMESTAMP	NOT NULL,
	////	`status`	ENUM('DEFAULT', 'MANAGED') NOT NULL,
	////	`item`		BIGINT NOT NULL,
	////	PRIMARY KEY(`id`),
	////	FOREIGN KEY(`book_id`) REFERENCES books(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	////	FOREIGN KEY(`user_id`) REFERENCES users(`id`) ON DELETE CASCADE ON UPDATE CASCADE)
	////	ENGINE=INNODB DEFAULT CHARSET=utf8;
	public enum LOANS
	{
		DEFAULT	("default"),
		MANAGED	("managed");

		private final String value;
		private LOANS(String value)	{ this.value	= value; }

		@Override
		public String toString()	{ return value; }
		public boolean isManaged()	{ return this == MANAGED; }
	}

	////CREATE TABLE IF NOT EXISTS tickets(
	////	`id`		INT UNSIGNED		NOT NULL AUTO_INCREMENT,
	////	`book_id`	SMALLINT(5) UNSIGNED	NOT NULL,
	////	`user_id`	INT UNSIGNED		NOT NULL,
	////	`status`	ENUM('DEFAULT', 'MANAGED') NOT NULL,
	////	`item`		BIGINT NOT NULL,
	////	PRIMARY KEY(`id`),
	////	FOREIGN KEY(`book_id`) REFERENCES books(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
	////	FOREIGN KEY(`user_id`) REFERENCES users(`id`) ON DELETE CASCADE ON UPDATE CASCADE)
	////	ENGINE=INNODB DEFAULT CHARSET=utf8;
	public enum TICKETS
	{
		INVALID	("invalid"),
		VALID	("valid");

		private final String value;
		private TICKETS(String value)	{ this.value	= value; }

		@Override
		public String toString()	{ return value; }
		public boolean isValid()	{ return this == VALID; }
	}

	////CREATE TABLE IF NOT EXISTS users(
	////	`id`		INT UNSIGNED	NOT NULL AUTO_INCREMENT,
	////	`login`		VARCHAR(255)	NOT NULL UNIQUE,
	////	`password`	VARCHAR(255)	NOT NULL,
	////	`email`		VARCHAR(255)	NOT NULL UNIQUE,
	////	`locale`	VARCHAR(32)	NOT NULL,
	////	`role`		ENUM('CLIENT', 'PATRON') NOT NULL,
	////	`status`	ENUM('DEFAULT', 'MANAGED') NOT NULL,
	////	`token`		TEXT		NULL,
	////	`entry`		TIMESTAMP	NULL,
	////	`item`		BIGINT		NOT NULL,
	////	PRIMARY KEY(`id`)) ENGINE=INNODB DEFAULT CHARSET=utf8;
	////
	//// NOTE: The index key prefix length limit is 767 bytes for InnoDB tables,
	////	(also constrains a UNIQUE key).
	////
	////	255 * 3 (bytes/utf8 char) == 765 < 767 bytes
	////
	//// See refman-*-en.html-chapter/innodb-storage-engine.html
	public enum USERS
	{
		CLIENT	("client"),
		PATRON	("patron"),
		INVALID	("invalid"),
		VALID	("valid");

		private final String value;
		private USERS(String value)	{ this.value	= value; }

		@Override
		public String toString()	{ return value; }
		public boolean isPatron()	{ return this == PATRON; }
		public boolean isValid()	{ return this == VALID; }
	}

	/* Miscellaneous constraints. */
	public enum QUOTA
	{
		LOANS		(5L),
		DAYS		(14L),
		DAYS_AS_MILLISEC(TimeUnit.DAYS.toMillis(14L)),
		TICKETS		(2L);

		private final long value;
		private QUOTA(long value)	{ this.value	= value; }

		@Override
		public String toString()	{ return String.valueOf(value); }
		public long getValue()		{ return value; }

		public boolean exceedsLoanQuota(long i)
		{
			return (0L > i || i >= LOANS.value);
		}

		public boolean exceedsTicketQuota(long i)
		{
			return (0L > i || i >= TICKETS.value);
		}
	}
}
