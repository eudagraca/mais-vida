package mz.co.vida.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;
import lib.kingja.switchbutton.SwitchMultiButton;
import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.utils.MyUtils;
import mz.co.vida.R;
import mz.co.vida.entities.Usuario;

import static mz.co.vida.utils.MyUtils.SP_IS_DOADOR;


public class PerfilFragment extends Fragment {

    public static final int DOADOR_TAB = 0;
    public static final int REQUISITANTE_TAB = 1;

    private TextView tv_nome, tv_sexo, tv_contacto, tv_sangue, tv_provincia, tv_unidadeSanitaria;
    private CircleImageView civ_foto;
    private SwitchMultiButton smb_estado;

    FirebaseAuth mAuth;
    DatabaseReference profileUser;
    String usuario_actual;


    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        mAuth = ConfiguracaoFirebase.getFirebaseAuth();
        usuario_actual = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        profileUser = ConfiguracaoFirebase.getFirebase().child("Usuario").child(usuario_actual);

        // Init Components
        tv_nome             = view.findViewById(R.id.tv_nome);
        tv_contacto         = view.findViewById(R.id.tv_contacto);
        tv_sexo             = view.findViewById(R.id.tv_Sexo);
        tv_provincia        = view.findViewById(R.id.tv_provincia);
        tv_unidadeSanitaria = view.findViewById(R.id.tv_unidade_sanitaria);
        tv_sangue           = view.findViewById(R.id.tv_tiposanguineo);

        civ_foto            = view.findViewById(R.id.iv_foto);
        smb_estado          = view.findViewById(R.id.smb_estado);

        profileUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Usuario usuario = new Usuario();

                    if (dataSnapshot.child("foto").exists()){
                        String foto = dataSnapshot.child("foto").getValue(String.class);
                        usuario.setFoto(foto);
                        Picasso.get().load(foto).placeholder(R.drawable.ic_user).into(civ_foto);
                    }

                    usuario.setNome(dataSnapshot.child("nome").getValue(String.class));
                    usuario.setProvincia(dataSnapshot.child("provincia").getValue(String.class));
                    usuario.setUnidadeProxima(dataSnapshot.child("unidadeProxima").getValue(String.class));
                    usuario.setTipo_sangue(dataSnapshot.child("tipo_sangue").getValue(String.class));
                    usuario.setSexo(dataSnapshot.child("sexo").getValue(String.class));
                    usuario.setDisponibilidade(dataSnapshot.child("disponibilidade").getValue(String.class));
                    usuario.setEstado(dataSnapshot.child("estado").getValue(String.class));
                    usuario.setContacto(dataSnapshot.child("contacto").getValue(String.class));

                    if (usuario.getEstado().equals("Doador")) {
                        smb_estado.setSelectedTab(DOADOR_TAB);

                    }else if (usuario.getEstado().equals("Requisitante")) {
                        smb_estado.setSelectedTab(REQUISITANTE_TAB);

                    }

                    tv_nome.setText(usuario.getNome());
                    tv_contacto.setText(usuario.getContacto());
                    tv_sexo.setText(usuario.getSexo());
                    tv_provincia.setText(usuario.getProvincia());
                    tv_unidadeSanitaria.setText(usuario.getUnidadeProxima());
                    tv_sangue.setText(usuario.getTipo_sangue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // When user changes it's estado
        smb_estado.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int tabPosition, String estado) {
                final boolean isDoador = tabPosition==DOADOR_TAB;
                profileUser.child("estado").setValue(estado).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyUtils.SP_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(SP_IS_DOADOR, isDoador);
                        editor.apply();
                    }
                });

            }
        });


        return view;


    }

}