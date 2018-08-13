package chickenmumani.com.allshelf;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import chickenmumani.com.allshelf.R;

public class BookInfo_Activity extends AppCompatActivity {

    private String TTBKey = "ttbybson20100846003"; // Aladin Open API TTBKEY 값
    Bitmap bitmap;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        Intent intent = getIntent();
        final String ISBN13 = intent.getExtras().getString("barcodeContents");

        final RelativeLayout bookinfo_oolin = (RelativeLayout) findViewById(R.id.bookinfo_oolin);
        final RelativeLayout bookinfo_xxlin = (RelativeLayout) findViewById(R.id.bookinfo_xxlin);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("User_Book")
                .child(user.getUid());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(ISBN13)) {
                    bookinfo_xxlin.setVisibility(View.INVISIBLE);
                } else {
                    bookinfo_oolin.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        try{
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    final ArrayList<String> bInfo = aISBNJsonParser(getJsonData(ISBN13));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            TextView vTitle = (TextView) findViewById(R.id.bookinfo_bookname);
                            TextView vAuthor = (TextView) findViewById(R.id.bookinfo_author);
                            TextView vPublisher = (TextView) findViewById(R.id.bookinfo_company);
                            TextView vPubDate = (TextView) findViewById(R.id.bookinfo_date);
                            TextView vISBN = (TextView) findViewById(R.id.bookinfo_isbn);
                            Button vPur = (Button) findViewById(R.id.bookinfo_purchase);
                            Button vAdd = (Button) findViewById(R.id.bookinfo_regist);
                            ImageView img = (ImageView) findViewById(R.id.bookinfo_img);

                            vPur.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    Uri uri = Uri.parse(bInfo.get(5));
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            });

                            Thread mThread = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        URL url = new URL(bInfo.get(6));

                                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                        conn.setDoInput(true);
                                        conn.connect();

                                        InputStream is = conn.getInputStream();
                                        bitmap = BitmapFactory.decodeStream(is);
                                    } catch (Exception e) { e.printStackTrace(); }
                                }
                            };
                            mThread.start();
                            try {
                                mThread.join();
                                img.setImageBitmap(bitmap);
                            } catch (Exception e) { e.printStackTrace(); }

                            vTitle.setText(bInfo.get(0));
                            vAuthor.setText(bInfo.get(1));
                            vPublisher.setText(bInfo.get(2));
                            vPubDate.setText(bInfo.get(3));
                            vISBN.setText(bInfo.get(4));

                            vAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String getTime = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date(System.currentTimeMillis()));

                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                    DatabaseReference databaseReference = firebaseDatabase.getReference();
                                    mDatabase = FirebaseDatabase.getInstance().getReference("User_Book")
                                            .child(user.getUid()).child(bInfo.get(4));
                                    mDatabase.child("author").setValue(bInfo.get(1));
                                    mDatabase.child("imgurl").setValue(bInfo.get(6));
                                    mDatabase.child("isbn").setValue(bInfo.get(4));
                                    mDatabase.child("time").setValue(getTime);
                                    mDatabase.child("title").setValue(bInfo.get(0));
                                    finish();
                                }
                            });
                        }
                    });
                }
            });

            Button close = (Button) findViewById(R.id.bookinfo_close);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        } catch(Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(BookInfo_Activity.this);     // 여기서 this는 Activity의 this
            builder .setMessage("정보를 불러오는 중 문제가 발생했습니다. 다시 시도하세요.")
                    .setCancelable(false)
                    .setPositiveButton("닫기", null);

            AlertDialog dialog = builder.create();    // 알림창 객체 생성
            dialog.show();    // 알림창 띄우기
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("result", "No");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    public String getJsonData(String ISBN13){
        String str = null;
        String res = null;

        String Query = null;

        Query = "http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?"
                + "ttbkey=" + TTBKey
                + "&itemIdType=ISBN13&ItemId=" + ISBN13
                + "&output=js&Version=" + "20131101"
                + "&OptResult=" + "ebookList,usedList,reviewList";

        try{
            URL url = new URL(Query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            if (conn.getResponseCode() == conn.HTTP_OK){
                InputStreamReader temp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader bReader = new BufferedReader(temp);
                StringBuffer buffer = new StringBuffer();

                while((str=bReader.readLine()) != null){
                    buffer.append(str);
                }

                res = buffer.toString();

                Log.i("res = ", res);
                bReader.close();
            } else {
                Log.i("Err = ", conn.getResponseCode() + "Error");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public ArrayList aISBNJsonParser(String res){
        ArrayList<String> bookInfos = new ArrayList<String>();

        String tBf = null;
        String aBf = null;
        String pBf = null;
        String pDBf = null;
        String iBf = null;
        String lBf = null;
        String cBf = null;


        try {
            JSONArray jarray = new JSONObject(res).getJSONArray("item");

            for (int i = 0; i < jarray.length(); i++){
                HashMap hs = new HashMap<>();
                JSONObject jObject = jarray.getJSONObject(i);

                tBf = jObject.optString("title");
                if(tBf != null) bookInfos.add(0, tBf);

                aBf = jObject.optString("author");
                if(aBf != null) bookInfos.add(1, aBf);

                pBf = jObject.optString("publisher");
                if(pBf != null) bookInfos.add(2, pBf);

                pDBf = jObject.optString("pubDate");
                if(pDBf != null) bookInfos.add(3, pDBf);

                iBf = jObject.optString("isbn13");
                if(iBf != null) bookInfos.add(4, iBf);

                lBf = jObject.optString("link");
                if(lBf != null) bookInfos.add(5, lBf);

                cBf = jObject.optString("cover");
                if(cBf != null) bookInfos.add(6, cBf);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return bookInfos;
    }
}
