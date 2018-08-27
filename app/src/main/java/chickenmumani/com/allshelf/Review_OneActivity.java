package chickenmumani.com.allshelf;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.Map;

import static java.lang.Boolean.FALSE;

public class Review_OneActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Post_Item p;
    private Bitmap bitmap;
    FirebaseStorage storage;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_post);
        setTitle("");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference("Review")
                .child("ReviewList").child(getIntent().getStringExtra("num"));
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Object> map = (Map<String,Object>) dataSnapshot.getValue();
                Map<String,Object> mapUser = (Map<String,Object>) map.get("UserInfo");
                Map<String,Object> mapFav = (Map<String,Object>) map.get("Good");
                p = new Post_Item(mapUser.get("uid").toString(), map.get("Book").toString(),
                        map.get("ISBN").toString(), mapUser.get("proimg").toString(), mapUser.get("name").toString(),
                        Integer.parseInt(map.get("Rate").toString()), map.get("Time").toString(),
                        FALSE, Integer.parseInt(mapFav.get("Count").toString()),
                        map.get("Image").toString(), map.get("Text").toString());
                ((TextView)findViewById(R.id.post_name)).setText(p.getUname());
                ((TextView)findViewById(R.id.post_book)).setText(p.getBook());
                ((TextView)findViewById(R.id.post_date)).setText(p.getDate());
                ((TextView)findViewById(R.id.post_favcount)).setText(String.valueOf(p.getFavcount()));
                ((TextView)findViewById(R.id.post_revtext)).setText(p.getPosttext());
                Thread mThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(p.getProfile());
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true);
                            conn.connect();
                            InputStream is = conn.getInputStream();
                            bitmap = BitmapFactory.decodeStream(is);
                            storage = FirebaseStorage.getInstance();
                            storageRef = storage.getReference();

                            StorageReference islandRef = storageRef.child(p.getPostimg());
                            final long ONE_MEGABYTE = 1024 * 1024;
                            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    ((ImageView)findViewById(R.id.post_revimg)).
                                            setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {}
                            });
                        } catch (Exception e) { e.printStackTrace(); }
                    }
                };
                mThread.start();
                try {
                    mThread.join();
                    ((ImageView)findViewById(R.id.post_img)).setImageBitmap(bitmap);
                    ((ImageView)findViewById(R.id.post_img)).setBackground(new ShapeDrawable(new OvalShape()));
                    ((ImageView)findViewById(R.id.post_img)).setClipToOutline(true);
                } catch (Exception e) { e.printStackTrace(); }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
