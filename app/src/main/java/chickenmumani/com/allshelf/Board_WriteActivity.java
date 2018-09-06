package chickenmumani.com.allshelf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Board_WriteActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    FirebaseStorage storage;
    StorageReference storageRef;
    ProgressBar progressBar;
    int openrange = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);
        setTitle("게시물 쓰기");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        progressBar = (ProgressBar)findViewById(R.id.boardWriteProgressBar);
        progressBar.setVisibility(View.INVISIBLE);

        RadioGroup group1=(RadioGroup)findViewById(R.id.board_radcom);
        group1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.writere_radcomyes:
                        openrange = 1;
                        break;
                    case R.id.writere_radcomno:
                        openrange = 0;
                        break;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_upload:
            {
                if(((EditText)findViewById(R.id.board_titleedit)).getText().toString().length() >= 20) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder .setMessage("제목이 너무 깁니다. 조금만 줄여주세요.")
                            .setCancelable(false)
                            .setPositiveButton("확인", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
                else if(((EditText)findViewById(R.id.board_edit)).getText().toString().length() < 10) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder .setMessage("내용이 10자 미만으로 확인되어 게시물 등록에 실패하였습니다.")
                            .setCancelable(false)
                            .setPositiveButton("확인", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                    Date date = new Date();
                    String getTime = sdf.format(date);
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();;
                    FirebaseUser user = mAuth.getCurrentUser();
                    DatabaseReference tdata = mDatabase.child("chickenmumani.com.allshelf.Board_Item").child(date.getTime() + " " + user.getUid().toString());
                    tdata.child("date").setValue(getTime);
                    tdata.child("name").setValue(user.getDisplayName());
                    tdata.child("title").setValue(((EditText)findViewById(R.id.board_titleedit)).getText().toString());
                    tdata.child("text").setValue(((EditText)findViewById(R.id.board_edit)).getText().toString());
                    tdata.child("uuid").setValue(user.getUid().toString());

                    progressBar.setVisibility(View.INVISIBLE);
                    Board_WriteActivity.this.finish();
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.writere, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
