package org.example.zzzyxwvut.armaria.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStore.PasswordProtection;
import java.security.SecureRandom;
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
 * valid secret key alias-entries and the {@link StoreBean} attributes
 * to fetch the keys.
 *
 * @see StoreBean
 */
public enum Encryptor
{
	/** The only instance. */
	INSTANCE;

	private final Cipher cipher;
	private final SecretKey secret_0;
	private final SecretKey secret_1;
	private final SecureRandom random;
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
		String extraName	= Objects.requireNonNull(bean.getExtraName());
		String extraWord	= Objects.requireNonNull(bean.getExtraWord());
		Cipher cipher		= null;
		SecretKey secret_0	= null;
		SecretKey secret_1	= null;

		try	(FileInputStream input	= new FileInputStream(
				new File(System.getProperty("user.home"),
								storeName))) {
			KeyStore store	= KeyStore.getInstance(storeType);
			store.load(input, storeWord.toCharArray());

			PasswordProtection wp_0	= new KeyStore.PasswordProtection(
							aliasWord.toCharArray());
			Entry entry_0	= store.getEntry(aliasName, wp_0);
			SecretKey sk_0	= ((KeyStore.SecretKeyEntry) entry_0).getSecretKey();
			secret_0	= new SecretKeySpec(sk_0.getEncoded(), keyAlgorithm);

			PasswordProtection wp_1	= new KeyStore.PasswordProtection(
							extraWord.toCharArray());
			Entry entry_1	= store.getEntry(extraName, wp_1);
			SecretKey sk_1	= ((KeyStore.SecretKeyEntry) entry_1).getSecretKey();
			secret_1	= new SecretKeySpec(sk_1.getEncoded(), keyAlgorithm);

			cipher	= Cipher.getInstance(keyAlgorithm);
		} catch (IOException | GeneralSecurityException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			this.secret_0	= Objects.requireNonNull(secret_0);
			this.secret_1	= Objects.requireNonNull(secret_1);
			this.cipher	= Objects.requireNonNull(cipher);
			this.random	= new SecureRandom();
			logger.debug("Encryptor is initialised");
		}
	}

	/* Sprinkles a word with a poor man's salt. */
	private byte[] sprinkle(byte[] word)
	{
		assert word != null;

		byte[] uniform	= new byte[word.length << 1];
		random.nextBytes(uniform);

		for (int i = 1, j = 0, k = 0, w = word.length; k < w;
							i += 2, j += 2, ++k)
			uniform[i]	= (byte) (uniform[j] ^ word[k]);

		/*
		 * (random)[0]			XOR(word[0], (random)[0]) == uniform[1]
		 * uniform[1] <- word[0]
		 *
		 * (random)[2]
		 * uniform[3] <- word[1]
		 */
		return uniform;
	}

	/* Winnows a poor man's salt from a word. */
	private byte[] winnow(byte[] word)
	{
		assert word != null;

		byte[] uniform	= new byte[word.length >> 1];

		for (int i = 0, j = 0, k = 1, u = uniform.length; i < u;
							++i, j += 2, k += 2)
			uniform[i]	= (byte) (word[j] ^ word[k]);

		/*
		 *		 word[0]	XOR(word[0], word[1]) == uniform[0]
		 * uniform[0] <- word[1]	   (random)
		 *
		 *		 word[2]
		 * uniform[1] <- word[3]
		 */
		return uniform;
	}

	private String encrypt0(String word, SecretKey secret)
	{
		assert word != null && secret != null;

		byte[] value	= null;

		try {
			cipher.init(Cipher.ENCRYPT_MODE, secret);
			value	= cipher.doFinal(
					word.getBytes(StandardCharsets.UTF_8));
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}

		return Base64.getEncoder().encodeToString(sprinkle(value));
	}

	private String decrypt0(String word, SecretKey secret)
	{
		assert word != null && secret != null;

		byte[] value	= null;

		try {
			cipher.init(Cipher.DECRYPT_MODE, secret);
			value	= cipher.doFinal(winnow(
					Base64.getDecoder().decode(word)));
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}

		return new String(value, StandardCharsets.UTF_8);
	}

	/**
	 * Encrypts a word.
	 *
	 * @param word	a word to encrypt
	 * @return	the encrypted word stored in the Base64 encoding scheme
	 * @throws NullPointerException if {@code word} is {@code null}
	 * @throws RuntimeException if encrypting fails
	 */
	public String encrypt(String word)
	{
		if (word == null)
			throw new NullPointerException();

		return encrypt0(word, secret_0);
	}

	/**
	 * Encrypts a word, using an alternative secret key.
	 *
	 * @see #encrypt(String)
	 */
	public String encryptAlternative(String word)
	{
		if (word == null)
			throw new NullPointerException();

		return encrypt0(word, secret_1);
	}

	/**
	 * Decrypts an encrypted word stored in the Base64 encoding scheme.
	 *
	 * @param word	an encrypted word stored in the Base64 encoding scheme
	 * @return	the decrypted word
	 * @throws NullPointerException if {@code word} is {@code null}
	 * @throws RuntimeException if decrypting fails
	 */
	public String decrypt(String word)
	{
		if (word == null)
			throw new NullPointerException();

		return decrypt0(word, secret_0);
	}

	/**
	 * Decrypts an encrypted word stored in the Base64 encoding scheme,
	 * using an alternative secret key.
	 *
	 * @see #decrypt(String)
	 */
	public String decryptAlternative(String word)
	{
		if (word == null)
			throw new NullPointerException();

		return decrypt0(word, secret_1);
	}

	/**
	 * Tests whether the arguments are equal to each other.
	 *
	 * @param tenable	a tenable value to compare to
	 * @param pending	a pending value to compare to
	 * @return	true if the arguments are equal to each other and false otherwise
	 * @throws NullPointerException if either {@code tenable} or {@code pending}
	 *			is {@code null}
	 */
	public boolean equals(byte[] tenable, byte[] pending)
	{
		if (tenable == null || pending == null)
			throw new NullPointerException();

		int test	= tenable.length ^ pending.length;
		byte[] uniform	= (tenable.length < 1)
							? new byte[] { 0 }
							: tenable;

		/* Ay, observe time-constant iteration, no fail-fast! */
		for (int i = 0, t = uniform.length, p = pending.length; i < p; ++i)
			test	|= uniform[i % t] ^ pending[i];

		return (test == 0);
	}

	/**
	 * Tests whether the arguments are equal to each other.
	 *
	 * @see #equals(byte[], byte[])
	 */
	public boolean equals(String tenable, String pending)
	{
		if (tenable == null || pending == null)
			throw new NullPointerException();

		return Encryptor.INSTANCE.equals(tenable.getBytes(StandardCharsets.UTF_8),
					pending.getBytes(StandardCharsets.UTF_8));
	}
}
