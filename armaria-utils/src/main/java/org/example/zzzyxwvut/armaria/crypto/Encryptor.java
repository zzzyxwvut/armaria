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

		String storeType	= bean.getStoreType();
		String keyAlgorithm	= bean.getKeyAlgorithm();
		String storeName	= bean.getStoreName();
		String storeWord	= bean.getStoreWord();
		String aliasName	= bean.getAliasName();
		String aliasWord	= bean.getAliasWord();
		Cipher cipher		= null;
		SecretKey secret	= null;

		try	(FileInputStream input	= new FileInputStream(
				new File(System.getProperty("user.home"),
								storeName))) {
			KeyStore store	= KeyStore.getInstance(storeType);
			PasswordProtection wordProtection =
						new KeyStore.PasswordProtection(
							aliasWord.toCharArray());
			store.load(input, storeWord.toCharArray());
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
	 * @throws	RuntimeException if encrypting fails
	 */
	public String encrypt(String word)
	{
		byte[] value	= null;

		try {
			cipher.init(Cipher.ENCRYPT_MODE, secret);
			value	= cipher.doFinal(word.getBytes(StandardCharsets.UTF_8));
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
	 * @throws	RuntimeException if decrypting fails
	 */
	public String decrypt(String word)
	{
		byte[] value	= null;

		try {
			cipher.init(Cipher.DECRYPT_MODE, secret);
			value	= cipher.doFinal(Base64.getDecoder().decode(word));
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}

		return new String(value, StandardCharsets.UTF_8);
	}

	/**
	 * Tests whether the arguments are equal to each other.
	 *
	 * @param aa	a value to compare to
	 * @param bb	a value to compare to
	 * @return	true if the arguments are equal to each other and false otherwise
	 */
	public boolean equals(byte[] aa, byte[] bb)
	{
		if (aa == null || bb == null)
			throw new NullPointerException();

		if (aa.length != bb.length)
			return false;

		int test	= 0;

		/* Ay, observe time-constant iteration, no fail-fast! */
		for (int i = 0, j = aa.length; i < j; ++i)
			test	|= aa[i] ^ bb[i];

		return (test == 0);
	}

	/**
	 * Tests whether the arguments are equal to each other.
	 *
	 * @param aa	a value to compare to
	 * @param bb	a value to compare to
	 * @return	true if the arguments are equal to each other and false otherwise
	 */
	public boolean equals(String aa, String bb)
	{
		if (aa == null || bb == null)
			throw new NullPointerException();

		return Encryptor.INSTANCE.equals(aa.getBytes(StandardCharsets.UTF_8),
					bb.getBytes(StandardCharsets.UTF_8));
	}
}
