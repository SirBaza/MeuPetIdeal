# 🐶 MeuPetIdeal 😺

> Plataforma digital que conecta ONGs e protetores de animais a pessoas interessadas em adotar um pet, promovendo a adoção responsável, segura e facilitada com auxílio de tecnologia.

---

## 📌 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Requisitos do Sistema](#requisitos-do-sistema)
- [Instalação](#instalação)
- [Uso](#uso)
- [Contribuição](#contribuição)
- [Licença](#licença)
- [Autor](#autor)

---

## 🐾 Sobre o Projeto

O **MeuPetIdeal** é uma aplicação web com o objetivo de facilitar e promover o processo de adoção de animais. O sistema conecta **ONGs e protetores** a **usuários interessados em adotar** pets, oferecendo um ambiente organizado com filtros, comunicação segura .

O projeto está sendo desenvolvido como parte da disciplina de Desenvolvimento Web, mestrada pelo professor Ronaldo Amaral – [GitHub: RonaldoAmaralIFF](https://github.com/RonaldoAmaralIFF), no Instituto Federal Fluminense - IFF.

---

## ✨ Funcionalidades

### Interface Web (Thymeleaf)
- Cadastro com perfis (ONG, PET)
- Cadastro de pets com fotos e localização
- Comunicação segura entre protetores e candidatos
- Estatísticas de adoções, usuários

### API REST
- **Endpoints para ONGs**: CRUD completo com validações
- **Endpoints para Pets**: CRUD completo com validações
- **Documentação Swagger**: Interface interativa para testar a API
- **Tratamento de exceções**: Respostas padronizadas para erros
- **Validação de dados**: Validações automáticas com Bean Validation

---

## 🛠 Tecnologias Utilizadas

- HTML5 / CSS3
- Thymeleaf
- Spring Boot 3.5.4
- Java 17
- Spring Data JPA
- H2 Database
- Swagger/OpenAPI 3
- Bean Validation
- Lombok

---

## 📋 Requisitos do Sistema

Os requisitos funcionais e não funcionais estão descritos no arquivo [`docs/requisitos.md`](docs/requisitos.md).  
O modelo de dados está parcialmente representado no diagrama de classes disponível em [`docs/diagrama-classe.png`](docs/diagrama-classe.png).

---

## 📦 Instalação e Configuração

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6 ou superior

### Passo 1: Clone o projeto
```bash
git clone https://github.com/SirBaza/MeuPetIdeal.git
cd MeuPetIdeal/meupetideal/meupetideal
```

### Passo 2: Instalar dependências e compilar
```bash
mvn clean install
```

### Passo 3: Executar a aplicação
```bash
mvn spring-boot:run
```
#### Ou executar o JAR gerado:
```bash
java -jar target/meupetideal-0.0.1-SNAPSHOT.jar
```

## 🚀 Uso

### Interface Web
Após iniciar a aplicação, acesse no navegador:
- **Aplicação Principal**: http://localhost:8080/MeuPetIdeal/home

### API REST
A API REST está disponível nos seguintes endpoints base:
- **ONGs**: http://localhost:8080/api/ongs
- **Pets**: http://localhost:8080/api/pets

### 📖 Documentação da API (Swagger)
A documentação interativa da API está disponível em:
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### Endpoints Principais

#### ONGs
- `GET /api/ongs` - Listar todas as ONGs
- `GET /api/ongs/{id}` - Buscar ONG por ID
- `POST /api/ongs` - Criar nova ONG
- `PUT /api/ongs/{id}` - Atualizar ONG
- `DELETE /api/ongs/{id}` - Deletar ONG

#### Pets
- `GET /api/pets` - Listar todos os pets
- `GET /api/pets/{id}` - Buscar pet por ID
- `POST /api/pets` - Criar novo pet
- `PUT /api/pets/{id}` - Atualizar pet
- `DELETE /api/pets/{id}` - Deletar pet

---

## 👨‍💻 Autores

- Ronaldo Amaral – [GitHub: RonaldoAmaralIFF](https://github.com/RonaldoAmaralIFF)
- Vitor Baza – [GitHub: SirBaza](https://github.com/SirBaza)
- Ana Carolina – [GitHub: CarolesHaddad](https://github.com/CarolesHaddad)




