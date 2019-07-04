package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import ligacao.RespostasPadrao;

/**
 * Class do servidor que contem um main que requer receber nos argumentos o
 * numero do porto
 * 
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
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

			int port = Integer.parseInt(args[0]);

			System.out.println(":server: " + port);

			PhotoShareServer server = new PhotoShareServer();
			server.startServer(port);

		}
	}

	/**
	 * Comecar o servidor
	 * 
	 * @param port
	 *            porto no qual vai ser aberto o servidor
	 */
	private void startServer(int port) {

		ServerSocket serverSocket = null;
		Socket socket = null;

		BaseDados.getInstance().recuperarInformacao();

		AtualizarBackup time = new AtualizarBackup();
		Timer timer = new Timer("nomeRandom", true);
		timer.schedule(time, 1000, 5000);

		Imprimir imprimir = new Imprimir();
		imprimir.start();

		try {
			serverSocket = new ServerSocket(port);

			while (correr) {

				socket = serverSocket.accept();

				System.out.println(":Thread:");
				ServerThread newServerThread = new ServerThread(socket);
				System.out.println(":Inicio da Thread:");
				newServerThread.start();

			}

			serverSocket.close();

		}
		catch (IOException e) {
			System.err.println("Erro na inicializacao do servidor");
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
		@Override
		public void run() {

			Scanner sc = new Scanner(System.in);
			while (correr) {
				String linha = sc.nextLine();
				if (linha.equals(":bd")) {
					System.out.println(BaseDados.getInstance().toString());
				}
				else if(linha.equals(":q")) {
					correr = false;
					
				}

			}
			sc.close();
		}
	}

}
