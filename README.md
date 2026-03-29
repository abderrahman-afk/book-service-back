# Book Service

Spring Boot CRUD backend for a Flutter application, with PostgreSQL persistence.

## Stack

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Docker Compose

## API

Base URL: `http://localhost:8080/api/books`

Default PostgreSQL host port: `5433`

### Book payload

```json
{
  "id": 1,
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "9780132350884",
  "publishedDate": "2008-08-01"
}
```

## Run PostgreSQL

```bash
docker compose up -d
```

## Run the application

```bash
mvn spring-boot:run
```

## Example requests

Create a book:

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Architecture",
    "author": "Robert C. Martin",
    "isbn": "9780134494166",
    "publishedDate": "2017-09-20"
  }'
```

Get all books:

```bash
curl http://localhost:8080/api/books
```

Update a book:

```bash
curl -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Refactoring",
    "author": "Martin Fowler",
    "isbn": "9780201485677",
    "publishedDate": "1999-07-08"
  }'
```

Delete a book:

```bash
curl -X DELETE http://localhost:8080/api/books/1
```
