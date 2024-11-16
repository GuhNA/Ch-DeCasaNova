# Como rodar o backend
O backend todo roda em containers do Docker, por isso é importante ter o docker instalado localmente, todas as demais dependencias serão gerenciadas por ele.

## Requisitos
- Java SDK 17

## Como subir o banco de dados

Para subir o banco de dados se utilizado o seguinte comando no terminal:
```
docker compose up -d site_db
```
Com isso, é para estar rodando um container com postgres na sua porta 5432.
O Docker ira criar um volume permanete para você, caso queira deletar tudo, lembre-se de deletar o volume também.

## Buildar Applicação Java
Em seguida, é necessário buildar o arquivo `.jar` da applicação.

## Como subir a API

Para iniciar o container com a API, você deve executar o comando abaixo:
```
docker compose up --build spring_boot_api
```
Com este comando, a API estará rodando no `localhost:8080` e o seu terminal estara preso nos logs do servidor, isso é bom para ver logs de erro.
Porem se quiser apenas iniciar o container sem travar o terminal, adicione um `-d` no commando.

# Escopo do projeto

O sistema web terá duas telas podendo ser selecionadas via navbar.
- Página inicial
- Lista de presentes

Página Inicial:
Introdução sobre o sistema, imagens chamativas, marketing, etc.

Lista de presentes:
- imagem do casal (configurável)
- cor principal (configurável)
- título (configurável)
- upload de QR Code PIX (configurável)

- A lista deve possuir itens pré-cadastrados
- O usuário pode acessar a lista e adicionar novos itens baseados em imagens pré-cadastradas (banco de imagens) OU fazer upload de imagens próprio
- Cada item da lista deve exibir: nome do produto, descrição do produto, preço e imagem
- Ao clicar sobre o produto, o qr code deve aparecer em destaque com o fundo escurecido
- # Ch-DeCasaNova
