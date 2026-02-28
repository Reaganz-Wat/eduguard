# EduGuard AI

An AI-Powered Digital Discipline & Academic Focus Platform for Secondary School Students.

## Project Overview

EduGuard is an Android application designed to help schools manage student device usage during school hours. It combines app restriction, VPN detection, AI-powered monitoring, and academic tools to keep students focused on their studies.

## Technology Stack

| Component | Technology | Purpose |
|-----------|------------|---------|
| Mobile App | Kotlin (Android) | Native Android development with Device Policy Manager |
| Backend API | FastAPI (Python) | High-performance REST API with async support |
| Database | PostgreSQL | Robust relational database for user data and analytics |
| AI/ML | TensorFlow + TF Serving | Pattern recognition and behavioral analysis |
| Offline Storage | SQLite | Local data persistence without internet |
| Authentication | FastAPI Security + JWT | Secure user authentication |

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Android App                          │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │   Student   │  │    Admin    │  │  App Restriction    │ │
│  │   UI        │  │   Dashboard │  │     Engine          │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │ Class Chat  │  │AI Research  │  │  VPN Detection      │ │
│  │   Board     │  │  Assistant  │  │     System          │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      FastAPI Backend                        │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐ │
│  │  Auth API   │  │ Analytics   │  │  AI/ML Service       │ │
│  │             │  │   API       │  │  (TF Serving)        │ │
│  └─────────────┘  └─────────────┘  └─────────────────────┘ │
│  ┌─────────────┐  ┌─────────────┐                          │
│  │  Chat API   │  │ Research    │                          │
│  │             │  │ Assistant   │                          │
│  └─────────────┘  └─────────────┘                          │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                        Database                              │
│  ┌─────────────┐  ┌─────────────┐                          │
│  │ PostgreSQL  │  │   SQLite    │                          │
│  │ (Primary)   │  │  (Offline)  │                          │
│  └─────────────┘  └─────────────┘                          │
└─────────────────────────────────────────────────────────────┘
```

## Core Features

### 1. App Restriction Engine

Blocks social media, entertainment apps, and unauthorized applications during school hours using Device Policy Manager API.

**Capabilities:**
- Social Media Blocking (Facebook, Instagram, TikTok, WhatsApp)
- Entertainment Blocking (YouTube, Netflix, Games)
- VPN Detection and Blocking
- Time-based Automatic Enable/Disable
- Call and SMS Restrictions during school hours

**Implementation Notes:**
- Use Android DeviceAdminReceiver for admin privileges
- Use UsageStatsManager for app usage monitoring
- Maintain list of restricted package names
- Implement WorkManager for scheduled restrictions

### 2. VPN Detection System

Monitors network traffic and detects VPN usage to prevent students from bypassing restrictions.

**Capabilities:**
- Real-time VPN service monitoring
- Network traffic analysis
- Automatic blocking of detected VPN apps
- Admin alerts for bypass attempts

**Implementation Notes:**
- Monitor for VPN-related processes and services
- Check /proc/net/tcp and /proc/net/udp for VPN connections
- Detect VPN apps installed on device
- Send alerts to backend when VPN is detected

### 3. AI-Powered Monitoring

Machine learning models analyze usage patterns and predict potential violations before they occur.

**Capabilities:**
- Behavioral pattern recognition
- Anomaly detection (unusual app switching, excessive screen time)
- Predictive alerts for administrators
- Usage trend analysis and reporting

**Implementation Notes:**
- Collect usage stats via UsageStatsManager
- Build TensorFlow model for anomaly detection
- Use background worker for periodic data collection
- Send data to backend for advanced analytics

### 4. Class Chat Board

Monitored communication platform for academic discussions and collaboration.

**Capabilities:**
- Class-based group messaging
- Teacher supervision and moderation
- Secure file sharing (study materials)
- Keyword filtering and content monitoring
- Announcement mode for teachers

**Implementation Notes:**
- WebSocket for real-time messaging
- Implement message filtering logic
- Store messages in PostgreSQL
- Teacher can mute/delete messages

### 5. AI Research Assistant

Intelligent chatbot that guides students to credible academic resources and prevents access to distracting content.

**Capabilities:**
- Natural language query processing
- Curated educational website recommendations
- Curriculum-aligned search results
- Safe browsing guidance

**Implementation Notes:**
- Integrate with NLP model for query understanding
- Maintain curated list of educational resources
- Filter inappropriate content
- Cache common queries for offline access

### 6. Real-time Analytics Dashboard

Admin interface showing device status, usage patterns, and compliance metrics.

**Capabilities:**
- Live device monitoring
- Usage statistics and screen time tracking
- Violation reports and alerts
- Compliance rate visualization
- Exportable PDF reports

**Implementation Notes:**
- Use WebSocket for real-time updates
- Build charts using MPAndroidChart or similar
- Generate PDF reports with iText
- Implement pagination for large datasets

## User Roles

### Student
- View restricted apps list
- Access Class Chat Board
- Use AI Research Assistant
- View personal usage statistics

### Admin/Teacher
- Configure restriction schedules
- Monitor all student devices
- View analytics dashboard
- Manage class groups
- Moderate chat messages
- Generate reports

## Database Schema (High-Level)

### Users
- id, email, password_hash, name, role, class_id, created_at

### Classes
- id, name, subject, admin_id

### Messages
- id, class_id, user_id, content, timestamp, is_announcement

### UsageLogs
- id, user_id, app_package, duration, timestamp, is_violation

### Restrictions
- id, app_package, blocked, schedule_start, schedule_end

### VPNAlerts
- id, user_id, detected_at, vpn_type

## API Endpoints (High-Level)

### Auth
- POST /api/auth/login
- POST /api/auth/register
- POST /api/auth/refresh

### Users
- GET /api/users/me
- GET /api/users/students
- PUT /api/users/{id}

### Classes
- GET /api/classes
- POST /api/classes
- GET /api/classes/{id}/students

### Chat
- GET /api/classes/{id}/messages
- POST /api/classes/{id}/messages
- DELETE /api/messages/{id}

### Analytics
- GET /api/analytics/usage
- GET /api/analytics/violations
- GET /api/analytics/compliance

### Restrictions
- GET /api/restrictions
- POST /api/restrictions
- PUT /api/restrictions/{id}

### AI Assistant
- POST /api/assistant/query

## Offline-First Architecture

- Use SQLite for local data storage
- Implement sync mechanism with PostgreSQL
- Queue API requests when offline
- Cache chat messages locally
- Store AI assistant responses for offline use

## Security Considerations

- JWT token-based authentication
- Encrypt sensitive data at rest
- Use HTTPS for all API calls
- Implement rate limiting
- Sanitize chat messages
- Secure API endpoints with proper authorization

## Getting Started

### Prerequisites
- Android Studio (latest version)
- Python 3.9+
- PostgreSQL 14+
- Node.js (for build tools)

### Setup

1. **Backend Setup:**
   ```bash
   cd backend
   pip install -r requirements.txt
   # Configure database connection
   uvicorn main:app --reload
   ```

2. **Android App Setup:**
   - Open in Android Studio
   - Configure API base URL
   - Build and run on device/emulator

3. **Device Admin Setup:**
   - Grant admin permissions in Settings
   - Configure restriction schedules

## Project Structure

```
eduguard/
├── android/                 # Android Kotlin app
│   ├── app/
│   │   ├── src/main/
│   │   │   ├── java/com/eduguard/
│   │   │   │   ├── ui/           # Activities & Fragments
│   │   │   │   ├── data/         # Repositories & DataSources
│   │   │   │   ├── domain/       # Use Cases & Models
│   │   │   │   └── service/      # Background Services
│   │   │   └── res/
│   │   └── build.gradle
│   └── build.gradle
│
├── backend/                 # FastAPI backend
│   ├── app/
│   │   ├── api/            # API routes
│   │   ├── core/           # Config & Security
│   │   ├── db/             # Database models
│   │   ├── models/         # Pydantic models
│   │   └── services/       # Business logic
│   ├── ml/                 # ML models
│   ├── requirements.txt
│   └── main.py
│
└── README.md
```

## License

MIT License

Author
- Reaganz-Wat | DevOps Engineeer
