package chickenmumani.com.allshelf;

import android.content.Intent;
import android.os.Bundle;
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

import chickenmumani.com.allshelf.R;

public class Navi_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, Logout_Activity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
