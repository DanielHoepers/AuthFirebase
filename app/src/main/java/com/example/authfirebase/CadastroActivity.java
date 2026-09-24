package com.example.authfirebase;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CadastroActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText editEmail;
    private EditText editSenha;
    private EditText editConfirmarSenha;
    private Button btnExecutar;
    private TextView textResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        auth = FirebaseAuth.getInstance();
        editEmail = findViewById(R.id.editEmail);
        editSenha = findViewById(R.id.editSenha);
        editConfirmarSenha = findViewById(R.id.editConfirmarSenha);
        btnExecutar = findViewById(R.id.btnExecutar);
        textResultado = findViewById(R.id.textResultado);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
        btnExecutar.setOnClickListener(v -> cadastrar());
    }

    private void cadastrar() {
        String email = editEmail.getText().toString().trim();
        String senha = editSenha.getText().toString();
        String confirmar = editConfirmarSenha.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mostrarResultado("Informe um e-mail válido.", false);
            return;
        }
        if (senha.length() < 6) {
            mostrarResultado("A senha precisa ter pelo menos 6 caracteres.", false);
            return;
        }
        if (!senha.equals(confirmar)) {
            mostrarResultado("As senhas informadas não são iguais.", false);
            return;
        }

        setCarregando(true);

        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        setCarregando(false);
                        mostrarResultado(AuthMessages.fromException(task.getException()), false);
                        return;
                    }

                    setCarregando(false);
                    FirebaseUser user = auth.getCurrentUser();
                    if (user == null) {
                        mostrarResultado("Conta criada, mas o usuário atual não foi encontrado.", false);
                        return;
                    }
                    mostrarResultado("Usuário criado com sucesso.\nUID: " + user.getUid(), true);
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
