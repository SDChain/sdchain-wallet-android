package com.io.sdchain.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *@author rulang1
 */
public class SaveObjectTools {

    private Context context;

    public SaveObjectTools(Context context) {
        this.context = context;
    }

    /**
     *
     * @param object Must be serialized data
     * @param mName Save the file name, it is best not to set the suffix name
     */
    public void saveData(Serializable object, String mName) {
        try {
            // logged data
            File file = new File(context.getFilesDir() + File.separator + "data" + File.separator
                    + mName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            ObjectOutputStream object_outStream = null;
            object_outStream = new ObjectOutputStream(new FileOutputStream(file));
            object_outStream.writeObject(object);
            object_outStream.close();
            LogInfo.e("- save---Save success information-----　");
        } catch (Exception e) {
            LogInfo.e("- save---exception message-----:　" + e.getMessage());
        }

    }

    /**
     *
     * @param mName Get the corresponding object by name
     * @return Returns the object currently accessed and then strongly rotates
     */
    public Object getObjectData(String mName) {
        try {
            // logged data
            File file = new File(context.getFilesDir() + File.separator + "data" + File.separator
                    + mName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) { // Who the file is is not there
                return null;
            }
            // Getting data out
            FileInputStream fileInputStream = new FileInputStream(file.toString());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object dateBean = objectInputStream.readObject();
            objectInputStream.close();
            LogInfo.e("　"+dateBean);
            return dateBean;
        } catch (Exception e) {
            // Maybe the current file has been deleted, so no corresponding data can be found, haha
            LogInfo.e("--There is no exception information-----:　" + e.getLocalizedMessage());
        }
        return null;
    }
}


