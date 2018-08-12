package chickenmumani.com.allshelf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import chickenmumani.com.allshelf.R;

public class Navi_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    Bitmap bitmap;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new Shelf_ImageFragment())
                .commit();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        dialog = ProgressDialog.show(Navi_Activity.this, "",
                "Loading... Please wait");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);

        TextView name = (TextView) hView.findViewById(R.id.header_name);
        TextView email = (TextView) hView.findViewById(R.id.header_email);
        ImageView img = (ImageView) hView.findViewById(R.id.header_img);

        name.setText(user.getDisplayName().toString());
        email.setText(user.getEmail().toString());

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                dialog.dismiss();
            }
        };

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(user.getPhotoUrl().toString());

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    handler.sendEmptyMessage(0);
                } catch (Exception e) { e.printStackTrace(); }
            }
        };

        mThread.start();

        try {
                      // 프로그레스다이얼로그 생성
            mThread.join();
            img.setImageBitmap(bitmap);
            img.setBackground(new ShapeDrawable(new OvalShape()));
            img.setClipToOutline(true);

            dialog.dismiss();
        } catch (Exception e) { e.printStackTrace(); }

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new Shelf_ImageFragment())
                    .commit();
        } else if (id == R.id.nav_notice) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new Notice_Fragment())
                    .commit();
            //for (Fragment fragment : getSupportFragmentManager().getFragments() ) {Log.d("1", "found"); getSupportFragmentManager().beginTransaction().remove(fragment).commit();}
        } else if (id == R.id.nav_version) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new Version_Fragment())
                    .commit();
        } else if (id == R.id.nav_security) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new Security_Fragment())
                    .commit();
        } else if (id == R.id.nav_alarm) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new Alarm_Fragment())
                    .commit();
        } else if (id == R.id.nav_rank) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new Rank_Fragment())
                    .commit();
        } else if (id == R.id.nav_center) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new ClientCenter_Fragment())
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

