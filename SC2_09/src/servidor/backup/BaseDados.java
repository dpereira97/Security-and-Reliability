package servidor.backup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import objectos.Foto;
import objectos.Utilizador;
import servidor.CifrarPass;
import servidor.DiretoriosServidor;
import servidor.MacValidacao;

/**
 * Class Singleton da base de dados
 * 
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */

public class BaseDados {

	protected static final String BACKUP = DiretoriosServidor.DIRETORIO_SERVIDOR + "backup.txt";

	private Map<String, Utilizador> listaUtilizadores;

	private static BaseDados instance = null;

	/**
	 * Metodo que retorna a instancia da class singleton
	 * 
	 * @return a instancia da class
	 */
	public static BaseDados getInstance() {
		if (instance == null)
			instance = new BaseDados();
		return instance;

	}

	/**
	 * Construtor
	 */
	private BaseDados() {
	}

	/**
	 * Metodo toString para debug
	 */
	public String toString() {

		StringBuilder sb = new StringBuilder("######Base Dados######\n");

		int i = 0;
		sb.append("\n");
		sb.append("#################\n");

		sb.append("###DADOS###\n");

		i = 0;
		for (Entry<String, Utilizador> entry : listaUtilizadores.entrySet()) {
			i++;
			sb.append(i + " Utilizador: " + entry.getKey() + ":\n");
			sb.append(entry.getValue().toString() + "\n");

			sb.append("Fim utilizador\n\n");
		}
		sb.delete(sb.length() - 2, sb.length());

		return sb.toString();
	}

	/**
	 * Verifica se o utilizador ja esta presente na base de dados e se a sua
	 * password e igual a pass
	 * 
	 * @param user
	 *            utilizador em questao
	 * @param pass
	 *            que se tenta autenticar
	 * @return True caso consiga se autenticar, False se nao
	 */
	public boolean autenticar(String user, String pass) {

		String[] linha = obterlinha(user);

		if (linha.length < 2) {
			return false;
		}

		String passwordSalgada = CifrarPass.passwordSalgada(linha[1], pass);

		return passwordSalgada.equals(linha[2]);
	}

	private String[] obterlinha(String user) {

		try (BufferedReader br = new BufferedReader(new FileReader(DiretoriosServidor.CREDENCIAIS_PATH))) {
			String[] linha = br.readLine().split(":");
			while (!linha[0].equals(user)) {
				linha = br.readLine().split(":");
			}
			return linha;
		} catch (NullPointerException | IOException e) {
			return new String[0];
		}
	}

	/**
	 * Recupera as credenciais e os dados, este metodo serve para ser utilizado
	 * quando o servidor vai a baixo
	 * 
	 * @param credenciaisPath
	 * @param macPath
	 * @param string
	 * @return
	 */
	public boolean recuperarInformacao(String passAdmin, String credenciaisPath, String macPath) {

		if (!MacValidacao.autenticacao(passAdmin, credenciaisPath, macPath))
			return false;

		listaUtilizadores = RecuperacaoBackup.run(credenciaisPath);
		if (listaUtilizadores.size() < 1) {
			return false;
		}
		return true;
	}

	/**
	 * Verifica se existe um utilizador user na base de dados
	 * 
	 * @param user
	 *            nome do utilizador
	 * @return true existe false nao existe
	 */
	public boolean existeUtilizador(String user) {
		return listaUtilizadores.containsKey(user);
	}

	/**
	 * Verifica se existe uma foto do utilizador user com nome nomeFoto
	 * 
	 * @param user
	 *            nome do user
	 * @param nomeFoto
	 *            nome da foto
	 * @return true caso exista, false caso contrario
	 */
	public boolean existeFoto(String user, String nomeFoto) {
		Utilizador utilizador = listaUtilizadores.get(user);
		return utilizador.existeFoto(nomeFoto);
	}

	/**
	 * Adiciona uma foto ao utilizador userCurr com a Foto foto
	 * 
	 * @param userCurr
	 *            utilizador Corrente
	 * 
	 * @param foto
	 *            uma instancia da class Foto
	 */
	public void adicionarFoto(String userCurr, Foto foto) {
		Utilizador utilizador = listaUtilizadores.get(userCurr);

		utilizador.adicionarFoto(foto);
	}

