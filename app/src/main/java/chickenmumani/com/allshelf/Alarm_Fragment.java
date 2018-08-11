package chickenmumani.com.allshelf;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import chickenmumani.com.allshelf.R;

public class Alarm_Fragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public Alarm_Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.alarm_recyclerview);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        mRecyclerView.setHasFixedSize(true);

        ArrayList<Message_Item> myList = new ArrayList<Message_Item>();

        myList.add(new Message_Item("1", ContextCompat.getDrawable(getActivity(), R.drawable.ic_close_black_24dp),
                "공돌이님이 새로운 도서를 등록했습니다.", "10분 전"));
        myList.add(new Message_Item("2", ContextCompat.getDrawable(getActivity(), R.drawable.ic_info_black_24dp),
                "공돌이님이 내 리뷰에 댓글을 달았습니다.", "1시간 전"));

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView mRecyclerView = (RecyclerView) view;

            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new Message_Adapter(myList);
            mRecyclerView.setAdapter(mAdapter);
        }

        return view;
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
