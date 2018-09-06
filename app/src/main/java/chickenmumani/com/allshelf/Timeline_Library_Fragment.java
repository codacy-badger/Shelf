package chickenmumani.com.allshelf;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class Timeline_Library_Fragment extends Fragment{
    private RecyclerView mRecyclerView;
    public Timeline_Library_Fragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline_library ,container,false);
        mRecyclerView = (RecyclerView)inflater.inflate(R.layout.fragment_timeline_review, container, false);
        return view;
    }
}
