package org.example.zzzyxwvut.tomcat.resources;

/*
 * XXX: Since the module of the class is of _provided_ scope, deliver
 *	it along with the WAR file.
 *
 *	Create a ${catalina.home}/shared/lib directory and put this
 *	module jar in it and include the path above to the property of
 *	common.loader in the ${catalina.home}/conf/catalina.properties
 *	configuration file.
 */

/**
 * This class collects the following declared attributes of a keystore
 * JNDI resource:
 * <pre>
 *	keyAlgorithm -- the secret key generating algorithm, e.g. AES, DES
 *	storeType -- the type of a keystore, e.g. jceks, pkcs12
 *	storeName -- the name of a keystore with a qualified path,
 *		assumed to be relative to the user.home system property
 *	storeWord -- the keystore password
 *	aliasName -- the alias name of an entry
 *	aliasWord -- the alias name password</pre>
 *
 * For example, with the keytool JDK utility we generate a 128-bit AES
 * secret "april" key valid for 30 days:
 * <pre>
 *	$ cd ~
 *	$ keytool -genseckey -v -alias april -keyalg AES -keysize 128 \
 *		-storetype jceks -keystore keys.jks -validity 30</pre>
 *
 * And later we would fill in the &lt;Resource&gt; element of the
 * server.xml configuration file as follows:
 * <pre>
 *	&lt;GlobalNamingResources&gt;
 *		&lt;!-- Any omitted entries... --&gt;
 *
 *		&lt;Resource
 *			aliasName="april"
 *			aliasWord="&lt;the-alias-name-password&gt;"
 *			auth="Container"
 *			factory="org.apache.naming.factory.BeanFactory"
 *			keyAlgorithm="AES"
 *			name="bean/StoreBeanFactory"
 *			storeName="keys.jks"
 *			storeType="jceks"
 *			storeWord="&lt;the-keystore-password&gt;"
 *			type="org.example.zzzyxwvut.tomcat.resources.StoreBean" /&gt;
 *	&lt;/GlobalNamingResources&gt;</pre>
 *
 * Note that the <code>name</code> attribute points to the resource link
 * defined in the project's META-INF/context.xml file.<br><br>
 *
 * See <a href="https://tomcat.apache.org/tomcat-8.5-doc/jndi-resources-howto.html#Generic_JavaBean_Resources">
 * Generic JavaBean Resources</a>.
 */
public final class StoreBean
{
	private transient String keyAlgorithm;
	private transient String storeType;
	private transient String storeName;
	private transient String storeWord;
	private transient String aliasName;
	private transient String aliasWord;

	public StoreBean() { }

	/**
	 * Gets the secret key generating algorithm, e.&nbsp;g.&nbsp; AES, DES.
	 *
	 * @return	the secret key generating algorithm
	 */
	public String getKeyAlgorithm()	{ return keyAlgorithm; }
	public void setKeyAlgorithm(String keyAlgorithm) { this.keyAlgorithm	= keyAlgorithm; }

	/**
	 * Gets the type of a keystore, e.&nbsp;g.&nbsp; jceks, pkcs12.
	 *
	 * @return	the type of a keystore
	 */
	public String getStoreType()	{ return storeType; }
	public void setStoreType(String storeType)	{ this.storeType	= storeType; }

	/**
	 * Gets the name of a keystore with a qualified path, assumed
	 *	to be relative to the user.home system property.
	 *
	 * @return	the name of a keystore
	 */
	public String getStoreName()	{ return storeName; }
	public void setStoreName(String storeName)	{ this.storeName	= storeName; }

	/**
	 * Gets the keystore password.
	 *
	 * @return	the keystore password
	 */
	public String getStoreWord()	{ return storeWord; }
	public void setStoreWord(String storeWord)	{ this.storeWord	= storeWord; }

	/**
	 * Gets the alias name of an entry.
	 *
	 * @return	the alias name of an entry
	 */
	public String getAliasName()	{ return aliasName; }
	public void setAliasName(String aliasName)	{ this.aliasName	= aliasName; }

	/**
	 * Gets the alias name password.
	 *
	 * @return	the alias name password
	 */
	public String getAliasWord()	{ return aliasWord; }
	public void setAliasWord(String aliasWord)	{ this.aliasWord	= aliasWord; }
}
