# Neurocom safe-card App (Assignment)


### **Goal:**

Build a small full-stack app where users can submit a card number and cardholder name. The backend must encrypt the card number at rest and provide an API that allows the UI to search for cards without ever returning or storing the plaintext PAN.



### Frontend (React):

* Simple UI to create a card record with fields for Cardholder Name and Card Number (PAN).
* Search bar to find existing cards by full PAN (exact match) and last 4 digits.
* Display results with Cardholder Name, masked PAN (e.g., 4385 12 ** 4342), and Created Time.
* Never display or log the full PAN in the UI or browser console.

### Backend (Java):

* Endpoints to create and search cards.
* Encrypt PANs at rest using AES GCM with a random IV per record.
* Create a searchable index of the PAN in an unreadable format.
* Persist data in any local DB (H2, Postgres, or in-memory for simplicity).

### Security Constraints:

* Plaintext PAN must never be stored or logged.
* Use strong randomness for IVs.
* Validate PAN using Luhn and basic length checks (12–19 digits).
* Return only masked PAN to the client.