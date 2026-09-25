# Firebase Lab

Aplicativo Android em Java para demonstrar Firebase Authentication, Cloud Functions e Cloud Firestore.

## Pré-requisitos

- Android Studio com suporte ao projeto Gradle existente.
- Projeto Firebase `authfirebase-efdb9`, já configurado em `app/google-services.json`.
- Método de login **E-mail/senha** habilitado no Firebase Authentication.
- Para publicar a função: projeto no plano **Blaze**, Node.js 22 e Firebase CLI com acesso ao projeto.

## Telas

O aplicativo abre em `MainActivity`, com as opções **Autenticação**, **Função em nuvem** e **Tarefas (Firestore)**. `AuthActivity` mantém o painel e as telas de cadastro, login, verificação de e-mail, recuperação de senha e token JWT.

Na tela **Função em nuvem**, toque em **Executar função**. O app chama `saudacao`, uma função HTTPS callable em `us-central1`, e mostra a resposta recebida em uma notificação. Essa demonstração não exige login e não grava dados.

Na tela **Tarefas (Firestore)**, o usuário logado cria tarefas em `usuarios/{uid}/tarefas` e vê a lista atualizar em tempo real. Toque numa tarefa para concluí-la e segure para excluí-la. Sem login, a tela pede para entrar em **Autenticação** primeiro.

## Configurar o Firestore

No console, crie o banco em **Firestore Database** no modo de produção. Depois publique as regras de `firestore.rules`, que deixam cada usuário acessar só as próprias tarefas:

```sh
firebase deploy --only firestore:rules --project authfirebase-efdb9
```

Também é possível colar o conteúdo do arquivo na aba **Regras** do console e clicar em **Publicar**.

## Publicar a função

Na raiz do projeto, instale as dependências do backend e publique somente a função do exemplo:

```sh
npm --prefix functions install
firebase login
firebase deploy --only functions:saudacao --project authfirebase-efdb9
```

O arquivo `firebase.json` aponta para `functions/`. A função está em `functions/index.js`. Depois do deploy, execute o aplicativo em um dispositivo ou emulador com conexão à internet. Se a publicação falhar, confira o acesso à conta Firebase, o plano Blaze e as APIs exigidas pela CLI.

Para conferir o backend sem acesso ao projeto remoto, inicie o emulador local e faça uma chamada callable em outro terminal:

```sh
npx firebase-tools emulators:start --only functions --project demo-authfirebase
curl -X POST -H 'Content-Type: application/json' -d '{"data":{}}' http://127.0.0.1:5001/demo-authfirebase/us-central1/saudacao
```

## Verificação rápida

1. Abra o app e confira as três opções do menu.
2. Entre em **Autenticação** e verifique que os fluxos existentes continuam disponíveis; Voltar retorna ao menu.
3. Entre em **Função em nuvem** sem fazer login e toque em **Executar função**. A notificação deve mostrar `Olá do Firebase Cloud Functions!`.
4. Com a rede desligada, repita a chamada e confira a mensagem de erro. O botão deve voltar a ficar habilitado.
5. Faça login em **Autenticação**, volte ao menu e abra **Tarefas (Firestore)**. Crie uma tarefa e confira o documento no console; edite o título no console e veja o app atualizar sozinho.
6. Entre com outro usuário e confira que a lista dele começa vazia.

## Observação sobre o token

O aplicativo obtém e exibe o ID Token e suas claims para fins didáticos. A validação segura da assinatura, expiração e revogação do token deve ser feita em um backend confiável usando o Firebase Admin SDK.
