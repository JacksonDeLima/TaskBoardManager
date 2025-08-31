# TaskBoard Manager

Sistema de gerenciamento de tarefas com boards personalizáveis, desenvolvido em Java com persistência em MySQL.

## 📋 Funcionalidades

- ✅ Criação, seleção e exclusão de boards
- ✅ Gerenciamento de colunas (A Fazer, Fazendo, Feito, Cancelado)
- ✅ Criação e movimentação de cards entre colunas
- ✅ Bloqueio e desbloqueio de cards com registro de motivos
- ✅ Relatórios de tempo de conclusão de tarefas
- ✅ Relatórios de histórico de bloqueios
- ✅ Persistência completa em banco de dados MySQL

## 🛠️ Tecnologias Utilizadas

- **Java** - Linguagem de programação
- **MySQL** - Banco de dados relacional
- **JDBC** - Conexão com o banco de dados
- **MySQL Connector/J** - Driver JDBC para MySQL

## 📦 Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

1. **Java JDK 8 ou superior**
   - Download: [Oracle JDK](https://www.oracle.com/java/technologies/javase-downloads.html) ou [OpenJDK](https://openjdk.java.net/install/)
   - Verifique a instalação: `java -version`

2. **MySQL Server**
   - Download: [MySQL Community Server](https://dev.mysql.com/downloads/mysql/)
   - Ou use: XAMPP/WAMP que inclui MySQL

3. **IDE (Opcional)**
   - Eclipse, IntelliJ IDEA ou VS Code

## 🚀 Configuração e Execução

### 1. Clone ou baixe o projeto

```bash
git clone <url-do-repositorio>
cd TaskBoardManager
```

### 2. Configure o MySQL

```sql
-- Conecte-se ao MySQL como root
mysql -u root -p

-- Execute para criar o usuário e banco (opcional)
CREATE DATABASE IF NOT EXISTS task_boards;
CREATE USER IF NOT EXISTS 'taskuser'@'localhost' IDENTIFIED BY 'taskpass';
GRANT ALL PRIVILEGES ON task_boards.* TO 'taskuser'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Baixe o driver JDBC do MySQL

- Acesse: https://dev.mysql.com/downloads/connector/j/
- Baixe a versão Platform Independent (arquivo ZIP)
- Extraia e coloque o arquivo `mysql-connector-java-x.x.x.jar` na pasta `lib/`

### 4. Configure a conexão com o banco

Edite o arquivo `src/dao/DatabaseConnection.java` e ajuste as credenciais:

```java
private static final String USER = "root"; // ou "taskuser"
private static final String PASSWORD = "sua_senha_aqui";
```

### 5. Compile e execute

#### Usando linha de comando:

```bash
# Compilar
javac -cp ".;lib/mysql-connector-java-8.0.x.jar" -d bin src/*.java src/model/*.java src/dao/*.java src/service/*.java

# Executar
java -cp ".;bin;lib/mysql-connector-java-8.0.x.jar" Main
```

#### Usando IDE:

1. Importe o projeto na sua IDE
2. Adicione o JAR do MySQL Connector ao classpath
3. Execute a classe `Main.java`

## 📁 Estrutura do Projeto

```
TaskBoardManager/
├── src/
│   ├── model/          # Classes de modelo (Board, Column, Card, etc.)
│   ├── dao/            # Data Access Object (conexão com banco)
│   ├── service/        # Lógica de negócio
│   └── Main.java       # Classe principal
├── lib/                # Bibliotecas externas (driver MySQL)
├── bin/                # Arquivos compilados (gerado automaticamente)
└── README.md           # Este arquivo
```

## 🎯 Como Usar

1. **Criar um novo board**
   - Digite um nome para o board
   - O sistema criará automaticamente as colunas padrão

2. **Gerenciar cards**
   - Crie cards com título e descrição
   - Movimente os cards entre colunas
   - Bloqueie/desbloqueie cards quando necessário

3. **Visualizar relatórios**
   - Acesse relatórios de tempo de conclusão
   - Consulte histórico de bloqueios

4. **Gerenciar múltiplos boards**
   - Crie, selecione e exclua boards conforme necessário

## 🔧 Estrutura do Banco de Dados

O sistema cria automaticamente as seguintes tabelas:

- `boards` - Armazena os boards criados
- `columns` - Colunas de cada board
- `cards` - Cards/tarefas
- `card_movements` - Histórico de movimentação entre colunas
- `block_history` - Histórico de bloqueios e desbloqueios

## 📊 Tipos de Colunas

Cada board possui quatro tipos de colunas:

1. **INICIAL** - Onde os cards são criados (A Fazer)
2. **PENDENTE** - Colunas intermediárias (Fazendo)
3. **FINAL** - Coluna de conclusão (Feito)
4. **CANCELAMENTO** - Coluna para cards cancelados

## 🐛 Solução de Problemas

### Erro de conexão com MySQL
- Verifique se o MySQL está em execução
- Confirme as credenciais no arquivo DatabaseConnection.java

### Driver não encontrado
- Certifique-se de que o JAR do MySQL Connector está na pasta lib/

### Erros de compilação
- Verifique se todas as classes estão implementadas
- Confirme o caminho do driver no classpath

## 📝 Licença

Este projeto é para fins educacionais. Sinta-se à vontade para usá-lo e modificá-lo.

## 🤝 Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para:
- Reportar problemas
- Sugerir novas funcionalidades
- Enviar pull requests

## 📞 Suporte

Em caso de dúvidas ou problemas, entre em contato ou abra uma issue no repositório do projeto.

---

**Desenvolvido com persistência usando Java e MySQL**
