import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Server {

    private final static int PORT = 8189;
    private final static String HOST = "localhost";
    private static int cnt = 1;

    private boolean running;
    public static ConcurrentLinkedDeque<ClientHandler> clients;

    public static ConcurrentLinkedDeque<ClientHandler> getClients() {
        return clients;
    }

    public Server(int port) {
        running = true;
        clients = new ConcurrentLinkedDeque<>();
        try (ServerSocket srv = new ServerSocket(PORT)) {
            System.out.println("Server started!");
            while (running) {
                Socket socket = srv.accept();
                ClientHandler client = new ClientHandler(socket, "User" + cnt);
                cnt++;
                clients.add(client); // can produce CME (concurrent modification exception)

                System.out.println(client.getNickName() + " accepted!");
                new Thread(client).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:chat_server/src/main/resources/chat.db");
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("insert into USER(user_name) values('User2')");
        new Server(PORT);
    }
}
