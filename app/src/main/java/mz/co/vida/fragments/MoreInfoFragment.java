package mz.co.vida.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mz.co.vida.R;
import mz.co.vida.activities.AboutAppActivity;
import mz.co.vida.activities.ContactUsActivity;

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
        Button bt_contact   = view.findViewById(R.id.bt_contact);
        Button bt_about_app = view.findViewById(R.id.bt_about);

        bt_about_app.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AboutAppActivity.class);
            startActivity(intent);
        });

        bt_contact.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ContactUsActivity.class);
            startActivity(intent);
        });
        return view;
    }
}