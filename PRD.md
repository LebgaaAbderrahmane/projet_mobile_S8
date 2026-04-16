# Queue Manager – Complete Product Requirements Document (PRD)

**Version:** 1.0  
**Date:** April 2026  
**Status:** Ready for Development  
**Target Platform:** Android (Kotlin + Jetpack Compose)  
**Backend:** Firebase (Firestore, Auth, FCM, Functions, Storage)  

---

## Table of Contents

1. [Introduction](#1-introduction)  
2. [User Personas & Journeys](#2-user-personas--journeys)  
3. [Functional Requirements](#3-functional-requirements)  
4. [Non-Functional Requirements](#4-non-functional-requirements)  
5. [System Architecture](#5-system-architecture)  
6. [Detailed System Design](#6-detailed-system-design)  
7. [API Specifications](#7-api-specifications)  
8. [UI/UX Design Specifications](#8-uiux-design-specifications)  
9. [Integration Points](#9-integration-points)  
10. [Data Privacy & Compliance](#10-data-privacy--compliance)  
11. [Testing Strategy](#11-testing-strategy)  
12. [Deployment & DevOps](#12-deployment--devops)  
13. [Monitoring & Analytics](#13-monitoring--analytics)  
14. [Future Roadmap](#14-future-roadmap)  
15. [Appendix](#15-appendix)

---

## 1. Introduction

### 1.1 Background
Physical waiting lines cause poor customer experience, wasted time, and operational inefficiencies for service providers (banks, pharmacies, hospitals, restaurants, etc.). Service owners lack real-time visibility into queue flow and data-driven insights for staffing optimization. Customers cannot check queue status remotely.

### 1.2 Product Vision
Queue Manager is a digital virtual queuing system that eliminates physical lines. Customers join queues remotely or via QR code scanning. Service owners manage queues in real time with AI‑powered wait‑time predictions. Everyone receives real‑time updates and turn notifications.

### 1.3 Goals
- Reduce average customer wait time by 30% (target).
- Enable 50 businesses to adopt the platform within 3 months.
- Achieve a customer satisfaction rating (NPS) > 50.
- Maintain 99.9% system uptime.

### 1.4 Scope
**In scope (MVP v1.0):**
- Customer registration, login, profile management.
- Business discovery (search, category, map).
- QR code scanning for instant queue entry.
- Remote queue reservation.
- Real‑time queue position & wait time updates.
- Push notifications (FCM) for turn alerts.
- “I will be late” postponement.
- Queue cancellation.
- Favorite businesses.
- Post‑service rating & review.
- Owner business profile setup.
- Owner real‑time dashboard.
- “Next customer” progression (serve / no‑show).
- Queue pause/close controls.
- Daily analytics aggregation & AI‑based wait time estimation.
- PDF/CSV report generation.

**Out of scope (v1.0):**
- Multiple parallel queues per service (single queue only).
- SMS notifications.
- RTL language support.
- iOS app.
- Payment integration.
- Web dashboard.

### 1.5 Assumptions
- Users have Android devices with Google Play Services.
- Internet connectivity is available (offline mode limited to cached reads).
- Businesses print and display QR codes at their physical locations.
- Each owner has exactly one business (one-to-one mapping).
- Service times are relatively consistent day-to-day for AI predictions.

---

## 2. User Personas & Journeys

### 2.1 Customer Persona – Ahmed
- **Demographics:** 34 years old, lives in Algiers, works as an accountant.
- **Goals:** Avoid waiting in long lines at the bank or pharmacy. Know exactly when to arrive.
- **Pain points:** Wastes 30+ minutes waiting; no visibility into queue length; misses turn because of poor announcements.
- **Typical journey:** Opens app, searches for nearby pharmacy, joins queue remotely, receives notification when turn approaches, walks in exactly on time, rates experience.

### 2.2 Owner Persona – Samira
- **Demographics:** 45 years old, owns a multi‑service clinic.
- **Goals:** Manage patient flow efficiently, reduce no‑shows, optimize staff schedules.
- **Pain points:** Overcrowded waiting room; patients complain about long waits; no data on peak hours.
- **Typical journey:** Opens dashboard in the morning, sees queue status, calls next patient with one tap, pauses queue during lunch break, reviews end‑of‑day analytics report.

### 2.3 High‑Level User Journeys
- **Customer remote join:** Search → select business → join queue → monitor position → receive turn alert → get served → leave review.
- **Customer QR scan:** Arrive at location → scan QR code → auto‑join → receive ticket → wait (short) → get served.
- **Owner daily operation:** Login → view dashboard → call next customer → mark served/no‑show → repeat → end of day → view analytics.

---

## 3. Functional Requirements

This section expands the SRS with acceptance criteria. Each requirement is traceable via an ID (e.g., FR‑AUTH‑01).

### 3.1 Authentication & Account Management

| ID | Requirement | Acceptance Criteria |
|----|-------------|----------------------|
| FR‑AUTH‑01 | User can sign up with email & password | - Fields: first name, last name, email, phone, password, confirm password, role (customer/business_owner). - Email and phone must be unique. - Password hashed with bcrypt (cost 10). - Verification email sent. - User document created in Firestore with `is_verified: false`. |
| FR‑AUTH‑02 | User can log in | - Email and password validated. - On success, JWT token generated (expiry 30 days). - Device push notification token captured and stored. - User redirected to appropriate dashboard (customer home or owner dashboard). |
| FR‑AUTH‑03 | Password recovery | - User requests reset via email. - 6‑digit OTP sent, valid 15 minutes. - OTP verified, new password hashed and updated. |
| FR‑AUTH‑04 | Profile management | - User can edit name, email, phone, profile image. - Email/phone uniqueness validated on change. - Image uploaded to Firebase Storage. |

### 3.2 Customer Features

| ID | Requirement | Acceptance Criteria |
|----|-------------|----------------------|
| FR‑CUST‑01 | Discover businesses | - Search by name, browse by category, or view map. - Results show business name, distance, queue length, estimated wait time. - Map markers color‑coded by queue load (green = short, yellow = medium, red = long). |
| FR‑CUST‑02 | Scan QR code to join queue | - Camera opens, decodes QR code. - QR code maps to a business. - Customer automatically joins the active queue for the business’s primary service. - Returns ticket number, position, ETA. - Owner receives push notification. |
| FR‑CUST‑03 | Remote queue reservation | - Customer selects a service. - System checks queue status (must be active). - Atomic increment of ticket number. - Reservation created with status `reserved`. - ETA calculated using AI model. - Push notification sent to customer. |
| FR‑CUST‑04 | Real‑time queue monitoring | - My Queue screen shows live position, people ahead, ETA. - Firestore listener updates UI within 500ms. - When position ≤ 2, notification “Your turn soon”. - When position = 1, full‑screen “Your turn now” modal. |
| FR‑CUST‑05 | I will be late | - Customer taps button, confirms. - Reservation status changed to `delayed`. - Ticket number reassigned to end of queue (max_ticket + 1). - Excluded from average service time analytics. - Notifications sent to owner and affected customers. |
| FR‑CUST‑06 | Cancel ticket | - Customer cancels with optional reason. - Status set to `cancelled`, queue positions recalculated. - Owner notified (optional). |
| FR‑CUST‑07 | Favorite businesses | - Heart icon toggles favorite. - Favorites list on home screen for quick access. |
| FR‑CUST‑08 | Review & rating | - After service (`status = served`), customer receives review prompt. - 1‑5 star rating, optional comment (max 500 chars). - Review stored, linked to reservation. - Business rating recalculated. |

### 3.3 Service Owner Features

| ID | Requirement | Acceptance Criteria |
|----|-------------|----------------------|
| FR‑OWN‑01 | Business profile setup | - Onboarding wizard collects business name, description, category, address, hours, logo. - Unique QR code generated and displayed for download/print. - Business document created. |
| FR‑OWN‑02 | Service management | - Owner can create, edit, soft‑delete services. - Each service has name, average duration (minutes), availability toggle. - Default service created automatically. |
| FR‑OWN‑03 | Real‑time queue dashboard | - Shows current service, queue status (active/paused/closed), current ticket being served, total waiting, average wait time. - Scrollable list of waiting reservations (ticket, name, wait time). - Real‑time updates via Firestore listeners. |
| FR‑OWN‑04 | Next customer progression | - Owner taps “Next Customer”. - Bottom sheet shows first waiting customer. - Options: Mark as Served (status `served`) or No‑Show (status `no_show`). - If served, triggers review prompt notification. - Queue advances, positions update for all. |
| FR‑OWN‑05 | Queue control (pause/close) | - Owner can pause queue (no new joins, existing continue). - Owner can close queue (no joins, existing invalidated). - Customers receive push notification. |
| FR‑OWN‑06 | Analytics & reporting | - Daily analytics aggregated at 11:59 PM (avg service time, peak hour, cancellations, no‑shows). - Owner can generate PDF/CSV reports for daily, weekly, monthly, custom ranges. - Reports stored in Firebase Storage, downloadable. |

### 3.4 Push Notifications

| ID | Requirement | Acceptance Criteria |
|----|-------------|----------------------|
| FR‑NOTIF‑01 | Customer notifications | - Ticket confirmed, position update, turn soon, turn now, queue paused/closed, review prompt. - Delivered via FCM within 5 seconds of trigger. - Stored in `notifications` collection. |
| FR‑NOTIF‑02 | Owner notifications | - New reservation, queue full, queue empty, no‑show alert, peak hour alert, analytics summary. |

---

## 4. Non-Functional Requirements

| Category | Requirement | Target |
|----------|-------------|--------|
| **Performance** | App launch time | < 2 seconds |
| | Queue position update latency | < 500 ms |
| | Search results display | < 1 second |
| | Notification delivery | < 5 seconds |
| | API response time (95th percentile) | < 1 second |
| **Security** | Password hashing | bcrypt cost factor 10 |
| | Authentication | JWT with 30‑day expiry, HTTPS only |
| | Firestore Security Rules | Role‑based access control (customers see own data, owners see their business) |
| | Rate limiting | 5 login attempts per email per 5 minutes |
| **Scalability** | Firestore | Auto‑scaling, supports millions of reads/writes |
| | Real‑time listeners | Up to 1 million concurrent connections |
| **Availability** | Uptime | 99.9% |
| | Backups | Daily automated backups |
| **Offline Support** | Firestore offline persistence | Cached queues and favorites; write operations require online |
| **Compliance** | GDPR | Support user data export and deletion; soft delete with hard delete after 90 days |

---

## 5. System Architecture

### 5.1 High‑Level Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                      Mobile App (Kotlin)                         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────────┐  │
│  │   UI Layer  │  │ ViewModel   │  │      Repository         │  │
│  │ (Compose)   │◄─┤   Layer     │◄─┤     Layer               │  │
│  └─────────────┘  └─────────────┘  └───────────┬─────────────┘  │
│                                                  │                │
└──────────────────────────────────────────────────┼────────────────┘
                                                   │
                                                   ▼
┌─────────────────────────────────────────────────────────────────┐
│                        Firebase Services                         │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐  ┌───────────┐  │
│  │ Firestore  │  │   Auth     │  │    FCM     │  │ Storage   │  │
│  │ (NoSQL DB) │  │ (Identity) │  │ (Push)     │  │ (Images/  │  │
│  └────────────┘  └────────────┘  └────────────┘  │  Reports)  │  │
│                                                   └───────────┘  │
│  ┌────────────────────────────────────────────────────────────┐  │
│  │              Cloud Functions (Node.js)                      │  │
│  │  - Daily analytics aggregation (scheduled)                  │  │
│  │  - Send notifications (trigger on reservation change)       │  │
│  │  - Report generation (on‑demand)                            │  │
│  └────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                                                   │
                                                   ▼
┌─────────────────────────────────────────────────────────────────┐
│                     External Services                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────┐   │
│  │ Google Maps  │  │  Google ML   │  │ SendGrid (email)      │   │
│  │ API (geocode,│  │  Kit (QR on‑ │  │ (password reset)      │   │
│  │  map tiles)  │  │  device)     │  │                       │   │
│  └──────────────┘  └──────────────┘  └──────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
```

### 5.2 Component Descriptions

| Component | Technology | Responsibility |
|-----------|------------|----------------|
| **Mobile App** | Kotlin + Jetpack Compose | UI rendering, user interaction, real‑time listeners, camera access, local caching. |
| **Firestore** | Firebase NoSQL DB | Persistent storage of users, businesses, queues, reservations, reviews, analytics. Real‑time data sync. |
| **Firebase Auth** | Firebase Auth | Email/password authentication, JWT token management. |
| **FCM** | Firebase Cloud Messaging | Push notifications to Android devices. |
| **Firebase Storage** | Cloud Storage | Store profile images, business logos, generated PDF/CSV reports. |
| **Cloud Functions** | Node.js 18+ | Scheduled tasks (daily analytics), database triggers (send notifications on reservation status change), report generation. |
| **Google Maps API** | Maps SDK for Android | Display maps, geocode addresses, show business locations. |
| **ML Kit** | Google ML Kit (barcode scanning) | On‑device QR code decoding (no internet required). |
| **SendGrid** | Email API | Send password reset OTP emails. |

### 5.3 Data Flow – Join Queue (Remote)

1. Customer taps “Join Queue” on Business Details screen.
2. App validates queue status (active) via Firestore read.
3. App calls Firestore transaction:
   - Read current `queues.current_ticket_number`.
   - Increment atomically.
   - Create `reservations` document with new ticket number.
4. Cloud Function trigger on `reservations` create:
   - Calculate ETA using `daily_analytics` (last 30 days avg).
   - Write ETA to reservation.
   - Send FCM to customer (ticket confirmed).
   - Send FCM to owner (new reservation).
5. App receives real‑time update on `reservations` document and navigates to My Queue screen.

### 5.4 Data Flow – Next Customer (Owner)

1. Owner taps “Next Customer” on dashboard.
2. App queries first `reservations` with `status = 'reserved'` and lowest `ticket_number`.
3. Bottom sheet shows customer info.
4. Owner selects “Served” or “No‑Show”.
5. App updates reservation `status` and `served_at` (if served).
6. Cloud Function trigger:
   - If `served`: send review prompt notification to customer.
   - If `no_show`: increment `daily_analytics.total_no_shows` (for current day).
7. Real‑time listener on dashboard updates the waiting list automatically.

---

## 6. Detailed System Design

### 6.1 Firebase Data Model (Expanded)

Refer to the SRS (section 4) for full collection schemas. Key additions:

- **Indexes:** All foreign keys and frequently queried fields are indexed (e.g., `users.email`, `reservations.queue_id`, `queues.service_id_date`).
- **Atomic operations:** `queues.current_ticket_number` uses Firestore `FieldValue.increment()`.
- **Soft delete:** `deleted_at` timestamp on `users`, `businesses`, `services`.
- **Unique constraints:** `users.email`, `users.phone`, `businesses.qr_code`, `daily_analytics.service_id_date`, `reviews.reservation_id`.

### 6.2 Firestore Security Rules (Example)

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users can read/write their own profile
    match /users/{userId} {
      allow read, write: if request.auth.uid == userId;
    }
    // Reservations: customers read own; owners read business reservations
    match /reservations/{reservationId} {
      allow read: if resource.data.customer_id == request.auth.uid ||
        (exists(/databases/$(database)/documents/businesses/$(request.auth.uid)) && 
         resource.data.queue_id in getOwnerQueueIds());
      allow create: if request.auth.uid != null;
      allow update: if resource.data.customer_id == request.auth.uid ||
        (exists(/databases/$(database)/documents/businesses/$(request.auth.uid)) &&
         resource.data.queue_id in getOwnerQueueIds());
    }
    // Businesses: public read, write only by owner
    match /businesses/{businessId} {
      allow read: if true;
      allow write: if request.auth.uid == resource.data.user_id;
    }
    // Helper function
    function getOwnerQueueIds() {
      // returns list of queue IDs belonging to owner's business
      return get(/databases/$(database)/documents/businesses/$(request.auth.uid)).data.queueIds;
    }
  }
}
```

### 6.3 Cloud Functions (Node.js)

| Function Name | Trigger | Description |
|---------------|---------|-------------|
| `dailyAnalyticsAggregator` | Scheduled (11:59 PM daily) | For each service, queries served reservations (excluding delayed), calculates avg service time, peak hour, cancellations, no‑shows, updates `daily_analytics`. |
| `onReservationCreate` | Firestore `create` on `reservations` | Calculates ETA using last 30 days of `daily_analytics` (avg service time × people ahead, +20% if current hour = peak hour). Sends ticket confirmation push. |
| `onReservationUpdate` | Firestore `update` on `reservations` | If status changes to `served` → send review prompt. If `no_show` → increment daily no‑show counter. If `delayed` → notify owner. |
| `onQueueStatusChange` | Firestore `update` on `queues.status` | When status becomes `paused` or `closed`, fetch all active reservations for that queue and send FCM notifications. |
| `generateReport` | HTTP callable (from app) | Queries `daily_analytics` for date range, generates PDF using template (via PDFKit), uploads to Storage, returns download URL. |
| `sendPasswordResetOtp` | HTTP callable | Generates 6‑digit OTP, stores hashed in `password_resets`, sends email via SendGrid. |

### 6.4 Real‑Time Synchronization

- **Customer My Queue screen:** Listens to `reservations/{reservationId}`. On change, recomputes position (count of reservations with lower ticket number and status `reserved`).
- **Owner Dashboard:** Listens to `queues/{queueId}` and `reservations` query (`queueId == X, status != cancelled`). Updates waiting list and metrics in real time.
- **Offline persistence:** Firestore enables disk persistence. Cached data shown immediately, writes are queued and synced when online. Queue join is not allowed offline (atomic increment requires server).

### 6.5 AI Wait‑Time Estimation (v1)

- **Algorithm (simple linear):**
  ```
  avg_service_time = recent_daily_analytics.average_service_time (last 30 days, weighted recent)
  people_ahead = current_position - 1
  estimated_wait = avg_service_time * people_ahead
  if current_hour == daily_analytics.peak_hour:
      estimated_wait *= 1.2
  ```
- **Fallback:** If no historical data, use service’s `average_duration` from `services` collection.
- **Future enhancement:** Use ML model (TensorFlow Lite) on device for dynamic adjustments based on real‑time service speed.

---

## 7. API Specifications

Queue Manager uses Firebase SDK directly; no custom REST API except for two Cloud Function callables.

### 7.1 Cloud Function Endpoints

#### `generateReport` (Callable)

**Request:**
```json
{
  "serviceId": "uuid",
  "reportType": "daily|weekly|monthly|custom",
  "startDate": "2026-04-01",
  "endDate": "2026-04-07",
  "includeSections": ["summary", "hourly", "noShows", "cancellations"]
}
```

**Response:**
```json
{
  "reportUrl": "https://storage.googleapis.com/.../report.pdf",
  "expiresAt": "2026-04-30T00:00:00Z"
}
```

#### `sendPasswordResetOtp` (Callable)

**Request:**
```json
{
  "email": "user@example.com"
}
```

**Response:**
```json
{
  "message": "OTP sent"
}
```

### 7.2 Firebase SDK Usage

All other operations (CRUD on Firestore, authentication, storage uploads) use native Firebase Android SDK. No additional API layer.

### 7.3 Google Maps Integration

- **Geocoding:** Convert address string to lat/lng during business setup (Geocoding API).
- **Map display:** Show user location and nearby business markers using Maps SDK for Android.
- **Directions (optional future):** Provide walking/driving directions from user location to business.

---

## 8. UI/UX Design Specifications

*Note: A separate design PRD document has been created. This section summarizes key guidelines.*

### 8.1 Design System (Material Design 3)

- **Colors:** Primary Deep Indigo (`#1A237E`), Secondary Teal (`#00796B`), Tertiary Orange (`#F57C00`). Dark mode variants defined.
- **Typography:** Roboto (Headline, Title, Body, Label). Sizes from 12sp to 32sp.
- **Spacing:** 8dp base unit. Screen margins 16dp.
- **Icons:** Material Symbols (outlined/filled).

### 8.2 Key Screens (Customer)

- Splash, Welcome, Sign Up, Login, Forgot Password, Permissions.
- Home (search, categories, nearby list, map).
- Business Details (info, queue status, join button, services).
- My Queue (real‑time position, wait time, action buttons).
- Your Turn Now (full‑screen modal).
- Review Prompt (bottom sheet).
- Profile & Settings.

### 8.3 Key Screens (Owner)

- Business Setup Wizard (4 steps).
- Dashboard (queue metrics, waiting list, Next Customer button).
- Queue Controls (bottom sheet).
- Analytics (charts, metrics, report generation).

### 8.4 Accessibility

- Touch targets ≥48dp.
- Contrast ratio ≥4.5:1.
- TalkBack labels for all interactive elements.
- Focus indicators for keyboard navigation.

---

## 9. Integration Points

| Integration | Purpose | Data exchanged | Frequency |
|-------------|---------|----------------|------------|
| Google Maps SDK | Display maps, geocode addresses | Lat/lng, address strings | On demand |
| Google ML Kit | On‑device QR code scanning | Camera frames → decoded string | Real‑time |
| FCM | Push notifications | Notification payload (title, body, data) | Per event |
| SendGrid | Password reset emails | Email address, OTP code | Low volume |
| Firebase Storage | File hosting | Profile images, logos, reports | On upload/download |

---

## 10. Data Privacy & Compliance

### 10.1 GDPR Compliance

- **Right to access:** User can export all personal data via “Download my data” in Profile.
- **Right to deletion:** User can request account deletion; soft delete immediately, hard delete after 90 days.
- **Data minimization:** Only store necessary PII (name, email, phone, profile image). No sensitive data.
- **Third‑party sharing:** No sharing without explicit consent. Google Maps and Firebase are sub‑processors.

### 10.2 Data Retention

- Active users: retained indefinitely.
- Soft‑deleted users: marked `deleted_at`, excluded from all queries, hard deleted after 90 days via scheduled Cloud Function.
- Reservations: kept for analytics for 2 years, then anonymized.

### 10.3 Security Measures

- All communication over HTTPS.
- Passwords hashed with bcrypt (cost 10).
- Firestore Security Rules enforce role‑based access.
- JWT tokens stored securely (Android Keystore).
- Rate limiting on authentication endpoints.

---

## 11. Testing Strategy

### 11.1 Unit Tests

- **Framework:** JUnit + MockK (Kotlin).
- **Coverage:** ViewModels, repositories, utility functions (ETA calculator, validators).
- **Target:** >80% code coverage for business logic.

### 11.2 Integration Tests

- **Firebase Emulator Suite:** Run Firestore, Auth, Functions locally.
- **Test scenarios:** User registration, queue joining, next customer flow, analytics aggregation.
- **CI integration:** Run on every pull request.

### 11.3 UI / End‑to‑End Tests

- **Framework:** Compose UI Test + Espresso.
- **Flows:** Sign up → join queue → serve → review. QR code scan simulation.
- **Device cloud:** Firebase Test Lab (physical devices: Pixel 6, Samsung S21, etc.).

### 11.4 Performance Tests

- **Load testing:** Simulate 1000 concurrent users joining queue (using Firebase performance monitoring).
- **Real‑time listener stress:** 500 simultaneous listeners on a single queue document.
- **App startup time:** Measure cold start (target <2s).

### 11.5 Security Tests

- Firestore Security Rules test suite (using Firebase Emulator).
- JWT expiration and validation.
- Input validation (XSS, SQL injection – NoSQL injection prevention).

---

## 12. Deployment & DevOps

### 12.1 Environments

| Environment | Purpose | Firebase project | Access |
|-------------|---------|------------------|--------|
| Development | Local development | queue-manager-dev | Developers |
| Staging | Integration testing | queue-manager-staging | QA team, product |
| Production | Live users | queue-manager-prod | Admin only |

### 12.2 CI/CD Pipeline (GitHub Actions)

1. Developer pushes to `develop` branch.
2. Run unit tests + lint.
3. Build debug APK, upload to Firebase App Distribution (internal testers).
4. On merge to `main`:
   - Run integration tests (Firebase Emulator).
   - Build release AAB.
   - Deploy to Firebase Hosting (for static assets) and update Cloud Functions.
   - Upload to Google Play Console (internal testing track).

### 12.3 Release Process

- **Weekly sprints:** Feature branches → PR → merge to develop.
- **Bi‑weekly release:** Cut release branch from develop, run full regression, promote to staging, then to production.
- **Hotfix:** Direct to main, then cherry‑pick to develop.

### 12.4 Infrastructure as Code

- Firebase project configuration managed via Firebase CLI + `.firebaserc`.
- Firestore indexes defined in `firestore.indexes.json`.
- Security rules in `firestore.rules`.
- Cloud Functions deployed via `firebase deploy --only functions`.

---

## 13. Monitoring & Analytics

### 13.1 Firebase Performance Monitoring

- **Custom traces:** App startup, join queue, next customer, report generation.
- **Network monitoring:** Firestore read/write latency.
- **Crashlytics:** Real‑time crash reporting and non‑fatal errors.

### 13.2 Business Analytics (in‑app)

- **Daily active users (DAU)** – Firestore analytics events.
- **Queue conversion rate:** # joined vs # served.
- **Average wait time per service** – from `daily_analytics`.
- **No‑show rate** – per business/service.

### 13.3 Alerting

- **Uptime monitoring:** Better Uptime or Pingdom (ping Cloud Function health endpoint).
- **Error budget:** 99.9% uptime → alerts if error rate >0.1% over 5 minutes.
- **Cloud Monitoring:** Set up alerts for Firestore high latency (>1s) or high number of concurrent listeners.

---

## 14. Future Roadmap (Post‑MVP)

| Phase | Features | Estimated effort |
|-------|----------|------------------|
| **v1.1** | - Web dashboard for owners<br>- Multiple queues per service (parallel desks)<br>- Advanced charts (trends, forecasting) | 2 months |
| **v1.2** | - SMS notifications (Twilio)<br>- Loyalty points system<br>- Customer chat with service desk | 2 months |
| **v2.0** | - iOS app (SwiftUI)<br>- Payment integration (Stripe)<br>- API for third‑party POS integration<br>- AI model improvement (real‑time ML) | 3 months |

---

## 15. Appendix

### 15.1 Glossary

| Term | Definition |
|------|-------------|
| **Queue** | A daily instance of a service line. Contains reservations for a specific date. |
| **Reservation** | A customer’s ticket in a queue. Has status (reserved, served, cancelled, no‑show, delayed). |
| **Delayed** | Customer moved to end of queue via “I will be late”. Excluded from average service time. |
| **No‑show** | Customer did not respond when called; marked by owner. |
| **ETA** | Estimated time of arrival (wait time). |
| **FCM** | Firebase Cloud Messaging – push notification service. |
| **JWT** | JSON Web Token – used for authentication. |

### 15.2 References

- [Firebase Documentation](https://firebase.google.com/docs)
- [Material Design 3](https://m3.material.io/)
- [Google Maps SDK for Android](https://developers.google.com/maps/documentation/android-sdk)
- [Original SRS Document (Queue_Manager_SRS.docx)](./Queue_Manager_SRS.docx)

### 15.3 Document Sign‑off

| Role | Name | Signature | Date |
|------|------|-----------|------|
| Product Manager | [Name] | _______ | 2026-04-16 |
| Tech Lead | [Name] | _______ | 2026-04-16 |
| UI/UX Lead | [Name] | _______ | 2026-04-16 |
| Project Sponsor | [Name] | _______ | 2026-04-16 |

---

**End of Queue Manager PRD v1.0**