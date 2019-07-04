package objectos;

import java.io.Serializable;
import java.util.Date;

/**
 * Class imutavel do comentario
 * 
 * @author fc47804 Jose Aguas
 * @author fc47827 Simao Ferreira
 * @author fc47888 Diogo Pereira
 *
 */
public class Comentario implements Serializable {

	/**
	 * Versao de serializacao utilizada
	 */
	private static final long serialVersionUID = 1L;

	private String autor;
	private String texto;
	private Date date;

	/**
	 * Construtor
	 * 
	 * @param autor
	 *            nome do autor
	 * @param texto
	 *            comentario
	 */
	public Comentario(String autor, String texto) {
		this.autor = autor;
		this.texto = texto;
		this.date = new Date();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("O utilizador " + autor + " comentou:\n");
		sb.append("--> " + texto + "\n");
		sb.append("No dia " + date);

		return sb.toString();
	}

	/**
	 * Retorna o texto do comentario
	 * 
	 * @return o texto do comentario
	 */
	public String getTexto() {
		return texto;
	}

	/**
	 * Retorna o nome do autor
	 * 
	 * @return o nome do autor
	 */
	public String getAutor() {
		return autor;
	}

	/**
	 * Retorna a data da criacao do comentario
	 * 
	 * @return a data da criacao do comentario
	 */
	public Date getDate() {
		return date;
	}
}
