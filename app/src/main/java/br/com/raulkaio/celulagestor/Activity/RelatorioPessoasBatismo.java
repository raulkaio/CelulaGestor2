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

import br.com.raulkaio.celulagestor.Adapter.RelatorioPessoasBatismoAdapter;
import br.com.raulkaio.celulagestor.Classes.Pessoa;
import br.com.raulkaio.celulagestor.R;

public class RelatorioPessoasBatismo extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener{

    private FirebaseAuth autenticacao;

    /* Variáveis do RecyclerView */
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerViewPessoas;
    private RelatorioPessoasBatismoAdapter adapter;
    private List<Pessoa> pessoas;
    private DatabaseReference referencia;
    private Pessoa pessoasBatismo;
    private LinearLayoutManager mLayoutManagerPessoasBatismo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_pessoas_batismo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        mRecyclerViewPessoas = (RecyclerView) findViewById(R.id.recyclerViewPessoasBatismo);
        carregarPessoasBatismo();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onRefresh() {

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        /* Faz funcionar o botão Voltar na toolbar*/
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void carregarPessoasBatismo(){
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerViewPessoas.setHasFixedSize(true);
        mLayoutManagerPessoasBatismo = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewPessoas.setLayoutManager(mLayoutManagerPessoasBatismo);

        pessoas = new ArrayList<>();
        referencia = FirebaseDatabase.getInstance().getReference();
        autenticacao = FirebaseAuth.getInstance();

        String teste = autenticacao.getCurrentUser().getEmail().toString()+"_false";
        referencia.child("Pessoa")
                .orderByChild("keyEmailEncontro")
                .equalTo(autenticacao.getCurrentUser().getEmail().toString()+"~"+"false")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                            pessoasBatismo = postSnapshot.getValue(Pessoa.class);
                            pessoas.add(pessoasBatismo);
                        }
                        adapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        adapter = new RelatorioPessoasBatismoAdapter(pessoas, this);
        mRecyclerViewPessoas.setAdapter(adapter);
    }

}
