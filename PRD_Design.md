# 🎨 Queue Manager – Design PRD (v1.0)
**For Figma + Material Design 3**  
**Target platform:** Android (Kotlin + Jetpack Compose)  
**Language:** English only  
**Queue model:** Single queue per service  
**Notifications:** Push only (FCM)  

---

## 1. Brand Identity (Proposed)

### 1.1 Color Palette

| Role                | Color Name        | Hex                   | Usage                                         |
| ---------------------| -------------------| -----------------------| -----------------------------------------------|
| Primary             | Deep Indigo       | `#1A237E`             | Buttons, active states, app bar, selected tab |
| Primary Container   | Indigo 50         | `#E8EAF6`             | Background for chips, light surfaces          |
| Secondary           | Teal 700          | `#00796B`             | Progress indicators, links, secondary actions |
| Secondary Container | Teal 50           | `#E0F2F1`             | Light secondary backgrounds                   |
| Tertiary            | Orange 700        | `#F57C00`             | Urgent alerts, "Your Turn", critical warnings |
| Error               | Red 600           | `#D32F2F`             | Errors, cancellations, no‑show                |
| Success             | Green 700         | `#2E7D32`             | Confirmation, served, success toasts          |
| Surface             | White / Dark Grey | `#FFFFFF` / `#121212` | Cards, backgrounds (light/dark mode)          |
| Outline             | Grey 400          | `#BDBDBD`             | Borders, dividers                             |
| Text Primary        | Near Black        | `#1F1F1F`             | Body text, labels                             |
| Text Secondary      | Grey 700          | `#616161`             | Hints, timestamps, secondary info             |

### 1.2 Typography (Material 3 scale)

| Style | Font | Weight | Size (sp) | Line height (dp) | Usage |
|-------|------|--------|-----------|------------------|-------|
| Headline Large | Roboto | Bold | 32 | 40 | Onboarding title |
| Headline Medium | Roboto | Bold | 28 | 36 | Screen titles |
| Title Large | Roboto | Medium | 22 | 28 | Section headers |
| Title Medium | Roboto | Medium | 18 | 24 | Card titles |
| Body Large | Roboto | Regular | 16 | 24 | Primary text |
| Body Medium | Roboto | Regular | 14 | 20 | Secondary text, list items |
| Label Large | Roboto | Medium | 14 | 20 | Buttons, tabs |
| Label Small | Roboto | Regular | 12 | 16 | Timestamps, helper text |

### 1.3 Iconography

- Use **Material Symbols** (outlined for default, filled for active states)
- Size: 24dp for standard icons, 20dp for compact areas
- Stroke weight: 2px

### 1.4 Logo & Wordmark

- **Logo mark:** Stylized letter "Q" formed by two interlocking arrows (represents queue flow)
- **Wordmark:** "Queue Manager" in Roboto Medium, letter spacing +0.5
- Provide in light and dark variants

### 1.5 Spacing & Grid

- **Base unit:** 8dp
- **Screen margins:** 16dp left/right
- **Card padding:** 16dp
- **Between elements:** 8dp, 16dp, 24dp
- **Grid:** 4‑column on small screens (up to 360dp), 8‑column for larger

---

## 2. Design Principles

1. **Real‑time clarity** – Queue position and wait time must be instantly visible.
2. **One primary action per screen** – Avoid overwhelming users.
3. **Gentle urgency** – Use orange only for "your turn" states.
4. **Accessibility first** – Minimum touch target 48dp, contrast ratio ≥4.5:1.
5. **Dark mode ready** – All colors have dark equivalents (using Material 3 dynamic theming).

---

## 3. Customer App – Screens & Components

### 3.1 Onboarding & Authentication

#### Screen: Splash
- **Content:** Logo + wordmark centered, loading indicator (circular)
- **Duration:** 1.5s then auto‑navigate to Welcome
- **Edge case:** First launch → Welcome screen; logged in → Home

#### Screen: Welcome
- **Purpose:** Introduce value proposition
- **Elements:**
  - Illustration: People skipping a physical line (custom illustration)
  - Headline: "Skip the line, join from anywhere"
  - Subhead: "Join queues remotely, get real‑time updates"
  - Two large buttons: `Sign Up` (filled, primary) | `Log In` (outlined)
