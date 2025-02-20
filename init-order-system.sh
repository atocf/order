#!/bin/bash

# Define cores para mensagens
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # Sem cor

# Nome do cluster Kind
CLUSTER_NAME="order-cluster"

echo -e "${GREEN}Iniciando script de configuração do ambiente para o sistema de pedidos...${NC}"

# Passo 1: Criar cluster Kind
echo -e "${GREEN}Criando cluster Kind...${NC}"
kind create cluster --config kind-cluster.yaml
if [ $? -ne 0 ]; then
  echo -e "${RED}Erro ao criar o cluster Kind.${NC}"
  exit 1
fi

# Verificar se o cluster foi criado
kubectl get nodes
if [ $? -ne 0 ]; then
  echo -e "${RED}Erro ao verificar os nós do cluster.${NC}"
  exit 1
fi

# Passo 2: Aplicar deployments e serviços
echo -e "${GREEN}Aplicando MongoDB deployment...${NC}"
kubectl apply -f mongodb-deployment.yaml
if [ $? -ne 0 ]; then
  echo -e "${RED}Erro ao aplicar o MongoDB deployment.${NC}"
  exit 1
fi

echo -e "${GREEN}Aplicando RabbitMQ deployment...${NC}"
kubectl apply -f rabbitmq-deployment.yaml
if [ $? -ne 0 ]; then
  echo -e "${RED}Erro ao aplicar o RabbitMQ deployment.${NC}"
  exit 1
fi

echo -e "${GREEN}Aplicando Redis deployment...${NC}"
kubectl apply -f redis-deployment.yaml
if [ $? -ne 0 ]; then
  echo -e "${RED}Erro ao aplicar o Redis deployment.${NC}"
  exit 1
fi

echo -e "${GREEN}Aplicando Order Processor deployment...${NC}"
kubectl apply -f order-deployment.yaml
if [ $? -ne 0 ]; then
  echo -e "${RED}Erro ao aplicar o Order Processor deployment.${NC}"
  exit 1
fi

# Passo 3: Verificar pods e serviços
echo -e "${GREEN}Verificando os pods no cluster...${NC}"
kubectl get pods -A

echo -e "${GREEN}Verificando os serviços no cluster...${NC}"
kubectl get services -A

echo -e "${GREEN}Instalando dependência...${NC}"
pip install pika

echo -e "${GREEN}Script concluído com sucesso!${NC}"