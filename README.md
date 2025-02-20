# Order
O sistema foi projetado para gerenciar pedidos recebidos de um Produto Externo A e disponibilizá-los para outro Produto Externo B. A arquitetura segue os princípios de responsabilidade única e alta performance, com foco na escalabilidade e na consistência dos dados.


## Pré-requisitos
- Docker
- Docker Compose
- Kind
- mongosh - https://www.mongodb.com/try/download/shell

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
│   ├── src.test
│   │   ├── java
│   │   │   └── br.com.atocf.order.processor
│   │   │       ├── dto
│   │   │       │   └── OrderRequestTests.java
│   │   │       ├── exception
│   │   │       │   ├── DuplicateOrderExceptionTests.java
│   │   │       │   └── OrderNotFoundExceptionTest.java
│   │   │       ├── model
│   │   │       │   ├── CustomerTests.java
│   │   │       │   ├── OrderTests.java
│   │   │       │   ├── OrderStatusTests.java
│   │   │       │   └── ProductTests.java
│   │   │       ├── repository
│   │   │       │   └── OrderRepositoryTests.java
│   │   │       ├── service
│   │   │       │   └── OrderConsumerTests.java
│   │   │       ├── util
│   │   │       │   └── DateUtilsTests.java
│   │   │       └── ProcessorApplication.java
│   │   └── resources
│   │       └── application-test.properties
│   └── Dockerfile
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

### Executar local com Docker Compose:
```shell
   docker-compose up
```

### Executar local com Kind:
- Crie o cluster Kind:
```shell
   kind create cluster --config kind-cluster.yaml
```
- Verifique se o cluster foi criado:
```shell
   kubectl get nodes
```
- Crie os deployments e service:
```shell
   kubectl apply -f mongodb-deployment.yaml
```   
```shell
   kubectl apply -f rabbitmq-deployment.yaml
```
```shell
   kubectl apply -f order-deployment.yaml
```
- Verifique se os pods foram criados:
```shell
   kubectl get pods
```
- Verifique os serviços:
```shell
   kubectl get services
```

### Acessar o RabbitMQ:
- Acesse o RabbitMQ no navegador:
```shell
   kubectl port-forward service/rabbitmq-service 15672:15672
```
- Usuário: admin
- Senha: inicial1234
```shell
   http://localhost:15672
```
- Acesse o RabbitMQ no terminal e liberar para teste de carga:
```shell
   kubectl port-forward service/rabbitmq-service 5672:5672
```
- Teste de carga
```shell
  pip install pika
```
```shell
python testeCarga.py
```
- Modelo de dados 
```
{
  "orderId": "ORD123456",
  "customer": {
    "id": "CUST001",
    "name": "Maria Silva"
  },
  "products": [
    {
      "productId": "PROD001",
      "name": "Teclado Gamer",
      "quantity": 1,
      "unitPrice": 250.0
    },
    {
      "productId": "PROD002",
      "name": "Mouse Gamer",
      "quantity": 2,
      "unitPrice": 120.0
    }
  ],
  "createdAt": "2025-02-18T01:26:07Z"
}
```

### Acessar o MongoDB:
```shell
   kubectl port-forward service/mongodb-service 27017:27017
```

- Conectar ao MongoDB:
```shell
   mongosh --host localhost --port 27017 -u admin -p inicial1234 order
```
- Consultar pedidos:
```shell
db.orders.find();
```
- Consultar pedidos duplicados:
```shell
db.orders.aggregate([
{ $group: { _id: "$orderId", count: { $sum: 1 } } },
{ $match: { count: { $gt: 1 } } }
])
```
- Consultar qtd de pedido:
```shell
db.orders.countDocuments();
```
- Limpar pedidos:
```shell
db.orders.deleteMany({});
```