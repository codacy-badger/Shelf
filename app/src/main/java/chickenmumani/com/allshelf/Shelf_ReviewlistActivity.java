package chickenmumani.com.allshelf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Shelf_ReviewlistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf_reviewlist);
        setTitle("리뷰");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
