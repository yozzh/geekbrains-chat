package geekbrains.chat.providers.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatProvider implements Runnable {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ChatProviderReceiver receiver;

    public ChatProvider(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            while (true) {
                String messageString = in.readUTF();
                ChatMessageContainer message = ChatMessageContainer.createFromString(messageString);
                if (this.receiver != null) {
                    this.receiver.update(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setReceiver(ChatProviderReceiver receiver) {
        this.receiver = receiver;
    }

    public void sendMessage(ChatMessageContainer message) throws IOException {
        out.writeUTF(message.toString());
    }
}
