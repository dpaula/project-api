# CLIENTES-API

Projeto Teste para candidatar a uma vaga na AG CAPITAL

API simples para gerenciamento de Projetos e Atividades

# Ambiente Para Desenvolvimento

## Pré requisito

- [IntelliJ](https://www.jetbrains.com/idea/download/)
- Maven 3.6+
- Java 17
- Docker 18+
- docker-compose

### Subindo API localmente para desenvolvimento

Ao clonar o Projeto e fazer o build para baixar as dependências do maven, basta seguir os pré-requisitos abaixo para
execução da API

Existem dois pré requisitos para execução da API:

- Servidor Banco de dados
    - Caso for executar API pela IDE, se faz necessário subir o banco via docker antes (a porta 1433 do host deve estar
      liberada)
        - Para subir o Servidor de Banco de Dados via docker basta estar na raiz do projeto, e executar o comando abaixo
        - ```docker-compose up -d postgres pgadmin```
- Zipkin (monitoramento e tracing)
    - Zipkin é uma ferramenta poderosa para monitorar e solucionar problemas em aplicativos distribuídos,
      permitindo que os desenvolvedores acompanhem o fluxo das solicitações e identifiquem possíveis problemas
      de desempenho em ambientes complexos
        - Para subir o Zipkin via docker basta estar na raiz do projeto, e executar o comando abaixo
        - ```docker-compose up -d zipkin```
- Service Discovery (Eureka) e Gateway
    - Importante salientar que para a comunicação das APIs, foi utilizado o Service Discovery (Eureka) e Gateway,
      para que as APIs se comuniquem entre si, e o Gateway para que as APIs se comuniquem com o mundo externo
        - Para subir o Service Discovery (Eureka) e Gateway via docker basta estar na raiz do projeto, e executar o
          comando abaixo
        - ```docker-compose up -d discovery gateway-api```

### Subindo API localmente para desenvolvimento via docker-compose

É possível subir as APIs sem precisar clonar os repositorios, basta executar o comando abaixo na raiz do projeto

```docker-compose up -d```

### Links para acesso aos componentes (localmente)

- APIs
    - [API - Customer-api](http://localhost:6060/swagger-ui.html)
    - [API - Customer-api Health Check](http://localhost:6060/actuator/health)
    - [API - Project-api](http://localhost:6070/swagger-ui/index.html)
    - [API - Project-api Health Check](http://localhost:6070/actuator/health)

- Outros Componentes
    - [Eureka](http://localhost:8761/)
    - [Zipkin](http://localhost:9411/zipkin/)
    - [Postgres](http://localhost:5050/browser/) (senha: "password")

## Principais Tecnologias Utilizadas na API

- Java 17
- Spring Boot 2.7
- Spring Data
- Spring Actuator
- Spring Validation
- Lombok
- Swagger v3
- Postgres
- Slueth (para tracing)
- Zipkin (para monitoramento e tracing)
- Docker
- Service Discovery (Eureka)
- Gateway

## Principais Regras Utilizadas

#### Cadastro de Projetos

- Utilizado apenas 4 propriedades para cadastro de projetos (nome, descrição, status e id do cliente), somente o nome é
  obrigatórios
- Campo nome foi utilizado com único
- Ao incluir um Projeto novo, o sistema registra data e hora de inclusão, e seta o mesmo como ativo

#### Alteração de Projetos

- Sistema valida apenas o nome ao efetuar alteração, onde não deve ser alterado para um nome já cadastrado
- Ao alterar, o sistema registar data e hora da alteração do Projeto

#### Incluindo nova Atividade ao Projeto

- As Atividades são incluídas via API de Projetos, onde é necessário informar o id do Projeto
- Ao alterar, o sistema registar data e hora da alteração da atividade
- É possível incluir mais de uma ao projeto

#### Outros

- A API depende de um cliente cadastrado para incluir um projeto, o que a torna dependente da API de Clientes

## Testando API

A principal documentação da API foi exportada via Swagger (link abaixo), e é a maneira mais simples (e eficiente) para
efetuar testes

A segunda opção para fácil utilização da API, é através do Postman, onde pode ser importado por
este [arquivo](https://github.com/dpaula/#)


