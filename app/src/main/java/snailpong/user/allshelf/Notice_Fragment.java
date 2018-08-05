package snailpong.user.allshelf;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class Notice_Fragment extends ListFragment {

    Notice_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Adapter 생성 및 Adapter 지정.
        adapter = new Notice_Adapter() ;
        setListAdapter(adapter) ; // 첫 번째 아이템 추가.
        adapter.addItem(1, "V1.0.1 업데이트 내용", "2018-05-26") ;
        adapter.addItem(2, "V1.0.2 업데이트 내용", "2018-06-20") ;
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    public void onResume(){
        super.onResume();
        ((Navi_Activity) getActivity())
                .setActionBarTitle("공지사항");

    }

    @Override
    public void onListItemClick (ListView l, View v, int position, long id) {
        // get TextView's Text.
        Notice_Item item = (Notice_Item)l.getItemAtPosition(position);
        String strText = item.getTitle();
        Toast.makeText(getActivity(), strText, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getActivity(),Notice_ContentActivity.class);
        intent.putExtra("id",item.getId());
        intent.putExtra("title",item.getTitle());
        intent.putExtra("date",item.getDate());
        startActivity(intent);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}
