# Financial API

[![Financial API CI](https://github.com/gmorenos-dev/financial-api/actions/workflows/ci.yml/badge.svg)](https://github.com/gmorenos-dev/financial-api/actions/workflows/ci.yml)

API REST para gerenciamento de transações financeiras, desenvolvida com Java e Spring Boot. O projeto demonstra a implementação de operações CRUD, validação de dados, filtros, paginação, tratamento padronizado de erros, documentação interativa e testes automatizados com verificação de cobertura.

## Funcionalidades

- Cadastro de transações financeiras.
- Consulta de transações por identificador.
- Listagem com paginação e ordenação.
- Filtros por tipo, período e valor.
- Atualização e exclusão de transações.
- Consulta de resumo financeiro por período.
- Validação de dados e parâmetros.
- Tratamento padronizado de erros.
- Documentação interativa dos endpoints com Swagger UI.
- Testes automatizados e verificação de cobertura com JaCoCo.
- Integração contínua com GitHub Actions.

## Tecnologias

- **Java 25:** linguagem de programação.
- **Spring Boot 4.1.1:** estrutura principal da aplicação.
- **Spring Data JPA:** persistência e acesso aos dados.
- **PostgreSQL:** banco de dados relacional.
- **Maven Wrapper:** gerenciamento de dependências e execução do projeto.
- **Springdoc OpenAPI:** documentação da API.
- **Swagger UI:** visualização e experimentação dos endpoints.
- **JUnit e Spring Boot Test:** testes automatizados.
- **JaCoCo:** análise de cobertura dos testes.
- **GitHub Actions:** integração contínua.
- **Flyway:** versionamento e execução de migrações do banco de dados.

## Pré-requisitos

Os requisitos dependem da forma escolhida para executar a aplicação.

| Requisito | Java e Maven | Docker Compose | Imagem publicada |
|---|---|---|---|
| Git | Sim | Sim | Sim |
| Java 25 | Sim | Não | Não |
| PostgreSQL | Sim | Não | Não |
| Docker com Docker Compose | Não | Sim | Sim |
| Compilação do código-fonte | Sim | Sim | Não |

O projeto inclui o Maven Wrapper (`mvnw`), portanto não é necessário instalar o Maven separadamente.

## Formas de executar a aplicação

A Financial API pode ser executada de três maneiras. Escolha a opção mais adequada ao seu objetivo.

- **Opção 1 — Java e Maven:** indicada para estudar o código, executar testes e desenvolver novas funcionalidades.
- **Opção 2 — Docker Compose:** indicada para executar a aplicação completa e construir a imagem localmente.
- **Opção 3 — Imagem publicada no GHCR:** indicada para testar uma versão pronta da API sem compilar o código-fonte.

### Opção 1 — Executar com Java e Maven

Essa opção executa a aplicação diretamente no ambiente Java, utilizando um PostgreSQL já instalado e configurado.

**Ainda não tem Java e PostgreSQL instalados?**

Sem problemas! Antes de executar a aplicação, consulte nosso [Guia de Preparação do Ambiente](docs/PREPARACAO-DO-AMBIENTE.md). Ele apresenta, passo a passo, como instalar as ferramentas necessárias, configurar o banco de dados e verificar se tudo está funcionando.

#### 1. Obter o código-fonte

```bash
git clone https://github.com/gmorenos-dev/financial-api.git
cd financial-api
```

#### 2. Configurar a conexão com o PostgreSQL

Certifique-se de que o PostgreSQL esteja em execução e que o banco `financial_api` e o usuário `financial_api_user` estejam configurados.

Defina as variáveis de ambiente utilizadas pela aplicação, ajustando o endereço e a senha conforme seu ambiente:

```bash
export DB_URL="jdbc:postgresql://localhost:5432/financial_api"
export DB_PASSWORD="substitua_pela_senha_do_banco"
```

`DB_PASSWORD` deve conter a senha do usuário `financial_api_user`. Não publique credenciais no repositório.

Se o PostgreSQL estiver em outra máquina ou utilizar outra porta, ajuste `DB_URL`.

#### 3. Iniciar a aplicação

Na raiz do projeto, execute:

```bash
./mvnw spring-boot:run
```

A aplicação ficará disponível em `http://localhost:8080`.

#### 4. Acessar a API

- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs
- **Listagem de transações:** http://localhost:8080/api/transactions

Para encerrar a aplicação, pressione `Ctrl+C` no terminal em que ela está sendo executada.

### Opção 2 — Construir e executar com Docker Compose

Essa opção constrói a imagem da aplicação a partir do código-fonte e inicia a API e o PostgreSQL em contêineres separados.

Não é necessário instalar Java ou PostgreSQL diretamente no computador. É necessário ter o Docker instalado, com o Docker Compose disponível.

**Ainda não tem o Docker instalado?**

Consulte nosso [Guia de Preparação do Ambiente — Docker](docs/PREPARACAO-DO-AMBIENTE-DOCKER.md). Ele apresenta a instalação, configuração e utilização do Docker Desktop ou Docker Engine, além da execução da aplicação com Docker Compose.

#### 1. Obter o código-fonte

```bash
git clone https://github.com/gmorenos-dev/financial-api.git
cd financial-api
```

#### 2. Configurar as variáveis de ambiente

Crie o arquivo `.env` a partir do modelo:

```bash
cp .env.example .env
```

Edite o arquivo `.env` e defina uma senha forte para `DB_PASSWORD`.

O arquivo contém configurações locais e não deve ser enviado ao GitHub.

#### 3. Construir e iniciar os serviços

Na raiz do projeto, execute:

```bash
docker compose up --build -d
```

O Docker Compose construirá a imagem local da API, iniciará o PostgreSQL e iniciará a aplicação após a verificação de saúde do banco.

Consulte o estado dos contêineres:

```bash
docker compose ps
```

#### 4. Acessar a aplicação

- **API:** http://localhost:8080/api/transactions
- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs
- **PostgreSQL:** porta `5435` no computador local.

A API utiliza a rede interna do Docker Compose para conectar-se ao PostgreSQL pela porta `5432`. A porta `5435` é publicada para permitir conexões de ferramentas instaladas no computador.

#### 5. Encerrar os serviços

Para interromper e remover os contêineres e a rede:

```bash
docker compose down
```

O volume nomeado do PostgreSQL é preservado, portanto os dados permanecem disponíveis quando os serviços são iniciados novamente.

**Atenção:** `docker compose down -v` também remove o volume do banco de dados e pode apagar os dados armazenados.

### Opção 3 — Executar a imagem publicada no GHCR

Essa opção utiliza uma imagem já construída e publicada no GitHub Container Registry (GHCR). Ela permite testar uma versão específica da API sem compilar o código-fonte.

**Versão documentada:** `v1.0.1`

**Imagem:** `ghcr.io/gmorenos-dev/financial-api:v1.0.1`

É necessário ter Git e Docker com Docker Compose disponíveis. Não é necessário instalar Java, Maven ou PostgreSQL diretamente no computador.

**Precisa preparar o ambiente Docker?**

Consulte nosso [Guia de Preparação do Ambiente — Docker](docs/PREPARACAO-DO-AMBIENTE-DOCKER.md). Ele explica como configurar o Docker, preparar o banco de dados e executar a imagem publicada no GHCR.

#### 1. Obter os arquivos de configuração

Clone o repositório para obter o arquivo `compose.yaml` e o modelo de configuração:

```bash
git clone https://github.com/gmorenos-dev/financial-api.git
cd financial-api
```

Crie o arquivo `.env` e configure uma senha forte para `DB_PASSWORD`:

```bash
cp .env.example .env
```

O arquivo `.env` não deve ser enviado ao GitHub.


Para garantir a compatibilidade entre o Docker Compose e o comando `docker run --env-file`, defina `DB_PASSWORD` no formato `DB_PASSWORD=valor`, sem aspas ou espaços. Utilize uma senha forte e não compartilhe o arquivo `.env` nem o envie ao repositório.

#### 2. Iniciar somente o banco de dados

```bash
docker compose up -d db
```

O comando inicia o PostgreSQL sem construir nem iniciar a aplicação local.

Aguarde até que o banco esteja saudável:

```bash
docker compose ps
```

#### 3. Baixar a imagem publicada

```bash
docker pull ghcr.io/gmorenos-dev/financial-api:v1.0.1
```

A imagem é identificada pela tag da versão, o que permite testar uma publicação específica.

#### 4. Executar a aplicação publicada

Se já houver outra aplicação utilizando a porta `8080`, encerre-a antes de prosseguir.

Execute a imagem publicada, carregando as variáveis do arquivo `.env` diretamente pelo Docker:

```bash
docker run -d \
  --name financial-api-release \
  --network financial-api_default \
  --env-file .env \
  -e DB_URL=jdbc:postgresql://db:5432/financial_api \
  -p 127.0.0.1:8080:8080 \
  --restart unless-stopped \
  ghcr.io/gmorenos-dev/financial-api:v1.0.1
```

A aplicação utiliza a rede Docker Compose para acessar o PostgreSQL pelo nome do serviço `db`. A variável `DB_URL` aponta para a porta interna `5432` do banco.

O comando pressupõe que a rede se chama `financial-api_default`, como ocorre quando o projeto utiliza o nome padrão `financial-api`. Se o nome da rede for diferente, ajuste o parâmetro `--network`.


#### 5. Verificar a inicialização

Consulte os logs da aplicação:

```bash
docker logs --tail=50 financial-api-release
```

Verifique se a aplicação iniciou sem erros de autenticação ou divergência de checksum do Flyway.

Teste o endpoint de listagem:

```bash
curl -i http://localhost:8080/api/transactions
```

#### 6. Acessar a API

- **API:** http://localhost:8080/api/transactions
- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

#### 7. Encerrar a execução

Para remover somente o contêiner da imagem publicada:

```bash
docker rm -f financial-api-release
```

Esse comando não remove a imagem do GHCR nem o volume do PostgreSQL.

Para encerrar também o banco e remover a rede do Docker Compose:

```bash
docker compose down
```

O volume do PostgreSQL permanece preservado. Evite `docker compose down -v` quando desejar manter os dados.

Para voltar a executar a aplicação construída localmente pelo Docker Compose, depois de remover o contêiner `financial-api-release`, execute:

```bash
docker compose up --build -d app
```



## Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/transactions` | Cadastrar transação |
| `GET` | `/api/transactions` | Listar transações |
| `GET` | `/api/transactions/{id}` | Consultar transação por ID |
| `PUT` | `/api/transactions/{id}` | Atualizar transação |
| `DELETE` | `/api/transactions/{id}` | Excluir transação |
| `GET` | `/api/transactions/summary` | Consultar resumo financeiro |

## Modelo de transação

| Campo | Tipo | Regra |
|---|---|---|
| `id` | `Long` | Identificador da transação, atribuído pelo banco de dados |
| `description` | Texto | Obrigatório e não pode ficar em branco |
| `amount` | `BigDecimal` | Obrigatório, maior que zero, até 14 dígitos inteiros e 2 casas decimais |
| `type` | Enumeração | `RECEITA` ou `DESPESA` |
| `date` | `LocalDate` | Obrigatória, formato `yyyy-MM-dd` |

## Migrações do banco de dados

O projeto utiliza o Flyway para versionar e aplicar alterações na estrutura do PostgreSQL.

As migrações SQL ficam em:

`src/main/resources/db/migration/`

Cada arquivo possui uma versão e uma descrição. Atualmente, o projeto contém:

- `V1__create_transaction_table.sql`: cria a tabela de transações e a restrição para os tipos aceitos.
- `V2__ensure_transaction_type_constraint.sql`: garante a existência da restrição `transaction_type_check`, permitindo a adoção do Flyway em bancos que já possuem a tabela.

Ao iniciar a aplicação, o Flyway verifica o histórico e aplica as migrações pendentes. O Hibernate utiliza `ddl-auto=validate` para verificar a compatibilidade entre as entidades Java e o esquema do banco, sem modificá-lo automaticamente.

Para adicionar uma nova alteração estrutural, crie uma nova migração versionada, como `V3__descricao_da_alteracao.sql`, em vez de modificar migrações já aplicadas.

## Cadastrar uma transação

**Requisição**

```http
POST /api/transactions
Content-Type: application/json
```

**Corpo da requisição**

```json
{
  "description": "Salário Outubro",
  "amount": 5000.00,
  "type": "RECEITA",
  "date": "2026-10-05"
}
```

**Resposta esperada:** HTTP `201 Created`.

```json
{
  "id": 86,
  "description": "Salário Outubro",
  "amount": 5000.00,
  "type": "RECEITA",
  "date": "2026-10-05"
}
```

O ID `86` é apenas ilustrativo. O valor real é atribuído pelo banco de dados.

A atualização utiliza `PUT /api/transactions/{id}` e o mesmo formato de corpo da requisição.

## Consultar uma transação

```http
GET /api/transactions/86
```

Retorna a transação correspondente ao ID informado.

Uma consulta bem-sucedida retorna HTTP `200 OK`. Se a transação não for encontrada, a API retorna HTTP `404 Not Found`.

## Listar transações

```http
GET /api/transactions
```

A resposta contém os registros da página e os metadados da paginação.

**Exemplo ilustrativo de resposta**

```json
{
  "content": [
    {
      "id": 86,
      "description": "Salário Outubro",
      "amount": 5000.00,
      "type": "RECEITA",
      "date": "2026-10-05"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1
}
```

### Paginação e ordenação

| Parâmetro | Descrição |
|---|---|
| `page` | Número da página, começando em zero |
| `size` | Quantidade de registros por página |
| `sort` | Campo e direção da ordenação |

Os padrões são página `0`, tamanho `10` e ordenação por ID crescente.

**Exemplo:**

```http
GET /api/transactions?page=0&size=10&sort=id,asc
```

Ordenação pelo valor, do maior para o menor:

```http
GET /api/transactions?page=0&size=10&sort=amount,desc
```

### Filtros

| Parâmetro | Descrição | Exemplo |
|---|---|---|
| `type` | Tipo de transação | `type=DESPESA` |
| `startDate` | Data inicial | `startDate=2026-09-01` |
| `endDate` | Data final | `endDate=2026-09-30` |
| `minAmount` | Valor mínimo | `minAmount=100` |
| `maxAmount` | Valor máximo | `maxAmount=200` |

Os filtros podem ser combinados.

**Exemplo:**

```http
GET /api/transactions?type=DESPESA&startDate=2026-09-01&endDate=2026-09-30&minAmount=100&maxAmount=200&page=0&size=10&sort=id,asc
```

**Regras dos filtros:**

- `type` aceita `RECEITA` ou `DESPESA`.
- `startDate` e `endDate` devem ser informados juntos.
- A data inicial não pode ser posterior à data final.
- Os valores mínimo e máximo não podem ser negativos.
- O valor mínimo não pode ser maior que o valor máximo.
- As datas utilizam o formato `yyyy-MM-dd`.

## Resumo financeiro

O resumo financeiro permite consultar os totais de receitas, despesas e saldo.

### Consulta sem período

```http
GET /api/transactions/summary
```

### Consulta de um período específico

```http
GET /api/transactions/summary?startDate=2026-09-01&endDate=2026-09-30
```

Os parâmetros de data devem ser informados juntos ou omitidos.

**Exemplo ilustrativo de resposta:**

```json
{
  "totalReceitas": 5000.00,
  "totalDespesas": 1250.50,
  "saldo": 3749.50
}
```

Os valores apresentados são exemplos, não dados reais do banco de dados.

## Atualizar uma transação

Para atualizar uma transação existente, utilize:

```http
PUT /api/transactions/86
Content-Type: application/json
```

**Corpo da requisição:**

```json
{
  "description": "Salário atualizado",
  "amount": 5500.00,
  "type": "RECEITA",
  "date": "2026-10-05"
}
```

O ID informado na URL identifica a transação a ser atualizada.

## Excluir uma transação

```http
DELETE /api/transactions/86
```

Uma exclusão bem-sucedida retorna HTTP `204 No Content`, sem corpo de resposta. Se a transação não for encontrada, a API retorna HTTP `404 Not Found`.

## Tratamento de erros

As respostas de erro seguem uma estrutura padronizada.

**Exemplo ilustrativo:**

```json
{
  "status": 400,
  "message": "Parâmetro inválido",
  "errors": {
    "type": "deve ser RECEITA ou DESPESA"
  },
  "path": "/api/transactions"
}
```

| Código HTTP | Significado |
|---|---|
| `200 OK` | Consulta ou atualização realizada |
| `201 Created` | Transação cadastrada |
| `204 No Content` | Transação excluída |
| `400 Bad Request` | Dados ou parâmetros inválidos |
| `404 Not Found` | Transação não encontrada |

As mensagens de erro variam conforme a validação ou a situação que provocou a falha.

## Testes automatizados

Para executar os testes automatizados:

```bash
./mvnw test
```

Para executar uma validação completa, incluindo a compilação, os testes, a geração do relatório de cobertura e a verificação dos limites configurados, execute:

```bash
./mvnw clean verify
```

O projeto utiliza o JaCoCo para medir a cobertura do código executado pelos testes.

### Limites mínimos de cobertura

A verificação de cobertura está configurada para exigir:

- **Cobertura de linhas:** mínimo de 95%.
- **Cobertura de ramificações:** mínimo de 90%.

Se os limites não forem atendidos, a etapa de verificação do Maven falhará.

### Relatório de cobertura

Após executar `./mvnw clean verify`, o relatório HTML estará disponível em:

`target/site/jacoco/index.html`

Abra esse arquivo em um navegador para consultar a cobertura geral, os pacotes, as classes e as linhas de código não cobertas pelos testes.

## Integração contínua

O projeto utiliza o GitHub Actions para executar automaticamente a validação do código.

O fluxo de integração contínua está definido em:

`.github/workflows/ci.yml`

A validação é acionada quando há:

- Envio de alterações para a branch `main`.
- Abertura ou atualização de um *pull request* direcionado à branch `main`.

O pipeline executa `./mvnw clean verify` e publica o relatório JaCoCo como artefato da execução.

Além disso, o pipeline valida a configuração do Docker Compose, inicia a aplicação e o PostgreSQL em contêineres, e realiza um teste de integração que cadastra uma transação e consulta os dados retornados pela API. Ao final, o ambiente temporário é encerrado.

O resultado pode ser acompanhado na página:

[Financial API — GitHub Actions](https://github.com/gmorenos-dev/financial-api/actions)

O indicador de status no início deste README também permite consultar o resultado mais recente do pipeline.
