package servidor.servidor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import servidor.ConstantesAlg;

/**
 * Classe responsavel por assinar um ficheiro, 
 * ler um ficheiro assinado e verificar a assinatura
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class Assinaturas {

	private static final int TAMANHO_SIGN = 256;

	private Assinaturas() {
	}

	public static void assinarFile(String file, PrivateKey key) {
		Signature sign = GetterCiphersSign.gerarSignAss(key, ConstantesAlg.SIGN_ALGORITHM);
		lerFicheiroSign(file, sign);
		try (FileOutputStream fos = new FileOutputStream(file + ".sig")) {
			fos.write(sign.sign());
		} catch (IOException | SignatureException e) {
			e.printStackTrace();
		}
	}

	private static void lerFicheiroSign(String file, Signature sign) {
		try (FileInputStream fis = new FileInputStream(file)) {
			byte[] bytes = new byte[1024];
			while ((fis.read(bytes)) != -1)
				sign.update(bytes);

		} catch (IOException | SignatureException e) {
			e.printStackTrace();
		}
	}

	public static boolean verificarSign(String file, PublicKey key) {

		Signature sign = GetterCiphersSign.gerarSignVer(key, ConstantesAlg.SIGN_ALGORITHM);
		lerFicheiroSign(file, sign);
		byte[] signature = new byte[TAMANHO_SIGN];
		try (FileInputStream fis = new FileInputStream(file + ".sig")) {
			fis.read(signature);
			return sign.verify(signature);
		} catch (IOException | SignatureException e) {
			e.printStackTrace();
			return false;
		}

	}

}
