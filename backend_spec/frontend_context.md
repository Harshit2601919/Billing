# Frontend Context: Integration for API

The backend must be designed to eventually serve the frontend files located in the `frontend/` directory (Pure HTML, CSS, Tailwind, Bootstrap).

## Design System Reference
- **Theme:** "Luminous Ledger" (Deep Navy/Purple Dark Mode)
- **Primary Color:** Electric Violet (`#6C5CE7`)
- **Secondary Color:** Cyan (`#00D2FF`)

## Frontend Pages to Integrate
- `01-login-signin.html`
- `02-register-signup.html`
- `03-dashboard.html` (Primary navigation hub)
- `04-partner-management.html` (Location info only)
- `05-create-bill.html` (Dynamic row logic)
- `06-bills-list.html`
- `07-bill-detail.html` (PDF Generation reference)

## Integration Points
1. **Dynamic Totals:** Post data from `05-create-bill.html` after the user adds dynamic items.
2. **PDF generation:** `07-bill-detail.html` should be used as the visual design reference for the iText 7 PDF service.
3. **CORS:** Ensure the backend (`localhost:8080`) allows cross-origin requests from the current frontend host (`localhost:3000`).
