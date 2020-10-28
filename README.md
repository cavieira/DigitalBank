# DigitalBank - API 

Esta é a API de banco digital desenvolvida por Camila Faria Vieira. 

Ela consiste num protótipo para operações básicas de um banco digital. As funcionalidades implementadas foram a criação de uma nova conta de pessoa física, o primeiro acesso pós aprovação de conta e o recebimento de dinheiro via transferência.

## Estrutura do projeto

A implementação foi realizada em Java utilizando o framework Spring. Como banco de dados de desenvolvimento foi utilizado o H2 e como servidor de messageria foi utilizado o RabbitMQ.

A API foi estruturada de acordo com o padrão MVC (Model-View-Controller), que consiste nos seguintes packages principais:

- **dtos**: contém as classes que encapsulam as informações para comunicação entre o cliente e o servidor.

- **controllers**: contém as classes que mapeiam as requisições para a API e fazem a comunicação com a camada de serviços.

- **services**: contém as classes que realizam o tratamento dos dados implementando as regras de negócio definidas para a API.

- **repositories**: contém as interfaces que mapeiam o banco de dados utilizando o framework Spring Data JPA.

- **models**: contém as classes que serão mapeadas para o banco de dados.

Essa API utiliza as requisições `GET` e `POST` para se comunicar e HTTP [response codes](https://en.wikipedia.org/wiki/List_of_HTTP_status_codes) para indicar status e erros. Todas as respostas são dadas em JSON.

