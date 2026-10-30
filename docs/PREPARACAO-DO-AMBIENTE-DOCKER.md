# Preparação do Ambiente — Docker | Financial API

Este guia complementa o [Guia de Preparação do Ambiente — Java e Maven](PREPARACAO-DO-AMBIENTE.md) e apresenta como preparar o Docker para executar a Financial API pelas opções 2 e 3 do README.

O objetivo é orientar pessoas que ainda não possuem experiência com Docker, desde a instalação até a inicialização, consulta e encerramento dos contêineres.

## 1. Entendendo as ferramentas e escolhendo o ambiente

Para executar a Financial API com Docker, precisamos de:

| Ferramenta | Finalidade |
|---|---|
| Git | Obter os arquivos do projeto e as configurações do Docker |
| Docker Desktop ou Docker Engine | Construir imagens e executar contêineres |
| Docker Compose | Gerenciar os serviços da API e do PostgreSQL em conjunto |

Não é necessário instalar Java, Maven ou PostgreSQL diretamente no computador. Na opção 2, a compilação Java ocorre dentro de uma etapa da construção da imagem Docker. Na opção 3, utilizamos uma imagem que já foi construída e publicada no GitHub Container Registry (GHCR).

**Qual opção do README devo utilizar?**

- **Opção 2 — Docker Compose:** constrói a imagem da Financial API a partir do código-fonte e inicia a aplicação e o PostgreSQL em contêineres separados.
- **Opção 3 — Imagem publicada no GHCR:** utiliza uma imagem pronta da aplicação, sem compilar o código-fonte no computador. O PostgreSQL continua sendo executado em um contêiner local.

**Escolha do ambiente Docker**

- **Windows:** recomendamos instalar o Docker Desktop e utilizar o mecanismo baseado em WSL 2.
- **Ubuntu pelo WSL:** instale o Docker Desktop no Windows e habilite a integração com a distribuição Ubuntu. Assim, os comandos Docker também poderão ser executados no terminal do Ubuntu.
- **Ubuntu instalado diretamente no computador:** instale o Docker Engine e o plugin Docker Compose dentro do próprio Ubuntu.

**Importante:** quando utilizar Docker Desktop com integração WSL, não instale outro Docker Engine dentro da mesma distribuição Ubuntu. Manter dois mecanismos Docker pode provocar conflitos. Consulte a documentação oficial sobre a integração com WSL: https://docs.docker.com/desktop/features/wsl/

## 2. Instalação do Docker

Escolha o procedimento correspondente ao ambiente selecionado na seção anterior.

### 2.1. Windows — Docker Desktop

O Docker Desktop fornece o mecanismo Docker, a interface gráfica e o Docker Compose. Para este projeto, utilizaremos contêineres Linux com o mecanismo baseado em WSL 2.

**Requisitos importantes**

- Windows em versão ainda suportada pela Microsoft.
- WSL 2 atualizado. A documentação do Docker informa como requisito mínimo a versão 2.1.5.
- Virtualização de hardware habilitada no BIOS/UEFI.
- Pelo menos 8 GB de memória RAM.

A documentação oficial apresenta os requisitos completos e atualizados: https://docs.docker.com/desktop/setup/install/windows-install/

**Licenciamento:** o Docker Desktop é gratuito para uso pessoal, educacional, projetos open source não comerciais e pequenas empresas que atendam aos limites definidos pelo Docker. Para uso profissional em outras organizações, pode ser necessária uma assinatura paga. Consulte as condições atuais em: https://www.docker.com/pricing/

**Etapa 1 — Atualizar o WSL**

Abra o PowerShell e execute:

```powershell
wsl --update
```

Para consultar as informações da instalação, execute:

```powershell
wsl --version
```

**Etapa 2 — Baixar o Docker Desktop**

Acesse a página oficial:

https://docs.docker.com/desktop/setup/install/windows-install/

Baixe o instalador correspondente ao computador.

**Etapa 3 — Instalar o Docker Desktop**

Execute o instalador e, quando solicitado, escolha o mecanismo baseado em **WSL 2**. Siga as instruções apresentadas pelo assistente.

Ao concluir, abra o Docker Desktop pelo menu Iniciar e aceite os termos de uso para iniciar o programa.

**Etapa 4 — Verificar o mecanismo WSL 2**

