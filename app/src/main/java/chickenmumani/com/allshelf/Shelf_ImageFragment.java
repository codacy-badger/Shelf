package chickenmumani.com.allshelf;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class Shelf_ImageFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                "Loading... Please wait");

        View view = inflater.inflate(R.layout.fragment_shelf_image,container,false);

        FloatingActionButton addBook = (FloatingActionButton) view.findViewById(R.id.shelf_addbook);
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCameraPermission();
            }
        });



        mRecyclerView = (RecyclerView) view.findViewById(R.id.book_shelfimagerecyclerview);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.setHasFixedSize(true);

        final ArrayList<Shelf_Item> myList = new ArrayList<Shelf_Item>();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference("User_Book")
            .child(user.getUid());

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //circle_bar.setVisibility(View.GONE);
                //http://www.aladin.co.kr/ttb/api/ItemLookUp.aspx?ttbkey=ttbybson20100846003&itemIdType=ISBN13&ItemId=9788968483318&output=js&Version=20131101&OptResult=ebookList,usedList,reviewList
                myList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Map<String,Object> map = (Map<String,Object>) ds.getValue();
                    myList.add(new Shelf_Item(map.get("isbn").toString(), map.get("title").toString(), map.get("author").toString()
                        ,map.get("imgurl").toString(), map.get("time").toString() )) ;
                }

                mAdapter = new Shelf_ImageAdapter(myList);
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
            mAdapter = new Shelf_ImageAdapter(myList);
            mRecyclerView.setAdapter(mAdapter);
        }

        dialog.dismiss();

        return view;
    }

    public void onResume(){
        super.onResume();
        ((Navi_Activity) getActivity())
                .setActionBarTitle("책장");

    }

    private void getCameraPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            //권한없음
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},0);
        } else {
            //권한있음
            Intent intent = new Intent(getActivity(), Shelf_BarcodeScanActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
