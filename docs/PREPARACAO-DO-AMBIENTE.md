# Preparação do Ambiente — Financial API

Este guia apresenta, passo a passo, como preparar um computador para executar a Financial API utilizando Java e PostgreSQL.

O objetivo é orientar pessoas que ainda não possuem experiência com essas ferramentas, desde a instalação até a verificação da conexão com o banco de dados.

## 1. Entendendo o que será instalado

Para preparar o ambiente e executar a aplicação diretamente com Java e Maven, precisamos de três ferramentas:

| Ferramenta | Finalidade |
|---|---|
| Git | Obter o código-fonte da Financial API disponibilizado no GitHub |
| Java JDK 25 | Ambiente necessário para compilar e executar a aplicação Java |
| PostgreSQL 17 | Banco de dados responsável por armazenar as transações financeiras |

**Observação sobre o Maven**

Não é necessário instalar o Maven separadamente. O projeto já inclui o **Maven Wrapper**, ferramenta que permite executar o Maven utilizando a versão definida pelo próprio projeto.

No Windows, utilizaremos o arquivo `mvnw.cmd`. No Ubuntu/Linux, utilizaremos o arquivo `mvnw`.

Na primeira execução, o Maven Wrapper poderá baixar automaticamente a versão do Maven necessária, caso ela ainda não esteja disponível no computador.


**O que é JDK?**

JDK significa *Java Development Kit*. É o conjunto de ferramentas necessário para desenvolver, compilar e executar aplicações Java.

O projeto utiliza Java 25, portanto recomendamos instalar essa versão para manter a compatibilidade com a aplicação.

**O que é PostgreSQL?**

PostgreSQL é um sistema gerenciador de banco de dados relacional. Ele será responsável por armazenar as receitas e despesas cadastradas na Financial API.

**Escolha do ambiente de execução**

A Financial API pode ser executada diretamente no Windows ou utilizando Ubuntu pelo WSL.

Antes de continuar, escolha onde deseja preparar e executar o projeto:

- **Windows:** utilize o PowerShell e instale as ferramentas diretamente no Windows.
- **Ubuntu/WSL:** utilize o terminal do Ubuntu e instale as ferramentas dentro dessa distribuição Linux.

**Importante:** mantenha o mesmo ambiente durante todo o procedimento. A instalação do Git, o download do projeto, a instalação do Java e do PostgreSQL, a configuração das variáveis de ambiente e a execução da aplicação devem ser realizados no ambiente escolhido.

Caso escolha Windows, não é necessário instalar o WSL. Caso escolha Ubuntu pelo WSL e ainda não tenha essa distribuição instalada, siga a próxima seção.

Para quem utiliza Ubuntu pelo WSL, não misture os comandos do PowerShell com os comandos do terminal Ubuntu durante a preparação do projeto.

### 1.1. Instalar o WSL e o Ubuntu (opcional para Windows)

Esta etapa é necessária somente para quem deseja executar a Financial API utilizando Ubuntu pelo Windows Subsystem for Linux (WSL).

Caso já tenha o WSL e o Ubuntu instalados, pode avançar para a próxima seção.

**Requisitos do Windows**

O comando de instalação apresentado neste guia requer Windows 10 versão 2004 (compilação 19041) ou posterior, ou Windows 11.

Para verificar a versão do Windows:

1. Pressione `Windows + R`.
2. Digite `winver`.
3. Pressione Enter.
4. Consulte a versão e a compilação apresentadas na janela.

Caso o computador utilize uma versão anterior, consulte o procedimento de instalação manual disponibilizado pela Microsoft:

https://learn.microsoft.com/pt-br/windows/wsl/install-manual

**Etapa 1 — Abrir o PowerShell como administrador**

No menu Iniciar do Windows, procure por `PowerShell`, clique com o botão direito e selecione **Executar como administrador**.

**Etapa 2 — Instalar o WSL e o Ubuntu**

Execute:

```powershell
wsl --install
```

O comando habilitará os componentes necessários e instalará o Ubuntu como distribuição Linux padrão.

**E se o comando apresentar apenas a tela de ajuda?**

Caso o comando `wsl --install` apresente uma lista de comandos e opções, em vez de iniciar a instalação, pode ser que o WSL já esteja instalado, mas ainda não tenha uma distribuição Linux configurada.

Nesse caso, siga os passos abaixo:

1. Consulte as distribuições disponíveis:

```powershell
wsl --list --online
```

2. Instale o Ubuntu:

```powershell
wsl --install -d Ubuntu
```

Aguarde a conclusão da instalação.

Depois, prossiga para a etapa de reinicialização do computador.

**Etapa 3 — Reiniciar o computador**

Após a conclusão da instalação, reinicie o Windows.

**Etapa 4 — Configurar o Ubuntu**

Abra o Ubuntu pelo menu Iniciar.

Na primeira inicialização, aguarde a preparação do ambiente. Em seguida, será solicitado que você crie um nome de usuário e uma senha para o Linux.

O nome de usuário e a senha serão utilizados para acessar e administrar sua distribuição Ubuntu.

**Importante:** essa senha será solicitada quando você executar comandos administrativos que utilizam `sudo`. Durante a digitação, os caracteres não serão exibidos na tela. Isso é normal.

Guarde essa senha, pois você precisará dela durante a instalação e configuração das ferramentas do projeto.

Essas credenciais são próprias do Ubuntu e não precisam ser iguais às utilizadas no Windows.

**Etapa 5 — Verificar a instalação**

Abra o PowerShell e execute:

```powershell
wsl --list --verbose
```