No Docker Desktop, abra **Settings > General** e confirme que a opção **Use the WSL 2 based engine** está habilitada. Em instalações compatíveis, essa opção já vem habilitada por padrão.

**Etapa 5 — Habilitar a integração com Ubuntu (somente para quem utilizar WSL)**

Caso pretenda executar os comandos Docker pelo terminal do Ubuntu, abra:

**Settings > Resources > WSL Integration**

Habilite a integração para a distribuição Ubuntu utilizada e aplique as alterações.

A integração permite utilizar o Docker Desktop instalado no Windows diretamente pelo terminal Ubuntu, sem instalar outro Docker Engine dentro do WSL.

### 2.2. Ubuntu instalado diretamente no computador — Docker Engine

Este procedimento é destinado ao Ubuntu instalado diretamente no computador, e não a uma distribuição WSL que já utiliza o Docker Desktop do Windows.

**Verificar instalações anteriores do Docker**

Caso já tenha instalado Docker anteriormente nessa distribuição Ubuntu, verifique se existem pacotes de outras instalações que possam entrar em conflito com o Docker Engine oficial.

A documentação do Docker apresenta a relação dos pacotes incompatíveis e o procedimento para removê-los:

https://docs.docker.com/engine/install/ubuntu/#uninstall-old-versions

Se for a primeira instalação do Docker nesse Ubuntu, não é necessário realizar esse procedimento. Você pode avançar diretamente para a Etapa 1.

**Atenção:** antes de remover qualquer instalação anterior, verifique se existem contêineres ou dados importantes que precisam ser preservados. A remoção dos pacotes não exclui automaticamente os volumes e os dados armazenados em `/var/lib/docker`.

A instalação utiliza o repositório oficial mantido pelo Docker. Consulte a lista atualizada de versões e arquiteturas suportadas antes de começar:

https://docs.docker.com/engine/install/ubuntu/

**Etapa 1 — Atualizar os pacotes e instalar os componentes necessários**

```bash
sudo apt update
sudo apt install -y ca-certificates curl
```

**Etapa 2 — Preparar o diretório de chaves**

```bash
sudo install -m 0755 -d /etc/apt/keyrings
```

**Etapa 3 — Baixar e instalar a chave oficial do Docker**

```bash
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc
```

**Etapa 4 — Adicionar o repositório oficial**

Execute o bloco completo abaixo:

```bash
sudo tee /etc/apt/sources.list.d/docker.sources <<EOF
Types: deb
URIs: https://download.docker.com/linux/ubuntu
Suites: $(. /etc/os-release && echo "${UBUNTU_CODENAME:-$VERSION_CODENAME}")
Components: stable
Architectures: $(dpkg --print-architecture)
Signed-By: /etc/apt/keyrings/docker.asc
EOF
```

**Etapa 5 — Atualizar a lista de pacotes**

```bash
sudo apt update
```

**Etapa 6 — Instalar Docker Engine e Docker Compose**

```bash
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```

O pacote `docker-compose-plugin` instala o comando `docker compose`, necessário para executar as opções 2 e 3.

**Etapa 7 — Verificar o serviço Docker**

```bash
sudo systemctl status docker
```

O serviço deverá estar em execução. Caso esteja parado, inicie-o:

```bash
sudo systemctl start docker
```

**Etapa 8 — Permitir executar comandos Docker sem `sudo`**

Por padrão, o acesso ao mecanismo Docker no Linux é restrito ao usuário administrador. Para utilizar os comandos deste guia sem precisar acrescentar `sudo` a cada execução, adicione seu usuário ao grupo `docker`:

```bash
sudo groupadd --force docker
sudo usermod -aG docker $USER
newgrp docker
```

O comando `newgrp docker` atualiza o grupo da sessão atual. Também é possível encerrar a sessão do Ubuntu e entrar novamente para aplicar a alteração.

**Atenção:** pertencer ao grupo `docker` concede privilégios equivalentes aos de administrador (root) no computador. Faça isso somente em uma máquina na qual você confia e cujos usuários têm autorização para administrar o Docker. Consulte: https://docs.docker.com/engine/install/linux-postinstall/

### 2.3. Verificar a instalação do Docker

Com o Docker Desktop aberto no Windows ou o Docker Engine em execução no Ubuntu, abra o terminal escolhido e execute:

