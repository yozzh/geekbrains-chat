package geekbrains.chat;

public class Application {
    public static void main(String[] args) {
        if (args.length == 0) return;

        switch (args[0]) {
            case "server":
                ServerApp.main();
                break;
            case "client":
                ClientApp.main();
                break;
        }
    }
}
