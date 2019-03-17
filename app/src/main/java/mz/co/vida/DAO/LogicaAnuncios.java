package mz.co.vida.DAO;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mz.co.vida.entities.Anuncio;
import mz.co.vida.utils.MyUtils;

public class LogicaAnuncios {

    private Anuncio anuncio;
    private DatabaseReference databaseReference;
    private SweetAlertDialog dialog;

    public LogicaAnuncios(Anuncio anuncio, Context context) {
        this.anuncio = anuncio;
        databaseReference = ConfiguracaoFirebase.getFirebase().child("anuncios");
        dialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
    }

    public void gravarAnuncio(Context context) {
        dialog.setTitle("Aguarde");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        databaseReference.child(String.valueOf(anuncio.getId())).setValue(toMapAnunciar()).addOnSuccessListener(aVoid -> {
            dialog.dismiss();
            MyUtils.alertaPosetiva(context, "Requisição anunciada");
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            MyUtils.alertaNegativa(context, "Não foi possível anunciar");
        });
    }


    @Exclude
    private Map<String, Object> toMapAnunciar() {
        HashMap<String, Object> hashMapUser = new HashMap<>();
        hashMapUser.put("nome", anuncio.getNome());
        hashMapUser.put("data", anuncio.getDataDoacao());
        hashMapUser.put("quantSanguinea", anuncio.getQuantSanguinea());
        hashMapUser.put("descricao", anuncio.getDescricao());
        hashMapUser.put("provincia", anuncio.getProvincia());
        hashMapUser.put("estado", anuncio.getEstado());
        hashMapUser.put("tipo_sangue", anuncio.getTipo_sangue());
        hashMapUser.put("contacto", anuncio.getContacto());
        hashMapUser.put("unidadeProxima", anuncio.getUnidadeProxima());

        return hashMapUser;
    }

    public void removerAnuncio(String uid, Context context) {
        dialog.setTitle("Aguarde");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        databaseReference.child(uid).removeValue().addOnSuccessListener(aVoid -> {
                    dialog.dismiss();
                    MyUtils.alertaPosetiva(context, "A sua requisição foi removida");
                }
        ).addOnFailureListener(e -> {
            dialog.dismiss();
            MyUtils.alertaNegativa(context, "Falha ao remover a requisição");
        });
    }


}
