package com.labconco.freezone;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by James Holdcroft
 */

public class data_view_fragment extends Fragment{
    private TextView temperatureTV;
    private TextView vacuumTV;

    @Override
    public View onCreateView(LayoutInflater Inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        //temperatureTV = getView().findViewById(R.id.temperature_view);
        //vacuumTV = getView().findViewById(R.id.vacuum_view);
        return Inflater.inflate(R.layout.data_view_fragment, container, true);
    }


}