O resultado deverá apresentar a distribuição Ubuntu instalada e sua versão do WSL, conforme o exemplo:

```text
  NAME      STATE     VERSION
* Ubuntu    Running   2
```

A coluna `STATE` pode apresentar `Running` (em execução) ou `Stopped` (parada), dependendo de a distribuição Ubuntu estar aberta ou fechada. Isso é normal. Para este procedimento, o importante é confirmar que a coluna `VERSION` apresenta o valor `2`.

Na coluna `VERSION`, o valor esperado é `2`, indicando que a distribuição utiliza WSL 2.

**E se aparecer a versão 1?**

Caso a coluna `VERSION` apresente o valor `1`, abra o PowerShell e execute:

```powershell
wsl --set-version Ubuntu 2
```

Esse comando converte a distribuição Ubuntu para WSL 2.

Caso o nome da distribuição seja diferente de `Ubuntu`, utilize exatamente o nome apresentado pelo comando `wsl --list --verbose`.

Aguarde a conclusão da conversão e execute novamente `wsl --list --verbose` para confirmar a versão.



### 1.2. Instalação do Git

O Git é uma ferramenta utilizada para controlar versões do código-fonte e obter projetos disponibilizados em repositórios como o GitHub.

Neste projeto, utilizaremos o Git para baixar o código-fonte da Financial API.

Escolha o procedimento correspondente ao sistema operacional utilizado.

**Windows 10 ou Windows 11**

Abra o Windows PowerShell e execute:

```powershell
winget install --id Git.Git -e --source winget
```

Aguarde a conclusão da instalação. Feche e abra novamente o PowerShell para que o Git fique disponível no terminal.

**Alternativa: utilizar o instalador gráfico**

Caso o comando `winget` não esteja disponível no computador, acesse a página oficial de instalação do Git para Windows:

https://git-scm.com/install/windows

Baixe o instalador correspondente ao seu computador, execute o arquivo e siga as instruções apresentadas pelo assistente de instalação.

Ao concluir, feche e abra novamente o PowerShell.


**Ubuntu ou Ubuntu pelo WSL**

Abra o terminal e execute:

```bash
sudo apt update
sudo apt install -y git
```

**Verificar a instalação**

Em qualquer um dos sistemas operacionais, execute:

```bash
git --version
```

O resultado deverá apresentar a versão instalada do Git.

**Resultado esperado:** Git instalado e disponível no terminal para baixar o código-fonte da Financial API.

Referência oficial: https://git-scm.com/install


### 1.3. Baixar o código-fonte da Financial API

Com o Git instalado, vamos obter uma cópia do código-fonte disponibilizado no GitHub.

**Etapa 1 — Abrir o terminal**

Abra o PowerShell no Windows ou o terminal do Ubuntu/Linux.

**Etapa 2 — Escolher a pasta de destino**

Acesse a pasta onde deseja guardar o projeto. Por exemplo, para utilizar a pasta pessoal do usuário, execute:

Windows — PowerShell:

```powershell
cd $HOME
```

Ubuntu/Linux:

```bash
cd ~
```

**Etapa 3 — Baixar o projeto**

Execute o comando:

Windows — PowerShell

```powershell
git clone https://github.com/gmorenos-dev/financial-api.git
```

Ubuntu/Linux

```bash
git clone https://github.com/gmorenos-dev/financial-api.git
```

O Git criará uma pasta chamada `financial-api` contendo os arquivos do projeto.

**Etapa 4 — Entrar na pasta do projeto**

Execute:

Windows — PowerShell

```powershell
cd financial-api
```

Ubuntu/Linux

```bash
cd financial-api
```

Mantenha essa pasta como referência para os próximos passos, pois será nela que executaremos os comandos do Maven e iniciaremos a aplicação.

**Resultado esperado:** código-fonte da Financial API baixado do GitHub e disponível na pasta `financial-api` do computador.


## 2. Instalação do Java JDK 25

Escolha o procedimento correspondente ao sistema operacional utilizado.

### 2.1. Windows 10 ou Windows 11

A instalação pode ser feita pelo gerenciador de pacotes do Windows ou pelo instalador gráfico.

**Método recomendado: Winget**

Abra o Windows PowerShell e execute:

```powershell
winget install EclipseAdoptium.Temurin.25.JDK
```

Aguarde a conclusão da instalação.

Caso prefira utilizar um instalador gráfico, acesse a página oficial do Eclipse Temurin:

https://adoptium.net/installation/windows/

Baixe o instalador MSI correspondente ao JDK 25 para Windows 64 bits.

Durante a instalação, certifique-se de habilitar as opções:

- Adicionar Java à variável de ambiente PATH.
- Atualizar a variável de ambiente JAVA_HOME.

Concluída a instalação, feche e abra novamente o terminal.

### 2.2. Ubuntu ou Ubuntu pelo WSL

Abra o terminal do Ubuntu.

**Etapa 1 — Atualizar os pacotes**

```bash
sudo apt update
```

**Etapa 2 — Instalar os pacotes necessários**

```bash
sudo apt install -y wget apt-transport-https gpg
```

**Etapa 3 — Adicionar a chave de segurança do repositório Eclipse Adoptium**

```bash
wget -qO - https://packages.adoptium.net/artifactory/api/gpg/key/public | gpg --dearmor | sudo tee /etc/apt/trusted.gpg.d/adoptium.gpg > /dev/null
```

**Etapa 4 — Adicionar o repositório do Eclipse Adoptium**

