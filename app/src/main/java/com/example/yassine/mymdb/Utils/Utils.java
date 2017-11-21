package com.example.yassine.mymdb.Utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yassine on 21/11/2017.
 */

public class Utils {

    private static final String fileName = "favories";
    public static List<Integer> list = new ArrayList<>();

    public static void saveList() {
        try {
            File file = Environment.getExternalStorageDirectory();
            File filename = new File(file, fileName);
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(list);
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static void readList()
    {


        try {
            File file = Environment.getExternalStorageDirectory();
            File filename = new File(file, fileName);

            if(!filename.exists()) {
                return;
            }

            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fis);
            list = (List<Integer>) in.readObject();
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }


}