- **Footer:** "Continue as guest" (text button – limited functionality)

#### Screen: Sign Up (Step 1 – Credentials)
- **Fields (with validation):**
  - First name (required)
  - Last name (required)
  - Email (required, email format)
  - Phone (required, numeric, 10‑15 digits)
  - Password (required, min 8 chars, show/hide toggle)
  - Confirm password
- **Role selector:** Segmented button (Customer / Business Owner) – **default: Customer**
- **Action:** `Create Account` (filled)
- **Footer:** "Already have an account? Log in" (text link)

#### Screen: Sign Up (Step 2 – Verification)
- **Content:** "We sent a 6‑digit code to [email]"
- **Input:** 6 OTP fields (auto‑focus, auto‑submit)
- **Resend timer:** 60 seconds countdown with "Resend code" button
- **Action:** After OTP, auto‑verify → navigate to Language Selection (first time only)

#### Screen: Language Selection
- **Note:** Only English is required per your answer, but design can include AR/FR as disabled/coming soon. Or skip entirely.
- **Recommendation:** Show single option "English (United States)" selected, with a note "More languages coming soon". Then go to Permissions.

#### Screen: Permissions Request
- **Sequence:**
  1. **Location** – "Allow Queue Manager to access your location to find nearby businesses?" (Allow / Deny)
  2. **Camera** – "Allow camera to scan QR codes?" (Allow / Deny)
  3. **Notifications** – "Stay updated – get real‑time queue alerts" (Allow / Deny)
- Each permission shown as a bottom sheet with illustration.
- **Action:** After all, navigate to Home.

#### Screen: Login
- **Fields:** Email, Password (show/hide)
- **Options:** `Forgot Password?` (text link)
- **Action:** `Log In` (filled)
- **Footer:** "Don't have an account? Sign Up"

#### Screen: Forgot Password
- **Step 1:** Enter email → "Send reset link"
- **Step 2:** OTP verification (same as sign up)
- **Step 3:** New password + confirm → "Reset password"
- **Success message** then navigate to Login.

---

### 3.2 Customer Home & Discovery

#### Screen: Home (Main Dashboard)
**Structure (top to bottom):**

1. **Top App Bar**
   - Left: Avatar (or placeholder icon) – tap to open Profile
   - Center: Logo / wordmark
   - Right: Notifications icon (badge count if unread)

2. **Search Bar** (filled surface)
   - Leading icon: Search (outlined)
   - Hint: "Search business or service..."
   - Trailing: Mic (optional) or Clear (when typing)
   - Tap → expands to full‑screen search (see Search screen)

3. **Category Chips** (horizontal scroll)
   - Chips: All (selected), Banks, Pharmacies, Hospitals, Restaurants, Salons, Government, etc.
   - Each chip has icon + text.

4. **Nearby Businesses** (horizontal scrollable row)
   - Title: "Near you" + `View all` (text link)
   - Each card:
     - Business logo (circle, 40dp)
     - Business name (Title Medium)
     - Distance (e.g., "0.3 km")
     - Queue status: short wait / medium wait / long wait (color‑coded dot)
     - Estimated wait time (e.g., "~12 min")
   - Tap → Business Details screen

5. **Favorites** (if any)
   - Title: "Your favorites" + `View all`
   - Same card style as above but with heart icon filled.

6. **Map View** (compact, 200dp height)
   - Shows nearby businesses as markers (color = queue load)
   - Tap marker → shows bottom sheet with business name + "View" button.
   - Button: `Open full map` (text link) → opens full‑screen Map screen.

**States:**
- **Loading:** Skeleton placeholders for all rows.
- **Empty:** "No businesses found nearby. Try changing location or search."
- **Offline:** Banner: "You're offline. Showing cached results."

#### Screen: Search (Full screen)
- **Triggered** by tapping search bar on Home.
- **Elements:**
  - Back arrow (close search)
  - Search input field (auto‑focus, keyboard opens)
  - Recent searches (list with clock icon, X to delete)
  - Popular searches (list with fire icon)
  - Search results appear as user types (debounced 300ms)
