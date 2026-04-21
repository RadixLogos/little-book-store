# Little Book Store 
API REST desenvolvida para auxiliar no gerenciamento de uma pequena livraria local, oferecendo funcionalidades essenciais para controle de clientes, livros e pedidos.

## Tecnologias Utilizadas

- Java 17

- Spring Boot

- Spring Data JPA

- Hibernate

- PostgreSQL / MySQL

- DTO Pattern

- Arquitetura MVC

- Paginação e Filtros

## Funcionalidades

### Clientes

- Cadastro de clientes

- Atualização de dados

- Listagem paginada com filtro por nome ou telefone

- Remoção de cliente

### Livros

- Cadastro de livros

- Atualização de informações

- Listagem paginada com filtro por nome

- Controle de estoque

- Remoção de livro

### Pedidos

- Criação de pedido com aplicação de desconto percentual

- Atualização de pedido

- Listagem paginada com filtros por cliente ou livro

- Remoção de itens do pedido com atualização automática de estoque

- Consulta de pedido por ID

## Regras de Negócio Implementadas

- Validação de estoque antes da confirmação do pedido

- Atualização automática do estoque ao remover itens

- Cálculo automático do valor total com desconto

- Relacionamentos entre entidades (OneToMany / ManyToOne)

## Banco de Dados
O projeto está configurado para rodar com H2 em memória para facilitar testes e execução local.

## Modelo de Domínio
<img width="1916" height="1061" alt="image" src="https://github.com/user-attachments/assets/279c53a2-506f-4239-bf89-0c45af75e6e5" />

