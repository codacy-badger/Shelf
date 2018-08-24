package chickenmumani.com.allshelf;

import android.graphics.drawable.Drawable;

public class Post_Item {

    private String uid;
    private Drawable profile;
    private String uname;
    private int star;
    private String date;
    private boolean isfav;
    private int favcount;
    private Drawable postimg;
    private String posttext;

    public Post_Item(String uid, Drawable profile, String uname, int star, String date, boolean isfav, int favcount, Drawable postimg, String posttext) {
        this.uid = uid;
        this.profile = profile;
        this.uname = uname;
        this.star = star;
        this.date = date;
        this.isfav = isfav;
        this.favcount = favcount;
        this.postimg = postimg;
        this.posttext = posttext;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Drawable getProfile() {
        return profile;
    }

    public void setProfile(Drawable profile) {
        this.profile = profile;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isIsfav() {
        return isfav;
    }

    public void setIsfav(boolean isfav) {
        this.isfav = isfav;
    }

    public int getFavcount() {
        return favcount;
    }

    public void setFavcount(int favcount) {
        this.favcount = favcount;
    }

    public Drawable getPostimg() {
        return postimg;
    }

    public void setPostimg(Drawable postimg) {
        this.postimg = postimg;
    }

    public String getPosttext() {
        return posttext;
    }

    public void setPosttext(String posttext) {
        this.posttext = posttext;
    }
}
