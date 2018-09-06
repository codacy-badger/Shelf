package chickenmumani.com.allshelf;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class Timeline_Review_Fragment extends Fragment{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase , mDatabase2, mDatabase4;
    private FirebaseUser user;
    private String uid, uname;

    public Timeline_Review_Fragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline_review ,container,false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.timeline_review_recyclerview);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        mRecyclerView.setHasFixedSize(true);

        Intent intent = new Intent(getActivity(), Shelf_TimelineActivity.class);
        uid = intent.getStringExtra("uid");
        uname = intent.getStringExtra("uname");

        final ArrayList<Post_Item> myList = new ArrayList<Post_Item>();

        mDatabase = FirebaseDatabase.getInstance().getReference("Review").child("User").child("2ZcuvjjG4RUHiJUUdTIXuu4LEgv2");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Object> map = (Map<String,Object>) dataSnapshot.getValue();
                Map<String,Object> mapUser = (Map<String,Object>) map.get("UserInfo");
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    try{
                        myList.add(new Post_Item(dataSnapshot.getKey(), uid, map.get("Book").toString(),
                                map.get("ISBN").toString(), mapUser.get("proimg").toString(), mapUser.get("name").toString(),
                                Integer.parseInt(map.get("Rate").toString()), map.get("Time").toString(),
                                true, (int)dataSnapshot.child("Good").getChildrenCount()-1,
                                map.get("Image").toString(), map.get("Text").toString()
                        ));
                        mDatabase2.child("Good").child("Count").setValue((int)dataSnapshot.child("Good").getChildrenCount()-1);
                    }
                    catch(NullPointerException event){
                        event.printStackTrace();
                    }
                }
                Collections.sort(myList,new TimingP());
                mAdapter = new Post_Adapter(myList, uid, uname, null);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    class TimingP implements Comparator<Post_Item> {
        @Override
        public int compare(Post_Item o1, Post_Item o2) {
            return o2.getDate().compareTo(o1.getDate());
        }

    }
}
