package snap.hackforresilience.utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class Preferences {
    public static void setPrefs(String key, String value, Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(Consts.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
//    public static void setArrayPrefs(String key, ArrayList<String> arr, Context context){
//        SharedPreferences sharedpreferences = context.getSharedPreferences(Consts.SP_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        Set<String> set = new HashSet<String>();
//        set.addAll(arr);
//        editor.putStringSet(key, set);
//        editor.apply();
//    }

    public static String getPrefs(String key, Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(Consts.SP_NAME, Context.MODE_PRIVATE);
        return sharedpreferences.getString(key, "notfound");
    }
//
//    public static ArrayList<String> getArrayPrefs(String key, Context context){
//        Set<String> pre = new HashSet<String>();
//        SharedPreferences sharedpreferences = context.getSharedPreferences(Consts.SP_NAME, Context.MODE_PRIVATE);
//        Set<String> set = sharedpreferences.getStringSet(key, pre);
//        ArrayList<String> sample=new ArrayList<String>(set);
//        return sample;
//    }

    public static void setArrayPrefs(String arrayName, ArrayList<String> array, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName +"_size", array.size());
        for(int i=0;i<array.size();i++)
            editor.putString(arrayName + "_" + i, array.get(i));
        editor.apply();
    }
    public static ArrayList<String> getArrayPrefs(String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        int size = prefs.getInt(arrayName + "_size", 0);
        ArrayList<String> array = new ArrayList<>(size);
        for(int i=0;i<size;i++)
            array.add(prefs.getString(arrayName + "_" + i, null));
        return array;
    }
}
