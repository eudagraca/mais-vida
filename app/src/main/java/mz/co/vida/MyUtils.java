package mz.co.vida;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MyUtils {


    /**
     * For selecting/changing fragments
     */
    public static void changeFragment(int frameLayoutId, Fragment fragmentName, FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayoutId, fragmentName);
        fragmentTransaction.commit();
    }

}
