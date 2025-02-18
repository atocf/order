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