**Verificar a versão do Docker:**

```bash
docker --version
```

**Verificar a versão do Docker Compose:**

```bash
docker compose version
```

**Executar uma imagem de teste:**

```bash
docker run hello-world
```

Na primeira execução, o Docker baixará uma pequena imagem de teste, executará um contêiner e apresentará uma mensagem de confirmação. O processo termina automaticamente.

**Resultado esperado:** Docker em execução, Docker Compose disponível e comando `docker run` funcionando.

## 3. Obter o código-fonte e preparar as configurações

As opções 2 e 3 precisam dos arquivos `compose.yaml` e `.env.example` disponíveis na raiz do projeto. A opção 2 também utiliza o `Dockerfile` para construir a imagem localmente.

### 3.1. Obter o código-fonte

Caso ainda não tenha o projeto no computador, abra o terminal escolhido e execute:

```bash
git clone https://github.com/gmorenos-dev/financial-api.git
cd financial-api
```

Se o Git ainda não estiver instalado, consulte a seção **1.2 — Instalação do Git** deste guia.

Caso já tenha clonado o repositório, entre na pasta existente do projeto. Os comandos seguintes devem ser executados na pasta que contém o arquivo `compose.yaml`.

### 3.2. Criar o arquivo de configuração `.env`

**Antes de criar o arquivo**

Verifique se já existe um arquivo chamado `.env` na raiz do projeto.

- Caso seja a primeira configuração e o arquivo ainda não exista, prossiga com a cópia do `.env.example`.

- Caso já exista, não execute novamente os comandos de cópia, pois isso poderá substituir suas configurações anteriores. Utilize o arquivo existente e preserve a senha associada ao banco de dados.

**Atenção:** se o PostgreSQL já tiver sido inicializado anteriormente e os dados estiverem armazenados no volume `postgres_data`, mantenha a senha que foi utilizada na criação desse banco. Alterar somente o arquivo `.env` não modifica a senha armazenada no PostgreSQL.

O projeto disponibiliza o arquivo `.env.example` como modelo. Vamos criar uma cópia chamada `.env`, que será utilizada pelo Docker Compose e, na opção 3, pelo comando `docker run`.

**Windows — PowerShell**

```powershell
Copy-Item .env.example .env
```

**Ubuntu/Linux**

```bash
cp .env.example .env
```

Abra o arquivo `.env` em um editor de texto. No Windows, pode utilizar o Bloco de Notas:

```powershell
notepad .env
```

No Ubuntu, pode utilizar o editor `nano`:

```bash
nano .env
```

O arquivo contém estas configurações:

```dotenv
POSTGRES_DB=financial_api
POSTGRES_PORT=5435
DB_PASSWORD=replace_with_a_strong_password
```

Substitua `replace_with_a_strong_password` por uma senha forte escolhida por você. Mantenha o nome do banco `financial_api` e, inicialmente, a porta `5435`.

**Importante sobre a senha:** para que o mesmo arquivo funcione tanto com o Docker Compose quanto com o parâmetro `--env-file` do comando `docker run`, informe a senha no formato `DB_PASSWORD=valor`, sem aspas e sem espaços.

Recomendamos utilizar letras maiúsculas e minúsculas, números e caracteres especiais como `!`, `@`, `_`, `-` e `%`.

Evite utilizar o caractere `$`, pois ele pode ser interpretado pelo Docker Compose como uma referência a outra variável. Não mantenha o texto de exemplo `replace_with_a_strong_password`.

O arquivo `.env` contém uma credencial local e não deve ser enviado ao GitHub nem compartilhado. O projeto já o inclui nas regras do `.gitignore`.

**Resultado esperado:** arquivo `.env` criado na raiz do projeto, com uma senha definida e as demais configurações preservadas.

## 4. Opção 2 — Construir e executar com Docker Compose

Nesta opção, o Docker constrói uma imagem da Financial API a partir do código-fonte e inicia dois contêineres: um para a aplicação e outro para o PostgreSQL.

A compilação Java acontece dentro da etapa de construção da imagem. Portanto, não precisamos instalar Java ou Maven diretamente no computador.

### 4.1. Construir a imagem e iniciar os serviços

Confirme que o terminal está na pasta do projeto, onde se encontram `compose.yaml` e `.env`.

Execute:

