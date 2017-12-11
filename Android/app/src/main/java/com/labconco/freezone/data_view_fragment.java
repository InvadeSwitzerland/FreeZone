package com.labconco.freezone;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by James Holdcroft
 */

public class data_view_fragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater Inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        return Inflater.inflate(R.layout.data_view_fragment, container, true);
    }
}
