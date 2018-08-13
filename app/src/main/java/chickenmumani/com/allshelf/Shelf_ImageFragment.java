package chickenmumani.com.allshelf;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

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

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private TextView ftxt1, ftxt2;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                "Loading... Please wait");

        View view = inflater.inflate(R.layout.fragment_shelf_image,container,false);

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
                int check = 1;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        Map<String,Object> map = (Map<String,Object>) ds.getValue();
                        myList.add(new Shelf_Item(map.get("isbn").toString(), map.get("title").toString(), map.get("author").toString()
                                ,map.get("imgurl").toString(), map.get("time").toString() )) ;
                    } catch(Exception e) {
                        check = 0;
                    }
                }

                if(check == 1) {
                    mAdapter = new Shelf_ImageAdapter(myList);
                    mRecyclerView.setAdapter(mAdapter);
                }

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

        fab_open = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_close);

        fab = (FloatingActionButton) getActivity().findViewById(R.id.shelf_addbookfab);
        fab1 = (FloatingActionButton) getActivity().findViewById(R.id.shelf_addbookfabbarcode);
        fab2 = (FloatingActionButton) getActivity().findViewById(R.id.shelf_addbookfabsearch);

        ftxt1 = (TextView) getActivity().findViewById(R.id.shelf_lintxt1);
        ftxt2 = (TextView) getActivity().findViewById(R.id.shelf_lintxt2);

        final View shfab = (View) getActivity().findViewById(R.id.shelfimage_fabimgview);
        FrameLayout shfra = (FrameLayout) getActivity().findViewById(R.id.shelf_framelay);
        shfab.bringToFront();
        shfra.bringToFront();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shfab.getVisibility() == View.VISIBLE)
                    shfab.setVisibility(View.INVISIBLE);
                else shfab.setVisibility(View.VISIBLE);
                anim();
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shfab.setVisibility(View.INVISIBLE);
                getCameraPermission();
                anim();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shfab.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(getActivity(), Search_Activity.class);
                startActivity(intent);
                anim();
            }
        });
        shfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shfab.setVisibility(View.INVISIBLE);
                anim();
            }
        });

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

    public void anim() {

        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            ftxt1.startAnimation(fab_close);
            ftxt2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            ftxt1.startAnimation(fab_open);
            ftxt2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }
}