- **Result item:** Same as business card in Home.
- **No results:** Illustration + "No businesses found. Try different keywords."

#### Screen: Business Details
**Layout:**

1. **Header image** (optional, can be placeholder) with overlay gradient.
   - Back arrow (top left)
   - Favorite icon (top right, heart outlined/filled)

2. **Business info**
   - Name (Headline Medium)
   - Rating (stars + number of reviews, e.g., 4.5 ★ (128))
   - Category, address, distance, open/closed status (chip)

3. **Queue status card** (prominent, Material 3 card)
   - Current ticket being served (e.g., "Now serving: #42")
   - People ahead (e.g., "You have 3 people ahead" if not in queue)
   - Estimated wait time (large text, e.g., "~15 min")
   - Button: `Join Queue` (filled, primary) – disabled if queue closed/paused.

4. **Services list** (if business has multiple)
   - Each service: name + average duration + "Join" button (small, outlined)
   - Tapping "Join" on a service joins that specific service's queue.

5. **Opening hours** (list of days)
6. **Location** (static map thumbnail + address, tap for navigation)
7. **Reviews summary** (3 latest reviews with avatar, rating, comment, "See all")

**States:**
- **Already in queue:** Show "You are in queue" card with current position, wait time, and "View my ticket" button (navigates to My Queue screen).
- **Queue closed/paused:** Disable Join button, show chip "Queue paused" or "Closed today".

#### Screen: Full Map
- Full‑screen Google Map with business markers.
- Bottom sheet when marker tapped: business name, queue wait, ETA, `View` button.
- User location shown (blue dot).
- Search bar at top (same as home search).

---

### 3.3 Queue Joining & Monitoring

#### Screen: Join Queue (Modal or screen)
- **Triggered** after tapping "Join Queue".
- **Content:**
  - Animation: ticket printing effect
  - Ticket number (large, e.g., "#45")
  - Position in queue (e.g., "You are 5th in line")
  - Estimated wait time (e.g., "About 18 minutes")
  - Business name and service name
  - Button: `Got it` (primary)
- **After dismiss:** Navigate to My Queue screen.

#### Screen: My Queue (Real‑time)
**Persistent for active reservation.**

**Layout:**

1. **Top app bar** with title "My Queue", back arrow (if deep link)

2. **Ticket card** (large, elevated)
   - Ticket number (e.g., "#45") – big, bold
   - Business name + service
   - Position indicator (e.g., "Position: 3")
   - People ahead count (e.g., "2 people ahead of you")
   - Estimated wait time (large text, updates in real time)

3. **Progress bar** (Material 3 linear progress indicator)
   - Shows approximate progress based on people ahead / total (if total known)

4. **Action buttons** (two outlined, side by side)
   - `I will be late` – triggers confirmation dialog
   - `Cancel ticket` – triggers confirmation dialog

5. **Live queue status** (optional expandable)
   - Shows last 5 served tickets (e.g., "Now serving: #42", "Last: #41, #40...")

**Real‑time behavior:**
- Position and wait time update automatically via Firestore listener.
- When position ≤ 2: Show a distinct banner (orange background) "Your turn is soon! Get ready."
- When position = 1: Full‑screen modal (or bottom sheet) "Your turn now!" with prominent button "I'm on my way" (dismisses).

**State:**
- **Delayed:** After tapping "I will be late", show updated ticket number (end of queue) and message "You've been moved to the end."
- **Cancelled:** Show confirmation then navigate to Home.

#### Screen: Your Turn Now (Modal)
- **Trigger:** Position becomes 1.
- **Content:**
  - Large orange pulse animation or bell icon
  - "IT'S YOUR TURN"
  - "Please go to the service desk"
  - Button: `Got it` (primary) – dismisses and returns to My Queue (which will soon show "served" status)
- **Note:** After customer is served, the screen will automatically update to "Service completed" then navigate to review prompt.

---

### 3.4 Post‑Service & Reviews

#### Screen: Review Prompt (Bottom sheet)
- **Triggered** after reservation status changes to "served".
- **Content:**
  - "How was your experience at [Business Name]?"
  - 5 stars (tappable, interactive)
  - Text field: "Leave a comment (optional)" (max 500 chars)
  - Buttons: `Submit` (primary), `Remind me later` (text)
