package mz.co.vida.DAO;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mz.co.vida.activities.HomeActivity;
import mz.co.vida.entities.Endereco;
import mz.co.vida.entities.LocaisDoacao;
import mz.co.vida.utils.MyUtils;

import static cn.pedant.SweetAlert.SweetAlertDialog.PROGRESS_TYPE;

public class LogicaLocaisDoacao {
    private SweetAlertDialog dialog;
    private LocaisDoacao locaisDoacao;
    private Endereco endereco;
    private Context context;

    //Database
    private DatabaseReference database = ConfiguracaoFirebase.getFirebase();

    public LogicaLocaisDoacao(Context context, LocaisDoacao locaisDoacao, Endereco endereco) {
        this.context = context;
        this.endereco = endereco;
        this.locaisDoacao = locaisDoacao;
        dialog = new SweetAlertDialog(context, PROGRESS_TYPE);
    }

    public void gravar() {
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        database.child("Colecta_Sanguinea").push().setValue(toMap()).addOnSuccessListener(aVoid -> {
            dialog.dismiss();
            MyUtils.alertaPosetiva(context, "Local de doação registado");
            home();
        }).addOnFailureListener(e -> MyUtils.alertaNegativa(context, "Erro ao registar o local"));
    }

    @Exclude
    private Map<String, Object> toMap() {
        HashMap<String, Object> hashMapUser = new HashMap<>();
        hashMapUser.put("nome", locaisDoacao.getNome());
        hashMapUser.put("telefone", locaisDoacao.getTelefone());
        hashMapUser.put("data_coleta", locaisDoacao.getDataColeta());
        hashMapUser.put("horas_coleta", locaisDoacao.getHoras());
        hashMapUser.put("endereco_nome", endereco.getNome());
        hashMapUser.put("endereco_local", endereco.getLocal());
        hashMapUser.put("endereco_provincia", endereco.getProvincia());
        hashMapUser.put("horas_fim", locaisDoacao.getHorasFim());
        hashMapUser.put("data_fim", locaisDoacao.getDataFim());
        return hashMapUser;
    }

    private void home() {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

}
