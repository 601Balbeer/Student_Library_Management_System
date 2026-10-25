# Student Library Management System

A Spring Boot REST API for managing students, library cards, books, and borrowing/return history. The application uses Java 17, Spring Web, Spring Data JPA, Maven, and MySQL.

## Run the application

Create a MySQL database named `student_library_db` on port `3307`. The application connects as `root` with a blank password by default. Update `src/main/resources/application.properties` if your MySQL credentials or port differ.

Open a terminal in the project directory (the directory containing `pom.xml`) and run:

```powershell
.\mvnw.cmd spring-boot:run
```

On macOS or Linux, run `./mvnw spring-boot:run`. The service listens on port `7799` by default; change `server.port` in `application.properties` to use another port.

## API

The base URL is `http://localhost:7799`. Endpoints accept JSON request bodies where applicable. Read endpoints return JSON records. Student creation, book creation, and borrow/return requests return simple success messages. Path and query parameters are shown in braces or after `?`.

| Area | Method and endpoint | Purpose |
| --- | --- | --- |
| Students | `POST /student/apis/save` | Create a student and a library card that expires in three years |
| Students | `GET /student/apis/findAll` | List all students |
| Students | `GET /student/apis/findById/{id}` | Find a student by ID |
| Students | `PUT /student/apis/update/{id}` | Update student details |
| Students | `DELETE /student/apis/delete/{id}` | Delete a student by ID |
| Students | `GET /student/apis/findByPage?pageNo=0&pageSize=10` | Get a page of students, sorted by name |
| Students | `GET /student/apis/findByEmail?email=...` | Find a student by email |
| Students | `GET /student/apis/findByDept?dept=...` | List students by department |
| Cards | `GET /card/apis/findAll` | List all cards |
| Cards | `GET /card/apis/findById/{id}` | Find a card by ID |
| Cards | `GET /card/apis/findByStudent/{studentId}` | Find the card assigned to a student |
| Cards | `PATCH /card/apis/renew/{id}?years=3` | Extend a card's expiry by 1–5 years (defaults to 3) |
| Books | `POST /book/apis/save` | Add a catalogue entry with a total number of copies; new entries default to one copy |
| Books | `GET /book/apis/findAll` | List all books |
| Books | `GET /book/apis/findById/{id}` | Find a book by ID |
| Books | `GET /book/apis/findByTitle?title=...` | Search by title, ignoring case |
| Books | `GET /book/apis/findByCategory?category=...` | Search by category, ignoring case |
| Books | `GET /book/apis/available` | List catalogue entries with at least one available copy |
| Books | `PUT /book/apis/update/{id}` | Update a book; optionally change `totalCopies` |
| Books | `DELETE /book/apis/delete/{id}` | Delete an entry only when all copies are available and it has no transaction history |
| Lending | `POST /transaction/apis/borrow` | Borrow one available copy with an unexpired card; returns the transaction with its borrow date |
| Lending | `POST /transaction/apis/return/{bookId}?cardId=...` | Return a copy borrowed by that card; returns the updated transaction with its latest transaction date and status |
| Lending | `GET /transaction/apis/findAll` | List transaction history, newest first |
| Lending | `GET /transaction/apis/byCard/{cardId}` | List history for a card, newest first |
| Lending | `GET /transaction/apis/byBook/{bookId}` | List history for a book, newest first |

### Example requests

Create a student (`POST /student/apis/save`):

```json
{
  "name": "Asha Patel",
  "email": "asha@example.com",
  "mobile": "9876543210",
  "dept": "Computer Science",
  "sem": "3",
  "gender": "Female",
  "address": "Pune",
  "dob": "2005-04-12"
}
```

Add a book (`POST /book/apis/save`):

```json
{
  "title": "Clean Code",
  "publisherName": "Prentice Hall",
  "publishedDate": "2008-08-01",
  "pages": 464,
  "category": "Software Engineering",
  "rackNo": "A-12",
  "totalCopies": 5
}
```

Each catalogue entry tracks `totalCopies` and `availableCopies`; a book is available whenever `availableCopies` is greater than zero. A successful borrow decreases `availableCopies` by one; returning a copy increases it by one. If `totalCopies` is omitted when creating a book, it defaults to `1`. When updating a book, omit `totalCopies` to leave its inventory unchanged. The API response includes both copy counts.

Borrow a book (`POST /transaction/apis/borrow`). If `dueDate` is omitted or blank, it defaults to 14 days from today. Provide dates in `YYYY-MM-DD` format; past due dates are rejected.

```json
{
  "cardId": 1,
  "bookId": 1,
  "dueDate": "2026-11-02"
}
```

The service records each loan as one transaction with `borrowDate`, `dueDate`, `transactionDate`, and `transactionStatus` (`BORROWED` or `RETURNED`). `borrowDate` preserves when the loan began. `transactionDate` is set when borrowed and updated to the return date and time when returned. Borrow and return endpoints return the transaction record as JSON.

## Behavior and errors

- Student creation automatically creates a library card with a three-year expiry.
- Card eligibility is determined by its expiry date: borrowing and returning are allowed through the expiry date. Renewal accepts 1–5 years and extends from the later of today or the current expiry date.
- Books can only be deleted when available and with no transaction history.
- Student creation, book creation, and borrow/return requests return short success messages. Borrow/return validation failures such as a missing card ID or no available copies return a plain-text message with the corresponding HTTP status.
- Borrow and return update the book and save a transaction record in separate repository operations. These service methods do not use `@Transactional`; if saving the transaction fails, the book update may already have been saved.

## Project layout

- `controller/` — REST endpoints and exception handling
- `service/` — student, card, book, and lending business logic
- `repository/` — Spring Data JPA repositories
- `model/` — JPA entities
- `requestdto/` — JSON request data classes
- `src/main/resources/application.properties` — MySQL connection and JPA configuration
