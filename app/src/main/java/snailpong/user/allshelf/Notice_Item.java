package snailpong.user.allshelf;

public class Notice_Item {

    private int id;
    private String notcontent_title;
    private String notcontent_date;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public void setTitle(String title) {
        notcontent_title = title ;
    }

    public void setDate(String date) {
        notcontent_date = date;
    }

    public String getTitle() {
        return this.notcontent_title ;
    }

    public String getDate() {
        return this.notcontent_date ;
    }

}
