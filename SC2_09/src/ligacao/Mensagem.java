package ligacao;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Classe cujo os objetos sao Mensagens
 * 
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class Mensagem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Opcoes operacao;
	private String userCurr;
	private String pass;

	private String userID;
	private String fotoID;
	private String[] listaUsers;

	private String texto;

	/**
	 * Construtor que cria uma mensagem de login ou de autenticacao
	 * 
	 * @param userCurr
	 * @param pass
	 * @param opcao
	 */
	public Mensagem(String userCurr, String pass, Opcoes opcao) {

		this.userCurr = userCurr;
		this.pass = pass;
		this.operacao = opcao;

	}

	/**
	 * Construtor que cria uma mensagem de resposta
	 * 
	 * @param operacao
	 * @param resposta
	 */
	public Mensagem(Opcoes operacao, String userCurr, String resposta) {
		this.userCurr = userCurr;
		this.texto = resposta;
		this.operacao = operacao;
	}

	/**
	 * Metodo auxiliar para a construcao da mensagem
	 * 
	 * @param operacao
	 */
	private void completarMensagem(String[] operacao) {
		switch (this.operacao) {

		case ADICIONAR:
			this.fotoID = operacao[1];
			break;
			
		case LISTAFOTO:
		case STALKER:
			this.userID = operacao[1];
			break;
			
		case FOTOSTATUS:
		case JOINHA:
		case DISLIKE:
			this.userID = operacao[1];
			this.fotoID = operacao[2];
			break;

		case FOLLOWER:
		case UNFOLLOWER:
			this.listaUsers = Arrays.copyOfRange(operacao, 1, operacao.length);
			break;

		case COMENTAR:
			this.texto = operacao[1];
			this.userID = operacao[2];
			this.fotoID = operacao[3];
			break;

		default:
			break;
		}

	}

	/**
	 * Construtor da mensagem da operacao do servidor
	 * 
	 * @param userCurr
	 * @param operacao
	 */
	public Mensagem(String userCurr, String[] operacao) {
		this.userCurr = userCurr;
		this.operacao = Opcoes.getOpcao(operacao[0]);

		completarMensagem(operacao);
	}

	public Opcoes getOperacao() {
		return operacao;
	}

	public String getUserCurr() {
		return userCurr;
	}

	public String getPass() {
		return pass;
	}

	public String getUserID() {
		return userID;
	}

	public String getFotoID() {
		return fotoID;
	}

	public String[] getListaUsers() {
		return listaUsers;
	}

	public String getTexto() {
		return texto;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("#####MENSAGEM####");

		sb.append("operacao: " + operacao + "\n");
		sb.append("userCurr: " + userCurr + "\n");
		sb.append("pass: " + pass + "\n");
		sb.append("userID: " + userID + "\n");
		sb.append("fotoID: " + fotoID + "\n");
		
		sb.append("Lista de user: ");
		if (listaUsers != null) {
			for (String nome : listaUsers) {
				sb.append(nome + ", ");
			}
		}
		
		sb.append("\n");
		sb.append("texto: " + texto);

		sb.append("\n#####MENSAGEM####");
		return sb.toString();
	}
}
