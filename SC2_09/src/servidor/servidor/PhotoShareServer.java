package servidor.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.SSLServerSocketFactory;

import ligacao.RespostasPadrao;
import servidor.DiretoriosServidor;
import servidor.backup.BaseDados;

/**
 * Class do servidor que contem um main que requer receber nos argumentos o
 * numero do porto
 * 
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
@SuppressWarnings("squid:S106")
public class PhotoShareServer {

	
	private boolean correr = true;

	/**
	 * Metodo main que ira correr o servidor
	 * 
	 * @param args
	 *            lista de argumentos que ira ter o porto do servidor se estiver
	 *            mais do que um argumento dara uma mensagem de erro
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("PhotoShareServer <port>");
		}
		else {

			System.setProperty("javax.net.ssl.keyStore", DiretoriosServidor.SERVER_KEYSTORE);
			System.setProperty("javax.net.ssl.keyStorePassword", "toupas404");
			
			int port = Integer.parseInt(args[0]);

			System.out.println(":server: " + port);

			PhotoShareServer server = new PhotoShareServer();
			server.startServer(port);

		}
	}

	private static String pedirPassAdmin(Scanner sc) {
		System.out.println("Introduza a Palavra-chave do Admin: ");
		return sc.nextLine();
	}

	/**
	 * Comecar o servidor
	 * 
	 * @param port
	 *            porto no qual vai ser aberto o servidor
	 */
	private void startServer(int port) {

		Scanner sc = new Scanner(System.in);
		if (!BaseDados.getInstance().recuperarInformacao(pedirPassAdmin(sc), DiretoriosServidor.CREDENCIAIS_PATH,
				DiretoriosServidor.MAC_PATH)) {
			System.out.println("Nao foi possivel recuperar a informacao. vou terminar");
			sc.close();
			return;
		}
		System.out.println("Recuperamos a base de dados com sucesso");

		Imprimir imprimir = new Imprimir(sc);
		imprimir.start();

		AtualizarBackup time = new AtualizarBackup();
		Timer timer = new Timer("nomeRandom", true);
		timer.schedule(time, 1000, 15000);

		SSLServerSocketFactory sslf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		
		try (ServerSocket serverSocket = sslf.createServerSocket(port)) {
			System.out.println("A espera de clientes");
			while (correr) {
				
				Socket socket = serverSocket.accept();

				System.out.println(":Thread:");
				ServerThread newServerThread = new ServerThread(socket);
				System.out.println(":Inicio da Thread:");
				newServerThread.start();

			}

		}
		catch (IOException e) {
			System.err.println("Erro na inicializacao do servidor");
			e.printStackTrace();
			System.err.println(e.getMessage());
			return;
		}
	}

	/**
	 * 
	 * Uma thread do servidor que ira correr a ligacao do servidor com o cliente
	 * 
	 * @author fc47804 Ze Aguas
	 * @author fc47827 Simao Ferreira
	 * @author fc47888 Diogo Pereira
	 *
	 */
	class ServerThread extends Thread {

		private Socket socket;

		ServerThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {

			OperacoesServer operar = new OperacoesServer(socket);

			String[] userResposta = operar.autenticarCliente();

			if (userResposta == null) {
				System.err.println("Erro na autenticacao");
			}
			else {
				System.out.printf("O utilizador %s foi: ", userResposta[0]);

				System.out.println(userResposta[1]);
				if (userResposta[1].equals(RespostasPadrao.AUTENTICADO)) { // esta autenticado

					operar.operacao(userResposta[0]);
				}
				System.out.println(":Fim da Thread:\n");
			}
		}

	}

	/**
	 * 
	 * Thread que atualiza o ficheiro de backup
	 * 
	 * @author fc47804 Ze Aguas
	 * @author fc47827 Simao Ferreira
	 * @author fc47888 Diogo Pereira
	 * 
	 */
	class AtualizarBackup extends TimerTask {

		public void run() {

			BaseDados.getInstance().atualizarBackup();
		}
	}

	/**
	 * 
	 * Thread que serve de debuff, imprime o que a base de dados tem neste momento
	 * com o comando ":bd"
	 * 
	 * @author fc47804 Ze Aguas
	 * @author fc47827 Simao Ferreira
	 * @author fc47888 Diogo Pereira
	 *
	 */
	class Imprimir extends Thread {
		private Scanner sc;

		public Imprimir(Scanner sc) {
			this.sc = sc;
		}

		@Override
		public void run() {

			while (correr) {
				String linha = sc.nextLine();
				if (linha.equals(":bd")) {
					System.out.println(BaseDados.getInstance().toString());
				}
			}
			sc.close();
		}
	}

}
