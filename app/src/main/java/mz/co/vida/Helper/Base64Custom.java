package mz.co.vida.Helper;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

class Base64Custom {

    public static String codificarBase64(String strToEncode){
        byte[] data = null;
        try {
            data = strToEncode.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(data, Base64.NO_WRAP);

          }
}