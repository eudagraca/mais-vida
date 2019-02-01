package mz.co.vida.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import java.util.Objects;
import cn.pedant.SweetAlert.SweetAlertDialog;
import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.R;
import mz.co.vida.activities.AboutAppActivity;
import mz.co.vida.activities.ContactUsActivity;
import mz.co.vida.activities.MainActivity;

public class MoreInfoFragment extends Fragment {

    public MoreInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_more_info, container, false);

        //Componets
        //Button bt_delete_account = view.findViewById(R.id.bt_apagar_conta);
        Button bt_contact= view.findViewById(R.id.bt_contact);
        Button bt_about_app = view.findViewById(R.id.bt_about);

        /*
        bt_delete_account.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               final FirebaseUser user = ConfiguracaoFirebase.getFirebaseAuth().getCurrentUser();
               final AuthCredential authCredential = EmailAuthProvider.getCredential("user@example.com", "password1234");

               if(user != null) {
                   Objects.requireNonNull(user).reauthenticate(authCredential)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {

                                   new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                           .setTitleText("Alerta!!")
                                           .setContentText("Apagar a conta?")
                                           .setConfirmText("Sim!")
                                           .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                               @Override
                                               public void onClick(SweetAlertDialog sDialog) {
                                                   user.delete()
                                                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                               @Override
                                                               public void onComplete(@NonNull Task<Void> task) {
                                                                   if (task.isSuccessful()) {


                                                                       String id = user.getUid();

                                                                       DatabaseReference mData = ConfiguracaoFirebase.getFirebase();
                                                                       mData.child("Usuario").child(id).removeValue();

                                                                       Intent intent = new Intent(getActivity(), MainActivity.class);
                                                                       getActivity().startActivity(intent);
                                                                       new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                                                                   .setTitleText("Conta apagada")
                                                                                   .show();

                                                                   }
                                                               }
                                                           });
                                                   sDialog.dismissWithAnimation();
                                               }
                                           })
                                           .setCancelButton("Não", new SweetAlertDialog.OnSweetClickListener() {
                                               @Override
                                               public void onClick(SweetAlertDialog sDialog) {
                                                   sDialog.dismissWithAnimation();
                                               }
                                           })
                                           .show();
                               }
                           });
               }
           }
       }); */

       bt_about_app.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getActivity(), AboutAppActivity.class);
               startActivity(intent);
           }
       });

        bt_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}