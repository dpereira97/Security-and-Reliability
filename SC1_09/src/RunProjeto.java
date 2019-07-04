
import client.PhotoShare;

public class RunProjeto {

	private static final String endereco = "127.0.0.1:23232";

	private static void dormeCrl() {
		try {
			Thread.sleep(100);
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void imprimirargumentos(String[] argumentos) {
		System.out.println();

		System.out.print("argumentos9: ");

		for (String string : argumentos) {
			System.out.print(string + " ");
		}
		System.out.println();

	}

	private static void adicionarSeisUsers() {
		String[] argumentos9 = { "Diogo", "pass", endereco };
		imprimirargumentos(argumentos9);
		PhotoShare.main(argumentos9);
		dormeCrl();
		String[] argumentos2 = { "Simao", "pass", endereco };
		imprimirargumentos(argumentos2);
		PhotoShare.main(argumentos2);
		dormeCrl();
		String[] argumentos3 = { "Campos", "pass", endereco };
		imprimirargumentos(argumentos3);
		PhotoShare.main(argumentos3);
		dormeCrl();
		String[] argumentos4 = { "Ze", "pass", endereco };
		imprimirargumentos(argumentos4);
		PhotoShare.main(argumentos4);
		dormeCrl();

		String[] argumentos5 = { "Ricardo", "pass", endereco };
		imprimirargumentos(argumentos5);
		PhotoShare.main(argumentos5);
		dormeCrl();

		String[] argumentos6 = { "Li", "pass", endereco };
		imprimirargumentos(argumentos6);
		PhotoShare.main(argumentos6);
		dormeCrl();
	}

	private static void adicionaCincoFotos() {
		String[] argumentos1 = { "Ze", "pass", endereco, "-a", "banana.jpg" };
		imprimirargumentos(argumentos1);
		PhotoShare.main(argumentos1);
		dormeCrl();
		String[] argumentos2 = { "Diogo", "pass", endereco, "-a", "diogo.jpg" };
		imprimirargumentos(argumentos2);
		PhotoShare.main(argumentos2);
		dormeCrl();
		String[] argumentos3 = { "Diogo", "pass", endereco, "-a", "banana.jpg" };
		imprimirargumentos(argumentos3);
		PhotoShare.main(argumentos3);
		dormeCrl();
		String[] argumentos4 = { "Diogo", "pass", endereco, "-a", "asus.jpg" };
		imprimirargumentos(argumentos4);
		PhotoShare.main(argumentos4);
		dormeCrl();
		String[] argumentos5 = { "Campos", "pass", endereco, "-a", "banana.jpg" };
		imprimirargumentos(argumentos5);
		PhotoShare.main(argumentos5);
		dormeCrl();
		String[] argumentos6 = { "Chourico", "pass", endereco, "-a", "banana.jpg" };
		imprimirargumentos(argumentos6);
		PhotoShare.main(argumentos6);
		dormeCrl();

		String[] argumentos8 = { "Campos", "pass", endereco, "-a", "1.jpg" };
		imprimirargumentos(argumentos8);
		PhotoShare.main(argumentos8);
		dormeCrl();

	}

	private static void segue() {
		String[] argumentos9 = { "Simao", "pass", endereco, "-f", "Simao", "Ze", "Ricardo", "Chourico", "Diogo", "Ze" };
		imprimirargumentos(argumentos9);
		PhotoShare.main(argumentos9);
	}

	private static void naoSegue() {
		String[] argumentos9 = { "Diogo", "pass", endereco, "-r", "Ricardo", "Chourico", "Diogo", "Ze", "Ricardo" };
		imprimirargumentos(argumentos9);
		PhotoShare.main(argumentos9);
	}

	private static void daLike() {
		// DIOGO METE LIKE NA FOTO DO SIMAO
		String[] argumentos9 = { "Simao", "pass", endereco, "-L", "Diogo", "banana.jpg" };
		imprimirargumentos(argumentos9);
		PhotoShare.main(argumentos9);

		// DIOGO METE LIKE NUMA FOTO QUE NAO EXISTE
		String[] argumentos2 = { "Simao", "pass", endereco, "-L", "Diogo", "asus.jpg" };
		imprimirargumentos(argumentos2);
		PhotoShare.main(argumentos2);

		// DIOGO METE LIKE NUMA FOTO DE UMA PESSOA QUE NAO SEGUE
		String[] argumentos3 = { "Simao", "pass", endereco, "-L", "Diogo", "diogo.jpg" };
		imprimirargumentos(argumentos3);
		PhotoShare.main(argumentos3);

		// DIOGO METE LIKE NUMA FOTO DE UMA PESSOA QUE NAO EXISTE
		String[] argumentos4 = { "Simao", "pass", endereco, "-L", "Diogo", "11.jpg" };
		imprimirargumentos(argumentos4);
		PhotoShare.main(argumentos4);

		String[] argumentos5 = { "Simao", "pass", endereco, "-L", "Diogo", "asus.jpg" };
		imprimirargumentos(argumentos5);
		PhotoShare.main(argumentos5);
	}

	private static void daUnlike() {
		// DIOGO METE DISLIKE NA FOTO DO SIMAO
		String[] argumentos9 = { "Simao", "pass", endereco, "-D", "Diogo", "banana.jpg" };
		imprimirargumentos(argumentos9);
		PhotoShare.main(argumentos9);

		// DIOGO METE DISLIKE NUMA FOTO QUE NAO EXISTE
		String[] argumentos2 = { "Simao", "pass", endereco, "-D", "Diogo", "11.jpg" };
		imprimirargumentos(argumentos2);
		PhotoShare.main(argumentos2);

		// DIOGO METE DISLIKE NUMA FOTO DE UMA PESSOA QUE NAO SEGUE
		String[] argumentos3 = { "Diogo", "pass", endereco, "-D", "Ricardo", "banana.jpg" };
		imprimirargumentos(argumentos3);
		PhotoShare.main(argumentos3);

		// DIOGO METE LIKE NUMA FOTO DE UMA PESSOA QUE NAO EXISTE
		String[] argumentos4 = { "Diogo", "pass", endereco, "-D", "Chourico", "banana.jpg" };
		imprimirargumentos(argumentos4);
		PhotoShare.main(argumentos4);

	}

	private static void comenta() {

		// DIOGO COMENTA FOTO DO SIMAO
		String[] argumentos9 = { "Campos", "pass", endereco, "-c", "Es um tremoco", "Campos", "banana.jpg" };
		imprimirargumentos(argumentos9);
		PhotoShare.main(argumentos9);

		// DIOGO COMENTA FOTO DO LI QUE NAO EXISTE
		String[] argumentos2 = { "Simao", "pass", endereco, "-c", "Merda", "Li", "banana.jpg" };
		imprimirargumentos(argumentos2);
		PhotoShare.main(argumentos2);

		// DIOGO COMENTA FOTO DE UM UTILIZADOR QUE NAO EXISTE
		String[] argumentos3 = { "Simao", "pass", endereco, "-c", "Cenas", "Chourico", "banana.jpg" };
		imprimirargumentos(argumentos3);
		PhotoShare.main(argumentos3);

		// DIOGO COMENTA 3 VEZES NA FOTO DO ZE
		String[] argumentos4 = { "Simao", "pass", endereco, "-c", "Toupeiras", "Diogo", "banana.jpg" };
		String[] argumentos5 = { "Simao", "pass", endereco, "-c", "Anda jogar gta", "Diogo", "asus.jpg" };
		String[] argumentos6 = { "Simao", "pass", endereco, "-c", "Cenas", "Diogo", "diogo.jpg" };
		imprimirargumentos(argumentos4);
		PhotoShare.main(argumentos4);
		imprimirargumentos(argumentos5);
		PhotoShare.main(argumentos5);
		imprimirargumentos(argumentos6);
		PhotoShare.main(argumentos6);
		
		String[] argumentos7 = { "Simao", "pass", endereco, "-c", "gay", "Diogo", "banana.jpg" };
		imprimirargumentos(argumentos7);
		PhotoShare.main(argumentos7);
	}

	private static void guardaFotos() {
		// DIOGO GUARDA TODAS AS FOTOS DO ZÉ (STALKER DO CARALHO)
		String[] argumentos9 = { "Simao", "pass", endereco, "-g", "Diogo" };
		imprimirargumentos(argumentos9);
		PhotoShare.main(argumentos9);

//		// DIOGO GUARDA FOTOS DE UM UTILIZADOR QUE NAO EXISTE
//		String[] argumentos2 = { "Diogo", "pass", endereco, "-g", "Chourico" };
//		imprimirargumentos(argumentos2);
//		PhotoShare.main(argumentos2);
//
//		// DIOGO GUARDA FOTOS DE UM UITLIZADOR QUE NAO TEM FOTOS
//		String[] argumentos3 = { "Diogo", "pass", endereco, "-g", "Li" };
//		imprimirargumentos(argumentos3);
//		PhotoShare.main(argumentos3);
	}

	private static void listaFotos() {
		// DIOGO LISTA AS FOTOS DO SIMAO
		String[] argumentos9 = { "Simao", "pass", endereco, "-l", "Simao" };
		imprimirargumentos(argumentos9);
		PhotoShare.main(argumentos9);

		// DIOGO LISTA AS FOTOS DO ZE
		String[] argumentos2 = { "Simao", "pass", endereco, "-l", "Ze" };
		imprimirargumentos(argumentos2);
		PhotoShare.main(argumentos2);

		// DIOGO LISTA AS FOTOS DO RICARDO
		String[] argumentos3 = { "Simao", "pass", endereco, "-l", "Ricardo" };
		imprimirargumentos(argumentos3);
		PhotoShare.main(argumentos3);
		
		String[] argumentos4 = { "Simao", "pass", endereco, "-l", "Diogo" };
		imprimirargumentos(argumentos4);
		PhotoShare.main(argumentos4);
	}

	private static void fotoStatus() {
		// DIOGO LISTA AS FOTOS DO SIMAO
		String[] argumentos9 = { "Simao", "pass", endereco, "-i", "Diogo", "banana.jpg" };
		imprimirargumentos(argumentos9);
		PhotoShare.main(argumentos9);

		// DIOGO LISTA AS FOTOS DO ZE
		String[] argumentos2 = { "Simao", "pass", endereco, "-i", "Ze" , "banana.jpg"};
		imprimirargumentos(argumentos2);
		PhotoShare.main(argumentos2);

		// DIOGO LISTA AS FOTOS DO RICARDO
		String[] argumentos3 = { "Simao", "pass", endereco, "-i", "Simao" , "ola"};
		imprimirargumentos(argumentos3);
		PhotoShare.main(argumentos3);
		
		String[] argumentos4 = { "Simao", "pass", endereco, "-i", "Chourico", "sgjshai" };
		imprimirargumentos(argumentos4);
		PhotoShare.main(argumentos4);
	}
	public static void main(String[] args) throws InterruptedException {

		System.out.println("---- ADICIONAR 6 USERS -----");

		 adicionarSeisUsers();
		 adicionaCincoFotos();
		 segue();
		 comenta();
		 naoSegue();
		 daLike();
		 daUnlike();
		 guardaFotos();


		// System.out.println("---- ADICIONAR 5 FOTOS ----");
		//
		// System.out.println("-------->fiz o padrao<-----------");
		//
		// Thread.sleep(2000);
		//
		// System.out.println("------ OPERACAO FOLLOW ------");

		// System.out.println("------ OPERACAO UNFOLLOW ------");
		//
		// System.out.println("------ OPERACAO METER LIKE ------");
		//
		// System.out.println("------ OPERACAO METER DISLIKE ------");

		// System.out.println("------ OPERACAO COMENTAR ------");
		//
		// System.out.println("------ OPERACAO GUARDAR ------");
//		listaFotos();
		
//		fotoStatus();
		// System.out.println("-------->fiz o que tinha de fazer<-----------");

	}
}