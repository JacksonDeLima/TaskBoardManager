# 📋 TaskBoard Manager

Sistema completo de gerenciamento de tarefas com boards personalizáveis, desenvolvido em Java com persistência em MySQL.

## ✨ Funcionalidades

### Principais
- ✅ **Criação, seleção e exclusão de boards**
- ✅ **Gerenciamento de colunas** (A Fazer, Fazendo, Feito, Cancelado)
- ✅ **Criação e movimentação de cards** entre colunas
- ✅ **Bloqueio e desbloqueio de cards** com registro de motivos
- ✅ **Visualização completa do board** com status dos cards

### Relatórios
- 📊 **Relatório de tempos** - Tempo de conclusão de tarefas
- 🔒 **Relatório de bloqueios** - Histórico de bloqueios com tempo e justificativas
- 📈 **Análise de desempenho** - Tempo gasto em cada coluna

### Persistência
- 💾 **Armazenamento em MySQL** - Todos os dados são persistidos
- 🔄 **Histórico completo** - Movimentações e alterações de status
- ⏱️ **Registro temporal** - Datas e horas de todas as operações

## 🛠️ Tecnologias Utilizadas

- **Java** - Linguagem de programação principal
- **MySQL** - Banco de dados relacional
- **JDBC** - Conexão com banco de dados
- **MySQL Connector/J** - Driver JDBC para MySQL

## 📋 Requisitos Atendidos

### Obrigatórios
1. ✅ Menu principal com opções de gerenciamento de boards
2. ✅ Persistência completa em MySQL
3. ✅ Boards com 4 colunas (Inicial, Pendente, Final, Cancelamento)
4. ✅ Cards com título, descrição, data de criação e status de bloqueio
5. ✅ Movimentação sequencial entre colunas
6. ✅ Sistema de bloqueio/desbloqueio com motivo
7. ✅ Menu completo de manipulação do board

### Opcionais
1. ✅ Registro de data/hora de movimentação entre colunas
2. ✅ Relatório de tempo de conclusão de tarefas
3. ✅ Relatório de bloqueios com tempo e justificativas

## 🚀 Como Executar

### Pré-requisitos
- **Java JDK 8 ou superior**
- **MySQL Server**
- **MySQL Connector/J** (incluído no projeto)

### Configuração do Banco de Dados

1. **Inicie o MySQL Server**:
   ```bash
   # Windows
   net start mysql
   
   # Linux/Mac
   sudo systemctl start mysql
   ```

