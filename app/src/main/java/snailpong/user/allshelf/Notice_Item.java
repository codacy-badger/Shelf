package snailpong.user.allshelf;

public class Notice_Item {

    private String notcontent_title;
    private String notcontent_date;

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
