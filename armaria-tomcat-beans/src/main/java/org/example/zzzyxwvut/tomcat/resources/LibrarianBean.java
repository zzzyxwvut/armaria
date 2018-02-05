package org.example.zzzyxwvut.tomcat.resources;

/*
 * XXX: Since the module of the class is of _provided_ scope, deliver
 *	it along with the WAR file.
 */

/**
 * This class collects the following declared attributes of a library
 * JNDI resource:
 * <pre>
 *	libraryName -- the name of a library with a qualified path,
 *		assumed to be relative to the user.home system property
 *	fileSuffix -- a file suffix, if any, e.g. ".pdf"</pre>
 */
public final class LibrarianBean
{
	private transient String libraryName;
	private transient String fileSuffix;

	public LibrarianBean() { }

	/**
	 * Gets the name of a library with a qualified path, assumed
	 *	to be relative to the user.home system property.
	 *
	 * @return	the name of a library
	 */
	public String getLibraryName()	{ return libraryName; }
	public void setLibraryName(String libraryName)	{ this.libraryName	= libraryName; }

	/**
	 * Gets a file suffix, if any.
	 *
	 * @return	a file suffix
	 */
	public String getFileSuffix()	{ return fileSuffix; }
	public void setFileSuffix(String fileSuffix)	{ this.fileSuffix	= fileSuffix; }
}
