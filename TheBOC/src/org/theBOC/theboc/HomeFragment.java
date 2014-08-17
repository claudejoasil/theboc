package org.theBOC.theboc;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeFragment extends Fragment {
	public HomeFragment() {
        // Empty constructor required for fragment subclasses
    }
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        TextView txtView = (TextView) rootView.findViewById(R.id.txtTest);
        //int i = getArguments().getInt(ARG_PLANET_NUMBER);
        //String planet = getResources().getStringArray(R.array.menu_Items)[i];

        //int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
                        //"drawable", getActivity().getPackageName());
        //((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
        //getActivity().setTitle(planet);
        return rootView;
    }

}
