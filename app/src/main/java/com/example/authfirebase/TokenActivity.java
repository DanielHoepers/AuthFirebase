package com.example.authfirebase;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

public class TokenActivity extends AppCompatActivity {

    private TextView textResultado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);

        textResultado = findViewById(R.id.textResultado);
        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());
        findViewById(R.id.btnExecutar).setOnClickListener(v -> obterToken());
    }

    private void obterToken() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            mostrarErro("Nenhum usuário está conectado. Faça login antes de obter o token.");
            return;
        }

        textResultado.setText("Gerando um token atualizado...");

        user.getIdToken(true).addOnCompleteListener(task -> {
            if (!task.isSuccessful() || task.getResult() == null) {
                mostrarErro(AuthMessages.fromException(task.getException()));
                return;
            }

            GetTokenResult result = task.getResult();
            String token = result.getToken();

            String claims;
            try {
                claims = new JSONObject(result.getClaims()).toString(2);
            } catch (JSONException exception) {
                claims = result.getClaims().toString();
            }

            DateFormat formato = DateFormat.getDateTimeInstance();
            String texto = "ID TOKEN JWT\n\n"
                    + token
                    + "\n\nCLAIMS\n"
                    + claims
                    + "\n\nEmitido em: "
                    + formato.format(new Date(result.getIssuedAtTimestamp()))
                    + "\nExpira em: "
                    + formato.format(new Date(result.getExpirationTimestamp()))
                    + "\nProvedor: " + result.getSignInProvider()
                    + "\n\nIMPORTANTE\n"
                    + "O aplicativo cliente obtém o token. A API/backend deve validar sua assinatura, expiração e revogação usando o Firebase Admin SDK.";

            textResultado.setText(texto);
            textResultado.setTextColor(getColor(R.color.text_primary));
        });
    }

    private void mostrarErro(String mensagem) {
        textResultado.setText(mensagem);
        textResultado.setTextColor(getColor(R.color.error_red));
    }
}
