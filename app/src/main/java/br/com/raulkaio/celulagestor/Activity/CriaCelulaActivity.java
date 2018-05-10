package br.com.raulkaio.celulagestor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.raulkaio.celulagestor.Classes.Celula;
import br.com.raulkaio.celulagestor.DAO.ConfiguracaoFirebase;
import br.com.raulkaio.celulagestor.R;

public class CriaCelulaActivity extends AppCompatActivity {

    private EditText edtNome, edtObservacoes;
    private Spinner spnFrequencia;
    private FirebaseAuth autenticacao;
    private DatabaseReference referencia;
    private Celula celula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cria_celula);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        preencherSpinnerFrequencia();

        FloatingActionButton fab_salvar_celula = (FloatingActionButton) findViewById(R.id.fab_salvar_celula);
        fab_salvar_celula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edtNome = (EditText) findViewById(R.id.editTextNome);
                spnFrequencia = (Spinner) findViewById(R.id.spinnerFrequencia);
                edtObservacoes = (EditText) findViewById(R.id.editTextObservacoes);

                if (edtNome.getText().toString().equals("")){
                    Toast.makeText(CriaCelulaActivity.this, "Por favor, preencha o campo Nome.", Toast.LENGTH_SHORT).show();
                } else{
                    celula = new Celula();
                    celula.setNome(edtNome.getText().toString());
                    celula.setFrequencia(spnFrequencia.getSelectedItem().toString());
                    celula.setObservacoes(edtObservacoes.getText().toString());
                    celula.setHorario("Não implementado");

                    autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
                    celula.setEmail(autenticacao.getCurrentUser().getEmail().toString());

                    cadastrarCelula(celula);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cria_celula, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        /* Faz funcionar o botão Voltar na toolbar*/
        if (id == R.id.home){
            onBackPressed();
            return true;
        } else if (id == R.id.action_limpar_campos){
            limparCampos();
            return true;
        } else if (id == R.id.action_cancelar_celula){
            limparCampos();
            Intent intent = new Intent(CriaCelulaActivity.this, PrincipalActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void preencherSpinnerFrequencia(){
        /* Preenche o Spinner */
        List<String> spinnerArray =  new ArrayList<>();
        spinnerArray.add("1");
        spinnerArray.add("3");
        spinnerArray.add("7");
        spinnerArray.add("10");
        spinnerArray.add("15");
        spinnerArray.add("30");;
        spinnerArray.add("Outro intervalo");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) findViewById(R.id.spinnerFrequencia);
        sItems.setAdapter(adapter);
    }

    private void limparCampos(){
        edtNome = (EditText) findViewById(R.id.editTextNome);
        spnFrequencia = (Spinner) findViewById(R.id.spinnerFrequencia);
        edtObservacoes = (EditText) findViewById(R.id.editTextObservacoes);

        edtNome.setText(null);
        spnFrequencia.setSelection(0);
        edtObservacoes.setText(null);

    }

    private boolean cadastrarCelula(Celula celula){

        try {
            autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
            referencia = ConfiguracaoFirebase.getFirebase().child("Celula");

            referencia.push().setValue(celula);

            Toast.makeText(CriaCelulaActivity.this, "Tudo certo!\n" +
                    "Célula cadastrada com sucesso :)", Toast.LENGTH_SHORT).show();

            limparCampos();

            return true;
        } catch (Exception e){

            Toast.makeText(CriaCelulaActivity.this, "Oops!\n" +
                    "Houve algum problema na hora de cadastrar a célula..." +
                    "Por favor, tente novamente mais tarde.", Toast.LENGTH_LONG).show();

            return false;
        }
    }

}
