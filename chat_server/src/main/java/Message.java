import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class Message implements Serializable {
    private final String cmd;
    private final String login;
    private String pass;
    private final String message;
    private String[] users;
    private final Date time;

    //Конструктор для клиента
    public Message(String cmd, String login, String pass, String message){
        this.cmd = cmd;
        this.login = login;
        this.pass = pass;
        this.message = message;
        this.time = java.util.Calendar.getInstance().getTime();
    }

    //Конструктор для сервера
    public Message(String cmd, String login, String message, String[] users){
        this.cmd = cmd;
        this.login = login;
        this.message = message;
        this.time = java.util.Calendar.getInstance().getTime();
        this.users = users;
    }

    public String getCmd() {
        return cmd;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public String getMessage() {
        return message;
    }

    public String[] getUsers() {
        return users;
    }

    public String getDate(){
        Time tm = new Time(this.time.getTime());
        return tm.toString();
    }
}
