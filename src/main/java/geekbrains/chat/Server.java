package geekbrains.chat;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Server extends javafx.application.Application {
    public static void main(String... args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent startRoot = start();
        Scene scene = new Scene(startRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chat Application");
        primaryStage.show();
    }
}
