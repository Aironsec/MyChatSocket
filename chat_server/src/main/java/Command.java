public enum Command {
    EXIT("/exit"),
    NEWUSER("/newUser"),
    NOTUSER("/notUser"),
    NOTPASS("/notPass"),
    WHOUSER("?User");

    private final String comm;

    public String getComm() {
        return comm;
    }

    Command(String s) {
        comm = s;
    }
}
