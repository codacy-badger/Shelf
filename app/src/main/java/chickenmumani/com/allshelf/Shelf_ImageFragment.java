package chickenmumani.com.allshelf;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import chickenmumani.com.allshelf.R;

public class Shelf_ImageFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_shelf_image,container,false);

        FloatingActionButton addBook = (FloatingActionButton) v.findViewById(R.id.shelf_addbook);
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCameraPermission();
            }
        });

        return v;
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
            //Intent intent = new Intent(getActivity(), BarcodeScanActivity.class);
            //startActivity(intent);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
