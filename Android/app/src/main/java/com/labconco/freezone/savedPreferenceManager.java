package com.labconco.freezone;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by James Holdcroft.
 * */

public class savedPreferenceManager {
    private SharedPreferences prefStore;

    public savedPreferenceManager(Context activity){
        prefStore = activity.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
    }

    protected void store(String key, String value){
        SharedPreferences.Editor prefEdit = prefStore.edit();
        prefEdit.putString(key, value);
        prefEdit.apply();

    }

    protected String pull(String key){
        return prefStore.getString(key, "Empty");
    }

    protected String pull(String key, String holder){
        return prefStore.getString(key, holder);
    }

}
