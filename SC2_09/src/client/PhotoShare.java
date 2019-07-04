package client;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

import javax.net.ssl.SSLSocketFactory;

import ligacao.RespostasPadrao;

/**
 * Classe do cliente
 * 
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class PhotoShare {

	private static final String CLIENT_TRUSTSTORE = "myClient.keyStore";
	private static final String FOTO_NAO_EXISTE_CLIENT = "A foto nao existe no cliente.";

	/**
	 * Transformar o args no tipo de user pass ip -x ... quando a pass nao e enviada
	 * logo
	 * 
	 * @param args
	 * @return array de string
	 */
	private static String[] pedirPass(String[] args) {
		String[] params = new String[args.length + 1];

		params[0] = args[0];

		Scanner leitor = new Scanner(System.in);
		System.out.print("Introduza a password: ");
		params[1] = leitor.next();

		leitor.close();

		for (int i = 1; i < args.length; i++) {
			params[i + 1] = args[i];
		}

		return params;
	}

	public static void main(String[] args) {

		System.setProperty("javax.net.ssl.trustStore", CLIENT_TRUSTSTORE);
		System.setProperty("javax.net.ssl.keyStorePassword", "toupas404");

		System.out.println(":client:");

		String[] params = args;
		// quer registar mas nao pos a pass
		if (args.length == 2) {
			params = pedirPass(args);
		}

		// fazer uma accao sem ter posto a pass
		else if (args.length > 3 && args[2].charAt(0) == '-') {
			// nao colocou a pass (user ip -x)
			params = pedirPass(args);
		}

		else if (args.length < 3) {

			System.out.println("PhotoShare <localUserId> <password> <serverAddress>  \n"
					+ "     [ -a <photos> | -l <userId>  | -i <userId> <photo> | -g <userId> |    \n"
					+ "       -c <comment> <userId> <photo> | -L <userId> <photo> |  \n"
					+ "       -D <userId> <photo> | -f <followUserIds>  | -r <followUserIds> ]");

			return;
		}

		PhotoShare client = new PhotoShare();
		client.startClient(params);
	}

	private Socket criarSocket(String addrPort) {
		String[] addrPortArray = addrPort.split(":");
		int port = Integer.parseInt(addrPortArray[1]);

		Socket socket = null;
		try {
			SSLSocketFactory sslf = (SSLSocketFactory) SSLSocketFactory.getDefault();
			socket = sslf.createSocket(addrPortArray[0], port);
		}
		catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return socket;
	}

	private boolean existeFotoCliente(String[] params) {
		return !(params.length > 3 && params[3].equals("-a") && (!new File(params[4]).exists()));
	}

	private void startClient(String[] params) {

		if (!existeFotoCliente(params)) {
			System.out.println(FOTO_NAO_EXISTE_CLIENT);
			return;
		}

		System.out.println(":A estabelecer a ligacao:");

		boolean registar = false;
		if (params.length == 3) {
			System.out.println(":signup:");
			registar = true;
		}

		Socket socket = criarSocket(params[2]);
		if (socket == null) {
			System.out.println("erro na criacao do socket");
			return;
		}

		Operacoes operar = new Operacoes(socket);

		System.out.println(":Ligacao feita:");

		System.out.println(":Verificar autentificacao:");

		String resposta = operar.autenticacaoClient(params[0], params[1], registar);
		System.out.println(resposta);

		if (resposta.equals(RespostasPadrao.AUTENTICADO)) {
			String result = operar.operar(params[0], Arrays.copyOfRange(params, 3, params.length));
			System.out.println(result);
		}

		operar.desligarLigacao();
		System.out.println(":Ligacao Fechada:");

	}
}
