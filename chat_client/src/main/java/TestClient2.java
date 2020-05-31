import com.sun.xml.internal.bind.v2.model.core.EnumLeafInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class TestClient2 {

    private static String userID = null;

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8189)) {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            boolean running = true;
            Scanner cin = new Scanner(System.in);

            Thread thread = new Thread(() -> {
                while (running) {
                    String message = null;
                    try {
                        message = in.readUTF();
                        if (message.startsWith("/")) {

                            if (message.startsWith("/exit")) {
                                String str = message.replace("/exit", "").trim();
                                if (userID.equals(str)) {
                                    in.close();
                                    out.close();
                                    socket.close();
                                    break;
                                }
                            }
                            if (message.startsWith("/newUser")) {
                                userID = message.replace("/newUser", "").trim();
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(message);
                }
            });
            thread.setDaemon(true);
            thread.start();
//            thread.join();
            while (running) {
                String line = cin.nextLine();
                if (line.equals("/exit")) {
                    out.writeUTF("/exit");
                    out.flush();
                    break;
                }
                out.writeUTF(line);
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
