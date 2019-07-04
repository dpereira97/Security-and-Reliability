import client.PhotoShare;

public class TestGeneral {

	public static String login = "%s 123 localhost:23232";
	public static String follow = "-f %s";
	public static String like = "-L hugo file1";
	public static String Comment = "-c this is a comment hugo file1";
	public static final int NUM_USERS = 100;

	public static void main(String[] args) throws InterruptedException {
		String[] users = new String[NUM_USERS];
		Thread[] threads = new Thread[NUM_USERS];
		for (int i = 0; i < NUM_USERS; i++) {
			users[i] = "u" + i;
		}

		// create users
		for (int i = 0; i < NUM_USERS; i++) {
			threads[i] = new Thread(runCommand(login, users[i]));
			threads[i].start();
		}
		for (int i = 0; i < NUM_USERS; i++) {
			threads[i].join();
		}

		// create follow
		for (int i = 0; i < NUM_USERS; i++) {
			for (int j = i + 1; j < i + 5; j++) {
				if (j < NUM_USERS) {
					threads[i] = new Thread(runCommand(login + " " + follow, users[i], users[j]));
					threads[i].start();
				}
			}
		}
		for (int i = 0; i < NUM_USERS; i++) {
			threads[i].join();
			System.out.println("i: " + i);
		}

	}

	public static Runnable runCommand(String commFormat, String... args) {
		return (Runnable) () -> {
			PhotoShare.main(String.format(commFormat, args).split(" "));
		};
	}

}
