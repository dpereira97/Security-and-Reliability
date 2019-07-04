package servidor;

/**
 * Classe com os algoritmos 
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class ConstantesAlg {

	private ConstantesAlg() {
	}

	public static final String MAC_ALGORITHM = "HmacSHA256";
	public static final String MD_ALGORITHM = "SHA-256";
	public static final String SKEYFAC_ALGORITHM_BUEDA_GRNADE = "PBEWithHmacSHA256AndAES_128";
	public static final String KEYSTORE_ALGORITHM = "JCEKS";
	public static final String KEY_SIMETRICA_ALGORITHM= "AES";
	
	public static final String WRAP_ALGORITHM = "RSA";
	public static final String SIGN_ALGORITHM = "SHA256withRSA";

}
