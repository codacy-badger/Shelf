package snailpong.user.allshelf;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Alarm_Fragment extends ListFragment {

    Message_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Adapter 생성 및 Adapter 지정.
        adapter = new Message_Adapter() ;
        setListAdapter(adapter) ; // 첫 번째 아이템 추가.
        adapter.addItem(1, ContextCompat.getDrawable(getActivity(), R.drawable.ic_close_black_24dp),
                "공돌이님이 새로운 도서를 등록했습니다.", "10분 전") ;
        adapter.addItem(2, ContextCompat.getDrawable(getActivity(), R.drawable.ic_info_black_24dp),
                "공돌이님이 내 리뷰에 댓글을 달았습니다.", "1시간 전") ;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void onResume(){
        super.onResume();
        ((Navi_Activity) getActivity())
                .setActionBarTitle("알람");

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
