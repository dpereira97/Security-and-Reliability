package objectos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Classe cujos os objetos sao fotos
 * 
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class Foto implements Serializable {

	/**
	 * Versao de serializacao utilizada
	 */
	private static final long serialVersionUID = 1L;

	private String nome;

	private Map<String, Boolean> listaJoinha;

	private Date data;
	private List<Comentario> listaComentarios;

	public Foto(String nome) {
		this.nome = nome;
		this.data = new Date();
		listaComentarios = new ArrayList<>();
		listaJoinha = new HashMap<>();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("#Data#\n");

		sb.append(data + "\n");

		sb.append("#Joinhas#\n");

		for (Entry<String, Boolean> entry : listaJoinha.entrySet()) {
			String deu = entry.getValue() ? "like" : "dislike";
			sb.append("O utilizador " + entry.getKey() + " deu " + deu + "\n");
		}

		sb.append("#Comentarios#\n");

		for (Comentario comentario : listaComentarios) {
			sb.append(comentario + "\n");
		}

		sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	public Date getData() {
		return data;
	}

	public String getNome() {
		return nome;
	}

	public Comentario[] getListaComentarios() {
		return listaComentarios.toArray(new Comentario[listaComentarios.size()]);
	}

	public Boolean avaliarFoto(String userCurr, boolean dedao) {
		return listaJoinha.put(userCurr, dedao);

	}

	public void addComentario(String userCurr, String texto) {
		Comentario comentario = new Comentario(userCurr, texto);

		listaComentarios.add(comentario);

	}
	
	public int[] getLikeDislikes() {
		int[] quantos = new int[2];

		for (Boolean deu : listaJoinha.values())
			if (deu)
				quantos[0]++;
			else
				quantos[1]++;

		return quantos;
	}

	public String getStatus() {
		StringBuilder sb = new StringBuilder();

		int[] likes = getLikeDislikes();
		sb.append(String.format("Tem %d Likes e %d dislikes", likes[0], likes[1]));

		if (!listaComentarios.isEmpty()) {
			sb.append("\nComentarios:\n");
			for (Comentario comentario : listaComentarios) {
				sb.append(comentario.getAutor() + ": " );
				sb.append(comentario.getTexto() + "\n");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		else {
			sb.append("\nSem comentarios");
		}

		return sb.toString();
	}
}