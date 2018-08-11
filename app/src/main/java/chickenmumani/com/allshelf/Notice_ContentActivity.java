package chickenmumani.com.allshelf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import chickenmumani.com.allshelf.R;

public class Notice_ContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_content);
        setTitle("공지사항");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView title = (TextView) findViewById(R.id.noticeview_title);
        TextView date = (TextView) findViewById(R.id.noticeview_date);
        TextView content = (TextView) findViewById(R.id.notcontent_content);

        Intent intent = new Intent(this.getIntent());

        title.setText(intent.getStringExtra("title"));
        date.setText(intent.getStringExtra("date"));
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
