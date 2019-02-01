package mz.co.vida.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.zcw.togglebutton.ToggleButton;
import java.util.Objects;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import lib.kingja.switchbutton.SwitchMultiButton;
import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.R;
import mz.co.vida.activities.LoginActivity;
import mz.co.vida.activities.Profile_UpdadeActivity;
import mz.co.vida.entities.Usuario;
import mz.co.vida.utils.MyUtils;
import mz.co.vida.utils.VerificaConexao;

import static mz.co.vida.utils.MyUtils.SP_IS_DOADOR;


public class PerfilFragment extends Fragment {

    private static final int DOADOR_TAB = 0;
    private static final int REQUISITANTE_TAB = 1;
    private TextView tv_nome, tv_sexo, tv_contacto, tv_sangue, tv_provincia, tv_unidadeSanitaria, tv_disp;
    private ToggleButton tb_disponibilidade;
    private View view_disp;
    private ImageView iv_on_of;
    private CircleImageView civ_foto;
    private SwitchMultiButton smb_estado;
    private LinearLayout linear_empty, ll_profile;
    //Firebase
    private DatabaseReference profile_user;
    //Utils
    private String usuario_actual;
    String foto=null;
    private Context context;
    Usuario usuario = new Usuario();

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        //Setup utils
        context = getContext();

        //Setup firebase
        FirebaseAuth mAuth = ConfiguracaoFirebase.getFirebaseAuth();
        usuario_actual = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        profile_user = ConfiguracaoFirebase.getFirebase().child("Usuario").child(usuario_actual);

        // Init Components
        tv_nome             = view.findViewById(R.id.tv_nome);
        tv_contacto         = view.findViewById(R.id.tv_contacto);
        tv_sexo             = view.findViewById(R.id.tv_Sexo);
        tv_provincia        = view.findViewById(R.id.tv_provincia);
        tv_unidadeSanitaria = view.findViewById(R.id.tv_unidade_sanitaria);
        tv_sangue           = view.findViewById(R.id.tv_tiposanguineo);
        linear_empty        = view.findViewById(R.id.ll_empty);
        ll_profile          = view.findViewById(R.id.ll_prof);
        civ_foto            = view.findViewById(R.id.iv_foto);
        smb_estado          = view.findViewById(R.id.smb_estado);
        tb_disponibilidade  = view.findViewById(R.id.tb_disponibilidade);
        tv_disp             = view.findViewById(R.id.tv_disp);
        view_disp           = view.findViewById(R.id.view_disp);
        iv_on_of            = view.findViewById(R.id.iv_on_of);
        Button btn_edit     = view.findViewById(R.id.bt_edit);

