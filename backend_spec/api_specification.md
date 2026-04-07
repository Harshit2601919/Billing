# API Specification: BillFlow REST API

Base URL: `http://localhost:8080/api`

---

## Authentication (`/auth`)
- **POST `/signup`**
  - Payload: `{ username, password, email }`
  - Action: Register and secure password with BCrypt.
- **POST `/login`**
  - Payload: `{ username, password }`
  - Response: `{ token, type: "Bearer" }`

---

## Partners Management (`/partners`)
- **GET `/`**
  - Output: List of all Active Partners.
- **POST `/`**
  - Payload: `{ name, location }`
  - Note: Location is exactly as previously captured (city name/area).
- **DELETE `/{id}`**
  - Action: Deletes correctly if no Bills are attached.

---

## Billing Logic (`/bills`)
- **GET `/`**
  - Action: Paginated list of Bills (ordered by date/desc).
- **GET `/{id}`**
  - Output: Full Bill with the nested BillItem collection.
- **POST `/`**
  - Payload:
    ```json
    {
      "partnerId": "uuid",
      "date": "2024-04-03",
      "items": [
        { "description": "Item 1", "quantity": 10, "unitPrice": 1500.00 }
      ]
    }
    ```
  - Action: Auto-generate `billNumber` sequentially (e.g. `BILL-001`).
- **GET `/{id}/pdf`**
  - Output: `application/pdf` binary.
  - Action: Render a professional invoice based on the Bill details.

---

## Security Context
All endpoints EXCEPT `/auth/**` require an `Authorization: Bearer <JWT>` header.
Enable Global CORS policy for `localhost:3000`.
