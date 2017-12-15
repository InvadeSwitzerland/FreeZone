package com.labconco.freezone;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by James Holdcroft
 */

public class ip_fragment extends DialogFragment implements View.OnClickListener{
    private EditText ip;
    Button setIP;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ip_fragment, null);
        setIP =  view.findViewById(R.id.IPbutton);
        setCancelable(false);
        setIP.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.IPbutton){
            Log.d("Debug", "ip set");
            ip = getDialog().findViewById(R.id.ipInput);
            savedPreferenceManager preferenceManager = new savedPreferenceManager(MainActivity.getContextOfApplication());
            if (ip != null) {
                IPValidator validator = new IPValidator(ip.getText().toString());
                if (validator.validate()){
                    preferenceManager.store("dryer_ip", ip.getText().toString());
                    Toast.makeText(getActivity(), "Freeze Dryer IP has been set.", Toast.LENGTH_SHORT).show();
                    dismiss();
                } else {
                    Toast.makeText(getActivity(), "Invalid IP", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