2. **Configure o banco** (opcional):
   ```sql
   CREATE DATABASE IF NOT EXISTS task_boards;
   CREATE USER IF NOT EXISTS 'taskuser'@'localhost' IDENTIFIED BY 'taskpass';
   GRANT ALL PRIVILEGES ON task_boards.* TO 'taskuser'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Ajuste as credenciais** em `DatabaseConnection.java`:
   ```java
   private static final String USER = "root"; // ou "taskuser"
   private static final String PASSWORD = "sua_senha_aqui";
   ```

### Execução do Projeto

#### Via Linha de Comando:
```bash
# Compilar
javac -cp ".;lib/mysql-connector-java-8.0.x.jar" -d bin src/*.java src/model/*.java src/dao/*.java src/service/*.java

# Executar
java -cp ".;bin;lib/mysql-connector-java-8.0.x.jar" Main
```

#### Via IDE:
1. Importe o projeto na sua IDE preferida
2. Adicione o JAR do MySQL Connector ao classpath
3. Execute a classe `Main.java`

## 📁 Estrutura do Projeto

```
TaskBoardManager/
├── src/
│   ├── model/          # Entidades do sistema
│   │   ├── Board.java           → Modelo de board
│   │   ├── Column.java          → Modelo de coluna
│   │   ├── Card.java            → Modelo de card
│   │   ├── ColumnType.java      → Enum de tipos de coluna
│   │   ├── CardMovement.java    → Histórico de movimentação
│   │   └── BlockHistory.java    → Histórico de bloqueios
│   ├── dao/            # Data Access Object
│   │   ├── DatabaseConnection.java → Conexão com MySQL
│   │   ├── BoardDAO.java           → Operações de boards
│   │   ├── ColumnDAO.java          → Operações de colunas
│   │   ├── CardDAO.java            → Operações de cards
│   │   ├── CardMovementDAO.java    → Operações de movimentação
│   │   └── BlockHistoryDAO.java    → Operações de bloqueio
│   ├── service/        # Lógica de negócio
│   │   ├── BoardService.java       → Serviços de board
│   │   ├── CardService.java        → Serviços de card
│   │   └── ReportService.java      → Serviços de relatório
│   └── Main.java       # Classe principal
├── lib/                # Bibliotecas externas
│   └── mysql-connector-java-8.0.x.jar → Driver MySQL
├── bin/                # Arquivos compilados (gerado)
└── README.md           # Este arquivo
```

## 🎯 Como Usar

### 1. Criando um Board
- Selecione "Criar novo board" no menu principal
- Digite um nome para o board
- O sistema criará automaticamente as 4 colunas padrão

### 2. Gerenciando Cards
- **Criar card**: Adicione título e descrição
- **Mover card**: Avance para a próxima coluna
- **Cancelar card**: Movimento direto para "Cancelado"
- **Bloquear/Desbloquear**: Com registro de motivo

### 3. Visualizando Relatórios
- **Relatório de tempos**: Tempo de conclusão por task
- **Relatório de bloqueios**: Histórico completo de bloqueios

### 4. Fluxo de Trabalho
1. Crie cards na coluna "A Fazer"
2. Movimente sequencialmente entre colunas
3. Finalize em "Feito" ou cancele em "Cancelado"
4. Use bloqueios para tasks com impedimentos

## 🗃️ Estrutura do Banco de Dados

O sistema cria automaticamente as tabelas:

| Tabela | Descrição |
|--------|-----------|
| `boards` | Armazena os boards criados |
| `columns` | Colunas de cada board |
| `cards` | Cards/tarefas do sistema |
| `card_movements` | Histórico de movimentação |
| `block_history` | Histórico de bloqueios |

## 🔧 Tipos de Colunas

| Tipo | Descrição | Quantidade |
|------|-----------|------------|
| **INICIAL** | Onde os cards são criados | 1 por board |
| **PENDENTE** | Colunas intermediárias | 1 ou mais |
| **FINAL** | Coluna de conclusão | 1 por board |
| **CANCELAMENTO** | Coluna para cancelamentos | 1 por board |

## 🐛 Solução de Problemas

### Erro de Conexão com MySQL
- Verifique se o MySQL está em execução
- Confirme usuário e senha em `DatabaseConnection.java`
- Teste a conexão com: `mysql -u usuario -p`

### Driver Não Encontrado
- Verifique se o JAR do MySQL Connector está na pasta `lib/`
- Confirme o caminho no classpath

### Erros de Compilação
- Verifique se todas as classes estão implementadas
- Confirme a versão do Java (JDK 8 ou superior)

## 📊 Exemplos de Uso

### Criando um Novo Board
```
=== MENU PRINCIPAL ===
1 - Criar novo board
2 - Selecionar board
3 - Excluir boards
4 - Sair

Escolha uma opção: 1
Digite o nome do novo board: Projeto Alpha
Board 'Projeto Alpha' criado com sucesso!
```

### Movimentando um Card
```
=== MENU DO BOARD ===
1 - Mover card para próxima coluna
2 - Cancelar card
3 - Criar card
4 - Bloquear card
5 - Desbloquear card
6 - Visualizar board
7 - Gerar relatório de tempos
8 - Gerar relatório de bloqueios
9 - Fechar board

Escolha uma opção: 1

Cards disponíveis para movimentação:
1 - Implementar login (na coluna: A Fazer)
2 - Criar dashboard (na coluna: Fazendo)

Selecione o card (número): 1
Card movido para: Fazendo
```

## 🤝 Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para:

1. Reportar problemas ou bugs
2. Sugerir novas funcionalidades
3. Enviar pull requests com melhorias

## 📞 Suporte

Em caso de dúvidas ou problemas:

1. Verifique a seção de Solução de Problemas
2. Consulte a documentação do código
3. Abra uma issue no repositório do projeto

## 📝 Licença

Este projeto é destinado para fins educacionais e de aprendizado. 

---

**Desenvolvido com persistência usando Java e MySQL**