- After submit: Thank you message then dismiss.

#### Screen: Past Reservations
- **Access:** Profile → Past Reservations
- **List** of all past reservations (served, cancelled, no‑show) grouped by date.
- Each item:
  - Business name, service, ticket #
  - Date & time
  - Status (Served / Cancelled / No‑show)
  - If served and not reviewed: `Leave a review` button (outlined)
- **Empty state:** "No past reservations yet."

---

### 3.5 Profile & Settings

#### Screen: Profile
- **Header:** Avatar (circle, editable), name, email
- **Menu items** (each navigates to sub‑screen):
  - Personal information (edit name, email, phone)
  - Change password
  - Past reservations
  - Favorites (list of saved businesses)
  - Notifications settings
  - Dark mode toggle (on/off)
  - Language (English only, grayed out)
  - Log out (red text, with confirmation dialog)

#### Screen: Edit Personal Info
- Fields: First name, last name, email, phone
- Save button (enabled only when changed)
- Avatar upload: tap to open gallery/camera

#### Screen: Notifications Settings
- Switches for each type:
  - Queue position updates
  - Your turn soon/now
  - Queue paused/closed
  - Review reminders
  - Promotional messages (future)

---

## 4. Business Owner App – Screens & Components

### 4.1 Onboarding (Business Setup)

#### Screen: Business Setup Wizard (first login)
**Step 1 – Basic info**
- Business name, description, category (dropdown), address (auto‑complete from Google Maps), phone, email

**Step 2 – Location & hours**
- Map picker (pin to confirm lat/lng)
- Opening time / closing time (time pickers)
- Days open (Monday–Sunday toggles)

**Step 3 – Services**
- Pre‑created service named same as business (average duration 15 min, editable)
- Button `+ Add service` – opens dialog: name, avg duration (minutes), is available toggle
- Each service has edit (pencil) and delete (trash) icons

**Step 4 – QR Code**
- Generated QR code displayed (large, centered)
- Buttons: `Download PNG`, `Print` (share intent)
- `Finish` → Dashboard

**Step 5 – Confirmation** (optional splash)

---

### 4.2 Owner Dashboard (Real‑time)

#### Screen: Queue Dashboard
**Layout:**

1. **Top app bar**
   - Business name (dropdown to switch if multiple businesses – future)
   - Notifications icon
   - Settings icon (profile)

2. **Service selector** (segmented buttons if multiple services, otherwise single)

3. **Queue control bar** (chip row)
   - Status indicator (Active / Paused / Closed) with color dot
   - Button: `Queue Controls` (text) – opens bottom sheet (Pause, Close, Set Max Capacity)

4. **Current serving card**
   - "Now serving: Ticket #42"
   - If no one: "No customers in service"

5. **Metrics row** (3 small cards)
   - Waiting: 12
   - Avg wait: 8 min
   - Completed today: 34

6. **Next Customer button** (large, filled, primary) – floating or full‑width

7. **Waiting list** (scrollable)
   - Each row:
     - Ticket number (bold)
     - Customer name (first + last initial, e.g., "Ahmed A.")
     - Wait time so far (e.g., "Waiting 5 min")
     - Status chip (reserved / delayed)
   - Swipe left on row: "Call" (marks as served) – optional

**Real‑time updates:**
- List updates as customers join, leave, or are delayed.
- When Next Customer is tapped, a bottom sheet appears:
  - "Customer #45 – Ahmed Ali"
  - Buttons: `Mark as Served` (green), `Mark as No‑Show` (red), `Cancel` (outlined)

#### Screen: Queue Controls (Bottom sheet)
- **Pause Queue** – toggle (when paused, shows "Resume Queue")
- **Close Queue** – dangerous action (red) – confirmation dialog
- **Set Max Capacity** – number picker (0 = unlimited)
- After action: status chip updates, customers receive push notification.

---

### 4.3 Analytics & Reports

#### Screen: Analytics Overview
- Tabs: Daily / Weekly / Monthly
- Key metrics (cards):
  - Total customers served
  - Average wait time
  - Cancellation rate
  - No‑show rate
