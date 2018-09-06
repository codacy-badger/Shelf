package chickenmumani.com.allshelf;

public class Board_Item {
    private String name;
    private String uuid;
    private String date;
    private String title;
    private String text;
    private String tag;

    public Board_Item(String date, String name, String uuid, String title, String text, String tag) {
        this.date = date;
        this.name = name;
        this.uuid = uuid;
        this.title = title;
        this.text = text;
        this.tag = tag;
    }

    public void setName(String name){ this.name = name; }

    public void setTitle(String title) {
        this.title = title ;
    }

    public void setDate(String date) { this.date = date; }

    public String getDBID(){ return this.date + " " + this.uuid; }

    public String getName() { return this.name; }

    public String getTitle() { return this.title; }

    public String getText() { return this.text; }

    public String getDate() { return this.date; }

    public String getTag() { return this.tag; }
}
