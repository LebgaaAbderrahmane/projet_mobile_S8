# Project Plan

Build an Android app called 'Queue Manager' based on the provided PRD. The app is a digital virtual queuing system that eliminates physical lines. Customers join queues remotely or via QR code scanning, and service owners manage queues in real time with AI-powered wait-time predictions. Key features include user authentication, business discovery, QR code scanning, remote queue reservation, real-time queue monitoring, push notifications, owner dashboard, and analytics. The app will use Firebase (Firestore, Auth, FCM, Storage) and Google Maps API. UI should follow Material Design 3. Full PRD: # Queue Manager – Complete Product Requirements Document (PRD)

**Version:** 1.0  
**Date:** April 2026  
**Status:** Ready for Development  
**Target Platform:** Android (Kotlin + Jetpack Compose)  
**Backend:** Firebase (Firestore, Auth, FCM, Functions, Storage)  

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

### 2. User Personas & Journeys

... (Detailed User Personas omitted for brevity in tool call, but implied by full PRD context)

### 3. Functional Requirements

... (Detailed FRs omitted for brevity in tool call)

### 4. Non-Functional Requirements

... (Detailed NFRs omitted for brevity in tool call)

### 5. System Architecture

... (Detailed Architecture omitted for brevity in tool call)

### 6. Detailed System Design

... (Detailed System Design omitted for brevity in tool call)

... (Remaining sections of PRD implied)

## Project Brief

# Project Brief: Queue Manager

## Features
- **Remote Queue Joining & QR Scanning**: Users can join a queue by scanning a physical QR code at a storefront or joining remotely through the app to save time.
- **Real-Time Monitoring & AI Predictions**: Provides users with their live position in the queue and AI-powered estimated wait times to reduce perceived waiting time.
- **Business Discovery & Navigation**: Integrated Google Maps allows users to discover nearby businesses, view their current queue status, and navigate to the location.
- **Service Owner Dashboard**: A specialized interface for businesses to manage their queue in real time, call the next customer, and view basic service analytics.
- **Smart Notifications**: Automated push notifications alert customers when they are near the front of the line or when it is their turn to be served.

## High-Level Technical Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material Design 3
- **Asynchronous Programming**: Kotlin Coroutines & Flow
- **Backend Services**: Firebase (Authentication, Firestore for real-time data, Cloud Messaging for notifications)
- **Mapping & Location**: Google Maps SDK for Android & Play Services Location
- **Hardware Integration**: CameraX for high-performance QR code scanning
- **Code Generation**: KSP (Kotlin Symbol Processing) for modern, efficient boilerplate generation

## Implementation Steps

### Task_1_Setup_Auth_Firebase: Initialize Firebase services (Auth, Firestore, Cloud Messaging), configure project architecture, and implement User Authentication with separate roles for Customers and Owners.
- **Status:** IN_PROGRESS
- **Acceptance Criteria:**
  - Firebase project is linked and initialized
  - Google Maps and Firebase API keys are integrated
  - Authentication (Sign up/Login) works for both Customer and Owner roles
  - Firestore initial collection structure is established
- **StartTime:** 2026-04-16 08:54:27 CET

### Task_2_Discovery_CustomerFlow: Implement business discovery using Google Maps SDK and develop the customer-facing flow including business details, remote queue joining, and a real-time queue monitoring screen.
- **Status:** PENDING
- **Acceptance Criteria:**
  - Google Maps displays nearby businesses with markers
  - Business details screen shows current queue status
  - Customer can join a queue remotely via Firestore
  - Real-time position and estimated wait time are displayed to the customer

### Task_3_QR_OwnerDashboard: Integrate CameraX for QR code scanning to join queues instantly and build the Owner Dashboard for real-time queue management (calling next customer, pausing/closing queue).
- **Status:** PENDING
- **Acceptance Criteria:**
  - QR code scanning correctly identifies and joins a business queue
  - Owner Dashboard displays the live queue list
  - 'Next Customer' action updates queue status in real-time for all users
  - Owner can successfully pause or close the queue

### Task_4_Notifications_Analytics_Finalize: Implement FCM notifications for turn alerts, basic wait-time estimation logic, report generation (PDF/CSV), and apply final Material 3 UI polish including adaptive icons and edge-to-edge display.
- **Status:** PENDING
- **Acceptance Criteria:**
  - Push notifications are received when a customer's turn is near
  - Daily analytics reports are generated in PDF/CSV format
  - App follows Material 3 guidelines with a vibrant color scheme
  - Adaptive app icon and edge-to-edge display are implemented
  - Final app builds successfully, all existing tests pass, and Critic confirms stability and requirement alignment

