# order

## Pré-requisitos

- Docker
- Docker Compose

## Estrutura de Pastas do Projeto
order/
│
│   ├── /src.main
│   │   ├── java
│   │   │   └── br.com.atocf.order.processor
│   │   │       ├── config
│   │   │       │   ├── MongoConfig.java
│   │   │       │   └── RabbitMQConfig.java
│   │   │       ├── dto
│   │   │       │   └── OrderRequest.java
│   │   │       ├── model
│   │   │       │   ├── Order.java
│   │   │       │   ├── Customer.java
│   │   │       │   ├── Product.java
│   │   │       │   └── OrderStatus.java
│   │   │       ├── repository
│   │   │       │   └── OrderRepository.java
│   │   │       ├── service
│   │   │       │   ├── OrderConsumer.java
│   │   │       │   └── DuplicateOrderHandler.java
│   │   │       ├── exception
│   │   │       │   ├── DuplicateOrderException.java
│   │   │       │   └── GlobalExceptionHandler.java
│   │   │       └── OrderProcessorApplication.java
│   │   └── resources
│   │       ├── application.properties
│   │       └── application-test.properties
│   └── /src.test
│       ├── java
│       │   └── br.com.atocf.orderprocessor
│       │       ├── service
│       │       │   ├── OrderConsumerTests.java
│       │       │   └── DuplicateOrderHandlerTests.java
│       │       ├── repository
│       │       │   └── OrderRepositoryTests.java
│       │       └── exception
│       │           └── DuplicateOrderExceptionTests.java
│       └── resources
│           └── application-test.properties
│   
├── orderQuery
│
├── compose.yaml
└── README.md












order/
│

│   │       ├── controller
│   │       │   └── OrderController.java
│   │       ├── model
│   │       │   ├── OrderRequest.java
│   │       │   └── OrderResponse.java
│   │       ├── exception
│   │       │   ├── OrderNotFoundException.java
│   │       │   ├── DuplicateOrderException.java
│   │       │   └── GlobalExceptionHandler.java
│   │       ├── model
│   │       │   ├── Customer.java
│   │       │   ├── Order.java
│   │       │   ├── OrderStatus.java
│   │       │   └── Product.java
│   │       ├── repository
│   │       │   └── OrderRepository.java
│   │       ├── service
│   │       │   ├── OrderConsumer.java
│   │       │   └── OrderService.java
│   │       ├── util
│   │       │   └── DateUtil.java
│   │       └── OrderApplication.java
│   └── resources
│       └── application.properties
├── /src.test
│   ├──java
│   │   └── br.com.atocf.order
│   │       ├── controller
│   │       │   └── OrderControllerTests.java - Falta criar os testes e a classe
│   │       ├── model
│   │       │   ├── OrderRequestTests.java
│   │       │   └── OrderResponseTests.java - Falta criar os testes e a classe
│   │       ├── exception
│   │       │   ├── OrderNotFoundExceptionTests.java
│   │       │   ├── DuplicateOrderExceptionTests.java
│   │       │   └── GlobalExceptionHandlerTests.java
│   │       ├── model
│   │       │   ├── CustomerTests.java
│   │       │   ├── OrderTests.java
│   │       │   ├── OrderStatusTests.java
│   │       │   └── ProductTests.java
│   │       ├── repository
│   │       │   └── OrderRepositoryTests.java - Falta criar os testes
│   │       ├── service
│   │       │   ├── OrderConsumerTests.java - falta casos de teste
│   │       │   └── OrderServiceTests.java - - Falta criar os testes e a classe
│   │       ├── util
│   │       │   └── DateUtilTests.java
│   │       └── OrderApplicationTests.java
│   └── resources
│       └── application-test.properties
├── compose.yaml
├── pom.xml
└── README.md

## Configuração e Execução

### Inicie o ambiente local:
```shell
   docker-compose up -d
```

### Para parar o ambiente:
```shell   
  docker-compose down
```