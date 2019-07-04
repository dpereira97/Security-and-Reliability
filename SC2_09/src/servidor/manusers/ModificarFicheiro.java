package servidor.manusers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Classe responsavel por conseguir
 * modificar ficheiros: apagar e mudar o nome
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class ModificarFicheiro {
	private ModificarFicheiro() {
	}

	private static void apagarFicheiro(Path path) throws IOException {
		Files.delete(path);
	}
	
	protected static boolean subFicheiros(String nomeOriginal, String nomeTemp) {
		Path path = Paths.get(nomeOriginal);
		
		try {
			ModificarFicheiro.apagarFicheiro(path);
			
			File file = new File(nomeTemp);
			return file.renameTo(new File(nomeOriginal));
		}
		catch (IOException e) {
			System.err.println("ficheiro nao existe");
			return false;
		}

	}
}
