package chickenmumani.com.allshelf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import chickenmumani.com.allshelf.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Temporary MainActivity");

        setContentView(R.layout.activity_main);

        Button btn_go1 = (Button) findViewById(R.id.mainButton1);
        btn_go1.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), Reviewview_Activity.class);
                        startActivity(intent);
                    }
                }
        );

        /*Button btn_go2 = (Button) findViewById(R.id.mainButton2);
        btn_go2.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), AlarmActivity.class);
                        startActivity(intent);
                    }
                }
        );*/

        Button btn_go3 = (Button) findViewById(R.id.mainButton3);
        btn_go3.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), Logout_Activity.class);
                        startActivityForResult(intent, 1);
                    }
                }
        );

        Button btn_go4 = (Button) findViewById(R.id.mainButton4);
        btn_go4.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), SettingsActivity.class);
                        startActivity(intent);
                    }
                }
        );

        Button btn_go5 = (Button) findViewById(R.id.mainButton5);
        btn_go5.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), Export_Activity2.class);
                        startActivity(intent);
                    }
                }
        );
    /*
        Button btn_go6 = (Button) findViewById(R.id.mainButton6);
        btn_go6.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), FriendManageActivity.class);
                        startActivity(intent);
                    }
                }
        );*/

        Button btn_go7 = (Button) findViewById(R.id.mainButton7);
        btn_go7.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), Message_PopActivity.class);
                        startActivityForResult(intent, 2);
                    }
                }
        );

        Button btn_go8 = (Button) findViewById(R.id.mainButton8);
        btn_go8.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), Navi_Activity.class);
                        startActivity(intent);
                    }
                }
        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        TextView txtResult = (TextView)findViewById(R.id.mainoutresult);

        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                String result = data.getStringExtra("result");
                txtResult.setText("1 " + result);
            }
        }

        if(requestCode==2){
            if(resultCode==RESULT_OK){
                //데이터 받기
                String result = data.getStringExtra("result");
                txtResult.setText("2 " + result);
            }
        }
    }

}
