package servidor;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Classe responsavel gerar chaves
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class GerarChaves {

	private GerarChaves() {
	}

	private static final int INTERATIONS = 20;

	private static final byte[] SALTS = new byte[] { (byte) 0xc9, (byte) 0x36, (byte) 0x78, (byte) 0x99, (byte) 0x52,
			(byte) 0x3e, (byte) 0xea, (byte) 0xf2 };

	public static SecretKey gerarChaveSecretaByPass(String passAdmin) {

		PBEKeySpec keySpec = new PBEKeySpec(passAdmin.toCharArray(), SALTS, INTERATIONS); // pass, salt, iterations
		SecretKeyFactory kf;
		try {
			kf = SecretKeyFactory.getInstance(ConstantesAlg.SKEYFAC_ALGORITHM_BUEDA_GRNADE);
			return kf.generateSecret(keySpec);
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			System.out.println("Erro a criar a chave secreta");
			return null;
		}
	}

	public static SecretKey gerarChave(String algorithm, int tamanhoKey) {

		try {
			KeyGenerator kg = KeyGenerator.getInstance(algorithm);
			kg.init(tamanhoKey);
			return kg.generateKey();
		}
		catch (NoSuchAlgorithmException e) {
			System.err.println("Erro a gerarChave");
			System.err.println(e.getMessage());
			return null;
		}
	}
}