```bash
echo "deb https://packages.adoptium.net/artifactory/deb $(awk -F= '/^VERSION_CODENAME/{print$2}' /etc/os-release) main" | sudo tee /etc/apt/sources.list.d/adoptium.list
```

**Etapa 5 — Atualizar a lista de pacotes**

```bash
sudo apt update
```

**Etapa 6 — Instalar o Java JDK 25**

```bash
sudo apt install -y temurin-25-jdk
```

## 3. Verificar a instalação do Java

Após concluir a instalação, abra um novo terminal.

Execute:

```bash
java -version
```

Em seguida:

```bash
javac -version
```

Os dois comandos devem apresentar a versão 25.

O primeiro verifica o ambiente de execução do Java. O segundo verifica o compilador Java, incluído no JDK.

**Resultado esperado:** Java 25 instalado e disponível no terminal.

## 4. Instalação do PostgreSQL 17

O PostgreSQL é o banco de dados utilizado pela Financial API para armazenar as transações financeiras.

O projeto utiliza PostgreSQL 17. Portanto, recomendamos instalar essa versão principal para manter a compatibilidade com o ambiente de desenvolvimento.

Escolha o procedimento correspondente ao seu sistema operacional.

### 4.1. Windows 10 ou Windows 11

A instalação pode ser realizada por meio do instalador gráfico disponibilizado pela EDB, empresa que fornece os instaladores indicados pelo site oficial do PostgreSQL.

**Etapa 1 — Baixar o instalador**

Acesse:

https://www.postgresql.org/download/windows/

Localize a versão PostgreSQL 17 e clique no link para baixar o instalador interativo.

**Etapa 2 — Executar o instalador**

Abra o arquivo baixado e siga as instruções do assistente de instalação.

Na seleção dos componentes, recomendamos manter:

- PostgreSQL Server: servidor do banco de dados.
- pgAdmin 4: ferramenta gráfica para administrar o banco.
- Command Line Tools: ferramentas de linha de comando, incluindo o cliente `psql`.

O Stack Builder é opcional e não é necessário para executar a Financial API.

**Etapa 3 — Configurar a senha do administrador**

Durante a instalação, será solicitada uma senha para o usuário administrador `postgres`.

Escolha uma senha forte e guarde-a em local seguro.

Essa senha será utilizada para administrar o PostgreSQL. Posteriormente, criaremos um usuário específico para a Financial API.

**Etapa 4 — Configurar a porta**

Mantenha a porta padrão `5432`.

**Atenção:** os comandos apresentados nas próximas etapas deste guia consideram que o PostgreSQL está utilizando a porta `5432`. Caso seja necessário configurar outra porta, utilize o número escolhido nas demais configurações de conexão, inclusive nos comandos `psql` e na variável de ambiente `DB_URL`.
Conclua a instalação e aguarde o assistente finalizar.

### 4.2. Ubuntu ou Ubuntu pelo WSL

No Ubuntu, utilizaremos o repositório oficial de pacotes mantido pelo projeto PostgreSQL (PGDG). Isso permite instalar especificamente a versão 17, sem depender da versão padrão disponibilizada pela distribuição Linux.

**Verificar a compatibilidade da versão do Ubuntu**

O repositório PGDG disponibiliza pacotes apenas para versões específicas do Ubuntu.

Para verificar a versão instalada, execute:

```bash
grep '^VERSION_CODENAME=' /etc/os-release
```

Na data desta revisão, as versões suportadas incluem Ubuntu 22.04 (jammy), 24.04 (noble) e 26.04 (resolute). A versão 26.10 (stonking) também é suportada para arquitetura `amd64`.

Consulte a relação atualizada de versões e arquiteturas suportadas antes de prosseguir:

https://wiki.postgresql.org/wiki/Apt

Caso sua distribuição não esteja na lista de versões suportadas, não prossiga com a configuração do repositório sem consultar as orientações oficiais.


**Etapa 1 — Atualizar os pacotes**

```bash
sudo apt update
```

**Etapa 2 — Instalar os componentes necessários**

```bash
sudo apt install -y postgresql-common ca-certificates
```

**Etapa 3 — Configurar o repositório oficial do PostgreSQL**

```bash
sudo /usr/share/postgresql-common/pgdg/apt.postgresql.org.sh
```

Siga as orientações apresentadas pelo instalador do repositório.

**Etapa 4 — Atualizar a lista de pacotes**

```bash
sudo apt update
```

**Etapa 5 — Instalar PostgreSQL 17**

```bash
sudo apt install -y postgresql-17 postgresql-client-17
```

O pacote `postgresql-17` instala o servidor do banco de dados. O pacote `postgresql-client-17` instala as ferramentas para conexão e administração pelo terminal.

**Etapa 6 — Iniciar o serviço**

```bash
sudo service postgresql start
```

No Ubuntu com WSL, pode ser necessário iniciar o serviço manualmente quando a distribuição for aberta.

**Etapa 7 — Verificar a porta utilizada pelo PostgreSQL**

Após iniciar o serviço, execute:

```bash
pg_lsclusters
```

Esse comando apresenta os clusters PostgreSQL instalados, suas versões, portas e situação de funcionamento.

O resultado será semelhante a:

```text
Ver Cluster Port Status Owner
17  main    5432 online postgres
```

Verifique a coluna `Port` correspondente à versão 17.


Verifique também a coluna `Status`. O valor esperado é `online`, indicando que o cluster está em execução.

Caso apareça `down`, o servidor não está ativo. Nesse caso, tente iniciar o serviço novamente com:

```bash
sudo service postgresql start
```

Depois, execute `pg_lsclusters` novamente para conferir o resultado.

