package snailpong.user.allshelf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;

public class Export_Activity extends AppCompatPreferenceActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_export);

        setTitle("내 서재 내보내기");
    }
    /*

    public void setContentView(int layoutResID) {
        ViewGroup contentView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.activity_export, new LinearLayout(this), false);

        toolbar = (Toolbar) contentView.findViewById(R.id.toolbar);

        ViewGroup contentWrapper = (ViewGroup) contentView
                .findViewById(R.id.content);
        LayoutInflater.from(this).inflate(layoutResID, contentWrapper, true);

        getWindow().setContentView(contentView);
    }*/
}
