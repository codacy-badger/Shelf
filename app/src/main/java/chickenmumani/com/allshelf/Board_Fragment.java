package chickenmumani.com.allshelf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class Board_Fragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;
    private ArrayList<Board_Item> myList;
    private int list_number;


    public Board_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_board, container, false);
        list_number = 1;
        myList = new ArrayList<Board_Item>();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.board_recyclerview);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        mRecyclerView.setHasFixedSize(true);


        mDatabase = FirebaseDatabase.getInstance().getReference("Board_Item");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String,Object> map = (Map<String,Object>) ds.getValue();
                    try{
                        myList.add(new Board_Item(map.get("date").toString(), map.get("name").toString(), map.get("uuid").toString(), map.get("title").toString(), map.get("text").toString(), map.get("tag").toString()));
                    }
                    catch(NullPointerException e){
                        e.printStackTrace();
                    }
                }
                Collections.sort(myList, new TimingN2());
                mAdapter = new Board_Adapter(myList);
                mRecyclerView.setAdapter(mAdapter);
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
            mAdapter = new Board_Adapter(myList);
            mRecyclerView.setAdapter(mAdapter);
        }

        return view;
    }

    public void onResume(){
        super.onResume();
        ((Navi_Activity) getActivity()).setActionBarTitle("게시판");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.board_menu, menu) ;
        final MenuItem item1 = menu.findItem(R.id.action_board_add);
        final MenuItem item2 = menu.findItem(R.id.action_board_list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case  R.id.action_board_add:
                Intent intent = new Intent(getContext(), Board_WriteActivity.class);
                startActivity(intent);
                break;
            case  R.id.action_board_list:
                ArrayList<Board_Item> insList = new ArrayList<Board_Item>();
                insList.clear();
                switch (list_number % 3){
                    case 0:
                        for(Board_Item insItem : myList){
                            if(insItem.getTag().equalsIgnoreCase("토론")){
                                insList.add(insItem);
                            }
                        }
                        break;
                    case 1:
                        for(Board_Item insItem : myList){
                            if(insItem.getTag().equalsIgnoreCase("교환") || insItem.getTag().equalsIgnoreCase("토론") ){
                                insList.add(insItem);
                            }
                        }
                        break;
                    case 2:
                        for(Board_Item insItem : myList){
                            if(insItem.getTag().equalsIgnoreCase("교환")){
                                insList.add(insItem);
                            }
                        }
                        break;
                    default:
                        break;
                }
                list_number += 1;
                mAdapter = new Board_Adapter(insList);
                mRecyclerView.setAdapter(mAdapter);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}

class TimingN2 implements Comparator<Board_Item> {
    @Override
    public int compare(Board_Item o1, Board_Item o2) {
        return o2.getDBID().compareTo(o1.getDBID());
    }
}