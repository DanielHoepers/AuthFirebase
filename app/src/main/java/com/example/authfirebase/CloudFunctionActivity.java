package com.example.authfirebase;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.functions.FirebaseFunctions;

import java.util.Map;

public class CloudFunctionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_function);

        findViewById(R.id.btnVoltar).setOnClickListener(v -> finish());

        Button btnExecutar = findViewById(R.id.btnExecutarFuncao);
        btnExecutar.setOnClickListener(v -> {
            btnExecutar.setEnabled(false);
            FirebaseFunctions.getInstance("us-central1")
                    .getHttpsCallable("saudacao")
                    .call()
                    .addOnCompleteListener(task -> {
                        btnExecutar.setEnabled(true);
                        if (!task.isSuccessful()) {
                            Toast.makeText(this,
                                    "Não foi possível executar a função. Verifique a conexão e a publicação.",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        Object data = task.getResult().getData();
                        if (!(data instanceof Map)
                                || !(((Map<?, ?>) data).get("message") instanceof String)) {
                            Toast.makeText(this, "A função retornou uma resposta inválida.",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        String message = (String) ((Map<?, ?>) data).get("message");
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    });
        });
    }
}
