# GameLocadora

Projeto acadêmico de locadora de jogos em mídia física, desenvolvido com Java 17+ e PostgreSQL.

## Escopo desta primeira etapa

Este pacote contém o banco relacional e o início do backend:
- cadastro e listagem de clientes;
- cadastro e listagem de jogos;
- registro de aluguel;
- registro de devolução com cálculo de multa;
- validação do horário de funcionamento;
- proteção contra aluguel simultâneo da mesma cópia física.

A interface Swing ainda deverá ser construída/adaptada na próxima etapa. O PDF da disciplina exige uma janela Swing com campos, botões e listagem; um programa somente de console não atende ao requisito A10.

## Regras adotadas

- Cada aluguel dura 7 dias corridos.
- A multa é de 2% do valor total do aluguel por dia de atraso.
- O valor total do aluguel é calculado como valor da diária x 7 e fica registrado como fotografia do preço no momento da locação.
- Uma cópia física (exemplar) não pode ter dois aluguéis em aberto.
- A locadora funciona de segunda a sexta, das 08:00 às 18:00, e aos sábados, das 08:00 às 12:00. Domingos são fechados.
- As validações de horário são aplicadas às operações de retirada e devolução. Não há calendário de feriados nesta versão.

## Modelo de dados

- `cliente`: pessoas que alugam.
- `jogo`: título, plataforma, formato e valor da diária.
- `exemplar`: cada cópia física individual de um jogo.
- `aluguel`: retirada, previsão de devolução, devolução real e multa.

A separação entre `jogo` e `exemplar` permite que a locadora tenha várias cópias do mesmo título. A restrição única parcial em `aluguel` impede dois aluguéis abertos para o mesmo exemplar, inclusive se duas operações ocorrerem quase simultaneamente.

## Requisitos

- JDK 17 ou superior
- Maven
- PostgreSQL 14 ou superior

## Configuração

1. Crie um banco PostgreSQL chamado `gamelocadora`.
2. Execute `banco.sql` nesse banco.
3. Copie `config.exemplo.properties` para `config.properties`.
4. Informe URL, usuário e senha reais em `config.properties`.
5. Execute `mvn clean compile`.

O arquivo `config.properties` está no `.gitignore` e não deve ser enviado ao Git.

## Executar o teste inicial do backend

Depois de compilar, execute a classe `br.com.gamelocadora.app.Principal` pela IDE. Ela testa a conexão e lista os clientes cadastrados.

## Organização

- `app`: inicialização.
- `config`: conexão JDBC.
- `model`: entidades e conceitos de POO.
- `dao`: interfaces e implementação JDBC. Todo SQL da aplicação fica nos DAOs.
- `service`: regras de negócio.
- `exception`: exceção personalizada.

## POO contemplada

- Encapsulamento: atributos privados e validações nos modelos.
- Construtores sobrecarregados: versões para criação e leitura de registros.
- Herança: `JogoDisco` e `JogoCartucho` estendem `Midia`.
- Sobrescrita: `descricaoFormato()` e `resumo()`.
- Polimorfismo: `List<Midia>` pode armazenar diferentes formatos.
- Abstração: `Midia` é abstrata.
- Interface: `Resumivel` implementada por `Cliente`, `JogoDisco` e `JogoCartucho`.
- Composição: `Aluguel` possui um `Cliente` e um `Exemplar`; `Exemplar` possui uma `Midia`.
- Exceção personalizada: `RegraNegocioException`.

## Observação acadêmica

O PDF exige também tela Swing, operações de inserir/listar/excluir pela tela e, para o ponto extra, alteração, JOIN e transação em duas tabelas. Esta entrega é intencionalmente o começo do backend, não o projeto final completo. O artigo deve declarar o uso de IA como apoio, explicar quais partes foram auxiliadas e como foram revisadas.
