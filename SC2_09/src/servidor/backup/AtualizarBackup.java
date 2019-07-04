package servidor.backup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

import objectos.Utilizador;
import servidor.ConstantesAlg;
import servidor.ConstantesKeyStore;
import servidor.DiretoriosServidor;
import servidor.KeyStoreUI;
import servidor.servidor.Assinaturas;
import servidor.servidor.Cifrar;

/**
 * Classe por atualizar o ficheiro
 * de backup 
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class AtualizarBackup {

	private AtualizarBackup() {
	}

	/**
	 * Atualiza o backup
	 */
	public static void run(Map<String, Utilizador> listaUtilizadores) {

		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(BaseDados.BACKUP)))) {
			out.writeObject(new HashMap<>(listaUtilizadores));
		} catch (IOException e) {
			System.err.println("Problema a atualizar.");
			System.err.println(e.getMessage());
		}
		
		PrivateKey pk = KeyStoreUI.getPrivateKeyKeystore(ConstantesAlg.KEYSTORE_ALGORITHM, DiretoriosServidor.SERVER_KEYSTORE,
				ConstantesKeyStore.KEYSTORE_ALIAS_SERVER, ConstantesKeyStore.KEYSTORE_PASS);
		Assinaturas.assinarFile(BaseDados.BACKUP, pk);
		
		Cifrar.cifrarFicheiro(BaseDados.BACKUP, BaseDados.BACKUP + ".temp");
		Cifrar.apagarFile(BaseDados.BACKUP);
		Cifrar.mudarNomeFiles(BaseDados.BACKUP + ".temp", BaseDados.BACKUP);

	}

}
