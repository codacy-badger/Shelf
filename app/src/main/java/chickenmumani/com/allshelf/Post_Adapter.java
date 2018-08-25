package chickenmumani.com.allshelf;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class Post_Adapter extends RecyclerView.Adapter<Post_Adapter.ViewHolder> {

    private List<Post_Item> myList;
    Bitmap bitmap;
    Drawable upro;
    boolean is_uid_isbn;
    String uid, uname, isbn;
    FirebaseStorage storage;
    StorageReference storageRef;
    private DatabaseReference mDatabase;
    public View mView;

    public Post_Adapter(List<Post_Item> list, String uid, String uname, Drawable upro) {
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
        final ImageView popro = holder.popro;
        final ImageView poimg = holder.poimg;
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


/*
        Glide.with(mView)
                .load(storageRef.child(my.getPostimg()))
                .into(poimg);
*/
        StorageReference islandRef = storageRef.child(my.getPostimg());
        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                poimg.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });


        if(is_uid_isbn) {
            Thread mThread = new Thread() {
                @Override
                public void run() {
                    mDatabase = FirebaseDatabase.getInstance().getReference("User_Info")
                            .child(my.getUid()).child("Profile_URL");
                    ValueEventListener postListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String urls = (String)dataSnapshot.getValue();
                            try {
                                URL url = new URL(urls);

                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setDoInput(true);
                                conn.connect();

                                InputStream is = conn.getInputStream();
                                bitmap = BitmapFactory.decodeStream(is);
                            } catch (Exception e) { e.printStackTrace(); }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };
                    mDatabase.addListenerForSingleValueEvent(postListener);
                }
            };

            mThread.start();

            try {
                mThread.join();
                popro.setImageBitmap(bitmap);
            } catch (Exception e) { e.printStackTrace(); }
        } else {
            popro.setImageDrawable(upro);
        }

        if(my.getIsfav()) holder.poisfav.setImageResource(R.drawable.ic_favorite_orange_24dp);
        else holder.poisfav.setImageResource(R.drawable.ic_favorite_gray_24dp);

        holder.poname.setText(my.getUname());
        holder.podate.setText(my.getDate());
        holder.pofavcount.setText(String.valueOf(my.getFavcount()));
        holder.porevtext.setText(my.getPosttext());
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