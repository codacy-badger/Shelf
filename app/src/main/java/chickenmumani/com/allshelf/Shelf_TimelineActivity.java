package chickenmumani.com.allshelf;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import static java.lang.Boolean.FALSE;

public class Shelf_TimelineActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf_timeline);
        setTitle("리뷰");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final String uid = intent.getStringExtra("uid");
        String uname = intent.getStringExtra("uname");
        Drawable upro;
        final ImageView proimg = (ImageView) findViewById(R.id.timeline_img);
        TextView nameT = (TextView) findViewById(R.id.timeline_name);
        nameT.setText(uname);
        final ArrayList<String> reviewlist = new ArrayList<String>();

        mDatabase = FirebaseDatabase.getInstance().getReference("User_Info")
                .child(uid).child("Profile_Image");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String urls = (String)dataSnapshot.getValue();
                Thread mThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(urls);

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true);
                            conn.connect();

                            InputStream is = conn.getInputStream();
                            final Bitmap apro = BitmapFactory.decodeStream(is);
                            runOnUiThread(new Runnable(){
                                @Override
                                public void run() {
                                    proimg.setImageBitmap(apro);
                                    proimg.setBackground(new ShapeDrawable(new OvalShape()));
                                    proimg.setClipToOutline(true);
                                }
                            });
                        } catch (Exception e) { e.printStackTrace(); }
                    }
                };
                mThread.start();

                try {
                    mThread.join();
                } catch (Exception e) { e.printStackTrace(); }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabase.addValueEventListener(postListener);
        upro = proimg.getDrawable();

        mRecyclerView = (RecyclerView) findViewById(R.id.timeline_recyclerview);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(Shelf_TimelineActivity.this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);

        final ArrayList<Post_Item> myList = new ArrayList<Post_Item>();

        mDatabase = FirebaseDatabase.getInstance().getReference("Review")
                .child("User").child(uid);

        Log.d("w","review loading started");

        final Thread mThread = new Thread() {
            @Override
            public void run() {
                myList.clear();
                Log.d("w", "thread started");
                for(String n : reviewlist) {
                    DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference("Review")
                            .child("ReviewList").child(n);
                    Log.d("w", mDatabase2.toString());
                    ValueEventListener postListener2 = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("w", dataSnapshot.toString());
                            Map<String,Object> map = (Map<String,Object>) dataSnapshot.getValue();
                            Map<String,Object> mapUser = (Map<String,Object>) map.get("UserInfo");
                            Map<String,Object> mapFav = (Map<String,Object>) map.get("Good");
                            myList.add(new Post_Item(mapUser.get("uid").toString(), mapUser.get("proimg").toString(),
                                    mapUser.get("name").toString(), Integer.parseInt(map.get("Rate").toString()),
                                    map.get("Time").toString(), FALSE, Integer.parseInt(mapFav.get("Count").toString()),
                                    map.get("Image").toString(), map.get("Text").toString()
                            ));
                            Log.d("w",myList.toString());
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };
                    mDatabase2.addValueEventListener(postListener2);
                }
            }
        };


        ValueEventListener postListener3 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        reviewlist.add(ds.getValue().toString());
                    } catch(Exception e) {
                    }
                }

                mThread.start();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mDatabase.addListenerForSingleValueEvent(postListener3);
        Log.d("w","review loading started2222");

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        /*yList.add(new Post_Item("GwDXbIh64AeOH5dM29Wj3pg4yKc2",
                "https://lh4.googleusercontent.com/-KMK_rO0bACc/AAAAAAAAAAI/AAAAAAAAC6k/cCx-jNcjVOs/s96-c/photo.jpg",
                "달팡퐁", 4, "2018-08-25 17:09", FALSE, 67,
                "Review_Image/9788993178258_w92K9tUTyWTNEDZPxwwynnfzRdz1.jpg", "좋았다")); */

        mAdapter = new Post_Adapter(myList, uid, uname, upro);
        mRecyclerView.setAdapter(mAdapter);

        /*

        Thread mThread2 = new Thread() {
            @Override
            public void run() {
                while(true) {
                    try {
                        sleep(300);
                        Log.d("w", String.valueOf(myList.size()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        mThread2.start();
        */

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
