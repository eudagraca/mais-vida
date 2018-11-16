package mz.co.vida.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.zcw.togglebutton.ToggleButton;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;
import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.MyUtils;
import mz.co.vida.R;
import mz.co.vida.entidades.Usuario;


public class PerfilFragment extends Fragment {

    private TextView tNome, tSexo, tContacto,
            tSangue, tProvincia, tUnidadeSanitaria, tEstado;
    private ToggleButton tDisponibilidade;
    private CircleImageView imFoto;
    private FrameLayout relativeLayout;

    FirebaseAuth mAuth;
    DatabaseReference profileUser;
    String USUARIOACTUAL;
    public PerfilFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        mAuth = ConfiguracaoFirebase.getFirebaseAuth();
        USUARIOACTUAL = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        profileUser = ConfiguracaoFirebase.getFirebase().child("Usuario").child(USUARIOACTUAL);

        tNome              = view.findViewById(R.id.tv_nome);
        tContacto          = view.findViewById(R.id.tv_contacto);
        tSexo              = view.findViewById(R.id.tv_Sexo);
        tProvincia         = view.findViewById(R.id.tv_provincia);
        tUnidadeSanitaria  = view.findViewById(R.id.tv_unidade_sanitaria);
        tEstado            = view.findViewById(R.id.tv_estado);
        tDisponibilidade   = view.findViewById(R.id.tv_disponibilidade);
        tSangue            = view.findViewById(R.id.tv_tiposanguineo);
        imFoto             = view.findViewById(R.id.iv_foto);
        relativeLayout     = view.findViewById(R.id.rl);

        profileUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Usuario usuario = new Usuario();
                    usuario.setNome(dataSnapshot.child("nome").getValue(String.class));

                    usuario.setEmail(dataSnapshot.child("email").getValue(String.class));

                    if (dataSnapshot.child("foto").exists()){
                        String foto = dataSnapshot.child("foto").getValue(String.class);
                        usuario.setFoto(foto);
                        Picasso.get().load(foto).placeholder(R.drawable.ic_user).into(imFoto);
                    }

                    usuario.setProvincia(dataSnapshot.child("provincia").getValue(String.class));
                    usuario.setUnidadeProxima(dataSnapshot.child("unidadeProxima").getValue(String.class));
                    usuario.setTipoSanguineo(dataSnapshot.child("tipoSanguineo").getValue(String.class));
                    usuario.setSexo(dataSnapshot.child("sexo").getValue(String.class));
                    usuario.setDisponibilidade(dataSnapshot.child("disponibilidade").getValue(String.class));
                    usuario.setEstado(dataSnapshot.child("estado").getValue(String.class));
                    usuario.setTelefone(dataSnapshot.child("telefone").getValue(String.class));


                    tNome.setText(usuario.getNome());
                    tContacto.setText(usuario.getTelefone());
                    if (usuario.getEstado().equals("Doador")) {
                        tDisponibilidade.setVisibility(View.VISIBLE);
                        if (usuario.getDisponibilidade().equals("Sim")){
                            tDisponibilidade.setToggleOn();
                            relativeLayout.setBackgroundResource(R.color.md_red_50);
                        }else if (usuario.getDisponibilidade().equals("Não")){
                            tDisponibilidade.setToggleOff();
                        }
                    }else if (usuario.getEstado().equals("Requisitante")) {
                        tDisponibilidade.setVisibility(View.INVISIBLE);

                    }
                    tEstado.setText(usuario.getEstado());
                    tSexo.setText(usuario.getSexo());
                    tProvincia.setText(usuario.getProvincia());
                    tUnidadeSanitaria.setText(usuario.getUnidadeProxima());
                    tSangue.setText(usuario.getTipoSanguineo());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Saving Status on SPref
        tDisponibilidade.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean isOn) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyUtils.SP_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isRequisitante", !isOn);
                editor.apply();

                profileUser.child("estado").setValue(isOn ? "Doador" : "Requisitante");
            }
        });



        return view;


    }

}