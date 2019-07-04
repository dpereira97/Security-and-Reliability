package servidor.manusers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import servidor.DiretoriosServidor;

/**
 * Classe que trata de apagar um utilizador
 * apagando a linha correspondente do ficheiro
 * credenciais
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class Apagar {

	private Apagar() {
	}

	public static boolean run(String nome) {
		try {
			copiarFicheiro(nome);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return ModificarFicheiro.subFicheiros(DiretoriosServidor.CREDENCIAIS_PATH, DiretoriosServidor.CREDENCIAIS_PATH + "temp");
	}

	private static void copiarFicheiro(String nome) throws IOException {
		BufferedWriter bw = null;
		try (BufferedReader br = new BufferedReader(new FileReader(DiretoriosServidor.CREDENCIAIS_PATH))) {
			bw = new BufferedWriter(new FileWriter(DiretoriosServidor.CREDENCIAIS_PATH + "temp"));

			String linha = null;
			while ((linha = br.readLine()) != null) {
				if (!linha.split(":")[0].equals(nome)) {
					bw.write(linha);
					bw.newLine();
				}
			}

		}
		catch (IOException e) {
			System.err.println("Erro na copia do ficheiro");
		}
		finally {
			try {
				if (bw != null)
					bw.close();
			}
			catch (IOException e) {
				System.err.println("Erro na copia do ficheiro");
			}

		}
	}
}
