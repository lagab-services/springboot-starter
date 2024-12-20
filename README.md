# Java Spring Boot Boilerplate (Spring Boot 3.3.4)

### Included technologies

- Spring Boot 3.3.4
- Spring Security
- Spring Data JPA
- Spring Data Redis => TODO
- Hibernate
- JWT Authentication => add more info in JWT
- PostgreSQL => TODO
- Redis => TODO
- Liquibase
- Lombok
- Swagger
- JUnit 5
- Mockito

### Requirements

- Java 21
- Maven 3.9.3
- Docker 20.10.8
- Docker Compose 2.19.1
- PostgreSQL 13.11
- Redis 7.0.12

### Run with Docker Compose

```bash
docker-compose up --build -d
```

### Install dependencies

```bash
mvn clean install
```

### Run project

```bash
mvn spring-boot:run 
```

### Build project

```bash
mvn clean package
```

### Skip integration tests

```bash
mvn clean install -DskipITs=true
```