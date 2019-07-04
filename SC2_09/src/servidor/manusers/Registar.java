package servidor.manusers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

import servidor.CifrarPass;
import servidor.DiretoriosServidor;

/**
 * Classe responsavel por registar
 * utilizadores 
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class Registar {

	private Registar() {
	}

	public static void criarPasta(String nome) {
		File file = new File(DiretoriosServidor.DIRETORIO_SERVIDOR + nome);
		file.mkdir();
	}
	
	public static boolean run(String nome, String pass) {

		String linhaCifrada = linhaCifrada(nome, pass);
		criarPasta(nome);
		return escreverFicheiro(linhaCifrada);
	}

	private static boolean escreverFicheiro(String linhaCifrada) {
		try (BufferedWriter ficheiroUtilizadores = new BufferedWriter(
				new FileWriter(DiretoriosServidor.CREDENCIAIS_PATH, true))) {
			ficheiroUtilizadores.write(linhaCifrada + System.lineSeparator());

			return true;
		}
		catch (IOException e) {
			return false;
		}
	}

	private static String linhaCifrada(String nome, String pass) {
		StringBuilder sb = new StringBuilder();
		sb.append(nome);
		sb.append(":");

		byte[] salt = getNextSalt();

		StringBuilder sb2 = new StringBuilder();
		for (byte b : salt) {
			sb2.append(Integer.toHexString(b & 0xFF));
		}
		String saltText = sb2.toString();
		sb.append(saltText);

		sb.append(":");

		sb.append(CifrarPass.passwordSalgada(saltText, pass));

		return sb.toString();
	}

	private static final Random RANDOM = new SecureRandom();

	private static byte[] getNextSalt() {
		byte[] salt = new byte[16];
		RANDOM.nextBytes(salt);
		return salt;
	}
}