```bash
docker compose up --build -d
```

O que acontece:

1. O Docker lê as configurações do arquivo `compose.yaml` e do arquivo `.env`.
2. Constrói a imagem local da Financial API utilizando o `Dockerfile`.
3. Cria e inicia o contêiner do PostgreSQL.
4. Aguarda o PostgreSQL passar pela verificação de saúde (*healthcheck*).
5. Inicia o contêiner da aplicação.

A primeira construção pode demorar mais porque o Docker precisa baixar as imagens-base e as dependências necessárias.

### 4.2. Verificar os contêineres

Execute:

```bash
docker compose ps
```

O resultado deverá listar os serviços `app` e `db` em execução. O serviço `db` deverá apresentar a indicação `healthy` depois de passar pelo teste de saúde.

Para acompanhar as mensagens da aplicação em tempo real, execute:

```bash
docker compose logs -f app
```

Procure a mensagem `Started FinancialApiApplication`, que indica que a aplicação Spring Boot foi iniciada.

Para interromper apenas a exibição dos logs, pressione `Ctrl+C`. Isso não encerra os contêineres.

### 4.3. Acessar a aplicação

Abra o navegador e utilize:

| Recurso | Endereço |
|---|---|
| Listagem de transações | http://localhost:8080/api/transactions |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |


O PostgreSQL também fica acessível pelo computador local na porta definida pela variável `POSTGRES_PORT` do arquivo `.env`. Por padrão, essa porta é `5435`, permitindo conexões de ferramentas externas, como pgAdmin.

Dentro da rede Docker, a API acessa o banco pelo nome do serviço `db` e pela porta interna `5432`. Essa porta interna permanece a mesma, independentemente da porta publicada no computador.

### 4.4. Encerrar os serviços

Para interromper e remover os contêineres e a rede criada pelo Docker Compose, execute:

```bash
docker compose down
```

O volume nomeado `postgres_data` é preservado. Portanto, os dados do banco permanecem disponíveis quando os serviços forem iniciados novamente.

**Atenção:** não utilize `docker compose down -v` quando desejar preservar os dados. A opção `-v` também remove o volume do PostgreSQL e pode apagar as transações armazenadas.

**Resultado esperado:** aplicação e banco executados em contêineres, API acessível pelo navegador e dados preservados no volume do PostgreSQL.

## 5. Opção 3 — Executar a imagem publicada no GHCR

Nesta opção, utilizamos uma imagem da Financial API que já foi construída e publicada no GitHub Container Registry (GHCR). Assim, não é necessário compilar o código-fonte no computador.

A versão documentada no README é:

- **Versão:** `v1.0.1`
- **Imagem:** `ghcr.io/gmorenos-dev/financial-api:v1.0.1`

A opção 3 ainda utiliza o Docker Compose para iniciar o PostgreSQL e criar a rede que será compartilhada com o contêiner da aplicação publicada.

### 5.1. Iniciar somente o banco de dados

Confirme que o arquivo `.env` foi criado e configurado na seção 3.2.

Se a aplicação da opção 2 estiver em execução, interrompa somente o serviço da aplicação local:

```bash
docker compose stop app
```

Agora inicie apenas o PostgreSQL:

```bash
docker compose up -d db
```

Esse comando cria a rede do projeto e inicia o banco, sem construir ou iniciar a aplicação local.

Verifique o estado do banco:

```bash
docker compose ps
```

Aguarde até que o serviço `db` apresente o estado `healthy`.

### 5.2. Baixar a imagem publicada

Execute:

```bash
docker pull ghcr.io/gmorenos-dev/financial-api:v1.0.1
```

O Docker baixará a versão identificada pela tag `v1.0.1`. A tag permite selecionar uma publicação específica da aplicação.

**Acesso à imagem publicada**

A imagem está disponibilizada publicamente no GitHub Container Registry (GHCR). Portanto, não é necessário possuir uma conta no GitHub nem executar `docker login` para baixá-la.

**Compatibilidade de arquitetura**

A versão `v1.0.1` atualmente publicada disponibiliza a imagem para a arquitetura `linux/amd64`.

Essa arquitetura é compatível com a maioria dos computadores com processadores Intel ou AMD. Caso utilize um computador com arquitetura ARM, como alguns equipamentos com processadores Apple Silicon, recomendamos utilizar a opção 2 (Docker Compose), que constrói a imagem localmente para a arquitetura do ambiente.

