# koobe

> **⚠️ Work in Progress** — This project is actively under development and is not yet feature-complete. Expect breaking changes, missing screens, and incomplete functionality. Contributions and feedback are welcome, but please keep in mind the app is far from its final state.

---

A personal finance management app for Android, built with Jetpack Compose and Clean Architecture.

---

## About

Koobe is a personal finance tracker designed to give users a clear picture of their income, expenses, budgets, and spending habits — all in one place.

The app is being built with a strong focus on architecture quality, design system consistency, and a smooth, intuitive user experience.

---

## Features (planned)

The following screens and flows are planned or partially implemented:

- **Dashboard** — overview of balance, monthly expenses/income, budgets, shortcuts, and spending insights
- **Transaction Editor** — add and edit income/expense transactions with category, date, amount, payment method and currency
- **Historic** — browseable transaction history with 3-level expandable breakdown (category → subcategory → transaction)
- **Budgets** — create and track spending budgets by period (daily, weekly, monthly, yearly), with projections
- **Reports** — visual charts and trends for balance, categories, and payment methods
- **Category & Subcategory Manager** — create and customise expense/income categories with icons and colours
- **Shortcuts** — predefined quick-entry transactions for recurring expenses
- **Settings** — theme (light/dark/system), language, default currency, and first day of the week

---

## Tech stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.x |
| UI | Jetpack Compose |
| Architecture | Clean Architecture + MVVM + MVI |
| DI | Hilt |
| Local DB | Room |
| Preferences | DataStore |
| Async | Kotlin Coroutines + Flow |
| Navigation | Compose Navigation (type-safe routes) |

---

## Project structure

```
com.jorgelobo.koobe/
├── ui/
│   ├── screen/                  # Screens, ViewModels, and UiState per feature
│   ├── components/
│   │   ├── base/                # Low-level reusable primitives (buttons, inputs, dialogs…)
│   │   ├── composed/            # Higher-level, screen-ready widgets
│   │   └── common/              # Shared utility composables
│   ├── theme/                   # Design system (colors, typography, spacing, shapes)
│   └── navigation/              # Route definitions and NavGraph
│
├── domain/
│   ├── model/                   # Domain models and enums
│   ├── repository/              # Repository interfaces
│   └── usecase/                 # Business logic use cases
│
├── data/
│   ├── local/                   # Room database, DataStore, default seed data
│   └── repository/              # Repository implementations
│
├── di/                          # Hilt dependency injection modules
│
└── utils/                       # Stateless helpers and extensions
    └── date/

```

---

## Development status

This project is in early-to-mid development. Several screens have been designed and partially implemented, but many flows are still incomplete or untested.

**What's done (or mostly done):**
- Design system (colors, typography, spacing, component library)
- Base and composed component library (35+ base components, 20+ composed)
- Core domain models and use cases
- Dashboard screen structure
- Historic screen with expandable transaction list
- Category selector flow
- Transaction editor screen

**What's still pending:**
- Dashboard data binding (structure built; no real data yet)
- Category manager and editor screens
- Subcategory manager and editor screens
- Shortcut manager and editor screen
- Dark theme (tokens exist; distinct palette not yet applied)
- Reports screen charts and data binding
- Budget projections and notifications
- Full navigation graph integration
- Unit and UI tests
- Accessibility audit
- Full localization (English and Portuguese planned)

---

## Getting started

> Prerequisites: Android Studio Hedgehog or later, JDK 17+, Android SDK 26+.

```bash
git clone https://github.com/jorgelobo/koobe.git
cd koobe
```

Open the project in Android Studio and run on an emulator or physical device (API 26+).

There is no remote backend — the app runs fully offline using a local Room database.

---

## Contributing

The project is being shared publicly for visibility, not as an open-source collaboration project at this stage. That said, if you spot something worth flagging — a bug, an architectural concern, or a suggestion — feel free to open an issue.

---

## License

Apache-2.0 license

---

*Built by Jorge Lobo*
