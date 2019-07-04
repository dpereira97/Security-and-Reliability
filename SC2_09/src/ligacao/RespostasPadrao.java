package ligacao;

/**
 * Classe com codigos de operacao
 * 
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class RespostasPadrao {

	public static final String NAO_SEGUE = "Nao segue o utilizador!";
	public static final String FOTO_NEXISTE = "Foto nao existe!";
	public static final String PROPRIO = "Nao pode guardar suas fotos";

	/*
	 * Registar/Autenticar
	 */
	public static final String AUTENTICADO = "Sucesso na autenticacao";
	public static final String FAIL_AUTENTICADO = "Insucesso na autenticacao";
	public static final String REGISTADO = "Sucesso no registado";
	public static final String FAIL_REGISTADO = "Insucesso no registado";

	/*
	 * Adicionar Foto
	 */

	public static final String FOTO_EXISTE_SERVER = "A foto ja existe no servidor";
	public static final String FOTO_NAO_EXISTE_SERVER = "A foto nao existe no servidor";
	public static final String FOTO_ENVIADA = "A imagem foi enviada";
	public static final String FOTO_NAO_ENVIADA = "Houve um erro a enviar a imagem";

	/*
	 * Adicionar Seguidor
	 */

	private static final String SEGUIDOR_SUCC = "Seguiu o utilizador %s" + System.lineSeparator();
	private static final String SEGUIDOR_JA_SEGUE = "Ja seguia o utilizador %s" + System.lineSeparator();
	private static final String SEGUIDOR_PROPRIO = "Nao pode seguir-se a si mesmo" + System.lineSeparator();
	private static final String SEGUIDOR_NAO_EXISTE = "O utilizador %s nao existe" + System.lineSeparator();

	public static final String[] SEGUIDOR = { SEGUIDOR_SUCC, SEGUIDOR_JA_SEGUE, SEGUIDOR_NAO_EXISTE, SEGUIDOR_PROPRIO };

	/*
	 * Remover Seguidor
	 */
	private static final String RSEGUIDOR_SUCC = "Deixou de seguir o utilizador %s" + System.lineSeparator();
	private static final String RSEGUIDOR_NAO_SEGUE = "Nao pode deixar de seguir o uilizador %s porque nao o seguia"
			+ System.lineSeparator();
	private static final String RSEGUIDOR_PROPRIO = "Nao pode deixar de seguir a si proprio" + System.lineSeparator();

	public static final String[] RSEGUIDOR = { RSEGUIDOR_SUCC, RSEGUIDOR_NAO_SEGUE, SEGUIDOR_NAO_EXISTE,
			RSEGUIDOR_PROPRIO };

	/*
	 * Dar like
	 */
	private static final String LIKE_SUCC = "Deu tapa o dedao com sucesso!";
	private static final String LIKE_FOTO_NEXISTE = "A foto que esta a tentar dar like nao existe!";

	private static final String LIKE_LIKE = "Nao pode voltar a dar um tapa no dedao nessa foto";

	public static final String[] DEDAO_CIMA = { LIKE_SUCC, LIKE_FOTO_NEXISTE, LIKE_LIKE, NAO_SEGUE };

	/*
	 * Dar dislike
	 */
	private static final String DISLIKE_SUCC = "Deu dislike com sucesso!";
	private static final String DISLIKE_FOTO_NEXISTE = "A foto que esta a tentar dar dislike nao existe!";

	private static final String DISLIKE_DISLIKE = "Nao pode voltar a dar dislike nessa foto";

	public static final String[] DEDAO_BAIXO = { DISLIKE_SUCC, DISLIKE_FOTO_NEXISTE, DISLIKE_DISLIKE, NAO_SEGUE };

	/*
	 * Comentario
	 */

	private static final String COMENTARIO_SUCC = "O seu comentario foi registado!";
	private static final String COMENTARIO_ERROR = "O seu comentario nao foi registado!";

	public static final String[] COMENTARIO = { COMENTARIO_SUCC, COMENTARIO_ERROR, NAO_SEGUE };

	public static final String NAO_TEM_FOTOS = "O utilizador nao tem fotos!";

}
