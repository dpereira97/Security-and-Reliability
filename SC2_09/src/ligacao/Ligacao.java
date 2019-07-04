package ligacao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import objectos.Foto;

/**
 * Classe que trata da ligacao entre o servidor e cliente e viceversa
 * 
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class Ligacao {

	protected static final int MAXSIZE = 1024;

	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Socket socket;

	public Ligacao(Socket socket) {
		try {
			this.out = new ObjectOutputStream(socket.getOutputStream());

			this.in = new ObjectInputStream(socket.getInputStream());
			this.socket = socket;
		}
		catch (IOException e) {
			System.err.println("Erro na criacao dos stream de conversa");
			e.printStackTrace();
		}
	}

	protected ObjectInputStream getIn() {
		return in;
	}

	protected ObjectOutputStream getOut() {
		return out;
	}

	public void enviarMensagem(Mensagem msg) {
		try {
			out.writeObject(msg);
			out.flush();
		}
		catch (IOException e) {
			System.err.println("Erro a enviar Mensagem");
			System.err.println(e.getMessage());
		}
	}

	public Mensagem receberMensagem() {

		Mensagem msg = null;
		try {
			msg = (Mensagem) in.readObject();
		}
		catch (ClassNotFoundException | IOException e) {
			System.err.println("Erro a receber a Mensagem");
			System.err.println(e.getMessage());
		}

		return msg;

	}

	public void desligarLigacao() {
		try {
			out.close();
			in.close();
			socket.close();
		}
		catch (IOException e) {
			System.out.println("nao foi possivel desconectar-se ao servidor");
			System.err.println(e.getMessage());
		}
	}

	public void enviarMensagemInt(int numero) {

		try {
			out.writeInt(numero);
			out.flush();
		}
		catch (IOException e) {
			System.err.println("Erro a enviar um INT");
			System.err.println(e.getMessage());
		}
	}

	public int receberMensagemInt() {

		int result = 0;
		try {
			result = in.readInt();
		}
		catch (IOException e) {
			System.err.println("Erro a receber um INT");
			System.err.println(e.getMessage());
		}
		return result;

	}

	public Mensagem enviarImagem(String fotoID) {

		File file = new File(fotoID);
		int bytesLidos = 0;

		byte[] bytes = new byte[MAXSIZE];

		try (FileInputStream fileStream = new FileInputStream(file)) {

			int tamanhoFicheiro = (int) file.length();

			enviarMensagemInt(tamanhoFicheiro);

			while (bytesLidos < tamanhoFicheiro) {
				int bytesEnviar = fileStream.read(bytes, 0, MAXSIZE);

				bytesLidos += bytesEnviar;
				out.write(bytes, 0, bytesEnviar);
				out.flush();

			}
		}
		catch (IOException e) {
			System.err.println("erro a enviar uma IMG");
			System.err.println(e.getMessage());
		}

		return receberMensagem();

	}

	public Foto receberImagem(String userCurr, String nomeFoto, String diretorio) {
		int bytesEscritosFicheiro = 0;
		int tamanhoFicheiro = 0;

		byte[] bytes = new byte[MAXSIZE];

		try (FileOutputStream fileStream = new FileOutputStream(new File(diretorio + nomeFoto))) {

			tamanhoFicheiro = receberMensagemInt();

			while (bytesEscritosFicheiro < tamanhoFicheiro) {

				int bytesRecebidos = in.read(bytes, 0, MAXSIZE);
				fileStream.write(bytes, 0, bytesRecebidos);
				bytesEscritosFicheiro += bytesRecebidos;
				fileStream.flush();

			}

		}
		catch (IOException e) {
			System.err.println("Erro a receber a IMG");
			System.err.println(e.getMessage());
		}

		String resposta = bytesEscritosFicheiro == tamanhoFicheiro ? RespostasPadrao.FOTO_ENVIADA
				: RespostasPadrao.FOTO_NAO_ENVIADA;
		enviarMensagem(new Mensagem(Opcoes.ADICIONAR, userCurr, resposta));

		return new Foto(nomeFoto);
	}

}
