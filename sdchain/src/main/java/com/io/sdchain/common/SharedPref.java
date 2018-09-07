package com.io.sdchain.common;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * Created by Hello on 2015/8/18.
 */
public class SharedPref {

    private Context mContext;
    private SharedPreferences mySharedPreferences;

    public SharedPref(Context mContext) {
        this.mContext = mContext;
        //Constants.ANUO，save info named "info",mode:cant be read for other app
        mySharedPreferences = mContext.getSharedPreferences(Constants.ANUO, Context.MODE_PRIVATE);
    }

    /**
     * save info ,different type , different save
     *
     * @param key
     * @param object
     */
    public void saveData(String key, Object object) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else if (object instanceof Set<?>) {
            editor.putStringSet(key, (Set<String>) object);
        } else if (object instanceof String) {
            editor.putString(key, (String) object);
        }
        editor.commit();
    }

    /**
     * get info from key-value
     *
     * @param key
     * @return
     */
    public Object getData(String key) {
        Map map = mySharedPreferences.getAll();
        return map.get(key);
    }

}
