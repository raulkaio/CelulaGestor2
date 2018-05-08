package br.com.raulkaio.celulagestor.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.raulkaio.celulagestor.Classes.Usuario;
import br.com.raulkaio.celulagestor.DAO.ConfiguracaoFirebase;
import br.com.raulkaio.celulagestor.R;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    private EditText edtEmailLogin, edtSenhaLogin;
    private Button btnLogin;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmailLogin = (EditText) findViewById(R.id.edtEmail)
        edtSenhaLogin = (EditText) findViewById(R.id.edtSenha);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        if(usuarioLogado()){
            Intent intentMinhaConta = new Intent (MainActivity.this, PrincipalActivity.class);
            startActivity(intentMinhaConta);

        } else {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!edtEmailLogin.getText().toString().equals("") && !edtSenhaLogin.getText().toString().equals("")) {
                        usuario = new Usuario();
                        usuario.setEmail(edtEmailLogin.getText().toString());
                        usuario.setSenha(edtSenhaLogin.getText().toString());

                        validarLogin();

                    } else {
                        Toast.makeText(MainActivity.this, "Por favor, preencha os campos de e-mail e senha", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void validarLogin() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail().toString(), usuario.getSenha().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    abrirTelaAdministrador();
                    Toast.makeText(MainActivity.this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Usuário ou senha inválidos! Tente novamente.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void abrirTelaAdministrador() {
        Intent intent = new Intent(MainActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
        finish();
    }

    public Boolean usuarioLogado(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            return true;
        } else{
            return false;
        }
    }

    public void abrirNovaActvity(Intent intent){
        startActivity(intent);
    }
}
