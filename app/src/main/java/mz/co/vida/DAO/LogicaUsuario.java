package mz.co.vida.DAO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mz.co.vida.activities.HomeActivity;
import mz.co.vida.activities.LoginActivity;
import mz.co.vida.entities.Usuario;
import mz.co.vida.helpers.Preferencias;
import mz.co.vida.utils.MyUtils;

public class LogicaUsuario {

    private SweetAlertDialog dialog;
    private Usuario usuario;
    private DatabaseReference databaseReference;
    private Context context;
    private StorageReference storageReference;
    private StorageReference image_reference;
    private String chave;

    private Uri image_uri;
    private String img;

    public LogicaUsuario(Usuario usuario, Context context) {
        this.usuario = usuario;
        this.context = context;
        databaseReference = ConfiguracaoFirebase.getFirebase().child("Usuario");

        //init firebase
        storageReference = FirebaseStorage.getInstance().getReference();
        dialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
    }

    /**
     * Save User
     */
    private void saveUser() {
        databaseReference.child(String.valueOf(usuario.getUser_id())).setValue(toMap()).addOnSuccessListener(aVoid -> {
            uploadProfileImage();
            dialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
            dialog.setTitleText("Consulte a sua caixa de e-mail para validar o registo");
            dialog.setConfirmText(MyUtils.OK);
            dialog.setConfirmClickListener(sDialog -> {
                login();
                sDialog.dismiss();
            }).show();
        }).addOnFailureListener(e -> MyUtils.alertaNegativa(context, "Não foi possivel registar este usuário"));
    }

    @Exclude
    private Map<String, Object> toMap() {
        HashMap<String, Object> hashMapUser = new HashMap<>();
        hashMapUser.put("user_id", usuario.getUser_id());
        hashMapUser.put("nome", usuario.getNome());
        hashMapUser.put("email", usuario.getEmail());
        hashMapUser.put("foto", usuario.getFoto());
        hashMapUser.put("contacto", usuario.getContacto());
        hashMapUser.put("sexo", usuario.getSexo());
        hashMapUser.put("provincia", usuario.getProvincia());
        hashMapUser.put("unidadeProxima", usuario.getUnidadeProxima());
        hashMapUser.put("tipo_sangue", usuario.getTipo_sangue());
        hashMapUser.put("disponibilidade", usuario.getDisponibilidade());
        hashMapUser.put("estado", usuario.getEstado());
        return hashMapUser;
    }

    private void uploadProfileImage() {
        if (usuario.getFoto() != null) {
            image_reference = storageReference.child("imagens/").child("perfil" + UUID.randomUUID().toString());
            UploadTask uploadTask = image_reference.putFile(Uri.parse(usuario.getFoto()));
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return image_reference.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    image_uri = task.getResult();
                    assert image_uri != null;
                    img = image_uri.toString();
                    databaseReference.child(usuario.getUser_id()).child("foto").setValue(img);
                }
            });
        }
    }


    public void create_user(Context context) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#ff1c1c"));
        pDialog.setTitleText("Registando...");
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();

        FirebaseAuth mAuth = ConfiguracaoFirebase.getFirebaseAuth();
        mAuth.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener((Activity) context, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    usuario.setUidUser(user.getUid());
                    user.sendEmailVerification();
                    saveUser();
                    pDialog.dismiss();
                    Preferencias preferencias = new Preferencias(context);
                    preferencias.gravarUsuario(user.getUid(), usuario.getNome());
                }
            } else {
                pDialog.dismiss();
                String erro;
                try {
                    throw Objects.requireNonNull(task.getException());

                } catch (FirebaseAuthUserCollisionException ex) {
                    erro = "Este e-mail já possui um registo";

                } catch (Exception ex) {
                    erro = "Erro desconhecido";
                    ex.printStackTrace();
                }
                MyUtils.alertaNegativa(context, erro);
            }
        });
    }

    /**
     * Update user
     *
     * @param key receive an user id.
     */
    public void updateUser(String key, String foto) {
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        chave = key;
        databaseReference.child(key).updateChildren(toMapUpdate()).addOnSuccessListener(aVoid -> {

            uploadProfileImageAndDelete(foto);
            dialog.dismiss();
            dialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
            dialog.getProgressHelper().setBarColor(Color.parseColor("#ff1c1c"));
            dialog.setTitleText("Os dados foram actualizados");
            dialog.setConfirmText(MyUtils.OK);
            dialog.setConfirmClickListener(sDialog -> {
                home();
                sDialog.dismiss();
            }).show();
        }).addOnFailureListener(e -> MyUtils.alertaNegativa(context, "Erro", "Não foi possível actualizar os seus dados"));
    }


    @Exclude
    private Map<String, Object> toMapUpdate() {
        HashMap<String, Object> hashMapUser = new HashMap<>();
        hashMapUser.put("contacto", usuario.getContacto());
        hashMapUser.put("sexo", usuario.getSexo());
        hashMapUser.put("provincia", usuario.getProvincia());
        hashMapUser.put("unidadeProxima", usuario.getUnidadeProxima());
        hashMapUser.put("disponibilidade", usuario.getDisponibilidade());
        hashMapUser.put("estado", usuario.getEstado());
        return hashMapUser;
    }

    private void uploadProfileImageAndDelete(String foto) {
        if (usuario.getFoto() != null) {
            final StorageReference image_reference = storageReference.child("imagens/").child("perfil" + UUID.randomUUID().toString());
            UploadTask uploadTask = image_reference.putFile(Uri.parse(usuario.getFoto()));
            if (foto != null) {
                if (!foto.equals(usuario.getFoto())) {
                    uploadTask.continueWithTask(task -> {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }
                        return image_reference.getDownloadUrl();
                    }).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            image_uri = task.getResult();
                            assert image_uri != null;
                            img = image_uri.toString();
                            sucess();
                            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(foto);
                            storageRef.delete().addOnSuccessListener(aVoid -> {
                            }).addOnFailureListener(e -> Toast.makeText(context, "Foto de perfil não foi actualizada. Tente novamente", Toast.LENGTH_SHORT).show());
                        }
                    });
                }
            } else {
                uploadTask.continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return image_reference.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        image_uri = task.getResult();
                        assert image_uri != null;
                        img = image_uri.toString();
                        sucess();
                    }
                });
            }
        }
    }

    private void sucess() {
        databaseReference.child(chave).child("foto").setValue(img).addOnSuccessListener(aVoid -> {
        });
    }

    private void login() {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    private void home() {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

}