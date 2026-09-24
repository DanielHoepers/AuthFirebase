# Firebase Auth Lab

Aplicativo Android em Java criado para demonstrar, de forma isolada, os principais recursos do Firebase Authentication.

## Como instalar no projeto existente

1. Feche o Android Studio.
2. Faça uma cópia da pasta `app` atual, caso queira guardar o projeto vazio.
3. Extraia este pacote.
4. Copie a pasta `app` deste pacote para a raiz do seu projeto `AuthFirebase`, substituindo a pasta existente.
5. Substitua também o arquivo `build.gradle.kts` da raiz pelo arquivo incluído neste pacote.
6. Abra novamente o projeto no Android Studio.
7. Execute **File > Sync Project with Gradle Files**.
8. Confirme no Firebase Console que **Authentication > Método de login > E-mail/senha** está habilitado.
9. Execute o aplicativo no emulador ou celular.

O arquivo `google-services.json` incluído já contém o cliente Android `com.example.authfirebase`.

## Funcionalidades

- Cadastrar usuário.
- Fazer login com e-mail e senha.
- Enviar a verificação do e-mail.
- Enviar a recuperação de senha.
- Gerar, atualizar e visualizar o ID Token JWT.
- Encerrar a sessão pelo painel principal.

## Organização para apresentação

Cada função está em uma Activity separada. Assim, é possível abrir uma tela no aplicativo e, em seguida, mostrar apenas a classe Java relacionada ao recurso:

| Tela | Classe Java |
| --- | --- |
| Painel | `MainActivity.java` |
| Cadastro | `CadastroActivity.java` |
| Login | `LoginActivity.java` |
| Verificação de e-mail | `VerificarEmailActivity.java` |
| Recuperação de senha | `RecuperarSenhaActivity.java` |
| Token JWT | `TokenActivity.java` |

## Observação sobre o token

O aplicativo obtém e exibe o ID Token e suas claims para fins didáticos. A validação segura da assinatura, expiração e revogação do token deve ser feita em um backend confiável usando o Firebase Admin SDK.