**Atenção:** se a porta apresentada for diferente de `5432`, utilize o número identificado nos comandos de conexão `psql`, na verificação com `pg_isready` e na variável de ambiente `DB_URL`, nas próximas etapas deste guia.

### 4.3. Conhecendo o pgAdmin 4

O pgAdmin 4 é uma ferramenta gráfica que permite administrar o PostgreSQL sem precisar executar todos os comandos pelo terminal.

Com ele, é possível:

- Visualizar bancos de dados e tabelas.
- Criar usuários e bancos de dados.
- Consultar e executar comandos SQL.
- Visualizar registros armazenados nas tabelas.
- Administrar conexões com servidores PostgreSQL.

**Windows**

O pgAdmin 4 é disponibilizado como componente do instalador do PostgreSQL para Windows. Caso tenha sido selecionado durante a instalação, procure por `pgAdmin 4` no menu Iniciar.

**Ubuntu/Linux**

O pgAdmin pode ser instalado separadamente. Consulte as instruções oficiais para instalar a versão Desktop:

https://www.pgadmin.org/download/pgadmin-4-apt/


**Verificar a compatibilidade antes da instalação**

Antes de instalar, consulte a tabela de plataformas suportadas na página oficial e confirme se sua versão do Ubuntu e a arquitetura do computador estão relacionadas.

Na data desta revisão, o repositório APT disponibiliza pacotes para Ubuntu 22.04, 24.04 e 26.04, na arquitetura `x86_64`. Outras versões podem ainda não possuir pacotes disponíveis.

Caso sua distribuição não esteja na lista, não prossiga com a configuração desse repositório.


O pgAdmin é uma ferramenta opcional. Não é necessário instalá-lo para executar a Financial API, pois as operações também podem ser realizadas pelo terminal utilizando o cliente `psql`.

### 4.4. Verificar a instalação do PostgreSQL

Após concluir a instalação, precisamos confirmar que o PostgreSQL está instalado e que o serviço está funcionando.

**Windows**

Abra o PowerShell e execute:

```powershell
psql --version
```

O resultado deverá indicar a versão 17 do cliente `psql` instalado.

**Observação:** esse comando verifica a versão da ferramenta de conexão, não necessariamente a versão do servidor PostgreSQL. A confirmação da versão do servidor será realizada quando estabelecermos a conexão na próxima etapa.

Caso o Windows não reconheça o comando `psql`, abra o aplicativo `SQL Shell (psql)` pelo menu Iniciar para realizar a conexão.

**Ubuntu/Linux**

Abra o terminal e execute:

```bash
psql --version
```

O resultado deverá indicar a versão 17 do cliente `psql` instalado.

**Observação:** esse comando verifica a versão da ferramenta de conexão, não necessariamente a versão do servidor PostgreSQL.
A confirmação da versão do servidor será realizada mais adiante nesta seção, por meio de uma consulta SQL.

Em seguida, verifique se o servidor está aceitando conexões:

Utilize a porta identificada na coluna `Port` do PostgreSQL 17 na seção 4.2.

Os dois comandos abaixo utilizam a porta `5432` como exemplo. Caso seu PostgreSQL esteja utilizando outra porta, substitua o número nos dois comandos.

Utilize essa mesma porta também nas demais conexões com o PostgreSQL e na configuração da variável `DB_URL` das próximas seções.

```bash
pg_isready -h localhost -p 5432
```

O resultado esperado é semelhante a:

```text
localhost:5432 - accepting connections
```

Para consultar a versão do servidor instalado:

```bash
sudo -u postgres psql -p 5432 -c "SELECT version();"
```

O resultado deverá apresentar a versão 17 do PostgreSQL.

**Resultado esperado:** PostgreSQL 17 instalado e funcionando, pronto para receber a configuração do banco utilizado pela Financial API.

Na próxima etapa, vamos criar o usuário específico da aplicação e o banco de dados `financial_api`.

## 5. Criar o usuário e o banco de dados da Financial API

Com o PostgreSQL instalado e funcionando, precisamos criar um banco de dados e um usuário específico para a aplicação.

Neste projeto, utilizaremos:

| Configuração | Valor |
|---|---|
| Banco de dados | `financial_api` |
| Usuário | `financial_api_user` |
| Porta padrão | `5432` |

**Por que criar um usuário específico?**

O PostgreSQL possui um usuário administrador chamado `postgres`, utilizado para administrar o servidor e criar bancos e usuários.

A Financial API utilizará uma conta própria, chamada `financial_api_user`. Essa conta será proprietária do banco `financial_api` e não precisará ter privilégios de superusuário.

### 5.1. Windows 10 ou Windows 11

**Etapa 1 — Abrir o SQL Shell (psql)**

Procure por `SQL Shell (psql)` no menu Iniciar do Windows e abra o aplicativo.

O SQL Shell é um terminal que permite executar comandos no PostgreSQL.

**Etapa 2 — Conectar como administrador**

O aplicativo solicitará algumas informações. Utilize os valores abaixo:

| Pergunta | Valor |
|---|---|
| Server | `localhost` |
| Database | `postgres` |
| Port | `5432` |
| Username | `postgres` |
| Password | Senha definida durante a instalação |

Pressione Enter para aceitar os valores padrão quando estiverem corretos. Na solicitação da senha, digite a senha do administrador `postgres`.

Ao conectar, aparecerá um prompt semelhante a:

```text
postgres=#
```

Isso indica que a conexão foi estabelecida.


**Etapa 3 — Confirmar a versão do servidor**

Antes de criar o usuário da aplicação, execute:

```sql
SELECT version();
```

