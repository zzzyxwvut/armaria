package org.example.zzzyxwvut.tomcat.resources;

/*
 * XXX: Since the module of the class is of _provided_ scope, deliver
 *	it along with the WAR file.
 */

/**
 * This class collects the following declared attributes of an outbound
 * mail JNDI resource:
 * <pre>
 *	username -- the username for the account at the mail host
 *	password -- the password for the account at the mail host
 *	host -- the SMTP server to connect to
 *	protocol -- the mail protocol, e.g. smtp, smtps
 *	port -- the SMTP server port to connect to, e.g. 465, 587</pre>
 */
public final class MailWriterBean
{
	private transient String username;
	private transient String password;
	private transient String host;
	private transient String protocol;
	private transient int port;

	public MailWriterBean() { }

	/**
	 * Gets the username for the account at the mail host.
	 *
	 * @return	the username for the account at the mail host
	 */
	public String getUsername()		{ return username; }
	public void setUsername(String username) { this.username	= username; }

	/**
	 * Gets the password for the account at the mail host.
	 *
	 * @return	the password for the account at the mail host
	 */
	public String getPassword()		{ return password; }
	public void setPassword(String password) { this.password	= password; }

	/**
	 * Gets the SMTP server to connect to.
	 *
	 * @return	the SMTP server to connect to
	 */
	public String getHost()			{ return host; }
	public void setHost(String host)	{ this.host		= host; }

	/**
	 * Gets the mail protocol, e.g. smtp, smtps
	 *
	 * @return
	 */
	public String getProtocol()		{ return protocol; }
	public void setProtocol(String protocol) { this.protocol	= protocol; }

	/**
	 * Gets the SMTP server port to connect to, e.g. 465, 587
	 *
	 * @return	the SMTP server port to connect to
	 */
	public int getPort()			{ return port; }
	public void setPort(int port)		{ this.port		= port; }
}
