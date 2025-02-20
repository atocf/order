import pika
import time
import json

# Configurações do RabbitMQ
rabbitmq_url = 'amqp://admin:inicial1234@localhost:5672/%2F'  # Substitua pela sua URL

def publish_messages(url, queue_name, num_messages, repeat_count):
    try:
        # Estabelecer conexão
        parameters = pika.URLParameters(url)
        connection = pika.BlockingConnection(parameters)
        channel = connection.channel()

        # Declarar a fila
        channel.queue_declare(queue=queue_name, durable=True)  # Fila durável

        # Ativar confirmações de entrega
        channel.confirm_delivery()

        # Modelo de entrada
        order_template = {
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

        # Publicar mensagens
        for i in range(num_messages):
            # Enviar o mesmo pedido 'repeat_count' vezes
            for j in range(repeat_count):
                # Modificar o orderId para cada mensagem
                order = order_template.copy()
                order["orderId"] = f"ORD{i:06d}"

                message_body = json.dumps(order)

                # Publicar a mensagem com confirmação de entrega
                try:
                    channel.basic_publish(
                        exchange='',
                        routing_key=queue_name,
                        body=message_body,
                        properties=pika.BasicProperties(
                            delivery_mode=pika.spec.PERSISTENT_DELIVERY_MODE  # Mensagem persistente
                        ),
                        mandatory=True  # Garante que a mensagem seja roteada
                    )
                    print(f" [x] Sent {message_body}")
                except pika.exceptions.AMQPChannelError as e:
                    print(f" [!] Mensagem não entregue: {e}")
                    break  # Parar se a mensagem não puder ser entregue

                time.sleep(0.01)  # Opcional: adicionar um pequeno atraso

        print(f" [x] Publicadas {num_messages * repeat_count} mensagens.")

    except pika.exceptions.AMQPConnectionError as e:
        print(f" [!] Erro de conexão: {e}")

    finally:
        # Fechar a conexão
        if 'connection' in locals() and connection.is_open:
            connection.close()
            print(" [x] Conexão fechada.")

# Uso do script
if __name__ == '__main__':
    queue_name = 'orderQueue'
    num_messages = 100 # Número de pedidos a serem enviados
    repeat_count = 5  # Enviar cada pedido 5 vezes
    publish_messages(rabbitmq_url, queue_name, num_messages, repeat_count)