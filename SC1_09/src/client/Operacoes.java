package client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import ligacao.Ligacao;
import ligacao.Mensagem;
import ligacao.Opcoes;
import ligacao.RespostasPadrao;

/**
 * Classe das operacoes do cliente 
 * 
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class Operacoes {

	private Ligacao ligacao;

	public Operacoes(Socket socket) {
		ligacao = new Ligacao(socket);
	}

	public String autenticacaoClient(String user, String pass, boolean registar) {

		Opcoes operacao = registar ? Opcoes.REGISTAR : Opcoes.AUTENTICAR;

		Mensagem msg = new Mensagem(user, pass, operacao);

		ligacao.enviarMensagem(msg);

		return ligacao.receberMensagem().getTexto();
	}

	public void desligarLigacao() {
		ligacao.desligarLigacao();
	}

	public String operar(String userCurr, String[] operacao) {

		Mensagem msg = new Mensagem(userCurr, operacao);

		Mensagem msgResposta = operacaoPadrao(msg);

		return msgResposta.getTexto();

	}

	private Mensagem operacaoPadrao(Mensagem msg) {
		ligacao.enviarMensagem(msg);
		Mensagem msgResposta = ligacao.receberMensagem();

		if (msg.getOperacao() == Opcoes.ADICIONAR) {
			if (!msgResposta.getTexto().equals(RespostasPadrao.FOTO_EXISTE_SERVER)) {
				msgResposta = ligacao.enviarImagem(msg.getFotoID());
			}
		}
		else if (msg.getOperacao() == Opcoes.STALKER && (!(msgResposta.getTexto().equals(RespostasPadrao.NAO_SEGUE)
				|| msgResposta.getTexto().equals(RespostasPadrao.PROPRIO)
				|| msgResposta.getTexto().equals(RespostasPadrao.NAO_TEM_FOTOS)))) {
			receberFotos(msgResposta, msg.getUserID());
			return new Mensagem(Opcoes.STALKER, msg.getUserCurr(), "Correu tudo bem");
		}

		return msgResposta;
	}

	private static boolean criarPasta(String diretorio) {
		File file = new File(diretorio);
		return file.mkdir();
	}

	private void receberFotos(Mensagem msg, String userID) {
		String[] listaFotos = msg.getTexto().split("\n");
		String diretorio = (userID + File.separator);
		for (int i = 0; i < listaFotos.length; i++) {
			criarPasta(diretorio);
			ligacao.receberImagem(msg.getUserCurr(), listaFotos[i], diretorio);
			Mensagem msgComentario = ligacao.receberMensagem();

			StringBuilder comentarioPath = new StringBuilder(diretorio);
			comentarioPath.append(listaFotos[i]);
			comentarioPath.delete(comentarioPath.length() - 4, comentarioPath.length());
			comentarioPath.append(".txt");

			try {
				escreverComentarios(msgComentario, comentarioPath.toString());
			}
			catch (IOException e) {
				System.err.println("Erro a escrever os comentarios no ficheiro");
				System.err.println(e.getMessage());
			}

		}

	}

	private void escreverComentarios(Mensagem msgComentario, String fotoPath) throws IOException {

		String comentarios = msgComentario.getTexto();

		if (comentarios.length() > 0) {
			File file = new File(fotoPath);
			file.createNewFile();

			try (FileOutputStream fos = new FileOutputStream(file)) {

				fos.write(comentarios.getBytes());
			}
		}

	}
}
