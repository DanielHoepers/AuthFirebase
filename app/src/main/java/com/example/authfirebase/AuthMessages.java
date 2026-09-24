package com.example.authfirebase;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthException;

public final class AuthMessages {

    private AuthMessages() {
    }

    public static String fromException(Exception exception) {
        if (exception == null) {
            return "O Firebase não retornou detalhes do erro.";
        }

        if (exception instanceof FirebaseNetworkException) {
            return "Falha de conexão. Verifique a internet do aparelho ou emulador.";
        }

        if (exception instanceof FirebaseAuthException) {
            String code = ((FirebaseAuthException) exception).getErrorCode();

            switch (code) {
                case "ERROR_INVALID_EMAIL":
                    return "O endereço de e-mail é inválido.";
                case "ERROR_EMAIL_ALREADY_IN_USE":
                    return "Este e-mail já está cadastrado.";
                case "ERROR_WEAK_PASSWORD":
                    return "A senha não atende aos requisitos mínimos.";
                case "ERROR_USER_NOT_FOUND":
                case "ERROR_WRONG_PASSWORD":
                case "ERROR_INVALID_CREDENTIAL":
                    return "E-mail ou senha inválidos.";
                case "ERROR_USER_DISABLED":
                    return "Esta conta foi desativada.";
                case "ERROR_TOO_MANY_REQUESTS":
                    return "Muitas tentativas. Aguarde um pouco e tente novamente.";
                case "ERROR_REQUIRES_RECENT_LOGIN":
                    return "Esta operação exige uma autenticação recente.";
                default:
                    return code + ": " + exception.getMessage();
            }
        }

        return exception.getMessage() == null
                ? exception.getClass().getSimpleName()
                : exception.getMessage();
    }
}

