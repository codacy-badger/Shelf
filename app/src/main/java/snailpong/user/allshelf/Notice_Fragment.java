package snailpong.user.allshelf;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Notice_Fragment extends ListFragment {

    Notice_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Adapter 생성 및 Adapter 지정.
        adapter = new Notice_Adapter() ;
        setListAdapter(adapter) ; // 첫 번째 아이템 추가.
        adapter.addItem("V1.0.1 업데이트 내용", "2018-5-26") ;
        adapter.addItem("V1.0.2 업데이트 내용", "2018-6-20") ;
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    public void onResume(){
        super.onResume();
        ((Navi_Activity) getActivity())
                .setActionBarTitle("공지사항");

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
