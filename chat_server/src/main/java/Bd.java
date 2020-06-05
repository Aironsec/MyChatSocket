import java.sql.*;

public class Bd {

    private final Statement stmt;

    public Bd() throws SQLException, ClassNotFoundException {
        System.out.println("Server started!");
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:chat_server/src/main/resources/chat.db");
        stmt = connection.createStatement();
    }

    public ResultSet findUser (String user_name) throws SQLException {
        return stmt.executeQuery("select * from user\n" +
                "where user_name = '" + user_name + "'");
    }

}
