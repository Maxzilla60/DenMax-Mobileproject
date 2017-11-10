package be.pxl.denmax.poopchasers.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import be.pxl.denmax.poopchasers.R;

/**
 * Created by dennis on 09.11.17.
 */

public class PreferenceStorage {

    private static final String USERNAME = "username";

    public static void setUserName(Context context, String username){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.user_preference_file), Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USERNAME, username);
        editor.commit();
    }

    public static String getUsername(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.user_preference_file), Context.MODE_PRIVATE
        );
        String username = sharedPref.getString(USERNAME, null);
        return username;
    }

}
