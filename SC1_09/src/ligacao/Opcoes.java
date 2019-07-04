package ligacao;

public enum Opcoes {
	REGISTAR("-registar"), AUTENTICAR("-autenticar"), ADICIONAR("-a"), FOTOSTATUS("-i"), LISTAFOTO("-l"), STALKER(
			"-g"), JOINHA("-L"), DISLIKE("-D"), COMENTAR("-c"), FOLLOWER("-f"), UNFOLLOWER("-r");

	private final String operacao;

	Opcoes(String operacao) {
		this.operacao = operacao;
	}

	public String getOperacao() {
		return operacao;
	}

	/**
	 * Tendo a operacao retorna um ENUM
	 * 
	 * @param operacao
	 *            texto da operacao
	 * 
	 * @return um ENUM ou null caso nao exista
	 */
	public static Opcoes getOpcao(String operacao) {
		for (Opcoes op : Opcoes.values())
			if (operacao.equals(op.getOperacao()))
				return op;

		return null;
	}
}
