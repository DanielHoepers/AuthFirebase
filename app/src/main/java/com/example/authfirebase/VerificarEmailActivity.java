package com.example.authfirebase;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerificarEmailActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextView textUsuario;
    private TextView textResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_email);

        auth = FirebaseAuth.getInstance();
        textUsuario = findViewById(R.id.textUsuario);
        textResultado = findViewById(R.id.textResultado);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
        findViewById(R.id.btnEnviar).setOnClickListener(v -> enviarVerificacao());
        exibirUsuario();
    }

    private void enviarVerificacao() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            mostrarResultado("Nenhum usuário está conectado.", false);
            return;
        }
        if (user.isEmailVerified()) {
            mostrarResultado("O e-mail deste usuário já está verificado.", true);
            return;
        }

        textResultado.setText("Enviando e-mail de verificação...");
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                mostrarResultado("E-mail de verificação enviado para " + user.getEmail(), true);
            } else {
                mostrarResultado(AuthMessages.fromException(task.getException()), false);
            }
        });
    }

    private void exibirUsuario() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            textUsuario.setText("Usuário atual: nenhum");
            return;
        }

        textUsuario.setText("E-mail: " + user.getEmail()
                + "\nVerificado: " + (user.isEmailVerified() ? "sim" : "não"));
    }

    private void mostrarResultado(String mensagem, boolean sucesso) {
        textResultado.setText(mensagem);
        textResultado.setTextColor(getColor(sucesso ? R.color.success_green : R.color.error_red));
    }
}
