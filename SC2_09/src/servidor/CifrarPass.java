package servidor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe responsavel por cifrar passwords
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class CifrarPass {


	private CifrarPass() {
	}

	public static String passwordSalgada(String saltText, String password) {

		String saltPassword = saltText + password;
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(ConstantesAlg.MD_ALGORITHM);
			byte[] buf = saltPassword.getBytes();

			return passHex(md.digest(buf));
		}
		catch (NoSuchAlgorithmException e) {
			return null;
		}


	}
	
	private static String passHex(byte[] passwordSalgada) {
		
		StringBuilder sb = new StringBuilder();
		
		for (byte b : passwordSalgada) {
			sb.append(Integer.toHexString(b & 0xFF));
		}
		
		return sb.toString();
		
	}


}
