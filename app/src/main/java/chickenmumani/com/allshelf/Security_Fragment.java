package chickenmumani.com.allshelf;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import chickenmumani.com.allshelf.R;

public class Security_Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_security,container,false);

        LinearLayout b = (LinearLayout) v.findViewById(R.id.lin_exportb);
        b.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), Export_Activity.class);
                startActivity(intent);

            }

        });

        LinearLayout c = (LinearLayout) v.findViewById(R.id.lin_withb);
        c.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), WithDrawal_Activity.class);
                startActivity(intent);

            }

        });

        return v;
    }

    public void onResume(){
        super.onResume();
        ((Navi_Activity) getActivity())
                .setActionBarTitle("개인/보안");

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}