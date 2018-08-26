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
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import static java.lang.Boolean.FALSE;

public class Shelf_TimelineActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;
    private int count, allcount;
    String uname;
    Drawable upro;
    Thread mThread1;
    String urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf_timeline);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final String uid = intent.getStringExtra("uid");
        uname = intent.getStringExtra("uname");
        setTitle(uname);

        final ImageView proimg = (ImageView) findViewById(R.id.timeline_img);
        TextView nameT = (TextView) findViewById(R.id.timeline_name);
        nameT.setText(uname);
        final ArrayList<String> reviewlist = new ArrayList<String>();

        mThread1 = new Thread() {
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
                            upro = proimg.getDrawable();
                        }
                    });
                } catch (Exception e) { e.printStackTrace(); }
            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference("User_Info")
                .child(uid).child("Profile_Image");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                urls = (String)dataSnapshot.getValue();
                mThread1.start();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabase.addListenerForSingleValueEvent(postListener);

        mRecyclerView = (RecyclerView) findViewById(R.id.timeline_recyclerview);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(Shelf_TimelineActivity.this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);

        final ArrayList<Post_Item> myList = new ArrayList<Post_Item>();

        mDatabase = FirebaseDatabase.getInstance().getReference("Review")
                .child("User").child(uid);
        try {
            mThread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final Thread mThread = new Thread() {
            @Override
            public void run() {
                myList.clear();
                count = 0;
                allcount = reviewlist.size();
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        ((TextView)findViewById(R.id.timeline_reviewcount)).setText(String.valueOf(allcount));
                    }
                });
                for(String n : reviewlist) {
                    DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference("Review")
                            .child("ReviewList").child(n);
                    ValueEventListener postListener2 = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String,Object> map = (Map<String,Object>) dataSnapshot.getValue();
                            Map<String,Object> mapUser = (Map<String,Object>) map.get("UserInfo");
                            Map<String,Object> mapFav = (Map<String,Object>) map.get("Good");
                            myList.add(new Post_Item(mapUser.get("uid").toString(), map.get("Book").toString(),
                                    mapUser.get("proimg").toString(), mapUser.get("name").toString(),
                                    Integer.parseInt(map.get("Rate").toString()), map.get("Time").toString(),
                                    FALSE, Integer.parseInt(mapFav.get("Count").toString()),
                                    map.get("Image").toString(), map.get("Text").toString()
                            ));
                            count++;
                            if(allcount == count) {
                                Collections.sort(myList,new TimingP());
                                mAdapter = new Post_Adapter(myList, uid, uname, upro);
                                mRecyclerView.setAdapter(mAdapter);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };
                    mDatabase2.addListenerForSingleValueEvent(postListener2);
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

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

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

    class TimingP implements Comparator<Post_Item> {
        @Override
        public int compare(Post_Item o1, Post_Item o2) {
            return o2.getDate().compareTo(o1.getDate());
        }

    }
}
