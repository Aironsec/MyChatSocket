import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ClientHandler implements Runnable {

    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private String login;
    private boolean running;

    public ClientHandler(Socket socket, String login) throws IOException {
        this.socket = socket;
        this.login = login;
        System.out.println("1");
        out = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("2");
        in = new ObjectInputStream(socket.getInputStream());
        System.out.println("3");
        running = true;
        welcome();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void welcome() throws IOException {
        if (Server.getClients().isEmpty()) {
            sendMessage("/newUser", login, null, null);
        } else {
            for (ClientHandler client : Server.getClients()) {
                client.sendMessage("/newUser", login, null, null);
            }
        }
    }

    public void broadCastMessage(String cmd, String login, String pass, String message) throws IOException {
        for (ClientHandler client : Server.getClients()) {
//            if (!client.equals(this)) {
            client.sendMessage(cmd, login, pass, message);
//            }
        }
    }

//    private void messageForClient(String message) throws IOException {
//        String nName = message.replace("/w", "").trim();
//        nName = nName.substring(0, nName.indexOf(" "));
//        for (ClientHandler client : Server.getClients()) {
//            if (client.login.toLowerCase().equals(nName.toLowerCase())) {
//                message = message.replace("/w " + nName, "").trim();
//                client.sendMessage("от: " + this.login + " >\n" + message);
//                this.sendMessage("для: " + nName + " >\n" + message);
//            }
//        }
//
//    }

    public void sendMessage(String cmd, String login, String pass, String message) throws IOException {
        out.writeObject(new Message(cmd, login, pass, message));
        out.writeChars("q");
        out.flush();
    }

    @Override
    public void run() {
        while (running) {
            try {
                if (socket.isConnected()) {
                    Message message = (Message) in.readObject();
                    in.readChar();
                    if (message.getCmd() != null) {
                        if (message.getCmd().equals("/exit")) {
                            broadCastMessage("/exit", login, null, login + " отключился");
                            Server.getClients().remove(this);
                            System.out.println(login + " exit!");
                            break;
                        }

                        if (message.getCmd().equals("?User")) {
                            ResultSet res = Server.getStmt().executeQuery(
                                    "select * from user\n" +
                                    "where user_name = '" + message.getLogin() + "'");
                            if (!res.isClosed()) {
                                if (res.getString("pass").equals(message.getPass())){
                                    login = message.getLogin();
                                    sendMessage("/newUser", login, null, login + " подключился");
                                } else {
                                    //Не верный пароль
                                    sendMessage("/notPass", null, null, null);
                                    Server.getClients().remove(this);
                                    break;
                                }
                            } else {
                                //Пользователя нет
                                sendMessage("/notUser", null, null, null);
                                Server.getClients().remove(this);
                                break;
                            }
                        }
                    } else {
                        broadCastMessage(null, login, null, message.getMessage());
                    }

                }
            } catch (IOException | ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
