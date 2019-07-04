package servidor.manusers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import servidor.DiretoriosServidor;
/**
 * Classe responsavel pela instanciacao
 * do manUser
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
@SuppressWarnings("squid:S106")
public class ManUser {

	private static final String UTILIZADOR_JA_EXISTE = "O utilizador ja existe";
	private static final String UTILIZADOR_NAO_EXISTE = "O utilizador nao existe";

	private static final String MENSAGEM_SUCESSO = "A operacao correu com sucesso";
	private static final String MENSAGEM_INSUCESSO = "A operacao nao teve sucesso";
	private static final String MENSAGEM_SAIR = "Obrigado pela visita";
	private static final String MENSAGEM_INVALIDO = "A operacao nao existe";

	private static List<String> listaUtilizadores;

	/**
	 * Ver o que o Admin quer fazer
	 * 
	 * @param sc
	 * 
	 * @return O que admin quer fazer
	 */

	private static String oQueFazer(Scanner sc) {
		System.out.println("Escreva uma das 4: ");
		System.out.println("registar || mudarPass || apagar || sair");
		return sc.nextLine();
	}

	private static boolean existeUser(String nome) {
		return listaUtilizadores.contains(nome);
	}

	private static String operacao(Scanner sc) {
		String oQueFazer = oQueFazer(sc);
		String result;

		switch (oQueFazer) {
		case "registar":
			String nome = pedirNome(sc);
			if (!existeUser(nome)) {
				if (Registar.run(nome, pedirPass(sc))) {
					result = MENSAGEM_SUCESSO;
					listaUtilizadores.add(nome);
				}
				else
					result = MENSAGEM_INSUCESSO;
			}
			else {
				result = UTILIZADOR_JA_EXISTE;
			}

			break;

		case "mudarPass":
			nome = pedirNome(sc);
			String pass = pedirPass(sc);
			if (existeUser(nome)) {
				if (Apagar.run(nome) && Registar.run(nome, pass))
					result = MENSAGEM_SUCESSO;
				else
					result = MENSAGEM_INSUCESSO;
			}
			else {
				result = UTILIZADOR_NAO_EXISTE;
			}

			break;

		case "apagar":
			nome = pedirNome(sc);
			if (existeUser(nome)) {
				if (Apagar.run(nome)) {
					result = MENSAGEM_SUCESSO;
					listaUtilizadores.remove(nome);
				}
				else
					result = MENSAGEM_INSUCESSO;
			}
			else {
				result = UTILIZADOR_NAO_EXISTE;
			}
			break;

		case "sair":
			result = MENSAGEM_SAIR;
			break;

		default:
			result = MENSAGEM_INVALIDO;
		}

		return result;
	}

	private static String pedirNome(Scanner sc) {
		System.out.println("Nome do utilizador: ");
		return sc.nextLine();
	}

	private static String pedirPass(Scanner sc) {
		System.out.println("Nome da pass: ");
		return sc.nextLine();
	}

	private static String pedirPassAdmin(Scanner sc) {
		System.out.println("Introduza a Palavra-chave do Admin: ");
		return sc.nextLine();
	}

	private static void inicializacao(String servidorPath, String credenciaisPath) {
		new File(servidorPath).mkdir();
		new File(DiretoriosServidor.DIRETORIO_KEYSTORE).mkdir();
		criarCredenciais(credenciaisPath);
	}

	private static void criarCredenciais(String credenciaisPath) {
		try (FileOutputStream fos = new FileOutputStream(new File(credenciaisPath))) {
		}
		catch (IOException e) {
			System.err.println("Erro a escrever o ficheiro.");
		}

	}

	private static boolean primeiraVez(String serverFolder) {
		return !new File(serverFolder).exists();
	}

	private static boolean criarMac(String passAdmin, String credenciaisPath, String macPath) {
		return MacManUsers.semMac(passAdmin, credenciaisPath, macPath);
	}

	private static void iniciarListaUtilizador(){
		listaUtilizadores = new ArrayList<>(); 
		try(BufferedReader bis = new BufferedReader(new FileReader(DiretoriosServidor.CREDENCIAIS_PATH))){
			String linha = null;
			while((linha = bis.readLine())!= null)
				listaUtilizadores.add(linha.split(":")[0]);
		}
		catch (IOException e) {
			System.err.println("erro");
		}
	}
	
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		String passAdmin = pedirPassAdmin(sc);

		boolean autenticado;
		if (primeiraVez(DiretoriosServidor.MAC_PATH)) {
			System.out.println("E a primeira vez, inicia");
			inicializacao(DiretoriosServidor.DIRETORIO_SERVIDOR, DiretoriosServidor.CREDENCIAIS_PATH);
			autenticado = criarMac(passAdmin, DiretoriosServidor.CREDENCIAIS_PATH, DiretoriosServidor.MAC_PATH);
		}

		else {
			autenticado = MacManUsers.autenticacao(passAdmin, DiretoriosServidor.CREDENCIAIS_PATH, DiretoriosServidor.MAC_PATH);
		}
		iniciarListaUtilizador();
		if (autenticado) {
			String result;
			do {
				System.out.println();
				result = operacao(sc);

				System.out.println(result);

			}
			while (!result.equals(MENSAGEM_SAIR));
			if (!MacManUsers.atualizarMac(DiretoriosServidor.CREDENCIAIS_PATH, passAdmin, DiretoriosServidor.MAC_PATH)) {
				System.out.println("erro a escrever o mac");
			}
		}
		sc.close();
	}

}
