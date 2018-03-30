package com.debuggerx.pidilitesaleshandler.Working;

import android.content.Context;

import java.io.File;
import java.net.ContentHandler;

/**
 * Created by Kshitij on 5/21/2017.
 */

public class DestroyingCache {
    Context context;

    public DestroyingCache() {
        try {
            trimCache(); //if trimCache is static
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void trimCache() {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory())
            { deleteDir(dir);  }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success)
                    return false;
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }


}
