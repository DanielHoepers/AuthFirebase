package com.example.authfirebase;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText editEmail;
    private EditText editSenha;
    private Button btnExecutar;
    private TextView textResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        btnExecutar = findViewById(R.id.btnExecutar);
        textResultado = findViewById(R.id.textResultado);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
        btnExecutar.setOnClickListener(v -> entrar());
    }

    private void entrar() {
        String email = editEmail.getText().toString().trim();
        String senha = editSenha.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || senha.isEmpty()) {
            mostrarResultado("Informe um e-mail válido e a senha.", false);
            return;
        }

        setCarregando(true);
        auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    setCarregando(false);

                    if (!task.isSuccessful()) {
                        mostrarResultado(AuthMessages.fromException(task.getException()), false);
                        return;
                    }

                    FirebaseUser user = auth.getCurrentUser();
                    if (user == null) {
                        mostrarResultado("Login concluído, mas o usuário não foi encontrado.", false);
                        return;
                    }

                    String mensagem = "Login realizado com sucesso."
                            + "\nUID: " + user.getUid()
                            + "\nE-mail verificado: " + (user.isEmailVerified() ? "sim" : "não");
                    mostrarResultado(mensagem, true);
                });
    }

    private void setCarregando(boolean carregando) {
        btnExecutar.setEnabled(!carregando);
    }

    private void mostrarResultado(String mensagem, boolean sucesso) {
        textResultado.setText(mensagem);
        textResultado.setTextColor(getColor(sucesso ? R.color.success_green : R.color.error_red));
    }
}
