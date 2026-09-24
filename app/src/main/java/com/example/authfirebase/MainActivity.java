package com.example.authfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextView textStatus;
    private TextView textEmail;
    private TextView textUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        textStatus = findViewById(R.id.textStatus);
        textEmail = findViewById(R.id.textEmail);
        textUid = findViewById(R.id.textUid);

        configurarBotao(R.id.btnCadastro, CadastroActivity.class);
        configurarBotao(R.id.btnLogin, LoginActivity.class);
        configurarBotao(R.id.btnVerificarEmail, VerificarEmailActivity.class);
        configurarBotao(R.id.btnRecuperarSenha, RecuperarSenhaActivity.class);
        configurarBotao(R.id.btnToken, TokenActivity.class);

        Button btnSair = findViewById(R.id.btnSair);
        btnSair.setOnClickListener(v -> {
            if (auth.getCurrentUser() == null) {
                Toast.makeText(this, "Nenhum usuário está conectado.", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signOut();
            atualizarCabecalho();
            Toast.makeText(this, "Sessão encerrada.", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarCabecalho();
    }

    private void configurarBotao(int id, Class<?> destino) {
        findViewById(id).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, destino)));
    }

    private void atualizarCabecalho() {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            textStatus.setText("Status: desconectado");
            textStatus.setTextColor(getColor(R.color.error_red));
            textEmail.setText("E-mail: —");
            textUid.setText("UID: —");
            return;
        }

        textStatus.setText("Status: conectado");
        textStatus.setTextColor(getColor(R.color.success_green));
        textEmail.setText("E-mail: " + user.getEmail());
        textUid.setText("UID: " + user.getUid());
    }
}