O resultado deverá apresentar informações sobre o servidor PostgreSQL instalado, incluindo a versão 17.

**Atenção:** caso o resultado apresente uma versão diferente de 17, não prossiga com a criação do usuário e do banco de dados.

Isso pode indicar que a conexão foi estabelecida com outra instalação do PostgreSQL existente no computador.

Verifique a porta configurada durante a instalação do PostgreSQL 17, conforme explicado na seção 4.1, e estabeleça novamente a conexão utilizando essa porta.

Somente continue quando confirmar que está conectado ao PostgreSQL 17.


Após confirmar a versão, continue com a criação do usuário da aplicação.


**Etapa 4 — Criar o usuário da aplicação**

Execute:

```sql
CREATE ROLE financial_api_user WITH LOGIN;
```

Esse comando cria um usuário que poderá se autenticar no PostgreSQL.

**Etapa 5 — Definir a senha do usuário**

Execute:

```text
\password financial_api_user
```

O PostgreSQL solicitará a nova senha duas vezes.

Escolha uma senha forte e guarde-a em local seguro. Essa será a senha utilizada na configuração `DB_PASSWORD` da Financial API.

O comando `\password` evita colocar a senha diretamente no comando SQL.

**Etapa 6 — Criar o banco de dados**

Execute:

```sql
CREATE DATABASE financial_api OWNER financial_api_user;
```

Esse comando cria o banco `financial_api` e define `financial_api_user` como seu proprietário.

Como o usuário é proprietário do banco, ele poderá criar e administrar os objetos necessários à aplicação, sem precisar ser um superusuário.

**Etapa 7 — Encerrar a sessão**

Execute:

```text
\q
```

O comando encerra a sessão do PostgreSQL.

### 5.2. Ubuntu ou Ubuntu pelo WSL

**Etapa 1 — Acessar o PostgreSQL como administrador**

Abra o terminal e execute:

```bash
sudo -u postgres psql -p 5432
```

Esse comando abre uma sessão do PostgreSQL utilizando o usuário administrador `postgres`.

**Etapa 2 — Criar o usuário da aplicação**

Dentro do PostgreSQL, execute:

```sql
CREATE ROLE financial_api_user WITH LOGIN;
```

**Etapa 3 — Definir a senha do usuário**

Execute:

```text
\password financial_api_user
```

Digite a senha escolhida e confirme quando solicitado.

**Etapa 4 — Criar o banco de dados**

Execute:

```sql
CREATE DATABASE financial_api OWNER financial_api_user;
```

**Etapa 5 — Encerrar a sessão**

```text
\q
```

### 5.3. Verificar a conexão com o banco de dados

Após criar o usuário e o banco, vamos verificar se a nova conta consegue se conectar.

**Windows**

Abra novamente o `SQL Shell (psql)`.

Informe:

| Pergunta | Valor |
|---|---|
| Server | `localhost` |
| Database | `financial_api` |
| Port | `5432` |
| Username | `financial_api_user` |
| Password | Senha criada para `financial_api_user` |

**Ubuntu/Linux**

No terminal, execute:

```bash
psql -h localhost -p 5432 -U financial_api_user -d financial_api -W
```

Digite a senha quando solicitado.

**Etapa final — Confirmar a conexão**

Após conectar, execute:

```sql
SELECT current_database(), current_user;
```

O resultado esperado é:

```text
 current_database |    current_user
------------------+--------------------
 financial_api    | financial_api_user
```

Para encerrar a conexão:

```text
\q
```

**Resultado esperado:** banco `financial_api` criado, usuário `financial_api_user` configurado e autenticação validada.


### 5.4. Verificar as permissões para as migrações

Além de conseguir conectar ao banco de dados, o usuário da aplicação precisa ter as seguintes permissões:

- `CONNECT`: conectar ao banco de dados.
- `USAGE`: acessar o schema `public`.
- `CREATE`: criar tabelas e outros objetos nesse schema.

Essas permissões são necessárias porque o Flyway utiliza uma tabela chamada `flyway_schema_history` para registrar as migrações executadas. Ele precisa criar essa tabela e executar as alterações estruturais no banco.

**Por que definir o usuário como proprietário do banco?**

A partir do PostgreSQL 15, em bancos novos, o schema `public` passou a utilizar um papel especial chamado `pg_database_owner` como proprietário. Esse papel representa o proprietário do banco de dados atual.

Além disso, a permissão `CREATE` no schema `public` deixou de ser concedida automaticamente a todos os usuários.

Por esse motivo, neste projeto, criamos o banco com `financial_api_user` como proprietário. Assim, essa conta recebe os privilégios necessários para trabalhar no schema `public` e executar as migrações do Flyway, sem precisar utilizar uma conta administradora.

**Observação:** esse comportamento corresponde à configuração padrão de bancos novos. Bancos existentes, atualizados ou restaurados podem apresentar permissões diferentes. Por isso, realizamos a verificação a seguir.

Para verificar as permissões, abra uma **nova conexão com o banco de dados**, utilizando o usuário `financial_api_user` em vez do usuário administrador `postgres`.

**Windows**

Abra o aplicativo **SQL Shell (psql)**. Preencha os campos conforme o exemplo:

```text
Server [localhost]: pressione Enter
Database [postgres]: financial_api
Port [5432]: pressione Enter
Username [postgres]: financial_api_user
Password for user financial_api_user: digite a senha e pressione Enter
```
Nos campos `Server` e `Port`, pressione Enter para aceitar os valores padrão. Nos demais, informe os valores indicados. Quando solicitado, digite a senha do usuário `financial_api_user` (os caracteres não serão exibidos na tela).

