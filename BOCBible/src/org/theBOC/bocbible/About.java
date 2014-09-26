package org.theBOC.bocbible;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.theBOC.bocbible.R;
public class About extends Fragment {
	public About() {
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_about, container, false);
		
        return rootView;
    }
	
}
