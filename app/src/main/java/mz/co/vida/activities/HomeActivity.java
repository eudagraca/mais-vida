package mz.co.vida.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.R;
import mz.co.vida.fragments.AnuncioFragment;
import mz.co.vida.fragments.HomeFragment;
import mz.co.vida.fragments.LocaisFragment;
import mz.co.vida.fragments.MoreInfoFragment;
import mz.co.vida.fragments.PerfilFragment;
import mz.co.vida.helpers.BottomNavigationViewHelper;
import mz.co.vida.utils.MyUtils;

import static mz.co.vida.utils.MyUtils.SP_IS_DOADOR;

public class HomeActivity extends AppCompatActivity {

    private int fl_framelayout;
    private FragmentManager fragmentManager;
    private MoreInfoFragment sobre_fragment;
    private PerfilFragment perfil_fragment;
    private AnuncioFragment anuncioFragment;
    private HomeFragment homeFragment;
    private LocaisFragment LocaisFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseAuth mAuth = ConfiguracaoFirebase.getFirebaseAuth();
        FirebaseUser user  = mAuth.getCurrentUser();

        assert user != null;
        if (user.isEmailVerified()){
            // Init Utils
        fragmentManager = getSupportFragmentManager();
            // Utils
            SharedPreferences sharedPreferences = getSharedPreferences(MyUtils.SP_NAME, MODE_PRIVATE);
        fl_framelayout = R.id.fl_framelayout;
            boolean isDoador = sharedPreferences.getBoolean(SP_IS_DOADOR, false);

        // Init Components
        homeFragment    = new HomeFragment();
        perfil_fragment = new PerfilFragment();
        sobre_fragment  = new MoreInfoFragment();
        anuncioFragment = new AnuncioFragment();
        LocaisFragment  = new LocaisFragment();
            // Components
            BottomNavigationView main_nav = (BottomNavigationView) findViewById(R.id.nav_main);
            BottomNavigationViewHelper.disableShiftMode(main_nav);

            Menu menu = main_nav.getMenu();
            if (isDoador) {
                menu.removeItem(R.id.navigation_anunciar);
            }

            // auto choose the Home Screen
            MyUtils.changeFragment(fl_framelayout, homeFragment, fragmentManager);

            main_nav.setOnNavigationItemSelectedListener(item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        MyUtils.changeFragment(fl_framelayout, homeFragment, fragmentManager);
                        return true;

                    case R.id.navigation_anunciar:
                        MyUtils.changeFragment(fl_framelayout, anuncioFragment, fragmentManager);
                        return true;

                    case R.id.navigation_perfil:
                        MyUtils.changeFragment(fl_framelayout, perfil_fragment, fragmentManager);
                        return true;

                    case R.id.navigation_sobre:
                        MyUtils.changeFragment(fl_framelayout, sobre_fragment, fragmentManager);
                        return true;
                    case R.id.navigation_map:
                        MyUtils.changeFragment(fl_framelayout, LocaisFragment, fragmentManager);
                        return true;
                }

                return false;
            });

        } else {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
    }

    int backButtonCount = 0;

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Clique denovo para sair.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    @Override
    protected void onResume() {
        backButtonCount = 0;
        super.onResume();
    }
}