**Ubuntu/Linux**

Abra o terminal e execute:

```bash
psql -h localhost -p 5432 -U financial_api_user -d financial_api -W
```

Quando solicitado, informe a senha definida para o usuário `financial_api_user`.

Após estabelecer a conexão, execute a consulta SQL a seguir para verificar as permissões:

```sql id="jv893m"
SELECT
    current_user,
    current_database(),
    has_database_privilege(current_user, current_database(), 'CONNECT') AS can_connect,
    has_schema_privilege(current_user, 'public', 'USAGE') AS can_use_public,
    has_schema_privilege(current_user, 'public', 'CREATE') AS can_create_in_public;
```

O resultado esperado é:

| Verificação | Resultado esperado |
|---|---|
| `current_user` | `financial_api_user` |
| `current_database` | `financial_api` |
| `can_connect` | `t` |
| `can_use_public` | `t` |
| `can_create_in_public` | `t` |

**Observação:** o PostgreSQL apresenta os valores booleanos como `t` (verdadeiro) e `f` (falso) no terminal `psql`.


**E se alguma permissão apresentar `f`?**

Caso alguma das verificações retorne `f`, não prossiga para a inicialização da aplicação.

Confira se a conexão foi realizada utilizando o banco `financial_api` e o usuário `financial_api_user`, na instância correta do PostgreSQL.

Se os dados da conexão estiverem corretos e a permissão continuar ausente, será necessário verificar as configurações de propriedade e privilégios do banco de dados antes de continuar.

**O que significa cada permissão?**

- `can_connect`: permite conectar ao banco de dados.
- `can_use_public`: permite acessar objetos existentes no schema `public`.
- `can_create_in_public`: permite criar tabelas e outros objetos nesse schema.

**Resultado esperado:** usuário `financial_api_user` conectado ao banco `financial_api`, com as permissões necessárias para a execução das migrações.

Para encerrar a conexão com o PostgreSQL, execute:

```text
\q
```
O próximo passo será configurar as variáveis de ambiente `DB_URL` e `DB_PASSWORD`, necessárias para iniciar a aplicação.


## 6. Configurar as variáveis de ambiente

A Financial API utiliza duas variáveis de ambiente para estabelecer a conexão com o PostgreSQL:

| Variável | Finalidade |
|---|---|
| `DB_URL` | Informa o endereço, a porta e o nome do banco de dados |
| `DB_PASSWORD` | Informa a senha do usuário `financial_api_user` |

Essas variáveis são lidas pela aplicação durante sua inicialização.

A configuração pode ser realizada diretamente no terminal, sem precisar criar ou modificar um arquivo `.env`.

### 6.1. Windows — PowerShell

Abra o Windows PowerShell e navegue até a pasta do projeto.

Caso tenha seguido o exemplo da seção 1.3 e baixado o projeto na pasta pessoal do usuário, execute:

```powershell
cd "$HOME\financial-api"
```

Caso tenha escolhido outra pasta para baixar o projeto, utilize o caminho correspondente.

**Etapa 1 — Configurar o endereço do banco**

Se o PostgreSQL estiver instalado no próprio Windows, utilizando a porta padrão `5432`, execute:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/financial_api"
```

**Etapa 2 — Informar a senha sem exibi-la na tela**

Execute:

```powershell
$securePassword = Read-Host "Senha do banco" -AsSecureString
$env:DB_PASSWORD = [System.Net.NetworkCredential]::new("", $securePassword).Password
```

O PowerShell solicitará a senha sem mostrar os caracteres digitados.

A senha será disponibilizada na variável de ambiente `DB_PASSWORD`.

**Etapa 3 — Verificar as configurações**

```powershell
if ($env:DB_PASSWORD) {
    Write-Host "DB_PASSWORD está definida"
} else {
    Write-Host "DB_PASSWORD NÃO está definida"
}

Write-Host $env:DB_URL
```

A senha não será exibida no terminal.

### 6.2. Ubuntu ou Ubuntu pelo WSL

Abra o terminal do Ubuntu e navegue até a pasta do projeto.

Caso tenha seguido o exemplo da seção 1.3 e baixado o projeto na pasta pessoal do usuário, execute:

```bash
cd ~/financial-api
```

Caso tenha escolhido outra pasta para baixar o projeto, utilize o caminho correspondente.

**Etapa 1 — Configurar o endereço do banco**

Para simplificar a configuração, recomendamos instalar o PostgreSQL na mesma distribuição Ubuntu em que a aplicação Java será executada.

Quando Java e PostgreSQL estão instalados no mesmo ambiente Ubuntu, utilize:

```bash
export DB_URL="jdbc:postgresql://localhost:5432/financial_api"
```

**E se o PostgreSQL estiver instalado no Windows e a aplicação no WSL?**

Nesse cenário, a conexão exige configurações adicionais no PostgreSQL do Windows, incluindo a permissão para receber conexões externas, as regras de autenticação e o firewall do Windows.

Essa configuração é mais avançada e não faz parte do procedimento básico deste tutorial.

Consulte a documentação oficial da Microsoft sobre [acesso a aplicativos de rede entre Windows e WSL](https://learn.microsoft.com/pt-br/windows/wsl/networking).

Para quem está começando, recomendamos manter o Java e o PostgreSQL no mesmo ambiente operacional.

**Etapa 2 — Informar a senha sem exibi-la na tela**

Execute:

```bash
read -r -s -p "Senha do banco: " DB_PASSWORD
echo
export DB_PASSWORD
```

O parâmetro `-s` faz com que os caracteres digitados não sejam exibidos.

**Etapa 3 — Verificar as configurações**

```bash
if [ -n "$DB_PASSWORD" ]; then
    echo "DB_PASSWORD está definida"
