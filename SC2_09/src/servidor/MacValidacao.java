package servidor;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.SecretKey;

/**
 * Classe encarregue da validacao do mac
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
@SuppressWarnings("squid:S106")
public class MacValidacao {

	protected MacValidacao() {
	}

	private static boolean verificarPass(String passAdmin) {

		SecretKey chaveSecreta = GerarChaves.gerarChaveSecretaByPass(passAdmin);
		if (chaveSecreta == null) {
			System.out.println("chave gerada foi null");
			return false;
		}
		byte[] gerada = chaveSecreta.getEncoded();

		Key chaveStore = KeyStoreUI.getKeyKeystore(ConstantesAlg.KEYSTORE_ALGORITHM,
				DiretoriosServidor.KEYSTORE_PATH, ConstantesKeyStore.KEYSTORE_ALIAS_ADMIN, ConstantesKeyStore.KEYSTORE_PASS);
		if (chaveStore == null) {
			System.out.println("chave obtida foi null");
			return false;
		}

		byte[] obtida = chaveStore.getEncoded();

		return Arrays.equals(gerada, obtida);
	}

	public static boolean autenticacao(String passAdmin, String credenciaisPath, String macPath) {

		if (!verificarPass(passAdmin)) {
			System.out.println("Password Invalida");
			return false;
		}
		if (!verificarMac(passAdmin, credenciaisPath, macPath)) {
			System.out.println("O ficheiro credenciais foi corrompido vamos fechar, acho que foi a toupeira");
			return false;
		}
		return true;
	}

	private static boolean verificarMac(String passAdmin, String credenciaisPath, String macPath) {

		try (BufferedInputStream reader = new BufferedInputStream(new FileInputStream(macPath))) {
			byte[] gerada = gerarMac(credenciaisPath, passAdmin);

			int tamanhoArray = reader.available();

			byte[] obtida = new byte[tamanhoArray];

			if (reader.read(obtida) != tamanhoArray)
				return false;

			return Arrays.equals(gerada, obtida);

		}
		catch (IOException e) {
			System.err.println("Erro a ler o ficheiro");
			return false;
		}
	}

	protected static byte[] gerarMac(String credenciaisPath, String passAdmin) {

		try (BufferedReader reader = new BufferedReader(new FileReader(credenciaisPath))) {
			SecretKey key = GerarChaves.gerarChaveSecretaByPass(passAdmin);
			Mac mac = Mac.getInstance(ConstantesAlg.MAC_ALGORITHM);
			mac.init(key);

			if (verificarFile(credenciaisPath)) {
				String line = reader.readLine();

				while (line != null) {
					mac.update(line.getBytes());
					line = reader.readLine();
				}
			}

			return mac.doFinal();
		}
		catch (NoSuchAlgorithmException | IOException | InvalidKeyException e) {
			System.out.println("Erro a criar o BigMac");
			return new byte[0];
		}
	}

	protected static boolean verificarFile(String filePath) {
		return new File(filePath).exists();
	}
}
