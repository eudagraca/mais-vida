package mz.co.vida.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import java.io.File;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyUtils {

    public static final String SP_NAME = "maisVidaSP";
    public static final String SP_IS_DOADOR = "isDoador";
    public static final String REQUISITANTE = "Requisitante";
    public static final String DOADOR ="Doador";
    public static final String SIM = "Sim";
    public static final String NAO = "Não";
    public static final String CONTADOR = "Contador";

    /**
     * For selecting/changing fragments
     */
    public static void changeFragment(int frameLayoutId, Fragment fragmentName, FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayoutId, fragmentName);
        fragmentTransaction.commit();
    }


    public  static  void alertaNegativa(Context context, String mensagem){
        SweetAlertDialog aDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        aDialog.setTitleText("Oops...");
        aDialog.setContentText(mensagem);
        aDialog.show();
    }


    public static void alertaPosetiva(Context context, String mensagem){

        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(mensagem)
                .show();
    }


    public static void alertaPosetiva(Context context, String titulo, String mensagem){

        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(titulo)
                .setContentText(mensagem)
                .show();
    }


    //Delete cache
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            Toast.makeText(context, (CharSequence) e, Toast.LENGTH_SHORT).show();
        }
    }
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }
        return Objects.requireNonNull(dir).delete();
    }
}
