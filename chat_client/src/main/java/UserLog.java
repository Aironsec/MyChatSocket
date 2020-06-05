import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserLog implements Serializable {

    private int index = 0;
    private final ItemLog[] logs = new ItemLog[100];

    static class ItemLog implements Serializable {

        private final Date date;
        private final String mes;


        public ItemLog(String mes) {
            date = java.util.Calendar.getInstance().getTime();
            this.mes = mes;
        }

        public String getDate() {
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");
            return format.format(date.getTime());
        }
        public String getMes() {
            return mes;
        }

    }

    public int getIndex() {
        return index;
    }

    public ItemLog[] getLogs() {
        return logs;
    }

    public void writItem(String message) {
        if (index >= logs.length) index = 0;

        logs[index] = new ItemLog(message);
        index++;
    }
}
