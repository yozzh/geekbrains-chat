package geekbrains.chat;

import java.io.*;

public class Application {
    public static void main(String[] args) {
        if (args.length == 0) return;

        switch (args[0]) {
            case "server":
//                Server server = new Server(7777);
//                server.start();
                break;
            case "client":
                Client.main();
                break;
        }
    }
}
