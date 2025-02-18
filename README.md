# Order

## Pré-requisitos
- Docker
- Docker Compose  orderProcessor

## Estrutura de Pastas do Projeto
order/
│   
├── orderProcessor/
│   ├── src.main
│   │   ├── java
│   │   │  └── br.com.atocf.order.processor
│   │   │       ├── config
│   │   │       │   ├── MongoConfig.java
│   │   │       │   └── RabbitMQConfig.java
│   │   │       ├── dto
│   │   │       │   └── OrderRequest.java
│   │   │       ├── exception
│   │   │       │   ├── DuplicateOrderException.java
│   │   │       │   └── OrderNotFoundException.java
│   │   │       ├── model
│   │   │       │   ├── Customer.java
│   │   │       │   ├── Order.java
│   │   │       │   ├── OrderStatus.java
│   │   │       │   └── Product.java
│   │   │       ├── repository
│   │   │       │   └── OrderRepository.java
│   │   │       ├── service
│   │   │       │   └── OrderConsumer.java
│   │   │       ├── util
│   │   │       │   └── DateUtils.java
│   │   │       └── ProcessorApplication.java
│   │   └── resources
│   │       └── application.properties
│   └── src.test
│       ├── java
│       │   └── br.com.atocf.order.processor
│       │       ├── dto
│       │       │   └── OrderRequestTests.java
│       │       ├── exception
│       │       │   ├── DuplicateOrderExceptionTests.java
│       │       │   └── OrderNotFoundExceptionTest.java
│       │       ├── model
│       │       │   ├── CustomerTests.java
│       │       │   ├── OrderTests.java
│       │       │   ├── OrderStatusTests.java
│       │       │   └── ProductTests.java
│       │       ├── repository
│       │       │   └── OrderRepositoryTests.java
│       │       ├── service
│       │       │   └── OrderConsumerTests.java
│       │       ├── util
│       │       │   └── DateUtilsTests.java
│       │       └── ProcessorApplication.java
│       └── resources
│           └── application-test.properties
│
├── orderQuery/
│
├── compose.yaml
└── README.md

## Serviço

Optei por criar dois serviços distintos, OrderProcessor e OrderQuery, para separar as responsabilidades de consumo e processamento de pedidos e consulta de dados.

### Vantagens

 - Alta Escalabilidade: Cada serviço pode ser escalado de forma independente. Se o volume de consumo aumentar, você pode escalar o serviço OrderProcessor sem impactar o OrderQuery.
 - Isolamento de Responsabilidades: Separar as responsabilidades reduz a chance de que falhas no consumo e processamento impactem as consultas.
 - Desempenho Otimizado: O serviço de consultas pode ser otimizado para leitura, enquanto o serviço de consumo pode ser ajustado para alta performance de escrita.
 - Facilidade de Manutenção: O código é mais modular, com cada serviço focado em uma responsabilidade específica.

### OrderProcessor

#### Responsabilidade:
- Consumir mensagens do Produto Externo A.
- Processar os pedidos (verificar duplicidade, calcular valores).
- Persistir os dados no MongoDB.

####  Tecnologias: 
- Spring Boot 3.4.2
- Java 21
- RabbitMQ
- MongoDB.

### OrderQuery

#### Responsabilidade:
- Consultar os dados dos pedidos processados no MongoDB.
- Expor endpoints REST para o Produto Externo B.

#### Tecnologias
- Spring Boot 3.4.2
- Java 21
- MongoDB.

## Fluxo de Comunicação

- Recebimento de Pedidos (Produto Externo A → OrderProcessor):
  - O Produto Externo A envia mensagens para a fila do RabbitMQ.
  - O serviço OrderProcessor consome as mensagens, processa os pedidos e persiste os dados no MongoDB.

- Consulta de Dados (Produto Externo B → OrderQuery):
  - O Produto Externo B realiza consultas aos pedidos processados através dos endpoints REST do serviço OrderQuery.


## Execução

### Inicie o ambiente local:
```shell
   docker-compose up -d
```

### Para parar o ambiente:
```shell   
  docker-compose down
```