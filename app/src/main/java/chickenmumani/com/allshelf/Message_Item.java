package chickenmumani.com.allshelf;

import android.graphics.drawable.Drawable;

public class Message_Item {

    private int id;
    private Drawable iconDrawable ;
    private String message_msg;
    private String message_date;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public String getMessage_msg() {
        return message_msg;
    }

    public void setMessage_msg(String message_msg) {
        this.message_msg = message_msg;
    }

    public String getMessage_date() {
        return message_date;
    }

    public void setMessage_date(String message_date) {
        this.message_date = message_date;
    }
}
