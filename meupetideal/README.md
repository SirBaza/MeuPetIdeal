# 🐾 MeuPetIdeal - Sistema de Adoção de Pets

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![API REST](https://img.shields.io/badge/API-REST-blue.svg)](https://spring.io/guides/gs/rest-service/)
[![Swagger](https://img.shields.io/badge/Swagger-OpenAPI_3-85EA2D.svg)](https://swagger.io/)

Sistema web completo para conexão entre ONGs de proteção animal e pessoas interessadas em adotar pets. Desenvolvido com Spring Boot, oferece interface web intuitiva e API RESTful completa.

## 🎯 Funcionalidades

### 🌐 Interface Web
- ✅ **Listagem de ONGs** - Visualização com filtros por cidade e área de atuação
- ✅ **Cadastro/Edição de ONGs** - Formulários completos com upload de imagens
- ✅ **Listagem de Pets** - Galeria de pets disponíveis para adoção
- ✅ **Cadastro/Edição de Pets** - Gestão completa com upload de fotos
- ✅ **Sistema de Upload** - Imagens convertidas automaticamente para URLs

### 🔌 API RESTful
- ✅ **CRUD Completo** - ONGs e Pets
- ✅ **Upload de Arquivos** - Suporte para MultipartFile
- ✅ **Filtros Avançados** - Busca personalizada
- ✅ **Códigos HTTP Semânticos** - Respostas padronizadas
- ✅ **Documentação Swagger** - Interface interativa

## 🏗️ Arquitetura

```
├── 📁 controller/
│   ├── 📁 view/           # Controllers para interface web (Thymeleaf)
│   └── 📁 restapi/        # Controllers para API REST
├── 📁 service/            # Lógica de negócio
├── 📁 repository/         # Camada de persistência (Spring Data JPA)
├── 📁 entities/           # Entidades JPA
├── 📁 exception/          # Tratamento de exceções
└── 📁 DTOs/              # Objetos de transferência de dados
```

## 🚀 Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.6+

### 1. Clone o Repositório
```bash
git clone [URL_DO_REPOSITORIO]
cd meupetideal
```

### 2. Execute a Aplicação
```bash
# Com Maven
./mvnw spring-boot:run

# Ou compile e execute
./mvnw clean package
java -jar target/meupetideal-0.0.1-SNAPSHOT.jar
```

### 3. Acesse a Aplicação
- **Interface Web**: http://localhost:8080/MeuPetIdeal/ong
- **API REST**: http://localhost:8080/api/v1/
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console**: http://localhost:8080/h2-console

## 📊 Banco de Dados

O sistema utiliza **H2 Database** (em memória) com as seguintes configurações:

```properties
# Configuração H2
spring.datasource.url=jdbc:h2:file:./data/meupetideal
spring.datasource.username=sa
spring.datasource.password=password

# Recriar banco a cada inicialização (desenvolvimento)
spring.jpa.hibernate.ddl-auto=create-drop
```

### Entidades Principais

#### 🏢 ONG
```java
- id: Long (PK)
- nome: String (required)
- endereco: String (required)
- fundacao: String (required)
- atuacao: String (required)
- telefone: String (required)
- email: String (required)
- site: String (optional)
- instagram: String (optional)
- descricao: String (optional)
- imagem: String (URL)
- pets: List<Pet> (OneToMany)
```

#### 🐕 Pet
```java
- id: Long (PK)
- nome: String (required)
- raca: Raca (ManyToOne, required)
- tipo: Tipo (ManyToOne, required)
- ong: Ong (ManyToOne, required)
- descricao: String (required)
- idade: int (required)
- sexo: String (required)
- foto: String (URL)
```

## 🌐 API REST

### Base URL: `/api/v1`

### 🏢 Endpoints de ONGs

#### Listar ONGs
```http
GET /api/v1/ongs
```

#### Buscar ONG por ID
```http
GET /api/v1/ongs/{id}
```

#### Buscar ONGs com Filtros
```http
GET /api/v1/ongs/search?termo={termo}&cidade={cidade}&atuacao={atuacao}
```

#### Criar Nova ONG (com upload)
```http
POST /api/v1/ongs
Content-Type: multipart/form-data

Form Data:
- nome: string
- endereco: string
- fundacao: string
- atuacao: string
- telefone: string
- email: string
- descricao: string (optional)
- site: string (optional)
- instagram: string (optional)
- file: file (optional)
```

#### Atualizar ONG
```http
PUT /api/v1/ongs/{id}
Content-Type: application/json
```

#### Excluir ONG
```http
DELETE /api/v1/ongs/{id}
```

### 🐕 Endpoints de Pets

#### Listar Pets
```http
GET /api/v1/pets
```

#### Buscar Pet por ID
```http
GET /api/v1/pets/{id}
```

#### Criar Novo Pet (com upload)
```http
POST /api/v1/pets
Content-Type: multipart/form-data

Form Data:
- pet: string (JSON do pet)
- file: file (optional)
```

#### Atualizar Pet
```http
PUT /api/v1/pets/{id}
Content-Type: multipart/form-data
```

#### Excluir Pet
```http
DELETE /api/v1/pets/{id}
```

### 📝 Respostas HTTP

| Código | Significado |
|--------|-------------|
| 200 | ✅ Sucesso |
| 201 | ✅ Criado (com header Location) |
| 204 | ✅ Sem conteúdo (exclusão) |
| 400 | ❌ Dados inválidos |
| 404 | ❌ Recurso não encontrado |
| 500 | ❌ Erro interno |

## 🛠️ Tecnologias

### Backend
- **Spring Boot 3.3.7** - Framework principal
- **Spring Web** - Controllers REST e MVC
- **Spring Data JPA** - Persistência de dados
- **Spring Validation** - Validação de dados
- **H2 Database** - Banco de dados em memória

### Frontend
- **Thymeleaf** - Template engine
- **HTML5 + CSS3** - Interface responsive
- **Bootstrap** (via CDN) - Estilização

### Documentação
- **SpringDoc OpenAPI** - Documentação automática da API
- **Swagger UI** - Interface interativa para testes

### Qualidade
- **Bean Validation** - Validação declarativa
- **Global Exception Handler** - Tratamento centralizado de erros
- **ProblemDetail (RFC 7807)** - Padronização de erros da API

## 📸 Upload de Imagens

O sistema suporta upload de imagens para ONGs e Pets:

### Características:
- ✅ **Tipos aceitos**: JPG, JPEG, PNG, GIF, WebP
- ✅ **Tamanho máximo**: 10MB
- ✅ **Nomes únicos**: Timestamp + UUID para evitar conflitos
- ✅ **URLs automáticas**: Conversão para URLs completas
- ✅ **Limpeza automática**: Banco recriado a cada reinicialização

### Estrutura de arquivos:
```
src/main/resources/static/imgs/
├── ong_[timestamp]_[uuid].jpg
├── pet_[timestamp]_[uuid].png
└── [outras imagens estáticas]
```

## 🎯 Requisitos da P2 Atendidos

### ✅ Parte I: Design e Implementação da API REST
- [x] **Padrões RESTful**: Verbos HTTP corretos (GET, POST, PUT, DELETE)
- [x] **URLs semânticas**: `/api/v1/ongs`, `/api/v1/pets`
- [x] **CRUD Completo**: Todas operações implementadas
- [x] **Camada de Serviço**: Lógica de negócio encapsulada em @Service
- [x] **ResponseEntity**: Códigos HTTP apropriados (200, 201, 204, 404, 400, 500)
- [x] **Header Location**: Em criações (201 Created)

### ✅ Parte II: Persistência e Lógica de Negócio
- [x] **Spring Data JPA**: Repositórios implementados
- [x] **Consultas Customizadas**: `findByOngId()`, `findByTermo()`, etc.
- [x] **@Query Annotations**: Queries JPQL customizadas

### ✅ Parte III: Documentação e Tratamento de Exceções
- [x] **@ControllerAdvice**: Manipulador global de exceções
- [x] **ProblemDetail (RFC 7807)**: Respostas de erro padronizadas
- [x] **Exceções Customizadas**: `OngNotFoundException`, `PetValidationException`
- [x] **Swagger/OpenAPI**: Documentação completa com @Tag, @Operation, @ApiResponse
- [x] **UI Swagger**: Acessível e funcional

## 🤝 Contribuição

1. Faça fork do projeto
2. Crie sua feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para detalhes.

## 👥 Desenvolvedores

- **Seu Nome** - *Desenvolvimento Full Stack* - [GitHub](https://github.com/seuusuario)

---
