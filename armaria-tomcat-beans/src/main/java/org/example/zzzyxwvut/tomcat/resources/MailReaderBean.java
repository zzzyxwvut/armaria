package org.example.zzzyxwvut.tomcat.resources;

/*
 * XXX: Since the module of the class is of _provided_ scope, deliver
 *	it along with the WAR file.
 */

/**
 * This class collects the following declared attributes of an inbound
 * mail JNDI resource:
 * <pre>
 *	uri -- the URI for the Mail Store
 *	poll -- the polling delay in MILLISECONDS</pre>
 */
public final class MailReaderBean
{
	private transient String uri;
	private transient long poll;

	public MailReaderBean() { }

	/**
	 * Gets the URI for the Mail Store in the following form:<br><br>
	 * [pop3|imap]://[username]:[password]@[host]:[port]/INBOX
	 *
	 * @return	the URI for the Mail Store.
	 */
	public String getUri()		{ return uri; }
	public void setUri(String uri)	{ this.uri	= uri; }

	/**
	 * Gets the polling delay in MILLISECONDS (for an idle-less IMAP server).
	 *
	 * @return	the polling delay in MILLISECONDS
	 */
	public long getPoll()		{ return poll; }
	public void setPoll(long poll)	{ this.poll	= poll; }
}
