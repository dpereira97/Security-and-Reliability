package servidor;

import java.io.File;

/**
 * Classe com os caminhos relevantes para o servidor
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class DiretoriosServidor {

	private DiretoriosServidor() {
	}

	public static final String DIRETORIO_SERVIDOR = "servidor" + File.separator;
	public static final String CREDENCIAIS_PATH = DIRETORIO_SERVIDOR + "credenciais.txt";
	public static final String MAC_PATH = DIRETORIO_SERVIDOR + "mactext.txt";
	public static final String SERVER_KEYSTORE = "servidor/keystore/myServer.keyStore";
	public static final String DIRETORIO_KEYSTORE = DIRETORIO_SERVIDOR + "keystore" + File.separator;
	public static final String BACKUP = DIRETORIO_SERVIDOR + "backup.txt";
	public static final String KEYSTORE_PATH = DIRETORIO_KEYSTORE + "keystore.ks";

}
