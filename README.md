# ☕ Brewkery — Native Android App

A native Android coffee & bakery ordering application in Kotlin built for the **Clickretina Android Developer Take-Home Assessment**.

The app is powered by the Brewkery REST API, providing a seamless four-screen ordering flow with dynamic customizations, live cart calculations, store announcement / order tracking status, and clean MVVM architecture.

---

## 📱 Features & Screen Flow

### Screen 1: Home / Menu Catalog (`MenuScreen`)
- **Branding & Cart Badge**: Header displaying the "BK" artisan logo and a shopping bag with live dynamic badge counter.
- **Dynamic Store Info & Active Order Tracking**:
  - *Default State*: 🛵 **STORE INFO** — "Delivery in 20 - 30 mins", "$2.50 flat fee", "Open".
  - *Active Order State*: 🟢 **ACTIVE ORDER** — Displays `#BK-XXXXX`, "Preparing (Arriving in 20 - 30 mins)", and a direct "Track" action button to view the active ticket.
- **Real-time Search Bar**: Filter items instantly by roast title, description, or ingredients (e.g., espresso, cold brew, almond, croissant).
- **Horizontal Category Pills**: "All Items", "☕ Hot Coffee", "🧊 Cold Brews", and "🥐 Artisan Bakery".
- **Product Catalog Cards**: Hero thumbnails (loaded via Coil), item badges (`BESTSELLER`, `STAFF PICK`, `FRESHLY BAKED`, `SEASONAL`, `POPULAR`, `NITRO ON TAP`), ratings with review counts, preparation time, and base price.
- **Floating Cart Bar**: Automatically appears at the bottom whenever the cart has items, displaying item quantity, running subtotal, and quick navigation to checkout.
- **Network State Handling**: Clean loading spinner ("Brewing your menu...") and user-friendly error views with a retry mechanism.

### Screen 2: Item Detail & Customizer (`ItemDetailScreen`)
- **Hero Image & Badges**: Full-width item image with rounded geometry and status badge overlay.
- **Product Details & Key Ingredients**: Full product story and ingredient chips.
- **Dynamic Customization Matrix**:
  - **Sizes**: Size selector (e.g., *Tall (8 oz)*, *Grande (12 oz) +$0.65*, *Venti (16 oz) +$1.25*).
  - **Milk Options / Spreads**: Milk blend selector (e.g., *Oat Milk*, *Roasted Almond Milk +$0.50*, *Whole Milk*).
  - **Sugar Levels / Serving**: Sweetness selector (e.g., *0% Unsweetened*, *50% Mild*, *100% Standard Sweet*).
- **Quantity Stepper**: Stepper (`—` `qty` `+`) with a minimum quantity of 1.
- **Real-time Dynamic Price Calculation**: `(base_price + size_extra + milk_extra) * quantity` recalculated dynamically on the "Add to Cart" action button.
- **Favorite Action**: Interactive heart toggle supporting item bookmarking.

### Screen 3: Cart & Checkout (`CartScreen`)
- **In-Memory Cart Management**: List of items grouped with selected size, milk, and sugar level specifications.
- **Line Item Adjustment**: Increment or decrement item quantities directly; reducing an item to 0 automatically removes it from the cart.
- **Clear Cart Action**: Quick reset button to empty the in-memory cart.
- **Bill Breakdown**:
  - **Subtotal**: Sum of line items.
  - **Delivery Fee**: $2.50 flat fee.
  - **Est. Tax**: Calculated at 8.0% of subtotal.
  - **Total Payable**: Subtotal + Delivery Fee + Estimated Tax.
- **Order Placement**: "Place Order Now" action button that validates the order, clears the cart, generates a ticket ID, and transitions to the order confirmation screen.

### Screen 4: Order Status (`OrderStatusScreen`)
- **Celebratory Confirmation**: Coffee mug illustration with animated feedback and "Brewing in Progress!" status.
- **Live Ticket Card**:
  - Generated Order Ticket Reference: `#BK-XXXXX` in monospace font.
  - Pulse Badge: `PREPARING` badge.
  - Estimated Wait Time: `20 - 30 minutes`.
  - Items Count: Total items ordered.
  - Status Message: "Barista accepted your order!".
- **Back to Menu Action**: Returns to the home catalog, where the top store banner transitions to display active order tracking.

---

## 🏗️ Architecture & Technology Stack

- **Language**: Kotlin 1.9.23
- **UI Framework**: Jetpack Compose with Material 3
- **Design System**: Custom Brewkery color palette (Espresso `#140B07`, Terracotta `#D9532F`, Crema `#FDFAF7`, Warm Border `#EBD8CB`, Caramel `#F59E0B`)
- **Architecture**: Clean MVVM (Model-View-ViewModel) with Unidirectional Data Flow (UDF)
  - `BrewkeryViewModel`: Manages `StateFlow<BrewkeryUiState>` for menu catalog, filtering, customizations, cart calculations, and active orders.
  - `BrewkeryRepository`: Repository layer decoupling data sources from UI components.
