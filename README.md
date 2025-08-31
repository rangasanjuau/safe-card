# Neurocom safe-card App (Assignment)


### **Goal:**

Build a small full-stack app where users can submit a card number and cardholder name. The backend must encrypt the card number at rest and provide an API that allows the UI to search for cards without ever returning or storing the plaintext PAN.



### Frontend (React):

* Simple UI to create a card record with fields for Cardholder Name and Card Number (PAN).✅
* Search bar to find existing cards by full PAN (exact match) and last 4 digits.✅
* Display results with Cardholder Name, masked PAN (e.g., 4385 12 ** 4342), and Created Time.✅
* Never display or log the full PAN in the UI or browser console.✅

### Backend (Java):

* Endpoints to create and search cards.✅
* Encrypt PANs at rest using AES GCM with a random IV per record.✅
* Create a searchable index of the PAN in an unreadable format.✅
* Persist data in any local DB (H2, Postgres, or in-memory for simplicity).✅

### Security Constraints:

* Plaintext PAN must never be stored or logged.✅
* Use strong randomness for IVs.✅
* Validate PAN using Luhn and basic length checks (12–19 digits).✅
* Return only masked PAN to the client.✅



## **API Testing Instructions**

You can test the Card Vault API using **Swagger UI**, **Postman**, or **curl**.

---

#### **1. Using Swagger UI**

1. Start your Spring Boot application:

```bash
mvn spring-boot:run
```

If you need ready made test data in the database, you can start your Spring Boot application with **test** profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

2. Open your browser:

```bash
http://localhost:8080/swagger-ui/index.html
```


3. Use the Swagger UI to interact with the API endpoints:
   - **Create Card**: Use the `POST /api/cards` endpoint to create a new card.
   - **Search Card by Full PAN**: Use the `GET /api/cards/search?fullPan={fullPan}` endpoint to search for a card by its full PAN.
   - **Search Card by Last 4 Digits**: Use the `GET /api/cards/search?last4={last4}` endpoint to search for cards by their last 4 digits.


---

## ** Luhn Valid PAN examples**
```bash
4539 1488 0343 6467
4556 7375 8689 9855
6011 6011 6011 6611
```
