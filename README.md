# Financial API

API REST para gerenciamento de transações financeiras, desenvolvida com Java e Spring Boot.

## Funcionalidades

- Cadastro de transações financeiras.
- Consulta de transações por identificador.
- Listagem com paginação e ordenação.
- Filtros por tipo, período e valor.
- Atualização e exclusão de transações.
- Consulta de resumo financeiro.
- Tratamento de erros de validação.

## Tecnologias

- Java
- Spring Boot
- Spring Data JPA
- Maven
- PostgreSQL

## Executando o projeto

Configure o acesso ao PostgreSQL conforme as configurações da aplicação.

Na raiz do projeto, execute:

```bash
./mvnw spring-boot:run
```

A aplicação é acessada, por padrão, em:

`http://localhost:8080`

## Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/transactions` | Cadastrar transação |
| GET | `/api/transactions` | Listar transações |
| GET | `/api/transactions/{id}` | Consultar transação por ID |
| PUT | `/api/transactions/{id}` | Atualizar transação |
| DELETE | `/api/transactions/{id}` | Excluir transação |
| GET | `/api/transactions/summary` | Consultar resumo financeiro |

## Modelo de transação

| Campo | Tipo | Regra |
|---|---|---|
| `id` | Long | Identificador da transação |
| `description` | Texto | Obrigatório, não pode ficar em branco |
| `amount` | Decimal | Obrigatório, maior que zero, até 14 dígitos inteiros e 2 casas decimais |
| `type` | Enumeração | `RECEITA` ou `DESPESA` |
| `date` | Data | Obrigatória, formato `yyyy-MM-dd` |

## Cadastrar uma transação

Requisição:

```http
POST /api/transactions
Content-Type: application/json
```

Corpo da requisição:

```json
{
  "description": "Salário Outubro",
  "amount": 5000.00,
  "type": "RECEITA",
  "date": "2026-10-05"
}
```

Resposta esperada: HTTP `201 Created`.

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

## Listar transações

```http
GET /api/transactions
```

A resposta contém os registros da página e os metadados da paginação.

Exemplo ilustrativo:

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

Exemplo:

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

Exemplo:

```http
GET /api/transactions?type=DESPESA&startDate=2026-09-01&endDate=2026-09-30&minAmount=100&maxAmount=200&page=0&size=10&sort=id,asc
```

Regras dos filtros:

- `type` aceita `RECEITA` ou `DESPESA`.
- `startDate` e `endDate` devem ser informados juntos.
- A data inicial não pode ser posterior à data final.
- Os valores mínimo e máximo não podem ser negativos.
- O valor mínimo não pode ser maior que o valor máximo.
- As datas utilizam o formato `yyyy-MM-dd`.

## Resumo financeiro

Consulta sem definir um período:

```http
GET /api/transactions/summary
```

Consulta de um período específico:

```http
GET /api/transactions/summary?startDate=2026-09-01&endDate=2026-09-30
```

Os parâmetros de data devem ser informados juntos ou omitidos.

Exemplo ilustrativo de resposta:

```json
{
  "totalReceitas": 5000.00,
  "totalDespesas": 1250.50,
  "saldo": 3749.50
}
```

## Excluir uma transação

```http
DELETE /api/transactions/86
```

Uma exclusão bem-sucedida retorna HTTP `204 No Content`, sem corpo de resposta.

## Tratamento de erros

As respostas de erro seguem esta estrutura:

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

As mensagens de erro variam conforme a validação que falhou.

## Testes

Para executar os testes automatizados:

```bash
./mvnw test
```

### Verificação completa e cobertura

Para executar os testes, gerar o relatório de cobertura e validar os limites mínimos configurados, execute:

```bash
./mvnw clean verify
```

O JaCoCo verifica os seguintes limites mínimos de cobertura:

- **Linhas:** 95%.
- **Ramificações:** 90%.

Após uma execução bem-sucedida, o relatório HTML estará disponível em:

`target/site/jacoco/index.html`

O projeto também executa essa verificação automaticamente no GitHub Actions a cada envio de alterações para a branch `main`.
