package mz.co.vida.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyUtils {

    public static final String SP_NAME = "maisVidaSP";
    public static final String SP_IS_DOADOR = "isDoador";
    public static final String REQUISITANTE = "Requisitante";
    public static final String DOADOR = "Doador";
    public static final String SIM = "Sim";
    public static final String NAO = "Não";
    public static final String OK = "OK";

    /**
     * For selecting/changing fragments
     */
    public static void changeFragment(int frameLayoutId, Fragment fragmentName, FragmentManager fragmentManager) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frameLayoutId, fragmentName);
        fragmentTransaction.commit();
    }


    public static void alertaNegativa(Context context, String mensagem) {
        SweetAlertDialog aDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        aDialog.setContentText(mensagem);
        aDialog.show();
    }

    public static void alertaNegativa(Context context, String titulo, String mensagem) {
        SweetAlertDialog aDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        aDialog.setTitle(titulo);
        aDialog.setContentText(mensagem);
        aDialog.show();
    }


    public static void alertaPosetiva(Context context, String titulo) {

        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(titulo)
                .show();
    }


    public static void alertaPosetiva(Context context, String titulo, String mensagem) {

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
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            return ni != null && ni.isConnected();
        }
        return false;
    }


    public static Boolean  verificarDatas (String data){

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("Mozambique"));
        Date date = null;
        try {
            date = formatter.parse(data);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Date today = Calendar.getInstance().getTime();
        assert date != null;
        return date.before(today);
    }


    public static boolean isValidEmailAddressRegex(String email) {
        boolean isEmailIdValid = false;
        if (email != null && email.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                isEmailIdValid = true;
            }
        }
        return isEmailIdValid;
    }




}