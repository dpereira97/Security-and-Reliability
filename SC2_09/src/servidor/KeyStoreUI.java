package servidor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import javax.crypto.SecretKey;


/**
 * Classe responsavel por gerir keystores
 * com os metodos apropriados para tal
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
@SuppressWarnings("squid:S106")
public class KeyStoreUI {

	private KeyStoreUI() {
	}

	public static boolean gerarKeyStoreByPass(String keystoreAlg, String keystorePath, String passAdmin,
			String keystoreAlias, String keystorePass) {

		try {
			KeyStore kstore = KeyStore.getInstance(keystoreAlg);
			kstore.load(null, keystorePass.toCharArray());

			SecretKey key = GerarChaves.gerarChaveSecretaByPass(passAdmin);
			KeyStore.ProtectionParameter pass = new KeyStore.PasswordProtection(keystorePass.toCharArray());
			KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(key);

			kstore.setEntry(keystoreAlias, secretKeyEntry, pass);

			FileOutputStream kfileOut = new FileOutputStream(keystorePath);
			kstore.store(kfileOut, keystorePass.toCharArray());

			return true;
		} catch (KeyStoreException | CertificateException e) {
			System.out.println("Error creating keystore");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static PrivateKey getPrivateKeyKeystore(String keystoreServerAlgorithm, String keystorePath,
			String keystoreAlias, String keystorePass) {

		return (PrivateKey) getKeyKeystore(keystoreServerAlgorithm, keystorePath, keystoreAlias, keystorePass);

	}

	public static Key getKeyKeystore(String keystoreServerAlgorithm, String keystorePath, String keystoreAlias,
			String keystorePass) {
		try (FileInputStream kfile = new FileInputStream(keystorePath)) {
			KeyStore ks = KeyStore.getInstance(keystoreServerAlgorithm);
			ks.load(kfile, keystorePass.toCharArray());
			return ks.getKey(keystoreAlias, keystorePass.toCharArray());
		} catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException
				| UnrecoverableKeyException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static PublicKey getPublicKeyKeystore(String keystoreServerAlgorithm, String keystorePath,
			String keystoreAlias, String keystorePass) {
		Certificate cert = getCertKeystore(keystoreServerAlgorithm, keystorePath, keystoreAlias, keystorePass);
		if (cert != null) {
			return cert.getPublicKey();
		} else {
			return null;
		}
	}

	public static Certificate getCertKeystore(String keystoreServerAlgorithm, String keystorePath, String keystoreAlias,
			String keystorePass) {
		try (FileInputStream kfile = new FileInputStream(keystorePath)) {
			KeyStore ks = KeyStore.getInstance(keystoreServerAlgorithm);
			ks.load(kfile, keystorePass.toCharArray());
			return ks.getCertificate(keystoreAlias);
		} catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
			e.printStackTrace();
			return null;
		}

	}

}