package chickenmumani.com.allshelf;

import android.app.ProgressDialog;
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
import java.util.Collections;
import java.util.Comparator;
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

        final ProgressDialog dialog = ProgressDialog.show(getContext(), "",
                "Loading... Please wait");


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
                Collections.sort(myList,new TimingN());
                mAdapter = new Notice_Adapter(myList);
                mRecyclerView.setAdapter(mAdapter);
                //circle_bar.setVisibility(View.INVISIBLE);
                dialog.dismiss();
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

class TimingN implements Comparator<Notice_Item> {
    @Override
    public int compare(Notice_Item o1, Notice_Item o2) {
        return o2.getDate().compareTo(o1.getDate());
    }

}