	/**
	 * Verifica se o utilizador userCurr segue o utilizador user
	 * 
	 * @param userCurr
	 *            utilizador Corrent
	 * @param user
	 *            utilizador que sera verificado se esta a ser seguido pelo userCurr
	 * @return True caso o userCurr siga, false caso contrario
	 */
	public boolean existeSeguidor(String userCurr, String user) {
		Utilizador utilizadorCurr = listaUtilizadores.get(userCurr);

		return utilizadorCurr.existeSeguidor(user);

	}

	/**
	 * Atualiza o backup
	 */
	public void atualizarBackup() {
		AtualizarBackup.run(listaUtilizadores);
	}

	/**
	 * Avalia uma foto do utilizador user com nome foto, com like ou dislike
	 * dependendo do valor do dedao
	 * 
	 * @param userCurr
	 *            utilizador que ira avaliar
	 * @param user
	 *            utilizador que tem a foto
	 * @param foto
	 *            nome da foto que quer avaliar
	 * @param dedao
	 *            true se quiser dar like, false caso contrario
	 * @return True caso antes tinha dado um like, False caso dislike, null caso
	 *         ainda nao tenha dado nada
	 */
	public Boolean avaliarFoto(String userCurr, String user, String foto, boolean dedao) {
		Utilizador utilizador = listaUtilizadores.get(user);

		return utilizador.avaliarFoto(userCurr, foto, dedao);

	}

	/**
	 * Adiciona ou remove o utilizador user a lista de a seguir do utilizador
	 * userCurr, dependendo do valor de adicionar
	 * 
	 * @param userCurr
	 *            utilizador corrente
	 * @param user
	 *            utilizador que sera adicionado a lista a seguir
	 * 
	 * @param adicionar
	 *            true se quiser adicionar, false caso queira remover
	 */
	public void seguidor(String userCurr, String user, boolean adicionar) {

		Utilizador utilizador = listaUtilizadores.get(userCurr);

		if (adicionar) {
			utilizador.adicionarSeguidor(user);
		} else {
			utilizador.removerSeguidor(user);
		}

	}

	/**
	 * Adiciona um comentario a foto foto do utilizador user feito pelo utilizador
	 * userCurr
	 * 
	 * @param userCurr
	 *            utilizador corrente
	 * 
	 * @param user
	 *            utilizador que tem a foto
	 * 
	 * @param foto
	 *            nome da foto que ira ser comentada
	 * 
	 * @param comentario
	 *            comentario
	 */

	public void adicionarComentario(String userCurr, String user, String foto, String comentario) {

		Utilizador utilizador = listaUtilizadores.get(user);

		utilizador.adicionarComentario(userCurr, foto, comentario);
	}

	/**
	 * Retorna uma String com os status gerais das fotos do utilizador userID
	 * 
	 * @param userID
	 *            utilizador
	 * @return uma string com os status gerais das fotos do utilizador userID
	 */
	public String listaFotosStatus(String userID) {
		Utilizador utilizador = listaUtilizadores.get(userID);

		return utilizador.getStatusFotos();
	}

	/**
	 * Metodo para obter os status especificos de uma foto
	 * 
	 * @param userID
	 *            utilizador
	 * @param fotoID
	 *            nome da foto
	 * @return String que status especificios da foto
	 */

	public String getFotoStatus(String userID, String fotoID) {
		Utilizador utilizador = listaUtilizadores.get(userID);

		return utilizador.getFotoStatus(fotoID);
	}

	/**
	 * Obter todas as fotos de um utilizador
	 * 
	 * @param userID
	 *            utlizador
	 * 
	 * @return Uma collection com todas as fotos do utilizador
	 */
	public Collection<Foto> getFotos(String userID) {

		Utilizador utilizador = listaUtilizadores.get(userID);

		return utilizador.getFotos();
	}

	/**
	 * Verifica se o utilizador tem fotos
	 * 
	 * @param userID
	 *            utilizador
	 * 
	 * @return true se tiver, false caso contrario
	 */

	public boolean temFotos(String userID) {
		Utilizador utilizador = listaUtilizadores.get(userID);
		return utilizador.temFotos();
	}

}
