# GameLocadora

Sistema acadêmico de **locação de jogos em mídia física**, desenvolvido em **Java 17+**, com interface gráfica **Swing**, acesso a dados por **JDBC** e banco relacional **PostgreSQL**, hospedado no **Neon**.

## Integrantes

- Henrique David
- Guilherme Ribeiro

## Tema e objetivo

**Tema:** gerenciamento de locadora de jogos em mídia física.

O GameLocadora permite administrar clientes, jogos, exemplares físicos e aluguéis, mantendo o histórico das operações e aplicando regras de prazo, disponibilidade e multa por atraso.

## Tecnologias e requisitos

- **Linguagem:** Java 17 ou superior
- **Interface gráfica:** Java Swing
- **Persistência:** JDBC com `PreparedStatement`
- **Banco de dados:** PostgreSQL 14 ou superior (Utilizado:Neon). (Pode ser configurado em instancia local)
- **Gerenciamento de dependências e compilação:** Maven 3.9 ou superior

## Funcionalidades

- Cadastro, listagem, alteração e inativação de clientes.
- Cadastro, listagem, alteração e inativação de jogos e exemplares, com exclusão lógica para preservar o histórico.
- Registro de aluguéis com prazo de sete dias corridos e processamento de devoluções.
- Cálculo de multa de **2% por dia de atraso** sobre o valor total do aluguel (diária × 7).
- Consulta de exemplares disponíveis, aluguéis em aberto e aluguéis atrasados.
- Consulta com `JOIN` entre tabelas, exibida na interface.
- Cadastro de jogo e primeiro exemplar em uma transação JDBC, com `commit` e `rollback`.
- Validação dos horários de retirada e devolução.

### Regras de negócio

- **Segunda a sexta-feira:** 08h às 18h.
- **Sábado:** 08h às 12h.
- **Domingo:** fechado.
- Quando o vencimento recai em domingo, o prazo para devolução é considerado até segunda-feira para fins da regra de atraso.
- Feriados não são tratados pelo sistema.
- O valor da diária é consultado no banco; não é informado manualmente pela interface no momento da locação.
- O valor total do aluguel é registrado no momento da operação.
- Cada jogo pode ter diversos exemplares físicos; exemplares distintos do mesmo título podem ser alugados simultaneamente.
- O índice único parcial `uq_aluguel_exemplar_aberto` impede mais de uma locação em aberto para o mesmo exemplar, inclusive em acessos concorrentes.

## Estrutura do banco de dados

O PostgreSQL utiliza quatro tabelas principais:

| Tabela | Finalidade |
| --- | --- |
| `cliente` | Dados e situação cadastral dos clientes |
| `jogo` | Títulos disponíveis na locadora |
| `exemplar` | Unidades físicas associadas aos jogos |
| `aluguel` | Registros de retirada, prazo e devolução |

As relações entre as tabelas são mantidas por chaves estrangeiras. O arquivo `banco.sql` contém o esquema necessário à criação do banco. Para reproduzir a carga inicial, execute também `carga_inicial.sql`.

### Dados verificados no Neon

Após a execução do script SQL de carga, uma consulta de contagem retornou:

| Tabela | Registros |
| --- | ---: |
| `cliente` | 41 |
| `jogo` | 50 |
| `exemplar` | 250 |
| `aluguel` | 100 |
| **Total** | **441** |


## Configuração e execução

### 1. Preparar o ambiente

Instale o **JDK 17+** e o **Maven 3.9+**. Tenha acesso a um banco **PostgreSQL 14+**, local ou hospedado.

### 2. Criar e preparar o banco

Para PostgreSQL local, crie o banco:

```sql
CREATE DATABASE gamelocadora;
```

Conecte-se ao banco criado e execute o arquivo `banco.sql`, logo após execute `carga_inicial.sql`, para popular as tabelas com dados de exemplo.


### 3. Configurar a conexão

Na raiz do projeto, copie `config.exemplo.properties` para `config.properties` e informe os dados reais de conexão:

```properties
db.url=jdbc:postgresql://HOST:PORTA/BANCO?sslmode=require
db.usuario=SEU_USUARIO
db.senha=SUA_SENHA
```

Para PostgreSQL local sem SSL, ajuste a URL conforme a configuração da instância.

### 4. Compilar

Na raiz do projeto, execute:

```bash
mvn clean package
```

### 5. Iniciar o programa

```bash
java -jar target/gamelocadora-1.0.0.jar
```

Alternativamente, execute a classe `br.com.gamelocadora.app.Principal` pela IDE.

## Interface gráfica

A janela Swing organiza as funcionalidades nas abas **Clientes**, **Jogos**, **Exemplares**, **Aluguéis** e **Consulta JOIN**.

Na aba Clientes, os campos de ID, nome, e-mail e telefone aparecem na janela, com botões para inserir, listar, alterar, inativar e limpar. Nas demais abas, algumas operações utilizam caixas de diálogo, os resultados são apresentados em áreas de texto.

As mensagens de erro de regras de negócio são exibidas na interface para orientar o usuário.

## Conceitos de programação orientada a objetos

| Conceito | Aplicação no projeto |
| --- | --- |
| Encapsulamento | Atributos `private`, validações e identificador sem setter |
| Construtores e sobrecarga | Construtores de `Cliente`, `Midia` e `Aluguel`; métodos sobrecarregados |
| Herança | `JogoDisco` e `JogoCartucho` estendem `Midia` e utilizam `super(...)` |
| Polimorfismo | Sobrescrita de `descricaoFormato()` e uso de `List<Midia>` |
| Abstração | Classe abstrata `Midia` |
| Interfaces | `Resumivel` e `Validavel`, implementadas por classes do domínio |
| Composição | `Aluguel` referencia `Cliente` e `Exemplar`; `Exemplar` referencia `Midia` |
| Exceção personalizada | `RegraNegocioException`, tratada e apresentada na interface |

A aplicação mantém as consultas SQL nas classes DAO, utilizando `PreparedStatement` para operações parametrizadas.

## Testes e validação

O projeto contém testes automatizados em JUnit. Para executá-los:

```bash
mvn test
```

Também foram verificadas a conexão com o PostgreSQL hospedado no Neon, a execução do script SQL de carga inicial e operações funcionais de cadastro e inativação de clientes.


## Repositório Git

**URL:** `https://github.com/Henrique-dev12780/GameLocadora`

