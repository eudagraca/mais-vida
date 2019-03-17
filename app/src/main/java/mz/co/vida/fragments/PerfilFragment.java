package mz.co.vida.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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


public class PerfilFragment extends Fragment {

    private static final int DOADOR_TAB = 0;
    private static final int REQUISITANTE_TAB = 1;
    private TextView tv_nome;
    private TextView tv_sexo;
    private TextView tv_contacto;
    private TextView tv_sangue;
    private TextView tv_provincia;
    private TextView tv_unidadeSanitaria;
    private TextView tv_disp;
    private LabeledSwitch lb_disponibilidade;
    private View view_disp;
    private CircleImageView civ_foto;
    private SwitchMultiButton smb_estado;
    private LinearLayout linear_empty, ll_profile;
    private CardView cardView;


    private String foto = null;
    private Context context;
    final Usuario usuario = new Usuario();
    private SweetAlertDialog progressDialog;

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
        FirebaseAuth mAuth              = ConfiguracaoFirebase.getFirebaseAuth();
        String usuario_actual           = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        DatabaseReference profile_user  = ConfiguracaoFirebase.getFirebase().child("Usuario").child(usuario_actual);

        // Init Components
        tv_nome                     = view.findViewById(R.id.tv_nome);
        tv_contacto                 = view.findViewById(R.id.tv_contacto);
        tv_sexo                     = view.findViewById(R.id.tv_Sexo);
        tv_provincia                = view.findViewById(R.id.tv_provincia);
        tv_unidadeSanitaria         = view.findViewById(R.id.tv_unidade_sanitaria);
        tv_sangue                   = view.findViewById(R.id.tv_tiposanguineo);
        linear_empty                = view.findViewById(R.id.ll_empty);
        ll_profile                  = view.findViewById(R.id.ll_prof);
        civ_foto                    = view.findViewById(R.id.iv_foto);
        smb_estado                  = view.findViewById(R.id.smb_estado);
        lb_disponibilidade          = view.findViewById(R.id.lb_disponibilidade);
        tv_disp                     = view.findViewById(R.id.tv_disp);
        view_disp                   = view.findViewById(R.id.view_disp);
        TextView tv_emptyInfo       = view.findViewById(R.id.tv_emptyInfo);
        cardView                    = view.findViewById(R.id.cardview_id);
        ImageView image_perfil_wifi = view.findViewById(R.id.image_perfil_wifi);
        Button btn_edit             = view.findViewById(R.id.bt_edit);

        progressDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.show();

        if (MyUtils.isConnected(context)) {

            profile_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        ll_profile.setVisibility(View.VISIBLE);
                        linear_empty.setVisibility(View.GONE);
                        cardView.setVisibility(View.VISIBLE);
                        if (dataSnapshot.child("foto").exists()) {
                            foto = dataSnapshot.child("foto").getValue(String.class);
                            if (foto != null) {
                                usuario.setFoto(foto);
                                Glide.with(PerfilFragment.this).load(foto).into(civ_foto);
                            }
                        }
                        progressDialog.dismiss();
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
                            lb_disponibilidade.setVisibility(View.VISIBLE);
                            tv_disp.setVisibility(View.VISIBLE);
                            view_disp.setVisibility(View.VISIBLE);
                            if (usuario.getDisponibilidade().equals(MyUtils.SIM)) {
                                lb_disponibilidade.setOn(true);
                            } else {
                                lb_disponibilidade.setOn(false);
                            }
                        } else if (usuario.getEstado().equals(MyUtils.REQUISITANTE)) {
                            smb_estado.setSelectedTab(REQUISITANTE_TAB);
                            //hide toogle
                            lb_disponibilidade.setVisibility(View.GONE);
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                }
            });

            btn_edit.setOnClickListener(v -> {
                Intent intent = new Intent(context, Profile_UpdadeActivity.class);
                intent.putExtra("contacto", usuario.getContacto());
                intent.putExtra("key", mAuth.getCurrentUser().getUid());
                intent.putExtra("sexo", usuario.getSexo());
                intent.putExtra("estado", usuario.getEstado());
                intent.putExtra("unidade_proxima", usuario.getUnidadeProxima());
                intent.putExtra("provincia", usuario.getProvincia());
                if (foto != null) {
                    intent.putExtra("foto", foto);
                }
                if (usuario.getDisponibilidade() != null) {
                    intent.putExtra("disponibilidade", usuario.getDisponibilidade());
                }
                startActivity(intent);
            });
        } else {

            linear_empty.setVisibility(View.VISIBLE);
            ll_profile.setVisibility(View.GONE);
            tv_emptyInfo.setText(R.string.connection_problem);
            image_perfil_wifi.setBackgroundResource(R.drawable.ic_wifi);
            cardView.setVisibility(View.GONE);
            progressDialog.dismiss();
        }
        return view;
    }

}