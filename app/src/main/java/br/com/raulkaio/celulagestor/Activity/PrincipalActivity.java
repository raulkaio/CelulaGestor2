package br.com.raulkaio.celulagestor.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.raulkaio.celulagestor.Adapter.CelulaAdapter;
import br.com.raulkaio.celulagestor.Classes.Celula;
import br.com.raulkaio.celulagestor.R;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private FirebaseAuth autenticacao;

    /* Variáveis do RecyclerView */
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerViewCelulas;
    private CelulaAdapter adapter;
    private List<Celula> celulas;
    private DatabaseReference referencia;
    private Celula todasCelulas;
    private LinearLayoutManager mLayoutManagerTodasCelulas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        autenticacao = FirebaseAuth.getInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, CriaCelulaActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /* RecyclerView de Células */

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimaryLight,
                R.color.colorAccent);

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        mRecyclerViewCelulas = (RecyclerView) findViewById(R.id.recyclerViewTodasAsCelulas);
        carregarTodasCelulas();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_criar_celula) {
            Intent criarCelula  = new Intent(PrincipalActivity.this, CriaCelulaActivity.class);
            startActivity(criarCelula);
        } else if (id == R.id.nav_criar_pessoa) {
            Intent criarPessoa  = new Intent(PrincipalActivity.this, CriaPessoaActivity.class);
            startActivity(criarPessoa);
        } else if (id == R.id.nav_relatorio_todas_pessoas) {
            Intent relatorioTodasPessoas  = new Intent(PrincipalActivity.this, RelatorioTodasPessoas.class);
            startActivity(relatorioTodasPessoas);
        } else if (id == R.id.nav_relatorio_pessoas_encontro) {
            Intent relatorioPessoasEncontro  = new Intent(PrincipalActivity.this, RelatorioPessoasEncontro.class);
            startActivity(relatorioPessoasEncontro);
        } else if (id == R.id.nav_relatorio_pessoas_batismo) {
            Intent relatorioPessoasBatismo  = new Intent(PrincipalActivity.this, RelatorioPessoasBatismo.class);
            startActivity(relatorioPessoasBatismo);
        } else if (id == R.id.nav_sair) {
            deslogarUsuario();
        } else if (id == R.id.nav_ajuda) {
            emitirAlertaDeajuda();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void emitirAlertaDeajuda(){
        AlertDialog.Builder construtorAlerta = new AlertDialog.Builder(this);
        construtorAlerta.setMessage("Meu nome é Raul Kaio, eu fiz esse app. :)\n\n" +
                "Se houver algo que eu possa ajudar me envie um e-mail no:\n\n" +
                "meajuda@raulkaio.com.br\n\n" +
                "Um abraço!");

        construtorAlerta.setPositiveButton("ENTENDIDO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog meuAlerta = construtorAlerta.create();
        meuAlerta.show();
    }

    private void deslogarUsuario(){
        autenticacao.signOut();

        Intent intent = new Intent(PrincipalActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void carregarTodasCelulas(){
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerViewCelulas.setHasFixedSize(true);
        mLayoutManagerTodasCelulas = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewCelulas.setLayoutManager(mLayoutManagerTodasCelulas);

        celulas = new ArrayList<>();
        referencia = FirebaseDatabase.getInstance().getReference();

        referencia.child("Celula").orderByChild("email").equalTo(autenticacao.getCurrentUser().getEmail().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    todasCelulas = postSnapshot.getValue(Celula.class);
                    celulas.add(todasCelulas);
                }
                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter = new CelulaAdapter(celulas, this);
        mRecyclerViewCelulas.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        carregarTodasCelulas();
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        AlertDialog.Builder construtorAlerta;
        construtorAlerta = new AlertDialog.Builder(this);
        construtorAlerta.setTitle("Sair");
        construtorAlerta.setMessage("Deseja mesamo sair?");
        construtorAlerta.setPositiveButton("NÃO, QUERO FICAR", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
            }
        });
        construtorAlerta.setNegativeButton("SAIR", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                finish();
            }
        });
        AlertDialog meuAlerta = construtorAlerta.create();
        meuAlerta.show();
    }
}