### 5.3. Executar a aplicação publicada

O contêiner da aplicação precisa participar da mesma rede Docker Compose do banco de dados. Quando o repositório é clonado para a pasta padrão `financial-api`, o nome dessa rede é `financial-api_default`.

Execute o comando abaixo em uma única linha. Ele funciona tanto no PowerShell quanto no terminal Ubuntu:

```bash
docker run -d --name financial-api-release --network financial-api_default --env-file .env -e "DB_URL=jdbc:postgresql://db:5432/financial_api" -p 127.0.0.1:8080:8080 --restart unless-stopped ghcr.io/gmorenos-dev/financial-api:v1.0.1
```

O comando:

- Cria um contêiner chamado `financial-api-release`.
- Conecta esse contêiner à rede `financial-api_default`.
- Carrega a senha do banco a partir do arquivo `.env`.
- Configura o endereço interno do PostgreSQL como `db:5432`.
- Publica a aplicação na porta `8080` do computador.
- Define que o contêiner será reiniciado automaticamente, exceto quando for parado manualmente.

**Atenção:** o nome da rede depende do nome do projeto Docker Compose. Se a pasta do repositório tiver outro nome, a rede também poderá ter outro nome. Nesse caso, consulte as redes disponíveis com:

```bash
docker network ls
```

Utilize no parâmetro `--network` o nome da rede criada pelo comando `docker compose up -d db`.

### 5.4. Verificar a inicialização

Consulte os últimos registros da aplicação:

```bash
docker logs --tail=50 financial-api-release
```

Verifique se a aplicação iniciou sem erros de autenticação com o banco ou divergência de checksum do Flyway.

Depois, abra no navegador:

http://localhost:8080/api/transactions

A resposta esperada é HTTP `200 OK`. Para acessar a documentação interativa, utilize:

http://localhost:8080/swagger-ui/index.html

### 5.5. Encerrar a execução

Para remover somente o contêiner da imagem publicada, execute:

```bash
docker rm -f financial-api-release
```

Esse comando não remove a imagem baixada nem o volume do PostgreSQL.

Para encerrar também o banco e remover a rede do Docker Compose, execute:

```bash
docker compose down
```

O volume do PostgreSQL permanece preservado. Evite `docker compose down -v` quando desejar manter os dados.

**Para voltar à opção 2**, depois de remover o contêiner `financial-api-release`, execute:

```bash
docker compose up --build -d app
```

**Resultado esperado:** imagem publicada executada em um contêiner independente, conectada ao PostgreSQL iniciado pelo Docker Compose.

## 6. Problemas comuns

### 6.1. O Docker não está em execução

Se aparecer uma mensagem informando que não foi possível conectar ao mecanismo Docker, verifique:

- No Windows, se o Docker Desktop foi aberto e terminou de iniciar.
- No Ubuntu nativo, se o serviço está ativo. Para consultar, utilize `sudo systemctl status docker`; se necessário, inicie-o com `sudo systemctl start docker`.
- No Ubuntu pelo WSL, se a integração da distribuição está habilitada nas configurações do Docker Desktop.

### 6.2. A porta 8080 já está em uso

A porta `8080` é utilizada pela Financial API no computador local. Caso outro programa já esteja utilizando essa porta, o Docker não conseguirá disponibilizá-la para a aplicação.

Uma alternativa é utilizar outra porta no computador, sem modificar a configuração interna da aplicação.

Neste exemplo, utilizaremos a porta `8081`.

**Opção 2 — Docker Compose**

Na raiz do projeto, abra o arquivo `compose.yaml` e localize a configuração de portas do serviço `app`:

```yaml
ports:
  - "127.0.0.1:8080:8080"
```

Altere somente a porta do computador, que é o número do meio:

```yaml
ports:
  - "127.0.0.1:8081:8080"
```

Salve o arquivo e, no terminal aberto na pasta do projeto, execute:

```bash
docker compose up --build -d app
```

O Docker recriará o contêiner da aplicação utilizando o novo mapeamento de portas.

Acesse a API pelo navegador:

http://localhost:8081/api/transactions

**Opção 3 — Imagem publicada no GHCR**

