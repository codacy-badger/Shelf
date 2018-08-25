package chickenmumani.com.allshelf;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class Post_Adapter extends RecyclerView.Adapter<Post_Adapter.ViewHolder> {

    private List<Post_Item> myList;
    Bitmap bitmap1, bitmap2, upro;
    boolean is_uid_isbn;
    String uid, uname, isbn;

    public Post_Adapter(List<Post_Item> list, String uid, String uname, Bitmap upro) {
        this.myList = list;
        this.uid = uid;
        this.uname = uname;
        this.upro = upro;
        is_uid_isbn = FALSE;
    }

    public Post_Adapter(List<Post_Item> list, String isbn) {
        this.myList = list;
        this.isbn = isbn;
        is_uid_isbn = TRUE;
    }

    @Override

    public Post_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_post, parent, false);
        return new Post_Adapter.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final Post_Adapter.ViewHolder holder, int position) {
        final Post_Item my = myList.get(position);
        ImageView popro = holder.popro;
        ImageView poimg = holder.poimg;

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(my.getPostimg());

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap1 = BitmapFactory.decodeStream(is);

                    if(is_uid_isbn) {
                        url = new URL(my.getProfile());

                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        is = conn.getInputStream();
                        bitmap2 = BitmapFactory.decodeStream(is);
                    }


                } catch (Exception e) { e.printStackTrace(); }
            }
        };

        mThread.start();

        try {
            mThread.join();
            poimg.setImageBitmap(bitmap1);
            if(is_uid_isbn) {
                popro.setImageBitmap(bitmap2);
            } else {
                popro.setImageBitmap(upro);
            }
        } catch (Exception e) { e.printStackTrace(); }

        if(my.getIsfav()) holder.poisfav.setImageResource(R.drawable.ic_favorite_orange_24dp);
        else holder.poisfav.setImageResource(R.drawable.ic_favorite_gray_24dp);

        holder.poname.setText(my.getUname());
        holder.podate.setText(my.getUname());
        holder.pofavcount.setText(my.getFavcount());
        holder.porevtext.setText(my.getUname());
        holder.poratingbar.setNumStars(my.getStar());

        /*

        holder.nolay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Context context = v.getContext();
                Intent intent = new Intent(context, BookInfo_Activity.class);
                intent.putExtra("barcodeContents",my.getIsbn());
                context.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View mView;
        public ImageView popro;
        public TextView poname;
        public RatingBar poratingbar;
        public TextView podate;
        public TextView pofavcount;
        public ImageView poisfav;
        public ImageView poimg;
        public TextView porevtext;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            popro = (ImageView)view.findViewById(R.id.post_img);
            poname = (TextView)view.findViewById(R.id.post_name);
            poratingbar = (RatingBar)view.findViewById(R.id.post_ratingbar);
            podate = (TextView)view.findViewById(R.id.post_date);
            pofavcount = (TextView)view.findViewById(R.id.post_favcount);
            poisfav = (ImageView) view.findViewById(R.id.post_isfav);
            poimg = (ImageView) view.findViewById(R.id.post_revimg);
            porevtext = (TextView) view.findViewById(R.id.post_revtext);

            }


        }

    }

}