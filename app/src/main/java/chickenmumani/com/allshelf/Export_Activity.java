package chickenmumani.com.allshelf;

import android.os.Bundle;
import android.view.MenuItem;

import chickenmumani.com.allshelf.R;

public class Export_Activity extends AppCompatPreferenceActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_export);

        setTitle("내 서재 내보내기");
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
