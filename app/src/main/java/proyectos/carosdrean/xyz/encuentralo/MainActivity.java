package proyectos.carosdrean.xyz.encuentralo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import proyectos.carosdrean.xyz.encuentralo.Fragments.Categorias;
import proyectos.carosdrean.xyz.encuentralo.Fragments.ComoLlegar;
import proyectos.carosdrean.xyz.encuentralo.Fragments.Favoritos;
import proyectos.carosdrean.xyz.encuentralo.Fragments.Inicio;
import proyectos.carosdrean.xyz.encuentralo.Fragments.Ofertas;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private Fragment fragment = null;
    private boolean mapaVeri = false;

    private CircleImageView perfil;
    private TextView nombre;
    private TextView email;
    private ImageView fondoCabecera;

    private GoogleApiClient googleApiClient;

    private View vHome;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        vHome = navigationView.getHeaderView(0);

        if(navigationView != null){
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        }
        inicializarDatos();
        instanciaSignIn();
    }

    private void instanciaSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions
                .DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    setUserData(user);
                }else {
                    goLogInScreen();
                }

            }
        };
    }

    private void goLogInScreen(){
        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setUserData(FirebaseUser user) {
        nombre.setText(user.getDisplayName());
        email.setText(user.getEmail());
        Glide.with(this).load(R.drawable.registrar).into(fondoCabecera);
        Glide.with(this).load(user.getPhotoUrl()).into(perfil);
    }

    public void logOut() {
        firebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null){

            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_buscar) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        mapaVeri = false;

        int id = item.getItemId();

        boolean fragementManager = false;

        if (id == R.id.nav_inicio) {
            fragementManager = true;
            fragment = new Inicio();
            mapaVeri = true;
        } else if (id == R.id.nav_categorias) {
            fragementManager = true;
            fragment = new Categorias();
        } else if (id == R.id.nav_favorito) {
            fragementManager = true;
            fragment = new Favoritos();
        } else if (id == R.id.nav_ofertas) {
            fragementManager = true;
            fragment = new Ofertas();
        } else if (id == R.id.nav_ajustes) {

        }else if (id == R.id.nav_cerrar_sesion) {
            logOut();
        }

        if (fragementManager){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main, fragment)
                    .commit();

            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }

        DrawerLayout drawers = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawers.closeDrawer(GravityCompat.START);
        return true;
    }

    public void inicializarDatos(){
        perfil = (CircleImageView)vHome.findViewById(R.id.iv_perfilLogin);
        nombre = (TextView) vHome.findViewById(R.id.tvnombreLogin);
        email = (TextView) vHome.findViewById(R.id.tvCorreoLogin);
        fondoCabecera = (ImageView)vHome.findViewById(R.id.fondoNavHeaderCabecera);
    }

    public void inflarAgregarServicio(View v) {
        Intent i = new Intent(this, AgregarServicio.class);
        startActivity(i);
    }

    public void afrescoServicios(View v){
        Toast.makeText(this, "Lo sentimos, aun no disponible.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