else
    echo "DB_PASSWORD NÃO está definida"
fi

echo "$DB_URL"
```

A senha não será exibida no terminal.

### 6.3. Entendendo como as variáveis funcionam

As variáveis de ambiente são configurações disponibilizadas pelo sistema operacional aos processos iniciados por ele.

Quando executamos:

```bash
export DB_PASSWORD
```

A senha fica disponível para os programas iniciados a partir daquela sessão do terminal, incluindo o Maven e a aplicação Java.

**Importante:**

- As variáveis configuradas no terminal não são gravadas no arquivo `.env`.
- A senha permanece disponível na sessão até ser removida ou até o encerramento do ambiente.
- Para utilizar a configuração, inicie o Maven no mesmo terminal em que as variáveis foram definidas.
- Se utilizar o IntelliJ IDEA para iniciar a aplicação, configure as variáveis também na configuração de execução (*Run Configuration*).

Para remover as variáveis ao finalizar o trabalho:

**PowerShell:**

```powershell
Remove-Item Env:DB_PASSWORD
Remove-Item Env:DB_URL
```

**Ubuntu/Linux:**

```bash
unset DB_PASSWORD DB_URL
```

**Resultado esperado:** as variáveis `DB_URL` e `DB_PASSWORD` configuradas corretamente no ambiente, prontas para serem utilizadas pela Financial API.

## 7. Executar a Financial API

Após instalar o Java e o PostgreSQL, criar o banco de dados e configurar as variáveis de ambiente, podemos iniciar a aplicação.

Antes de executar, confirme:

- Java JDK 25 instalado.
- PostgreSQL em execução.
- Banco `financial_api` criado.
- Usuário `financial_api_user` configurado.
- Variáveis `DB_URL` e `DB_PASSWORD` definidas no terminal.

**Importante:** as variáveis de ambiente devem estar configuradas no mesmo terminal utilizado para iniciar a aplicação.

### 7.1. Windows — PowerShell

Utilize o mesmo PowerShell que foi utilizado para configurar as variáveis de ambiente na seção 6.1. Não abra uma nova sessão, pois as variáveis DB_URL e DB_PASSWORD precisam continuar disponíveis.

Execute:

```powershell id="w68x3r"
.\mvnw.cmd spring-boot:run
```

O Maven Wrapper iniciará a aplicação Spring Boot.

Na primeira execução, o Maven poderá baixar as dependências necessárias. Aguarde a conclusão da inicialização.

### 7.2. Ubuntu ou Ubuntu pelo WSL

Utilize o mesmo terminal do Ubuntu que foi utilizado para configurar as variáveis de ambiente na seção 6.2. Não abra uma nova sessão, pois as variáveis DB_URL e DB_PASSWORD precisam continuar disponíveis.

Execute:

```bash id="w68x3r"
./mvnw spring-boot:run
```

O Maven Wrapper iniciará a aplicação Spring Boot.

Na primeira execução, o Maven poderá baixar as dependências necessárias. Aguarde a conclusão da inicialização.

### 7.3. Verificar a inicialização

Durante a inicialização, observe as mensagens apresentadas no terminal.

O Flyway será responsável por verificar o histórico de migrações do banco e aplicar as alterações estruturais pendentes.

**O que acontece na primeira inicialização?**

Quando a Financial API é iniciada pela primeira vez em um banco de dados novo, o Flyway executa automaticamente as migrações SQL disponíveis no projeto.

O processo funciona da seguinte maneira:

1. A aplicação estabelece conexão com o PostgreSQL.
2. O Flyway verifica o histórico de migrações do banco.
3. Se necessário, cria a tabela `flyway_schema_history`, utilizada para controlar as alterações executadas.
4. Executa as migrações pendentes, em ordem de versão.
5. Após a execução das migrações pendentes, o Hibernate verifica se a estrutura existente do banco de dados é compatível com o mapeamento das entidades Java. O projeto utiliza a configuração `spring.jpa.hibernate.ddl-auto=validate`, que determina que o Hibernate apenas realize essa validação, sem criar, alterar ou excluir tabelas. As alterações estruturais são executadas exclusivamente pelas migrações versionadas do Flyway.


Atualmente, o projeto possui duas migrações:

| Migração | Finalidade |
|---|---|
| `V1__create_transaction_table.sql` | Cria a tabela de transações e a restrição dos tipos aceitos. |
| `V2__ensure_transaction_type_constraint.sql` | Garante a existência da restrição `transaction_type_check`. |

**E nas próximas inicializações?**

O Flyway consulta seu histórico e identifica quais migrações já foram executadas. Se não houver alterações pendentes, nenhuma migração será executada novamente.

**Importante:** nunca altere um arquivo de migração que já tenha sido aplicado em um banco de dados. Isso pode provocar divergência de checksum e impedir a inicialização da aplicação.

Para adicionar uma nova alteração estrutural, crie sempre um novo arquivo versionado, como `V3__descricao_da_alteracao.sql`.

**Não é necessário criar tabelas manualmente nem executar os arquivos SQL por conta própria.** O Flyway realiza esse trabalho durante a inicialização da aplicação.

Quando a inicialização for concluída, procure a mensagem:

```text id="7qj3zr"
Started FinancialApiApplication
```

Essa mensagem indica que a aplicação Spring Boot foi iniciada.

A porta padrão utilizada pela aplicação é `8080`.

### 7.4. Acessar a aplicação

Abra o navegador e utilize os seguintes endereços:

| Recurso | Endereço |
|---|---|
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |
| Listagem de transações | http://localhost:8080/api/transactions |

O Swagger UI permite consultar os endpoints, visualizar os parâmetros, verificar exemplos de requisições e executar operações diretamente pelo navegador.

### 7.5. Realizar o primeiro teste

Com a aplicação em execução, abra:

http://localhost:8080/api/transactions

A API deverá retornar uma resposta HTTP `200 OK`.

Caso o banco seja novo e ainda não possua transações, a resposta será semelhante a:

```json id="eub43p"
{
  "content": [],
  "page": 0,
  "size": 10,
  "totalElements": 0,
  "totalPages": 0
}
```

Esse resultado indica que a aplicação conseguiu consultar o banco de dados, mesmo sem existirem transações cadastradas.

Para experimentar a aplicação, acesse o Swagger UI e utilize o endpoint `POST /api/transactions` para cadastrar sua primeira transação.

### 7.6. Encerrar a aplicação

Para encerrar a aplicação, volte ao terminal onde ela está sendo executada e pressione:

```text id="ptvk2n"
Ctrl+C
```

Isso encerra a aplicação Java, mas não desliga o PostgreSQL nem exclui os dados armazenados no banco.

Depois de encerrar a aplicação, as variáveis de ambiente podem ser removidas da sessão do terminal.

**Windows — PowerShell:**

```powershell id="vd9p8j"
Remove-Item Env:DB_PASSWORD
Remove-Item Env:DB_URL
```

**Ubuntu/Linux:**

```bash id="2qv8n4"
unset DB_PASSWORD DB_URL
```

### 7.7. Problema comum: porta 8080 já está em uso

Durante a inicialização, pode ocorrer o seguinte erro:

```text
Web server failed to start. Port 8080 was already in use.
```

Isso significa que outro processo ou aplicação já está utilizando a porta `8080`, que é a porta padrão da Financial API.

Existem duas alternativas: identificar e encerrar a aplicação que está utilizando a porta ou executar a Financial API em outra porta.

**Solução recomendada para iniciantes: utilizar outra porta.**

Neste exemplo, utilizaremos a porta `8081`.

**Importante:** antes de executar novamente a aplicação, verifique se as variáveis `DB_URL` e `DB_PASSWORD` continuam definidas no terminal.

Caso já tenha seguido a seção 7.6 e removido essas variáveis, configure-as novamente conforme as instruções da seção 6.1 (Windows) ou 6.2 (Ubuntu/Linux).

Depois de configurar as variáveis, execute os comandos abaixo no mesmo terminal.


**Windows — PowerShell**

Execute:

```powershell
$env:SERVER_PORT = "8081"
.\mvnw.cmd spring-boot:run
```

**Ubuntu/Linux — Terminal**

Execute:

```bash
SERVER_PORT=8081 ./mvnw spring-boot:run
```

A variável `SERVER_PORT` informa ao Spring Boot qual porta deverá utilizar nessa execução.

Com a aplicação iniciada, acesse:

- **Swagger UI:** http://localhost:8081/swagger-ui/index.html
- **OpenAPI JSON:** http://localhost:8081/v3/api-docs
- **API:** http://localhost:8081/api/transactions

Ao utilizar a porta `8081`, a aplicação não precisa liberar a porta `8080`.

No PowerShell, após encerrar a aplicação, remova a variável configurada:

```powershell
Remove-Item Env:SERVER_PORT
```

No Ubuntu/Linux, não é necessário remover a variável, pois ela foi definida somente para o comando executado.

**Resultado esperado:** aplicação iniciada na porta alternativa `8081`, sem interferir no processo que já utiliza a porta `8080`.

## Referências oficiais

As instruções deste guia utilizam como referência a documentação oficial das ferramentas.

### Java — Eclipse Temurin 25

- [Instalação do Java Temurin](https://adoptium.net/pt-BR/installation)
- [Instalação no Windows](https://adoptium.net/installation/windows/)
- [Instalação no Ubuntu/Linux](https://adoptium.net/pt-BR/installation/linux)

### PostgreSQL 17

- [Página oficial de downloads do PostgreSQL](https://www.postgresql.org/download/)
- [Instalação no Windows](https://www.postgresql.org/download/windows/)
- [Instalação no Ubuntu](https://www.postgresql.org/download/linux/ubuntu/)
- [Documentação oficial do PostgreSQL 17](https://www.postgresql.org/docs/17/)

### pgAdmin 4

- [Site oficial do pgAdmin](https://www.pgadmin.org/)
- [Download e instalação no Windows](https://www.pgadmin.org/download/)
- [Instalação no Ubuntu/Linux](https://www.pgadmin.org/download/pgadmin-4-apt/)

### Windows Subsystem for Linux (WSL)

- [Documentação oficial do WSL](https://learn.microsoft.com/pt-br/windows/wsl/)
- [Configuração de rede e acesso entre Windows e WSL](https://learn.microsoft.com/pt-br/windows/wsl/networking)

### Git

- [Site oficial do Git](https://git-scm.com/)
- [Instalação do Git](https://git-scm.com/install)

### Spring Boot

- [Documentação oficial do Spring Boot](https://docs.spring.io/spring-boot/)
- [Spring Boot 4.1.1 — Documentação de referência](https://docs.spring.io/spring-boot/4.1.1/reference/html/)

### Flyway

- [Documentação oficial do Flyway](https://documentation.red-gate.com/fd)
