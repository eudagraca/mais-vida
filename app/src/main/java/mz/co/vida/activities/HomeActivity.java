package mz.co.vida.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import mz.co.vida.DAO.ConfiguracaoFirebase;
import mz.co.vida.R;
import mz.co.vida.fragments.AnuncioFragment;
import mz.co.vida.fragments.HomeFragment;
import mz.co.vida.fragments.PerfilFragment;
import mz.co.vida.fragments.MoreInfoFragment;
import mz.co.vida.helpers.BottomNavigationViewHelper;
import mz.co.vida.utils.MyUtils;
import static mz.co.vida.utils.MyUtils.SP_IS_DOADOR;

public class HomeActivity extends AppCompatActivity {

    // Utils
    private SharedPreferences sharedPreferences;
    private Menu menu;
    private boolean isDoador;
    // Components
    private BottomNavigationView main_nav;
    private int fl_framelayout;
    private FragmentManager fragmentManager;
    private MoreInfoFragment sobre_fragment;
    private PerfilFragment perfil_fragment;
    private AnuncioFragment anuncioFragment;
    private HomeFragment homeFragment;


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
        sharedPreferences = getSharedPreferences(MyUtils.SP_NAME, MODE_PRIVATE);
        fl_framelayout = R.id.fl_framelayout;
        isDoador = sharedPreferences.getBoolean(SP_IS_DOADOR, false);

        // Init Components
        homeFragment = new HomeFragment();
        perfil_fragment  = new PerfilFragment();
        sobre_fragment = new MoreInfoFragment();
        anuncioFragment = new AnuncioFragment();
        main_nav = (BottomNavigationView) findViewById(R.id.nav_main);
        BottomNavigationViewHelper.disableShiftMode(main_nav);

        menu = main_nav.getMenu();
            if (!isDoador) {
                menu.removeItem(R.id.navigation_anunciar);
            }

            // auto choose the Home Screen
        MyUtils.changeFragment(fl_framelayout, homeFragment, fragmentManager);

        main_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())  {
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
                }

                return false;
            }
        });


        }else {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

    }
}