- Charts (line/bar) for:
  - Hourly traffic (peak hour highlighted)
  - Wait time trend over period
- `Generate Report` button (opens report screen)

#### Screen: Generate Report
- Report type: Daily / Weekly / Monthly / Custom range (date pickers)
- Include sections: (checkboxes) – Summary, Hourly breakdown, No‑shows, Cancellations
- `Preview` button – shows PDF preview (web view)
- `Download PDF` / `Export CSV` buttons
- File saved to device (trigger download)

---

## 5. Shared Components & Patterns

### 5.1 Bottom Sheets
- Standard height: half screen (for actions)
- Full height for forms (e.g., add service)

### 5.2 Dialogs
- **Confirmation dialog:** Title, message, two buttons (Cancel / Confirm)
- **Alert dialog:** Single button (OK) for errors.

### 5.3 Snackbars & Toasts
- Snackbar for undo actions (e.g., "Ticket cancelled. Undo?")
- Toast for simple confirmations (non‑interactive)

### 5.4 Empty States
- Illustration + title + subtitle + optional action button.

### 5.5 Loading States
- Shimmer effect (skeleton) for lists.
- Circular progress indicator for full‑screen loading.

### 5.6 Error States
- Banner at top: "Something went wrong. Tap to retry."

### 5.7 Real‑time Indicators
- A small "live" dot (pulsing) next to queue positions or wait times.

---

## 6. Navigation Map

### Customer Flow
```
Splash → Welcome → SignUp/Login → Language → Permissions → Home
Home → Business Details → Join Queue → My Queue → Your Turn → Review Prompt → Home
Home → Search → Business Details
Home → Profile → (sub‑screens)
My Queue → I will be late / Cancel ticket (dialogs)
```

### Owner Flow
```
Splash → Login (business role) → Business Setup Wizard → Dashboard
Dashboard → Queue Controls (bottom sheet) → (Pause/Close)
Dashboard → Next Customer → Served/No‑Show
Dashboard → Analytics → Generate Report
```

---

## 7. Dark Mode Specifications

- Use Material 3 dynamic dark theme (surface = `#121212`, primary = lighter tint `#9FA8DA`)
- All illustrations must have dark variants (or use vector with theme‑aware colors)
- Avoid pure black (`#000000`) for surfaces – use `#1E1E1E` for elevated cards

---

## 8. Accessibility (WCAG 2.1 AA)

- **Touch targets:** minimum 48x48dp (buttons, list items)
- **Contrast:** text on background ≥4.5:1 (checked with Figma plugin)
- **TalkBack labels:** every interactive element has a content description (to be added by developer, but designer should note in component)
- **Focus indicators:** visible outline for keyboard navigation (blue `#6200EE`)

---

## 9. Asset Checklist for Designer

| Asset type | Quantity | Notes |
|------------|----------|-------|
| App icon | 1 | Adaptive (foreground + background) |
| Logo (horizontal) | 1 | For splash and home |
| Logo mark (square) | 1 | For notifications |
| Illustrations | 5 | Empty states: no search results, no favorites, no past reservations, no businesses, offline |
| Onboarding illustration | 1 | Welcome screen (skip the line concept) |
| QR code placeholder | 1 | For owner setup |
| Ticket animation | Lottie | Optional, nice to have |
| Icons (Material Symbols) | 30+ | Use from official library |

---

## 10. Next Steps for Designer

1. **Create a Figma file** with two pages: Customer App, Owner App.
2. **Set up color styles & text styles** according to the brand palette and typography above.
3. **Design light mode first** (dark mode after light is approved).
4. **Build a component library** (buttons, cards, chips, bottom sheets, dialogs, list items).
5. **Design each screen** in logical order, using auto‑layout and components.
6. **Create flows** (prototype links) for:
   - Customer joins queue via search
   - Customer scans QR code
   - Owner serves next customer
7. **Export assets** (icons, illustrations) in SVG/PNG and deliver to development.

---

**End of Design PRD**

Now the designer has everything needed to start building high‑fidelity screens in Figma using Material Design 3. If you need any screen wireframes or specific component behavior described in even more detail, let me know.