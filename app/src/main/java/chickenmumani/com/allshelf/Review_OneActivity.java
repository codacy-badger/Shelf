package chickenmumani.com.allshelf;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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


public class Review_OneActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Post_Item p;
    private Bitmap bitmap;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseUser user;
    boolean isfav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_post);
        setTitle("");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("Review")
                .child("ReviewList").child(getIntent().getStringExtra("num"));
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Object> map = (Map<String,Object>) dataSnapshot.getValue();
                Map<String,Object> mapUser = (Map<String,Object>) map.get("UserInfo");
                Map<String,Object> mapFav = (Map<String,Object>) map.get("Good");
                if(dataSnapshot.child("Good").child(user.getUid()).getValue() != null) isfav = true;
                else isfav = false;
                p = new Post_Item(dataSnapshot.getKey(), mapUser.get("uid").toString(), map.get("Book").toString(),
                        map.get("ISBN").toString(), mapUser.get("proimg").toString(), mapUser.get("name").toString(),
                        Integer.parseInt(map.get("Rate").toString()), map.get("Time").toString(),
                        isfav, Integer.parseInt(mapFav.get("Count").toString()),
                        map.get("Image").toString(), map.get("Text").toString());
                p.setFavcount((int)dataSnapshot.child("Good").getChildrenCount()-1);
                mDatabase.child("Good").child("Count").setValue(p.getFavcount());
                ((TextView)findViewById(R.id.post_name)).setText(p.getUname());
                ((TextView)findViewById(R.id.post_book)).setText(p.getBook());
                ((TextView)findViewById(R.id.post_date)).setText(p.getDate());
                ((TextView)findViewById(R.id.post_favcount)).setText(String.valueOf(p.getFavcount()));
                ((TextView)findViewById(R.id.post_revtext)).setText(p.getPosttext());
                if(isfav) ((ImageButton)findViewById(R.id.post_isfav)).setImageResource(R.drawable.ic_favorite_orange_24dp);
                else ((ImageButton)findViewById(R.id.post_isfav)).setImageResource(R.drawable.ic_favorite_gray_24dp);
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

        ((ImageButton)findViewById(R.id.post_isfav)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!p.getIsfav()) {
                    mDatabase.child("Good").child(user.getUid()).setValue(user.getDisplayName());
                    ((ImageButton)findViewById(R.id.post_isfav)).setImageResource(R.drawable.ic_favorite_orange_24dp);
                }
            }
        });

        ((ImageButton)findViewById(R.id.post_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Context wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.MyPopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, v);
                if(p.getUid().equals(user.getUid())) getMenuInflater().inflate(R.menu.post_menu2, popup.getMenu());
                else getMenuInflater().inflate(R.menu.post_menu1, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.action_edit) {
                            Intent intent = new Intent(Review_OneActivity.this, Review_EditActivity.class);
                            intent.putExtra("num", p.getKey());
                            startActivity(intent);
                        } else if(item.getItemId() == R.id.action_report) {
                            Intent intent = new Intent(Review_OneActivity.this, Report_Activity.class);
                            startActivity(intent);
                        }
                        return false;
                    }
                } );
                popup.show();
            }
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
