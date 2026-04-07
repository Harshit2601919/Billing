# Design System Specification: BillFlow — The Luminous Ledger

## 1. Overview & Creative North Star
**Creative North Star: "The Neon Architect"**

This design system transcends the typical "SaaS dashboard" aesthetic by treating financial data as a high-end, architectural experience. The system is built on the principle of **Atmospheric Depth** — moving away from flat, rigid grids toward a digital environment that feels layered, luminous, and intentionally curated.

**Stitch Project ID:** `2720119582694389139`
**Design System Asset:** `assets/d53ec3618c5846e8bb0d34d33a331fe5`

---

## 2. Colors & Surface Soul

| Token | Hex | Role |
|-------|-----|------|
| `primary` | `#c6bfff` | Primary text/icon on dark |
| `primary_container` | `#6c5ce7` | **Brand Violet — buttons, accents** |
| `secondary` | `#a5e7ff` | Secondary text/icon |
| `secondary_container` | `#00d2ff` | **Cyan Accent — highlights, progress** |
| `surface_dim` | `#12121d` | **Page background** |
| `surface_container` | `#1f1e2a` | Section backgrounds |
| `surface_container_low` | `#1b1a26` | Sidebar, input fields |
| `surface_container_high` | `#292935` | Elevated cards |
| `surface_container_highest` | `#343440` | Active/interactive elements |
| `on_surface` | `#e3e0f1` | Primary text on dark |
| `on_surface_variant` | `#c8c4d7` | Secondary/muted text |
| `outline_variant` | `#474554` | Ghost borders (15% opacity) |
| `error` | `#ffb4ab` | Error states |
| `tertiary` | `#ffb77d` | Warning/amber states |

### Design Rules
- **No-Line Rule:** No 1px solid borders. Use background shifts exclusively.
- **Luminous Gradient:** `#6C5CE7` → `#00D2FF` at 135° for primary CTAs and hero elements.
- **Glassmorphism:** 60% opacity `surface_container_low` + 20px backdrop-blur.

---

## 3. Typography
- **Font Family:** Inter (Google Fonts)
- **Display:** Bold, `-0.02em` letter-spacing — for KPIs and totals
- **Headline:** Bold — section headers
- **Body:** Regular, `on_surface_variant` color — for readability
- **Label:** Uppercase, `0.05em` letter-spacing — for table headers

---

## 4. Shape & Elevation
- **Cards:** 16px border-radius
- **Buttons:** 12-16px border-radius, pill shape for primary CTAs
- **Ambient Shadows:** 40px blur, 6% opacity using `#c6bfff`
- **Ghost Borders:** `outline_variant` at 15% opacity

---

## 5. Screens

| # | Screen | File | Screen ID |
|---|--------|------|-----------|
| 1 | Sign In | `01-login-signin.html` | `c4a12272c069485f93ea98dbf71d4183` |
| 2 | Sign Up | `02-register-signup.html` | `c14081cec9f14a72b279f57e417069d4` |
| 3 | Dashboard | `03-dashboard.html` | (from step 94) |
| 4 | Partner Management | `04-partner-management.html` | (from step 99) |
| 5 | Create Bill | `05-create-bill.html` | (from step 102) |
| 6 | Bills List | `06-bills-list.html` | (from step 105) |
| 7 | Bill Detail & PDF Preview | `07-bill-detail.html` | (from step 108) |

---

## 6. Component Patterns

### Buttons
- **Primary:** Luminous Gradient (Violet → Cyan), no border, white text, 16px rounded
- **Secondary:** `surface_container_high` bg, ghost border in `primary`
- **Hover:** Emit subtle `primary` outer glow (5px blur)

### Cards
- `surface_container_lowest` body, no dividers, 1.5rem vertical spacing
- Glass variant: 1px inner-border at 30% opacity (top + left only)

### Input Fields
- `surface_container_low` background, 16px rounding
- Active: 1px `primary` glow stroke, cyan caret

### Status Badges
- **Paid:** Cyan (#00D2FF) pill
- **Pending:** Amber (#FDCB6E) pill
- **Overdue:** Coral (#FF6B6B) pill
