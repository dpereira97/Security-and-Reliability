package objectos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Class representante do utilizador
 * 
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class Utilizador implements Serializable {

	/**
	 * Versao de serializacao utilizada
	 */
	private static final long serialVersionUID = 1L;

	// private String nome;

	private Map<String, Foto> lista_fotos;
	private ArrayList<String> lista_seguir;

	/**
	 * Construtor
	 */
	public Utilizador() {

		// this.nome = nome;

		lista_fotos = new HashMap<>();
		lista_seguir = new ArrayList<>();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("##FOTOS##\n");

		for (Entry<String, Foto> entry : lista_fotos.entrySet()) {

			sb.append("Foto " + entry.getKey() + ":\n");
			sb.append(entry.getValue().toString() + "\n");
		}

		sb.append("##SEGUIR##\n");
		sb.append("Segue os utilizadores:");
		for (String string : lista_seguir) {
			sb.append(" " + string + ";\n");
		}
		sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	/**
	 * Verifica se existe foto nome
	 * 
	 * @param nome
	 *            nome da foto
	 * @return true se existe, false caso contrario
	 */
	public boolean existeFoto(String nome) {
		return lista_fotos.containsKey(nome);
	}

	/**
	 * Adiciona uma foto
	 * 
	 * @param foto
	 *            foto que ira ser adicionada
	 */
	public void adicionarFoto(Foto foto) {
		lista_fotos.put(foto.getNome(), foto);
	}

	/**
	 * Obter a lista de utilizador a seguir
	 * 
	 * @return uma List de pessoas que este "utilizador" segue
	 */
	public List<String> getListaseguidores() {
		return lista_seguir;
	}

	/**
	 * Verifica se existe o utilizador user a seguir
	 * 
	 * @param user
	 *            utilizador
	 * @return true caso siga, false caso contrario
	 */
	public boolean existeSeguidor(String user) {
		return lista_seguir.contains(user);
	}

	/**
	 * Adiciona um utilizador a seguir
	 * 
	 * @param nome
	 *            nome do utilizador
	 */
	public void adicionarSeguidor(String nome) {
		lista_seguir.add(nome);

	}

	/**
	 * Remove um utilizador da lista a seguir
	 * 
	 * @param nome
	 *            nome do utilizador
	 */
	public void removerSeguidor(String nome) {
		lista_seguir.remove(nome);

	}

	public Boolean avaliarFoto(String userCurr, String foto, boolean dedao) {
		return lista_fotos.get(foto).avaliarFoto(userCurr, dedao);
	}

	public void adicionarComentario(String userCurr, String nome_foto, String comentario) {

		Foto foto = lista_fotos.get(nome_foto);

		foto.addComentario(userCurr, comentario);

	}

	public String getStatusFotos() {
		StringBuilder sb = new StringBuilder();

		for (Foto foto : lista_fotos.values()) {
			sb.append("Foto: " + foto.getNome() + " publicada em: " + foto.getData() + "\n");
		}

		return sb.toString();
	}

	public String getFotoStatus(String fotoID) {
		Foto foto = lista_fotos.get(fotoID);

		return foto.getStatus();
	}

	public Collection<Foto> getFotos() {
		return lista_fotos.values();
	}

	public boolean temFotos() {

		return !lista_fotos.isEmpty();
	}

}