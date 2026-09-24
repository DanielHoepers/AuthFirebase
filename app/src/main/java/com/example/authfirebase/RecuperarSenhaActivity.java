package com.example.authfirebase;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RecuperarSenhaActivity extends AppCompatActivity {

    private EditText editEmail;
    private Button btnExecutar;
    private TextView textResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        editEmail = findViewById(R.id.editEmail);
        btnExecutar = findViewById(R.id.btnExecutar);
        textResultado = findViewById(R.id.textResultado);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
        btnExecutar.setOnClickListener(v -> enviarRecuperacao());
    }

    private void enviarRecuperacao() {
        String email = editEmail.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mostrarResultado("Informe um e-mail válido.", false);
            return;
        }

        setCarregando(true);
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    setCarregando(false);
                    if (task.isSuccessful()) {
                        mostrarResultado("E-mail de recuperação enviado para " + email, true);
                    } else {
                        mostrarResultado(AuthMessages.fromException(task.getException()), false);
                    }
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
