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

## Pré-requisitos

Antes de executar o projeto, certifique-se de que o ambiente possui:

- Java 25 instalado.
- PostgreSQL instalado ou acessível.
- Banco de dados configurado para a aplicação.

O projeto inclui o Maven Wrapper (`mvnw`), portanto não é necessário instalar o Maven separadamente.

## Executando o projeto

### 1. Obter o código-fonte

Clone o repositório e entre no diretório do projeto:

```bash
git clone https://github.com/gmorenos-dev/financial-api.git
cd financial-api
```

### 2. Configurar o banco de dados

Configure a conexão com o PostgreSQL de acordo com as propriedades utilizadas pela aplicação.

Antes de iniciar, verifique se o banco de dados está disponível e se os parâmetros de conexão estão definidos corretamente no ambiente local.

Não publique senhas ou outras credenciais no repositório.

### 3. Iniciar a aplicação

Na raiz do projeto, execute:

```bash
./mvnw spring-boot:run
```

Por padrão, a aplicação fica disponível em:

`http://localhost:8080`

### 4. Acessar a documentação interativa

Com a aplicação em execução, acesse:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **Especificação OpenAPI em JSON:** http://localhost:8080/v3/api-docs

O Swagger UI permite consultar os parâmetros dos endpoints, visualizar exemplos documentados de requisições e respostas e executar chamadas diretamente pelo navegador.

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

O resultado pode ser acompanhado na página:

[Financial API — GitHub Actions](https://github.com/gmorenos-dev/financial-api/actions)

O indicador de status no início deste README também permite consultar o resultado mais recente do pipeline.
