package org.d3ifcool.nabung;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    public Session(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setLoggedin(boolean loggedin){
        editor.putBoolean("loggedInmode", loggedin);
        editor.commit();
    }

    public boolean loggedIn(){
        return preferences.getBoolean("loggedInmode", false);
    }
}
