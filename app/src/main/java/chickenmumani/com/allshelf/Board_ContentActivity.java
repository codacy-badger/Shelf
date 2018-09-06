package chickenmumani.com.allshelf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Board_ContentActivity extends AppCompatActivity {
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_content);
        setTitle("게시판");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar)findViewById(R.id.boardProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        TextView title = (TextView) findViewById(R.id.board_title_textview);
        TextView date = (TextView) findViewById(R.id.board_date_textview);
        TextView user = (TextView) findViewById(R.id.board_user_textview);
        TextView content = (TextView) findViewById(R.id.board_content_textview);
        TextView tagText = (TextView) findViewById(R.id.board_output_tag_text);
        ImageView tagImage = (ImageView) findViewById(R.id.board_output_tag_image);

        Intent intent = new Intent(this.getIntent());

        title.setText(intent.getStringExtra("title"));
        date.setText(intent.getStringExtra("date"));
        user.setText(intent.getStringExtra("name"));
        content.setText(intent.getStringExtra("text"));
        tagText.setText(intent.getStringExtra("tag"));
        if(intent.getStringExtra("tag").equalsIgnoreCase("교환")){
            tagImage.setImageResource(R.drawable.ic_board_change);
        }
        else{
            tagImage.setImageResource(R.drawable.ic_board_toron);
        }

        progressBar.setVisibility(View.INVISIBLE);
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