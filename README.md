Este projeto é uma aplicação de exemplo que mostra como implementar a autenticação usando JSON Web Tokens (JWT) em uma API RESTful com o Spring Boot e o Java 11.

# Tecnologias utilizadas
* Spring Boot
* Java 11
* Spring Security
* JWT

# Pré-requisitos
* Java 11 
* Maven
* IDE de sua preferência

# Como executar o projeto
1. Clone este repositório
2. Abra a pasta do projeto em sua IDE
3. Certifique-se de que o Maven baixou todas as dependências necessárias
4. Configure as informações do banco de dados no arquivo application.properties
5. Execute a classe AuthJwtApplication.java
6. Acesse o endpoint http://localhost:8080/auth para obter um token JWT válido

# Como usar a autenticação JWT
* Para acessar qualquer endpoint que requer autenticação, adicione o token JWT no header da requisição com a chave Authorization e valor Bearer <token>
* O token JWT tem duração limitada, então quando expirar, será necessário gerar um novo token usando o endpoint http://localhost:8080/auth/user/login

# Endpoints disponíveis 
* POST user/register - cria um novo usuário
* POST user/login - autenticação do usuário e geração do token JWT
* PUT user/change-password - alterar senha do usuário
