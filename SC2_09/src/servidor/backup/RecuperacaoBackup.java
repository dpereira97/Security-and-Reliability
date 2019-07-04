package servidor.backup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import objectos.Utilizador;
import servidor.ConstantesAlg;
import servidor.ConstantesKeyStore;
import servidor.DiretoriosServidor;
import servidor.KeyStoreUI;
import servidor.servidor.Assinaturas;
import servidor.servidor.Cifrar;


/**
 * Classe responsavel por fazer a recuperacao
 * do backup para o servidor
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class RecuperacaoBackup {

	private RecuperacaoBackup() {
	}

	private static String[] carregarNomes(String nomeFicheiro) {

		try (BufferedReader br = new BufferedReader(new FileReader(nomeFicheiro))) {
			List<String> lista = new ArrayList<>();

			String linha = br.readLine();
			while (linha != null) {

				lista.add(linha.split(":")[0]);
				linha = br.readLine();
			}

			return lista.toArray(new String[lista.size()]);
		} catch (IOException e) {
			return new String[0];
		}

	}

	public static Map<String, Utilizador> run(String nomeFicheiroCredenciais) {
		String[] listaNomes = carregarNomes(nomeFicheiroCredenciais);
		Map<String, Utilizador> listaUtilizador;

		if (!new File(BaseDados.BACKUP).exists()) {

			listaUtilizador = recuperarDados(BaseDados.BACKUP);
			

		} else {
			String temp = ".temp";
			if(!Cifrar.decifrarFicheiro(BaseDados.BACKUP, BaseDados.BACKUP + temp, BaseDados.BACKUP + ".key"))
				return new HashMap<>();
			PublicKey pk = KeyStoreUI.getPublicKeyKeystore(ConstantesAlg.KEYSTORE_ALGORITHM,
					DiretoriosServidor.SERVER_KEYSTORE, ConstantesKeyStore.KEYSTORE_ALIAS_SERVER,
					ConstantesKeyStore.KEYSTORE_PASS);

			
			Cifrar.mudarNomeFiles(BaseDados.BACKUP + temp, BaseDados.BACKUP);
			
			
			if (!Assinaturas.verificarSign(BaseDados.BACKUP, pk)) {
				return new HashMap<>();
			}

			listaUtilizador = recuperarDados(BaseDados.BACKUP);
			
			Cifrar.apagarFile(BaseDados.BACKUP);
			
		}

		verificarNovosUtilizadores(listaNomes, listaUtilizador);

		return listaUtilizador;
	}

	private static void verificarNovosUtilizadores(String[] listaNomes, Map<String, Utilizador> listaUtilizador) {
		for (String nome : listaNomes) {
			if (!listaUtilizador.containsKey(nome)) {
				listaUtilizador.put(nome, new Utilizador());
			}
		}

	}

	@SuppressWarnings("unchecked")
	private static Map<String, Utilizador> recuperarDados(String nomeFicheiroDados) {
		try (ObjectInputStream inp = new ObjectInputStream(new FileInputStream(nomeFicheiroDados))) {
			return (HashMap<String, Utilizador>) inp.readObject();
		} catch (ClassNotFoundException | IOException e) {
			return new HashMap<>();
		}
	}

}
