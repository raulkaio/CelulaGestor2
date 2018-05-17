package br.com.raulkaio.celulagestor.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.raulkaio.celulagestor.Adapter.CelulaAdapter;
import br.com.raulkaio.celulagestor.Adapter.PessoaAdapter;
import br.com.raulkaio.celulagestor.Classes.Celula;
import br.com.raulkaio.celulagestor.Classes.Pessoa;
import br.com.raulkaio.celulagestor.R;

public class RelatorioTodasPessoas extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener{

    private FirebaseAuth autenticacao;

    /* Variáveis do RecyclerView */
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerViewPessoas;
    private PessoaAdapter adapter;
    private List<Pessoa> pessoas;
    private DatabaseReference referencia;
    private Pessoa todasPessoas;
    private LinearLayoutManager mLayoutManagerTodasPessoas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_todas_pessoas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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

        mRecyclerViewPessoas = (RecyclerView) findViewById(R.id.recyclerViewTodasAsPessoas);
        carregarTodasPessoas();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onRefresh() {

    }

    private void carregarTodasPessoas(){
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerViewPessoas.setHasFixedSize(true);
        mLayoutManagerTodasPessoas = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewPessoas.setLayoutManager(mLayoutManagerTodasPessoas);

        pessoas = new ArrayList<>();
        referencia = FirebaseDatabase.getInstance().getReference();
        autenticacao = FirebaseAuth.getInstance();

        referencia.child("Pessoa").orderByChild("email").equalTo(autenticacao.getCurrentUser().getEmail().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    todasPessoas = postSnapshot.getValue(Pessoa.class);
                    pessoas.add(todasPessoas);
                }
                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter = new PessoaAdapter(pessoas, this);
        mRecyclerViewPessoas.setAdapter(adapter);
    }
}
