package org.example.zzzyxwvut.armaria.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStore.PasswordProtection;
import java.util.Base64;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.zzzyxwvut.tomcat.resources.StoreBean;

/**
 * This enum provides the functionality to encrypt and decrypt strings.
 * Its initialisation depends on the availability of a keystore with
 * a valid secret key alias-entry and the {@link StoreBean} attributes
 * to fetch this key.
 *
 * @see StoreBean
 */
public enum Encryptor
{
	/** The only instance. */
	INSTANCE;

	private final Cipher cipher;
	private final SecretKey secret;
	private final Logger logger	= LogManager.getLogger();

	private Encryptor()
	{
		StoreBean bean	= null;

		try {
			Context initContext	= new InitialContext();
			Context envContext	= (Context) initContext.lookup("java:comp/env");
			bean	= (StoreBean) envContext.lookup("bean/StoreBeanFactory");
		} catch (NamingException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		String storeType	= Objects.requireNonNull(bean.getStoreType());
		String keyAlgorithm	= Objects.requireNonNull(bean.getKeyAlgorithm());
		String storeName	= Objects.requireNonNull(bean.getStoreName());
		String storeWord	= Objects.requireNonNull(bean.getStoreWord());
		String aliasName	= Objects.requireNonNull(bean.getAliasName());
		String aliasWord	= Objects.requireNonNull(bean.getAliasWord());
		Cipher cipher		= null;
		SecretKey secret	= null;

		try	(FileInputStream input	= new FileInputStream(
				new File(System.getProperty("user.home"),
								storeName))) {
			KeyStore store	= KeyStore.getInstance(storeType);
			store.load(input, storeWord.toCharArray());
			PasswordProtection wordProtection =
						new KeyStore.PasswordProtection(
							aliasWord.toCharArray());
			Entry entry	= store.getEntry(aliasName, wordProtection);
			SecretKey sk	= ((KeyStore.SecretKeyEntry) entry).getSecretKey();

			secret	= new SecretKeySpec(sk.getEncoded(), keyAlgorithm);
			cipher	= Cipher.getInstance(keyAlgorithm);
		} catch (IOException | GeneralSecurityException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			this.secret	= Objects.requireNonNull(secret);
			this.cipher	= Objects.requireNonNull(cipher);
			logger.debug("Encryptor is initialised");
		}
	}

	/**
	 * Encrypts a word.
	 *
	 * @param word	a word to encrypt
	 * @return	the encrypted word stored in the Base64 encoding scheme
	 * @throws	NullPointerException if {@code word} is {@code null}
	 * @throws	RuntimeException if encrypting fails
	 */
	public String encrypt(String word)
	{
		if (word == null)
			throw new NullPointerException();

		byte[] value	= null;

		try {
			cipher.init(Cipher.ENCRYPT_MODE, secret);
			value	= cipher.doFinal(
					word.getBytes(StandardCharsets.UTF_8));
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}

		return Base64.getEncoder().encodeToString(value);
	}

	/**
	 * Decrypts an encrypted word stored in the Base64 encoding scheme.
	 *
	 * @param word	an encrypted word stored in the Base64 encoding scheme
	 * @return	the decrypted word
	 * @throws	NullPointerException if {@code word} is {@code null}
	 * @throws	RuntimeException if decrypting fails
	 */
	public String decrypt(String word)
	{
		if (word == null)
			throw new NullPointerException();

		byte[] value	= null;

		try {
			cipher.init(Cipher.DECRYPT_MODE, secret);
			value	= cipher.doFinal(
					Base64.getDecoder().decode(word));
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}

		return new String(value, StandardCharsets.UTF_8);
	}

	/**
	 * Tests whether the arguments are equal to each other.
	 *
	 * @param tenable	a tenable value to compare to
	 * @param pending	a pending value to compare to
	 * @return	true if the arguments are equal to each other and false otherwise
	 * @throws	NullPointerException if either {@code tenable} or {@code pending}
	 *			is {@code null}
	 */
	public boolean equals(byte[] tenable, byte[] pending)
	{
		if (tenable == null || pending == null)
			throw new NullPointerException();

		int test	= tenable.length ^ pending.length;

		/* Ay, observe time-constant iteration, no fail-fast! */
		for (int i = 0, t = tenable.length, p = pending.length; i < p; ++i)
			test	|= tenable[i % t] ^ pending[i];

		return (test == 0);
	}

	/**
	 * Tests whether the arguments are equal to each other.
	 *
	 * @param tenable	a tenable value to compare to
	 * @param pending	a pending value to compare to
	 * @return	true if the arguments are equal to each other and false otherwise
	 * @throws	NullPointerException if either {@code tenable} or {@code pending}
	 *			is {@code null}
	 */
	public boolean equals(String tenable, String pending)
	{
		if (tenable == null || pending == null)
			throw new NullPointerException();

		return Encryptor.INSTANCE.equals(tenable.getBytes(StandardCharsets.UTF_8),
					pending.getBytes(StandardCharsets.UTF_8));
	}
}
