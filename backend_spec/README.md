# README: Backend Implementation Instructions (Claude Core)

This folder contains the complete technical specification for building the **BillFlow Billing Management Backend**. It is designed for a **Spring Boot 3.x + MySQL** stack.

## Architecture
You must develop a RESTful API that handles authentication, partner records, and billing with itemized PDF generation.

## Key Requirements
1. **Security:** Use Spring Security and JWT for stateless authentication.
2. **Database:** Implement the schema provided in `database_schema.sql`.
3. **Sequential Numbering:** A custom service must generate sequential bill numbers (e.g., `BILL-001`, `BILL-002`) based on existing counts.
4. **PDF Generation:** Use iText 7 to generate a PDF matching the existing HTML template logic (`07-bill-detail.html`).
5. **CORS:** Explicitly enable requests from `http://localhost:3000` to allow the current frontend to communicate with your APIs.

## How to Proceed
Start by generating the project structure (Maven), then implement the domain models, followed by the security layer, and finally the business services. 

---
© 2024 BillFlow Project.
