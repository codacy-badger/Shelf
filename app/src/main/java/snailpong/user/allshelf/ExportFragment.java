package snailpong.user.allshelf;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class ExportFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_export);
    }

    public void onResume(){
        super.onResume();
        ((Navi_Activity) getActivity())
                .setActionBarTitle("내 서재 내보내기");

    }
}
