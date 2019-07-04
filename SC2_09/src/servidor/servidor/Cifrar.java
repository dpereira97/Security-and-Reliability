package servidor.servidor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;

import servidor.ConstantesAlg;
import servidor.ConstantesKeyStore;
import servidor.DiretoriosServidor;
import servidor.GerarChaves;
import servidor.KeyStoreUI;

public class Cifrar {

	private Cifrar() {
	}

	public static void cifrarKey(Key simetricKey, String nameFile) {

		Key publicKey = KeyStoreUI.getPublicKeyKeystore(ConstantesAlg.KEYSTORE_ALGORITHM,
				DiretoriosServidor.SERVER_KEYSTORE, ConstantesKeyStore.KEYSTORE_ALIAS_SERVER,
				ConstantesKeyStore.KEYSTORE_PASS);

		Cipher c = GetterCiphersSign.gerarCipher(publicKey, Cipher.WRAP_MODE, ConstantesAlg.WRAP_ALGORITHM);

		byte[] bytes = null;
		try {
			bytes = c.wrap(simetricKey);
		} catch (InvalidKeyException | IllegalBlockSizeException e1) {
			e1.printStackTrace();
			return;
		}

		try (FileOutputStream fos = new FileOutputStream(nameFile)) {
			fos.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	public static Key decifrarKey(String nameFile) {

		byte[] bytes = lerFicheiroKey(nameFile);
		if (bytes.length == 0)
			return null;

		Key privateKey = KeyStoreUI.getPrivateKeyKeystore(ConstantesAlg.KEYSTORE_ALGORITHM,
				DiretoriosServidor.SERVER_KEYSTORE, ConstantesKeyStore.KEYSTORE_ALIAS_SERVER,
				ConstantesKeyStore.KEYSTORE_PASS);

		Cipher c = GetterCiphersSign.gerarCipher(privateKey, Cipher.UNWRAP_MODE, ConstantesAlg.WRAP_ALGORITHM);

		try {
			return c.unwrap(bytes, ConstantesAlg.KEY_SIMETRICA_ALGORITHM, Cipher.SECRET_KEY);
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static byte[] lerFicheiroKey(String nameFile) {
		byte[] bytes;
		try (FileInputStream fis = new FileInputStream(nameFile)) {
			int available = fis.available();
			bytes = new byte[available];
			int lidos = fis.read(bytes);

			if (available != lidos)
				return new byte[0];

		} catch (IOException e) {
			e.printStackTrace();
			return new byte[0];
		}
		return bytes;
	}

	public static void cifrarFicheiro(String nameFile, String nameFileNovo) {
		SecretKey key = GerarChaves.gerarChave(ConstantesAlg.KEY_SIMETRICA_ALGORITHM, 128);
		Cipher c = GetterCiphersSign.gerarCipher(key, Cipher.ENCRYPT_MODE, ConstantesAlg.KEY_SIMETRICA_ALGORITHM);

		cifrarKey(key, nameFile + ".key");

		FileInputStream fis = null;
		try (CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(nameFileNovo), c)) {
			fis = new FileInputStream(nameFile);
			byte[] bytes = new byte[1024];
			int i;
			while ((i =fis.read(bytes)) != -1) {
				cos.write(bytes, 0 , i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static boolean decifrarFicheiro(String nameFile, String novoFile, String keyFile) {

		Key symmetricKey = decifrarKey(keyFile);
		Cipher c = GetterCiphersSign.gerarCipher(symmetricKey, Cipher.DECRYPT_MODE, ConstantesAlg.KEY_SIMETRICA_ALGORITHM);
		FileOutputStream fos = null;
		try (CipherInputStream cis = new CipherInputStream(new FileInputStream(nameFile), c)) {
			fos = new FileOutputStream(novoFile);
			byte[] bytes = new byte[1024];
			int i;
			while ((i = cis.read(bytes)) != -1) {
				fos.write(bytes,0 ,i);
			}
		} catch (IOException e) {
			return false;
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return true;
	}

	public static boolean mudarNomeFiles(String nomeAntigo, String nomeNovo) {
		File file = new File(nomeAntigo);
		return file.renameTo(new File(nomeNovo));
	}

	public static void apagarFile(String nomeFile) {
		try {
			Files.delete(Paths.get(nomeFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}