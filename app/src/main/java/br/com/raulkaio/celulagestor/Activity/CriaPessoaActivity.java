package br.com.raulkaio.celulagestor.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.com.raulkaio.celulagestor.R;

public class CriaPessoaActivity extends AppCompatActivity {

    private EditText edtNome, edtObservacoes, edtDataDeNascimento;
    private Spinner spnGenero, spnClassificacao;
    private Switch swtEncontro, swtBatismo;
    private Calendar dataDeNascimento;
    private DatePickerDialog.OnDateSetListener data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cria_pessoa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        preencheSpinnerGenero();
        preencheSpinnerClassificacao();
        dataDeNascimento();

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
            Intent intent = new Intent(CriaPessoaActivity.this, PrincipalActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void preencheSpinnerGenero(){
        List<String> listSpinnerGenero =  new ArrayList<>();
        listSpinnerGenero.add("Masculino");
        listSpinnerGenero.add("Feminino");

        ArrayAdapter<String> adapterGenero = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, listSpinnerGenero);

        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerGenero = (Spinner) findViewById(R.id.spinnerGenero);
        spinnerGenero.setAdapter(adapterGenero);
    }

    private void preencheSpinnerClassificacao(){
        List<String> listSpinnerClassificacao =  new ArrayList<>();
        listSpinnerClassificacao.add("Visitante");
        listSpinnerClassificacao.add("Co-líder");
        listSpinnerClassificacao.add("Líder");
        listSpinnerClassificacao.add("Líder em treinamento");
        listSpinnerClassificacao.add("Membro");

        ArrayAdapter<String> adapterClassificacao = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, listSpinnerClassificacao);

        adapterClassificacao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerClassificacao = (Spinner) findViewById(R.id.spinnerClassificacao);
        spinnerClassificacao.setAdapter(adapterClassificacao);
    }

    private void limparCampos(){
        edtNome = (EditText) findViewById(R.id.editTextNome);
        spnGenero = (Spinner) findViewById(R.id.spinnerGenero);
        spnClassificacao = (Spinner) findViewById(R.id.spinnerClassificacao);
        swtEncontro = (Switch) findViewById(R.id.switchEncontroComDeus);
        swtBatismo = (Switch) findViewById(R.id.switchBatismoNasAguas);
        edtDataDeNascimento = (EditText) findViewById(R.id.editTextObservacoes);
        edtObservacoes = (EditText) findViewById(R.id.editTextObservacoes);

        edtNome.setText(null);
        spnGenero.setSelection(0);
        spnClassificacao.setSelection(0);
        swtEncontro.setSelected(false);
        swtBatismo.setSelected(false);
        edtDataDeNascimento.setText(null);
        edtObservacoes.setText(null);

    }

    private void dataDeNascimento(){
        dataDeNascimento = Calendar.getInstance();
        edtDataDeNascimento = (EditText) findViewById(R.id.editTextDataNascimento);
        data = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                dataDeNascimento.set(Calendar.YEAR, year);
                dataDeNascimento.set(Calendar.MONTH, monthOfYear);
                dataDeNascimento.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edtDataDeNascimento.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CriaPessoaActivity.this, data, dataDeNascimento
                        .get(Calendar.YEAR), dataDeNascimento.get(Calendar.MONTH),
                        dataDeNascimento.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edtDataDeNascimento.setText(sdf.format(dataDeNascimento.getTime()));
    }
}