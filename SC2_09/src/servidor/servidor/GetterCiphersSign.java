package servidor.servidor;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class GetterCiphersSign {

	private GetterCiphersSign() {
	}

	public static Cipher gerarCipher(Key publicKey, int mode, String alg) {
		try {
			Cipher c = Cipher.getInstance(alg);
			c.init(mode, publicKey);
			return c;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {

			System.err.println("Erro a gerar cifra");
			e.printStackTrace();
			return null;
		}
	}

	public static Signature gerarSignAss(PrivateKey key, String alg) {
		try {
			Signature s = Signature.getInstance(alg);
			s.initSign(key);
			return s;
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Signature gerarSignVer(PublicKey key, String alg) {
		try {
			Signature s = Signature.getInstance(alg);
			s.initVerify(key);
			return s;
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