Na seção 5.3, o comando utilizado para iniciar a aplicação contém o seguinte parâmetro:

```bash
-p 127.0.0.1:8080:8080
```

Substitua-o por:

```bash
-p 127.0.0.1:8081:8080
```

Antes de executar novamente o comando completo da seção 5.3, verifique se já existe um contêiner chamado `financial-api-release` de uma tentativa anterior.

Caso exista, remova-o:

```bash
docker rm -f financial-api-release
```

Em seguida, execute novamente o comando completo da seção 5.3, agora utilizando o parâmetro de porta atualizado.

Acesse a API pelo navegador:

http://localhost:8081/api/transactions

**Entendendo o mapeamento das portas**

Nos dois procedimentos, utilizamos o formato:

`127.0.0.1:8081:8080`

- `127.0.0.1`: permite acessar a aplicação somente pelo próprio computador.
- `8081`: porta disponibilizada no computador, utilizada para acessar a API pelo navegador.
- `8080`: porta interna do contêiner, utilizada pela aplicação Spring Boot.

**Importante:** alteramos somente a porta de acesso no computador. A porta interna da aplicação permanece `8080`, e a comunicação com o PostgreSQL continua utilizando `db:5432`.

O mapeamento também exige que a porta `8081` esteja livre no computador.


### 6.3. A porta 5435 já está em uso

A porta `5435` é a porta publicada pelo PostgreSQL para conexões originadas no computador local.

No arquivo `.env`, altere `POSTGRES_PORT` para outra porta livre, por exemplo:

```dotenv
POSTGRES_PORT=5436
```

**Aplicar a nova configuração**

Após salvar o arquivo `.env`, recrie o serviço do PostgreSQL para aplicar a alteração da porta publicada:

```bash
docker compose up -d --force-recreate db
```

Aguarde o banco ficar saudável e confira o resultado:

```bash
docker compose ps
```

Os dados armazenados no volume `postgres_data` serão preservados durante essa recriação.


**Importante:** essa alteração modifica somente a porta de acesso pelo computador. A API continua acessando o banco pela rede interna do Docker, utilizando `db:5432`.


A porta interna do PostgreSQL continua sendo `5432`. Não altere o endereço interno `jdbc:postgresql://db:5432/financial_api` utilizado pela aplicação nos contêineres.

### 6.4. O banco de dados não fica saudável

Caso o serviço `db` não apresente o estado `healthy`, primeiro consulte o estado dos contêineres:

```bash
docker compose ps
```

Em seguida, consulte as mensagens do PostgreSQL:

```bash
docker compose logs --tail=100 db
```

Verifique se o servidor conseguiu inicializar corretamente e se não existem erros relacionados à configuração ou ao armazenamento dos dados.

**E se o banco estiver saudável, mas a API apresentar erro de autenticação?**

Nesse caso, verifique se a variável `DB_PASSWORD` do arquivo `.env` corresponde à senha configurada no PostgreSQL quando o banco foi inicializado pela primeira vez.

**Atenção:** quando o volume `postgres_data` já contém um banco inicializado, alterar `DB_PASSWORD` no arquivo `.env` não modifica automaticamente a senha armazenada no PostgreSQL. É necessário preservar a senha original ou realizar uma alteração diretamente no banco de dados.

Não remova o volume `postgres_data` para tentar resolver esse problema sem antes compreender que essa operação pode apagar os dados armazenados.


## Referências oficiais

### Docker Desktop e WSL

- [Instalação do Docker Desktop no Windows](https://docs.docker.com/desktop/setup/install/windows-install/)
- [Docker Desktop com WSL 2](https://docs.docker.com/desktop/features/wsl/)

### Docker Engine e Docker Compose no Ubuntu

- [Instalação do Docker Engine no Ubuntu](https://docs.docker.com/engine/install/ubuntu/)
- [Etapas pós-instalação do Docker Engine](https://docs.docker.com/engine/install/linux-postinstall/)
- [Instalação do plugin Docker Compose no Linux](https://docs.docker.com/compose/install/linux/)

### Comandos Docker

- [Referência do comando `docker run`](https://docs.docker.com/reference/cli/docker/container/run/)
- [Referência do comando `docker pull`](https://docs.docker.com/reference/cli/docker/image/pull/)

### GitHub Container Registry

- [Documentação do GitHub Container Registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry)
