package servidor;

import java.io.File;

/**
 * Class que contem as constantes que iram ser utilizadas pelo Servidor
 * 
 * Esta devara sofrer uma alteracao para ser um enum
 * 
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class Constantes {
	public static final String DIRETORIO_SERVIDOR = "servidor" + File.separator;
	public static final String CREDENCIAIS = DIRETORIO_SERVIDOR + "credenciais.txt";
	public static final String BACKUP = DIRETORIO_SERVIDOR + "backup.txt";
}
