package servidor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import objectos.Foto;
import objectos.Utilizador;

/**
 * Class Singleton da base de dados
 * 
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class BaseDados {

	private Map<String, String> credenciais;
	private HashMap<String, Utilizador> listaUtilizadores;

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
		credenciais = new HashMap<>();
		listaUtilizadores = new HashMap<>();

	}

	/**
	 * Metodo toString para debug
	 */
	public String toString() {

		StringBuilder sb = new StringBuilder("######Base Dados######\n");

		sb.append("###credenciais###\n");
		int i = 0;
		for (Entry<String, String> entry : credenciais.entrySet()) {
			i++;
			sb.append(i + " Utilizador: " + entry.getKey() + " -> pass: " + entry.getValue() + "\n");
		}
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
	 * Regista um utilizador na base de dados caso ainda nao exista
	 * 
	 * @param userCurr
	 *            Utilizador Corrente
	 * @param pass
	 *            Password
	 * @return True caso consiga registar False se nao conseguir
	 */
	public boolean registar(String userCurr, String pass) {

		boolean existeUser = existeUtilizador(userCurr);

		if (!existeUser) { // nao existe ainda
			colocarNovoUtilizador(userCurr, pass);
			return true;
		}
		else {
			return false;
		}
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
		return pass.equals(credenciais.get(user));
	}

	/**
	 * Cria uma pasta com o nome diretorio
	 * 
	 * @param diretorio
	 *            nome da pasta
	 * 
	 * @return true caso seja criada false caso ja exista
	 */
	private static boolean criarPasta(String diretorio) {
		File file = new File(diretorio);
		return file.mkdir();
	}

	/**
	 * Recupera as credenciais
	 * 
	 * @throws IOException
	 */
	private void recuperarCredenciais() throws IOException {

		try (BufferedReader br = new BufferedReader(new FileReader(Constantes.CREDENCIAIS))) {

			String linha;
			while ((linha = br.readLine()) != null) {
				String[] userPass = linha.split(":");

				credenciais.put(userPass[0], userPass[1]);
			}
		}

	}

	/**
	 * Recupera os dados
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private void recuperarDados() throws IOException, ClassNotFoundException {
		try (ObjectInputStream inp = new ObjectInputStream(new FileInputStream(new File(Constantes.BACKUP)))) {
			listaUtilizadores = (HashMap<String, Utilizador>) inp.readObject();
		}

	}

	/**
	 * Recupera as credenciais e os dados, este metodo serve para ser utilizado
	 * quando o servidor vai a baixo
	 */
	public void recuperarInformacao() {

		boolean primeiraVez = criarPasta(Constantes.DIRETORIO_SERVIDOR);

		if (!primeiraVez && (new File(Constantes.CREDENCIAIS).exists())) {
			try {
				recuperarCredenciais();
				recuperarDados();

			}
			catch (IOException e) {

				System.err.println("Nao foi possivel recuperar informacao");
				System.err.println(e.getMessage());
			}
			catch (ClassNotFoundException e) {

				System.err.println("Nao foi possivel criar a lista de utilizadores");
				System.err.println(e.getMessage());
			}
		}

	}

	/**
	 * Adiciona a base de dados as credenciais do utilizador, user e pass
	 * 
	 * @param user
	 *            nome do utilizador
	 * @param pass
	 *            password do utilizador
	 * @throws IOException
	 */
	private void addCredenciais(String user, String pass) throws IOException {
		credenciais.put(user, pass);

		try (FileOutputStream ficheiroUtilizadores = new FileOutputStream(Constantes.CREDENCIAIS, true)) {
			String novoUser = user + ":" + pass + System.lineSeparator();
			ficheiroUtilizadores.write(novoUser.getBytes());
		}

	}

	/**
	 * Adiciona um novo utilizador a base de dados
	 * 
	 * @param user
	 *            nome do utilizador
	 */
	private void addUtilizador(String user) {
		listaUtilizadores.put(user, new Utilizador());
	}

	/**
	 * Quando um novo utilizador e registado e executado este metodo ira adicionar
	 * tanto as credencias deste como os seus dados
	 * 
	 * @param user
	 *            nome do Utilizador
	 * @param pass
	 *            Password do utilizador
	 */
	public void colocarNovoUtilizador(String user, String pass) {

		try {
			addCredenciais(user, pass);
			addUtilizador(user);
			criarPasta(Constantes.DIRETORIO_SERVIDOR + user);
		}
		catch (IOException e) {
			System.err.println("Deu coco");
			System.err.println(e.getMessage());
		}

	}

	/**
	 * Verifica se existe um utilizador user na base de dados
	 * 
	 * @param user
	 *            nome do utilizador
	 * @return true existe false nao existe
	 */
	public boolean existeUtilizador(String user) {
		return credenciais.containsKey(user);
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

		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(Constantes.BACKUP)))) {
			out.writeObject(listaUtilizadores);
		}
		catch (IOException e) {
			System.err.println("Problema a atualizar.");
			System.err.println(e.getMessage());
		}
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
		}
		else {
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