- **Networking**:
  - Retrofit 2.11.0 + Gson Converter
  - OkHttp 4.12.0 with HTTP logging interceptor
  - Coroutines & `Dispatchers.IO` for asynchronous, non-blocking requests
  - Offline fallback bundled asset (`data.json`) to ensure high availability
- **Image Loading**: Coil Compose 2.6.0 with crossfade transitions and disk caching
- **Navigation**: Jetpack Navigation Compose 2.7.7
- **Testing**: JUnit 4 unit tests verifying cart calculations, tax/delivery formula, and ticket ID generation

---

## 🤖 How I Used AI (AI-First Development Workflow)

As part of Clickretina's AI-first philosophy, artificial intelligence tools were leveraged throughout the development lifecycle to accelerate architecture design, UI fidelity, and testing.

### 1. Which AI Tools Were Used
- **Claude & Gemini**: Used for initial architectural scaffolding, state modeling, Kotlin coroutine flow structure, and edge-case validation.
- **Cursor / GitHub Copilot**: Used for rapid inline Jetpack Compose UI component composition, Material 3 theming, and unit test generation.

### 2. Actual Prompts Sent
- **Prompt 1 (Data & Pricing Model Scaffolding)**:
  > *"Analyze the Brewkery `data.json` API schema and design a Kotlin data model and ViewModel for the cart. The cart items must support dynamic size and milk extra price additions, and calculate subtotal, a flat $2.50 delivery fee, 8% sales tax, and the final total payable."*
- **Prompt 2 (Interactive State & Order Lifecycle)**:
  > *"In Jetpack Compose, build a store banner component on the Menu screen that dynamically swaps between the default store delivery info and an active order tracking card (showing ticket ID `#BK-XXXXX`, arriving wait time, and a Track button) once an order is placed from the cart."*
- **Prompt 3 (Customization Math & Verification)**:
  > *"Write JUnit 4 unit tests for Brewkery's cart calculations verifying that an item with base price $4.85, Grande size +$0.65, and Almond milk +$0.50 computes to $6.00 unit price, and for 2 items calculates $12.00 line total, with 8% tax and $2.50 delivery fee accurately computed."*

### 3. What AI Got Right vs. What AI Got Wrong & How It Was Fixed
- **What AI Got Right**:
  - AI accurately extracted the nested customization JSON structure (`sizes`, `milk_options`, `sugar_levels`) and formulated the formula `(basePrice + sizeExtra + milkExtra) * qty`.
  - It mapped the Tailwind CSS styling from the HTML prototype into an exact Material 3 Compose theme (Espresso, Terracotta, and Crema colors).
- **What AI Got Wrong**:
  - *The Issue*: The initial AI suggestion attempted to combine cart items purely by matching `item.id`. However, in a coffee app, two items of the same coffee (e.g. *Toasted Caramel Macchiato*) could have different customized sizes or milk options (e.g., one *Tall Oat Milk* and one *Grande Almond Milk*). Merging them purely by `menuItemId` overwrote the user's customizations.
  - *How It Was Fixed*: I corrected the deduplication logic in `BrewkeryViewModel.addToCart` to match both `menuItemId` **and** the specific selected configuration (`selectedSize.id`, `selectedMilk?.id`, `selectedSugar`). If the customizations match, the quantity increments; if they differ, a new distinct `CartItem` entry is appended.

---

## 🚀 Building & Running the Project

### Prerequisites
- Android Studio Hedgehog / Iguana / Jellyfish (or newer)
- JDK 17 (recommended: OpenJDK 17)
- Android SDK Platform 34 and Build-Tools 34.0.0

### Run via Command Line
1. Clone the repository:
   ```bash
   git clone <REPO_URL>
   cd Brewkery
   ```
2. Build the Debug APK:
   ```bash
   ./gradlew assembleDebug
   ```
   The generated APK will be located at:
   `app/build/outputs/apk/debug/app-debug.apk`

3. Run Unit Tests:
   ```bash
   ./gradlew testDebugUnitTest
   ```

### Run via Android Studio
1. Open Android Studio.
2. Select **File &rarr; Open** and select the `Brewkery` folder.
3. Allow Gradle to sync.
4. Select an emulator or physical Android device (API 24+) and click **Run (Shift+F10)**.

---

## 🧪 Unit Testing

Unit tests are included under `app/src/test/java/com/clickretina/brewkery/CartCalculationTest.kt`:
- `testSingleItemCustomizationPriceCalculation()`: Validates size and milk add-ons with multiplier.
- `testCartSubtotalTaxAndDeliveryCalculation()`: Validates multi-item cart, $2.50 flat delivery fee, and 8.0% tax.
- `testEmptyCartProducesZeroTotals()`: Validates boundary state when cart is cleared.
- `testActiveOrderTicketIdFormat()`: Validates ticket reference code `#BK-XXXXX`.

---

## 📦 REST API Endpoints
- **Full Menu & Store Config**:
  `https://raw.githubusercontent.com/VivekShah138/Brewkery/main/data.json`
- **Single Item Detail**:
  `https://raw.githubusercontent.com/VivekShah138/Brewkery/main/api/items/{id}.json`
