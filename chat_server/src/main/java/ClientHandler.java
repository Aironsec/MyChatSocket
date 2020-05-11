import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private String nickName;
    private boolean running;

    public ClientHandler(Socket socket, String nickName) throws IOException {
        this.socket = socket;
        this.nickName = nickName;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        running = true;
        welcome();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void welcome() throws IOException {
        out.writeUTF("/newUser" + nickName);
        out.flush();
    }

    public void broadCastMessage(String message) throws IOException {
        for (ClientHandler client : Server.getClients()) {
//            if (!client.equals(this)) {
                client.sendMessage("Всем:\n" + message);
//            }
        }
    }

    private void messageForClient(String message) throws IOException {
        String nName = message.replace("/w", "").trim();
        nName = nName.substring(0, nName.indexOf(" "));
        for (ClientHandler client : Server.getClients()) {
            if (client.nickName.toLowerCase().equals(nName.toLowerCase())) {
                message = message.replace("/w " + nName, "").trim();
                client.sendMessage("от: " + this.nickName + " >\n" + message);
                this.sendMessage("для: " + nName + " >\n" + message);
            }
        }

    }

    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
        out.flush();
    }

    @Override
    public void run() {
        while (running) {
            try {
                if (socket.isConnected()) {
                    String clientMessage = in.readUTF();
                    if (clientMessage.equals("/exit")) {
                        Server.getClients().remove(this);
                        sendMessage(clientMessage);
                        break;
                    }
                    if (clientMessage.startsWith("/w")) {
                        messageForClient(clientMessage);
                    } else {
                        broadCastMessage(clientMessage);
                    }
//                    System.out.println(clientMessage);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
