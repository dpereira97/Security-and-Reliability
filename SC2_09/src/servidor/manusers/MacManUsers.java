package servidor.manusers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.util.Arrays;

import javax.crypto.SecretKey;

import servidor.ConstantesAlg;
import servidor.ConstantesKeyStore;
import servidor.DiretoriosServidor;
import servidor.GerarChaves;
import servidor.KeyStoreUI;
import servidor.MacValidacao;

/**
 * Classe responsavel por gerir o mac
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
@SuppressWarnings("squid:S106")
public class MacManUsers extends MacValidacao {

	public static boolean verificarExistenciaMac(String macPath) {
		return verificarFile(macPath);
	}

	public static boolean semMac(String passAdmin, String credenciaisPath, String macPath) {

		if (!KeyStoreUI.gerarKeyStoreByPass(ConstantesAlg.KEYSTORE_ALGORITHM, DiretoriosServidor.KEYSTORE_PATH,
				passAdmin, ConstantesKeyStore.KEYSTORE_ALIAS_ADMIN, ConstantesKeyStore.KEYSTORE_PASS))
			return false;
		else {
			return atualizarMac(credenciaisPath, passAdmin, macPath);
		}
	}

	public static boolean verificarPass(String passAdmin) {

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

	public static boolean atualizarMac(String credenciaisPath, String passAdmin, String macPath) {
		byte[] mac = gerarMac(credenciaisPath, passAdmin);
		if (mac.length < 1)
			return false;
		return updateMac(mac, macPath);
	}

	private static boolean updateMac(byte[] mac, String macPath) {

		try (FileOutputStream out = new FileOutputStream(macPath)) {
			out.write(mac);
		}
		catch (IOException e) {
			System.out.println("Erro a escrever o BigMac");
			return false;
		}
		return true;
	}

}
