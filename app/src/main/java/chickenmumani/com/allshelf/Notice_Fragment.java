package chickenmumani.com.allshelf;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class Notice_Fragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;

    ProgressBar circle_bar;


    public Notice_Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);

        //circle_bar = (ProgressBar) view.findViewById(R.id.progressbar_loading);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.notice_recyclerview);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        mRecyclerView.setHasFixedSize(true);

        final ArrayList<Notice_Item> myList = new ArrayList<Notice_Item>();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference("Notice_Item");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //circle_bar.setVisibility(View.GONE);
                myList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String,Object> map = (Map<String,Object>) ds.getValue();
                    myList.add(new Notice_Item(map.get("id").toString(), map.get("title").toString(), map.get("date").toString())) ;
                }

                mAdapter = new Notice_Adapter(myList);
                mRecyclerView.setAdapter(mAdapter);
                //circle_bar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView mRecyclerView = (RecyclerView) view;

            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            mAdapter = new Notice_Adapter(myList);
            mRecyclerView.setAdapter(mAdapter);
        }

        return view;
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


    /*

    Notice_Adapter adapter;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Adapter 생성 및 Adapter 지정.
        adapter = new Notice_Adapter() ;
        setListAdapter(adapter) ; // 첫 번째 아이템 추가

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference("Notice_Item");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String,Object> map = (Map<String,Object>) ds.getValue();
                    adapter.addItem(map.get("id").toString(), map.get("title").toString(), map.get("date").toString()) ;
                    setListAdapter(adapter) ;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        //Toast.makeText(getActivity(), strText, Toast.LENGTH_LONG).show();

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
*/