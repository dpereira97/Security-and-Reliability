package servidor;

import java.io.File;
import java.net.Socket;
import java.util.Collection;

import ligacao.Ligacao;
import ligacao.Mensagem;
import ligacao.Opcoes;
import ligacao.RespostasPadrao;
import objectos.Comentario;
import objectos.Foto;

/**
 * 
 * Class que ira coordenar as operacao que iram ser feitas quando o cliente pede
 * algo
 * 
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class OperacoesServer {

	private Ligacao ligacao;

	/**
	 *
	 * Construtor
	 *
	 * @param socket
	 *            Socket da ligacao entre o cliente e o servidor
	 */
	public OperacoesServer(Socket socket) {
		this.ligacao = new Ligacao(socket);
	}

	/**
	 * 
	 * Metodo que ira autenticar ou registar o cliente dependendo do que este pedir
	 * 
	 * @return um array com tamanho 2 onde no primeiro espaço esta o nome do
	 *         utilizador e no segundo esta uma string com a resposta de se foi
	 *         autenticado ou nao, esta resposta e uma constante que se encontra
	 *         noutra class
	 */
	public String[] autenticarCliente() {

		String[] userResposta = null;

		Mensagem msgRecebida = ligacao.receberMensagem();

		if (msgRecebida == null) {
			return userResposta;
		}

		userResposta = new String[2];

		userResposta[0] = msgRecebida.getUserCurr();
		String pass = msgRecebida.getPass();
		Opcoes operacao = msgRecebida.getOperacao();

		boolean resposta = false;

		if (operacao == Opcoes.REGISTAR) {

			resposta = BaseDados.getInstance().registar(userResposta[0], pass);
			userResposta[1] = resposta ? RespostasPadrao.REGISTADO : RespostasPadrao.FAIL_REGISTADO;

		} else {

			resposta = BaseDados.getInstance().autenticar(userResposta[0], pass);
			userResposta[1] = resposta ? RespostasPadrao.AUTENTICADO : RespostasPadrao.FAIL_AUTENTICADO;

		}

		ligacao.enviarMensagem(new Mensagem(operacao, userResposta[0], userResposta[1]));

		return userResposta;
	}

	/**
	 * 
	 * Metodo que ira receber a operacao que o utlizador quer e em seguida executara
	 * outro metodo dependendo do que o utilizador quiser Verifica se o utilizador
	 * corrente e igual ao utilizador que envia a mensagem
	 * 
	 * @param userCurr
	 *            o nome do Utilizador corrente
	 */
	public void operacao(String userCurr) {

		Mensagem msg = ligacao.receberMensagem();

		if (!msg.getUserCurr().equals(userCurr)) {
			System.out.println("Nao devia ter entrado aqui!");
			ligacao.enviarMensagem(new Mensagem(msg.getOperacao(), userCurr, "ERROR"));
			return;
		}

		switch (msg.getOperacao()) {
		case ADICIONAR:
			adicionarFoto(msg);
			break;

		case FOLLOWER:
		case UNFOLLOWER:
			seguidores(msg);
			break;

		case JOINHA:
		case DISLIKE:
			avaliarFoto(msg);
			break;

		case COMENTAR:
			comentario(msg);
			break;

		case FOTOSTATUS:
			fotoEstado(msg);
			break;

		case LISTAFOTO:
			listaFotos(msg);

			break;

		case STALKER:
			stalker(msg);

			break;

		default:

			break;
		}

	}

	/**
	 * Metodo Auxiliar para o comando "-g" quando este e recebido pelo cliente
	 * 
	 * @param msg
	 *            Mensagem recebida pelo cliente
	 */
	private void stalker(Mensagem msg) {
		String userCurr = msg.getUserCurr();
		String userID = msg.getUserID();

		if (userCurr.equals(userID))
			ligacao.enviarMensagem(new Mensagem(msg.getOperacao(), userCurr, RespostasPadrao.PROPRIO));

		else {
			if (BaseDados.getInstance().existeSeguidor(userCurr, userID)) {
				if (BaseDados.getInstance().temFotos(userID)) {
					copiarFotos(userCurr, userID);
				} 
				else {
					System.out.println("ola");
					ligacao.enviarMensagem(new Mensagem(msg.getOperacao(), userCurr, RespostasPadrao.NAO_TEM_FOTOS));
				}

			} else
				ligacao.enviarMensagem(new Mensagem(msg.getOperacao(), userCurr, RespostasPadrao.NAO_SEGUE));
		}

	}

	/**
	 * Envia as fotos todas e os comentarios de cada uma do userID para o userCurr
	 * 
	 * @param userCurr
	 *            Utilizador corrente
	 * @param userID
	 *            Utilizador do qual vamos copiar as fotos
	 */
	private void copiarFotos(String userCurr, String userID) {
		String fotoPath = Constantes.DIRETORIO_SERVIDOR + userID + File.separator;
		Collection<Foto> fotos = BaseDados.getInstance().getFotos(userID);

		StringBuilder sb = new StringBuilder();

		for (Foto foto : fotos) {
			sb.append(foto.getNome() + "\n");
		}
		sb.deleteCharAt(sb.length() - 1);
		ligacao.enviarMensagem(new Mensagem(Opcoes.STALKER, userCurr, sb.toString()));

		for (Foto foto : fotos) {
			ligacao.enviarImagem(fotoPath + foto.getNome());

			Comentario[] comentarios = foto.getListaComentarios();
			StringBuilder resposta = new StringBuilder();

			for (Comentario comentario : comentarios) {
				resposta.append(comentario + "\n");
			}

			ligacao.enviarMensagem(new Mensagem(Opcoes.STALKER, userCurr, resposta.toString()));
		}

	}

	/**
	 * Envia os comentarios o numero de joinhas e dislike para o cliente que se
	 * encontra na msg
	 * 
	 * @param msg
	 *            Mensagem recebida pelo cliente
	 */
	private void fotoEstado(Mensagem msg) {
		String userCurr = msg.getUserCurr();
		String userID = msg.getUserID();
		String fotoID = msg.getFotoID();

		String resposta;
		if (BaseDados.getInstance().existeSeguidor(userCurr, userID) || userCurr.equals(userID)) {
			if (BaseDados.getInstance().existeFoto(userID, fotoID)) {
				resposta = "A foto " + fotoID + " do utilizador " + userID + " tem:\n"
						+ BaseDados.getInstance().getFotoStatus(userID, fotoID);
			} else {
				resposta = RespostasPadrao.FOTO_NAO_EXISTE_SERVER;
			}
		} else {
			resposta = RespostasPadrao.NAO_SEGUE;
		}

		ligacao.enviarMensagem(new Mensagem(msg.getOperacao(), msg.getUserCurr(), resposta));

	}

	/**
	 * Envia o nome das fotos que o cliente que se encontra no userID da msg tem
	 * para o userCurr que tambem se encontra na msg
	 * 
	 * @param msg
	 *            Mensagem recebida pelo cliente
	 */
	private void listaFotos(Mensagem msg) {

		String userCurr = msg.getUserCurr();
		String userID = msg.getUserID();

		String resposta;
		if (BaseDados.getInstance().existeSeguidor(userCurr, userID) || userCurr.equals(userID)) {
			resposta = "O Utilizador " + userID + " tem as seguintes fotos:\n"
					+ BaseDados.getInstance().listaFotosStatus(userID);
		} else {
			resposta = RespostasPadrao.NAO_SEGUE;
		}

		ligacao.enviarMensagem(new Mensagem(msg.getOperacao(), msg.getUserCurr(), resposta));
	}

	/**
	 * 
	 * Metodo que ira receber uma foto e um nome de um utilizador e caso estes
	 * existam ira comentar essa foto. E executado caso o cliente envie um "-c"
	 * 
	 * @param msg
	 *            Mensagem recebida pelo cliente
	 */
	private void comentario(Mensagem msg) {

		String userCurr = msg.getUserCurr();

		String user = msg.getUserID();
		String foto = msg.getFotoID();
		String comentario = msg.getTexto();

		if (BaseDados.getInstance().existeSeguidor(userCurr, user)) {
			if (BaseDados.getInstance().existeFoto(user, foto)) {
				BaseDados.getInstance().adicionarComentario(userCurr, user, foto, comentario);

				ligacao.enviarMensagem(new Mensagem(Opcoes.COMENTAR, userCurr, RespostasPadrao.COMENTARIO[0]));
			} else {
				ligacao.enviarMensagem(new Mensagem(Opcoes.COMENTAR, userCurr, RespostasPadrao.COMENTARIO[1]));
			}
		} else {
			ligacao.enviarMensagem(new Mensagem(Opcoes.COMENTAR, userCurr, RespostasPadrao.COMENTARIO[2]));
		}

	}

	/**
	 * 
	 * Metodo que ira avaliar uma foto com like ou dislike, esta foto e recebida
	 * pelo cliente atraves do nome do utilizador que tem a foto e do nome da foto
	 * Este metodo e executado caso o cliente envie um "-L" para like ou "-D" para
	 * dislike
	 * 
	 * @param msg
	 *            Mensagem recebida pelo cliente
	 * 
	 */
	private void avaliarFoto(Mensagem msg) {

		String userCurr = msg.getUserCurr();
		String userID = msg.getUserID();
		String fotoID = msg.getFotoID();

		boolean dedao = msg.getOperacao() == Opcoes.JOINHA;

		String[] respostas;
		if (msg.getOperacao() == Opcoes.JOINHA) {
			respostas = RespostasPadrao.DEDAO_CIMA;
		} else {
			respostas = RespostasPadrao.DEDAO_BAIXO;
		}

		String resposta;

		if (BaseDados.getInstance().existeSeguidor(userCurr, userID)) {

			if (BaseDados.getInstance().existeFoto(userID, fotoID)) {

				Boolean dedaoAnterior = BaseDados.getInstance().avaliarFoto(userCurr, userID, fotoID, dedao);

				if (dedaoAnterior != null && dedaoAnterior == dedao) { // Deu algo que ja tinha dado
					resposta = respostas[2];
				} else { // tudo baril
					resposta = respostas[0];
				}
			} else { // foto nao existe
				resposta = respostas[1];
			}
		} // fim do userCurr segue o user
		else { // nao segue
			resposta = respostas[3];
		}

		ligacao.enviarMensagem(new Mensagem(msg.getOperacao(), userCurr, resposta));

	}

	/**
	 * 
	 * Metodo que ira adicionar ou retirar seguidores da lista de seguidores do
	 * utilizador corrente. Este metodo e executado caso o cliente envie um "-F"
	 * para adicionar ou "-r" para retirar
	 * 
	 * @param msg
	 *            Mensagem recebida pelo cliente
	 */
	private void seguidores(Mensagem msg) {

		String userCurr = msg.getUserCurr();

		String[] listaUtilizadores = msg.getListaUsers();
		boolean joinha = msg.getOperacao() == Opcoes.FOLLOWER;

		StringBuilder sb = new StringBuilder();

		for (String userID : listaUtilizadores) {
			sb.append(seguidor(userCurr, userID, joinha) + "\n");
		}
		sb.deleteCharAt(sb.length() - 1);
		ligacao.enviarMensagem(new Mensagem(msg.getOperacao(), userCurr, sb.toString()));

	}

	/**
	 * 
	 * Metodo auxiliar do seguidores, este ja ira alterar a base de dados
	 * 
	 * @param userCurr
	 *            Utilizador Corrente
	 * 
	 * @param userID
	 *            user que ira ser seguido pelo userCurr
	 * 
	 * @param adicionar
	 *            um boolean que se for true quer dizer que o cliente quer adicionar
	 *            a sua lista de seguidores e se for false quer retirar da sua lista
	 *            de seguidores
	 */
	private String seguidor(String userCurr, String userID, boolean joinha) {

		String[] gereSeguidor;
		if (joinha) {
			gereSeguidor = RespostasPadrao.SEGUIDOR;
		} else {
			gereSeguidor = RespostasPadrao.RSEGUIDOR;
		}
		String resposta;
		if (userID.equals(userCurr)) {
			resposta = gereSeguidor[3];
		}

		else {

			if (BaseDados.getInstance().existeUtilizador(userID)) {

				if (condicaoSeguidor(userCurr, userID, joinha)) {
					BaseDados.getInstance().seguidor(userCurr, userID, joinha);
					resposta = String.format(gereSeguidor[0], userID);
				} else {
					resposta = String.format(gereSeguidor[1], userID);
				}

			}

			else {
				resposta = (String.format(gereSeguidor[2], userID));
			}
		}

		return resposta;

	}

	/**
	 * 
	 * Metodo auxiliar de seguidor, verifica se pode ou nao adicionar consoante ja
	 * esta adiciona a lista ou nao Caso ja esteja na lista nao e possivel
	 * adicionar, caso ainda nao esteja na lista nao e possivel remover
	 * 
	 * @param usercurr
	 *            Utilizador Corrente
	 * @param user
	 *            Utilizador que se quer adicionar ou retirar
	 * @param adicionar
	 *            um boolean que se for true quer dizer que o cliente quer adicionar
	 *            a sua lista de seguidores e se for false quer retirar da sua lista
	 *            de seguidores
	 * 
	 * @return um boolean caso posso ou nao posso adicionar ou retirar da lista
	 */
	private boolean condicaoSeguidor(String usercurr, String user, boolean adicionar) {

		boolean existeSeguidor = BaseDados.getInstance().existeSeguidor(usercurr, user);

		return (!existeSeguidor && adicionar) || (existeSeguidor && !adicionar);
	}

	/**
	 * 
	 * Adiciona uma foto a basa de dados ao utilizador currente, a foto e enviada
	 * pelo cliente
	 * 
	 * @param msg
	 *            Mensagem recebida pelo cliente
	 */
	private void adicionarFoto(Mensagem msg) {
		System.out.println(":adicionar foto:");

		String userCurr = msg.getUserCurr();
		String nomeFoto = msg.getFotoID();

		boolean existeFoto = BaseDados.getInstance().existeFoto(userCurr, nomeFoto);

		String resposta = existeFoto ? RespostasPadrao.FOTO_EXISTE_SERVER : RespostasPadrao.FOTO_NAO_EXISTE_SERVER;
		ligacao.enviarMensagem(new Mensagem(Opcoes.ADICIONAR, userCurr, resposta));

		if (!existeFoto) {

			String diretorio = Constantes.DIRETORIO_SERVIDOR + userCurr + "/";
			Foto foto = ligacao.receberImagem(userCurr, nomeFoto, diretorio);
			BaseDados.getInstance().adicionarFoto(userCurr, foto);
		}

		System.out.println("::fim de adicionar foto::");

	}
}