        final SharedPreferences sharedPreferences   = context.getSharedPreferences(MyUtils.SP_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        if (VerificaConexao.isConnected(context)) {

            profile_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.equals(null)) {
                        ll_profile.setVisibility(View.GONE);
                        linear_empty.setVisibility(View.VISIBLE);
                    }else {

                        if (dataSnapshot.exists()) {
                            ll_profile.setVisibility(View.VISIBLE);
                            linear_empty.setVisibility(View.GONE);
                            if (dataSnapshot.child("foto").exists()) {
                                foto = dataSnapshot.child("foto").getValue(String.class);
                                usuario.setFoto(foto);
                                Glide.with(PerfilFragment.this).load(foto).into(civ_foto);
                            }
                            usuario.setNome(dataSnapshot.child("nome").getValue(String.class));
                            usuario.setProvincia(dataSnapshot.child("provincia").getValue(String.class));
                            usuario.setUnidadeProxima(dataSnapshot.child("unidadeProxima").getValue(String.class));
                            usuario.setTipo_sangue(dataSnapshot.child("tipo_sangue").getValue(String.class));
                            usuario.setSexo(dataSnapshot.child("sexo").getValue(String.class));
                            usuario.setDisponibilidade(dataSnapshot.child("disponibilidade").getValue(String.class));
                            usuario.setEstado(dataSnapshot.child("estado").getValue(String.class));
                            usuario.setContacto(dataSnapshot.child("contacto").getValue(String.class));

                            if (usuario.getEstado().equals(MyUtils.DOADOR)) {
                                smb_estado.setSelectedTab(DOADOR_TAB);
                                tb_disponibilidade.setVisibility(View.VISIBLE);
                                tv_disp.setVisibility(View.VISIBLE);
                                view_disp.setVisibility(View.VISIBLE);
                                if (usuario.getDisponibilidade().equals(MyUtils.SIM)) {
                                    tb_disponibilidade.toggleOn();
                                    iv_on_of.setVisibility(View.VISIBLE);
                                    iv_on_of.setImageResource(R.drawable.ic_thumbs_up_hand_symbol);
                                } else {
                                    tb_disponibilidade.toggleOff();
                                    iv_on_of.setVisibility(View.VISIBLE);
                                    iv_on_of.setImageResource(R.drawable.ic_thumbs_down_silhouette);
                                }
                            } else if (usuario.getEstado().equals(MyUtils.REQUISITANTE)) {
                                smb_estado.setSelectedTab(REQUISITANTE_TAB);

                                //hide toogle
                                tb_disponibilidade.setVisibility(View.GONE);
                                tv_disp.setVisibility(View.GONE);
                                view_disp.setVisibility(View.GONE);
                            }
                            tv_nome.setText(usuario.getNome());
                            tv_contacto.setText(usuario.getContacto());
                            tv_sexo.setText(usuario.getSexo());
                            tv_provincia.setText(usuario.getProvincia());
                            tv_unidadeSanitaria.setText(usuario.getUnidadeProxima());
                            tv_sangue.setText(usuario.getTipo_sangue());

                        } else {
                            Intent intent = new Intent(context, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });


            btn_edit.setOnClickListener(v -> {
                Intent intent = new Intent(context, Profile_UpdadeActivity.class);


                Log.i("FOTO ", foto);

                intent.putExtra("contacto", usuario.getContacto());
                intent.putExtra("key", mAuth.getCurrentUser().getUid());
                intent.putExtra("sexo", usuario.getSexo());
                intent.putExtra("estado", usuario.getEstado());
                intent.putExtra("unidade_proxima", usuario.getUnidadeProxima());
                intent.putExtra("provincia", usuario.getProvincia());
                if (foto!=null){
                    intent.putExtra("foto", foto);
                }

                if (usuario.getDisponibilidade()!=null){
                    intent.putExtra("disponibilidade", usuario.getDisponibilidade());
                }

                startActivity(intent);
            });
        }else {
            linear_empty.setVisibility(View.VISIBLE);
            ll_profile.setVisibility(View.GONE);
        }


        if (tb_disponibilidade.isSelected()) {
            iv_on_of.setImageResource(R.drawable.ic_thumbs_up_hand_symbol);
        }else {
            iv_on_of.setImageResource(R.drawable.ic_thumbs_down_silhouette);
        }


        //final SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        //dialog.dismiss();
        //if (tb_disponibilidade.isClickable()){
        //tb_disponibilidade.setOnToggleChanged(on -> {
        //            dialog.setTitleText("Alerta!");
        //            dialog.setContentText("Deseja alterar a sua disponibilidade? ");
        //            dialog.setConfirmText("Sim");
        //            dialog.setConfirmClickListener(sDialog -> {
        //                                        sDialog.dismiss();
        //            })
        //            .setCancelButton("Não", Dialog::dismiss)
        //            .show();
        //});
        //dialog.dismiss();
        //}

        // When user changes it's
        //final SweetAlertDialog alertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        //alertDialog.dismiss();
        //    smb_estado.setOnSwitchListener((tabPosition, estado) -> {
        //        final boolean isDoador = tabPosition == DOADOR_TAB;
        //            alertDialog.setTitleText("Alerta!");
        //            alertDialog.setContentText("Tem certeza que deseja alterar o tipo de usuário?");
        //            alertDialog.setConfirmText("Sim");
        //            alertDialog.setConfirmClickListener(sDialog -> {
        //                profile_user.child("disponibilidade").setValue(MyUtils.NAO);
        //                profile_user.child("estado").setValue(estado).addOnSuccessListener(aVoid -> {
        //                    editor.putBoolean(SP_IS_DOADOR, isDoador);
        //                    editor.apply();
        //                    if (isDoador) {
        //                        apagar();
        //                        MyUtils.deleteCache(context);
        //                    }
        //                    Intent intent = new Intent(getActivity(), LoginActivity.class);
        //                    startActivity(intent);
        //                });
        //                sDialog.dismiss();
        //            })
        //            .setCancelButton("Não", Dialog::dismiss)
        //            .show();
        //    });
        //    alertDialog.dismiss();
        return view;
    }

}