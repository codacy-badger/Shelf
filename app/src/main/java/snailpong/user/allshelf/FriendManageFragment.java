package snailpong.user.allshelf;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

public class FriendManageFragment extends PreferenceFragmentCompat
{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_friend_manage);
    }

    public void onResume(){
        super.onResume();
        ((Navi_Activity) getActivity())
                .setActionBarTitle("친구관리");

    }

}