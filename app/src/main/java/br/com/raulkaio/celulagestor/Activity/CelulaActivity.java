package br.com.raulkaio.celulagestor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.raulkaio.celulagestor.Adapter.PessoaAdapter;
import br.com.raulkaio.celulagestor.Classes.Celula;
import br.com.raulkaio.celulagestor.Classes.Pessoa;
import br.com.raulkaio.celulagestor.R;

public class CelulaActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;

    /* Variáveis do RecyclerView */
    private RecyclerView mRecyclerViewPessoas;
    private PessoaAdapter adapter;
    private List<Pessoa> pessoas;
    private DatabaseReference referencia;
    private Pessoa todasPessoas;
    private LinearLayoutManager mLayoutManagerTodasPessoas;

    private FrameLayout frameLayoutAba1, frameLayoutAba2, frameLayoutAba3;

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    mRecyclerViewPessoas.setVisibility(View.INVISIBLE);
//                    return true;
//                case R.id.navigation_dashboard:
//                    mRecyclerViewPessoas.setVisibility(View.VISIBLE);
//                    listarPessoas();
//                    return true;
//                case R.id.navigation_notifications:
//                    mRecyclerViewPessoas.setVisibility(View.INVISIBLE);
//                    return true;
//            }
//            return false;
//        }
//
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celula);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("CÉLULA"));
        tabLayout.addTab(tabLayout.newTab().setText("PESSOAS"));
        tabLayout.addTab(tabLayout.newTab().setText("REUNIÕES"));
        tabLayout.setupWithViewPager(pager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Integer numTab = tab.getPosition();
                if(numTab == 0){
                    mRecyclerViewPessoas.setVisibility(View.INVISIBLE);
                } else if (numTab == 1){
                    mRecyclerViewPessoas.setVisibility(View.VISIBLE);
                } else if (numTab == 2){
                    mRecyclerViewPessoas.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mRecyclerViewPessoas = (RecyclerView) findViewById(R.id.recyclerViewPessoasCelula);
        mRecyclerViewPessoas.setVisibility(View.INVISIBLE);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        /* Faz funcionar o botão Voltar na toolbar*/
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_cadastrar_pessoa){
            Intent criarPessoa  = new Intent(CelulaActivity.this, CriaPessoaActivity.class);
            startActivity(criarPessoa);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_celula, menu);
        return true;
    }

    public void listarPessoas(){
        mRecyclerViewPessoas.setHasFixedSize(true);
        mLayoutManagerTodasPessoas = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewPessoas.setLayoutManager(mLayoutManagerTodasPessoas);

        pessoas = new ArrayList<>();
        referencia = FirebaseDatabase.getInstance().getReference();
        autenticacao = FirebaseAuth.getInstance();

        referencia.child("Celula").child("-LCujklSFcoHTyZxIclX").child("Pessoa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    todasPessoas = postSnapshot.getValue(Pessoa.class);
                    pessoas.add(todasPessoas);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter = new PessoaAdapter(pessoas, this);
        mRecyclerViewPessoas.setAdapter(adapter);
    }
}
