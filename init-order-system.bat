@echo off

:: Define cores para mensagens
set GREEN=echo.
set RED=echo.
set NC=echo.

:: Nome do cluster Kind
set CLUSTER_NAME=order-cluster

%GREEN% Iniciando script de configuração do ambiente para o sistema de pedidos...

:: Passo 1: Criar cluster Kind
%GREEN% Criando cluster Kind...
kind create cluster --config kind-cluster.yaml
if %ERRORLEVEL% NEQ 0 (
    %RED% Erro ao criar o cluster Kind.
    exit /b 1
)

:: Verificar se o cluster foi criado
kubectl get nodes
if %ERRORLEVEL% NEQ 0 (
    %RED% Erro ao verificar os nós do cluster.
    exit /b 1
)

:: Passo 2: Aplicar deployments e serviços
%GREEN% Aplicando MongoDB deployment...
kubectl apply -f mongodb-deployment.yaml
if %ERRORLEVEL% NEQ 0 (
    %RED% Erro ao aplicar o MongoDB deployment.
    exit /b 1
)

%GREEN% Aplicando RabbitMQ deployment...
kubectl apply -f rabbitmq-deployment.yaml
if %ERRORLEVEL% NEQ 0 (
    %RED% Erro ao aplicar o RabbitMQ deployment.
    exit /b 1
)

%GREEN% Aplicando Redis deployment...
kubectl apply -f redis-deployment.yaml
if %ERRORLEVEL% NEQ 0 (
    %RED% Erro ao aplicar o Redis deployment.
    exit /b 1
)

%GREEN% Aplicando Order Processor deployment...
kubectl apply -f order-deployment.yaml
if %ERRORLEVEL% NEQ 0 (
    %RED% Erro ao aplicar o Order Processor deployment.
    exit /b 1
)

:: Passo 3: Verificar pods e serviços
%GREEN% Verificando os pods no cluster...
kubectl get pods -A

%GREEN% Verificando os serviços no cluster...
kubectl get services -A

%GREEN% Instalando dependência...
pip install pika

%GREEN% Script concluído com sucesso!
pause