package chickenmumani.com.allshelf;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
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

public class Shelf_ReviewlistActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;
    private int count, allcount;
    String uname, bookname, isbn, coverurl;
    Drawable upro;
    Thread mThread1;
    String urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf_reviewlist);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        bookname = intent.getStringExtra("bookname");
        isbn = intent.getStringExtra("isbn");
        coverurl = intent.getStringExtra("cover");

        Log.d("w", bookname + isbn + coverurl);

        ((TextView)findViewById(R.id.reviewlist_name)).setText(bookname);
        setTitle(bookname);

        final ImageView proimg = (ImageView) findViewById(R.id.reviewlist_img);
        final ArrayList<String> reviewlist = new ArrayList<String>();

        mThread1 = new Thread() {
            @Override
            public void run() {
                try {
                    Log.d("w",coverurl);
                    URL url = new URL(coverurl);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    final Bitmap apro = BitmapFactory.decodeStream(is);
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            proimg.setImageBitmap(apro);
                        }
                    });
                } catch (Exception e) { e.printStackTrace(); }
            }
        };

        mThread1.start();

        mRecyclerView = (RecyclerView) findViewById(R.id.timeline_recyclerview);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(Shelf_ReviewlistActivity.this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setHasFixedSize(true);

        final ArrayList<Post_Item> myList = new ArrayList<Post_Item>();

        mDatabase = FirebaseDatabase.getInstance().getReference("Review")
                .child("Book").child(isbn);


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
