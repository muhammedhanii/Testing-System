# AIU-Trips-And-Events Design Patterns Implementation Guide

## Overview
This document provides a comprehensive guide to the design patterns implemented in the AIU-Trips-And-Events application, based on the PlantUML diagrams in `Milestones/PM_3/Class Diagram/After DP/`.

## ✅ Completed Design Patterns

### 1. Factory Pattern ✅
**Location:** `com.aiu.trips.factory`

**Components:**
- `IModelFactory` - Factory interface
- `ModelFactory` - Concrete factory with registry
- `IBaseModel<T>` - Base model interface
- `IReadModel<T>` - Read operations interface
- `IWriteModel<T>` - Write operations interface

**Purpose:** Centralized creation and registration of repository models

**Usage:**
```java
@Autowired
private IModelFactory modelFactory;

// Register a model
modelFactory.register("userModel", userModel);

// Retrieve a model
IBaseModel<User> userModel = modelFactory.get("userModel");
```

### 2. Prototype Pattern ✅
**Location:** `com.aiu.trips.prototype`

**Components:**
- `IPrototype<T>` - Prototype interface with `clone()` method

**Purpose:** Enable cloning of Activity objects

**Usage:**
```java
public class EventEntity extends Activity implements IPrototype<EventEntity> {
    @Override
    public EventEntity clone() {
        // Deep copy implementation
    }
}
```

### 3. Command Pattern ✅
**Location:** `com.aiu.trips.command`

**Components:**
- `IControllerCommand` - Command interface
- `ControllerCommandInvoker` - Command queue manager

**Purpose:** Decouple request handling from execution

**Usage:**
```java
@Autowired
private ControllerCommandInvoker invoker;

IControllerCommand loginCommand = new LoginCommand(authService);
invoker.pushToQueue(loginCommand);
ResponseEntity<?> response = invoker.executeNext(requestData);
```

### 4. Chain of Responsibility Pattern ✅
**Location:** `com.aiu.trips.chain`

**Components:**
- `RequestHandler` - Abstract handler
- `AuthenticationHandler` - Validates JWT tokens
- `AuthorizationHandler` - Checks permissions
- `ValidationHandler` - Validates request data
- `RateLimitHandler` - Rate limiting

**Purpose:** Process requests through a chain of handlers

**Usage:**
```java
RequestHandler chain = new AuthenticationHandler();
chain.setNext(new AuthorizationHandler())
     .setNext(new ValidationHandler())
     .setNext(new RateLimitHandler());

chain.handle(request);
```

### 5. State Pattern ✅
**Location:** `com.aiu.trips.state`

**Components:**
- `ActivityState` - State interface
- `UpcomingState` - Upcoming activity state
- `CompletedState` - Completed activity state
- `CancelledState` - Cancelled activity state
- `ActivityLifecycle` - State context

**Purpose:** Manage activity lifecycle transitions

**Usage:**
```java
ActivityLifecycle lifecycle = new ActivityLifecycle();
lifecycle.initializeFromActivity(activity);
activity = lifecycle.complete(activity);
```

### 6. Strategy Pattern ✅
**Location:** `com.aiu.trips.strategy`

**Components:**
- `PricingStrategy` - Strategy interface
- `StandardPricingStrategy` - No discount
- `EarlyBirdPricingStrategy` - 15% early booking discount
- `BulkGroupDiscountStrategy` - 20% for 5+ tickets

**Purpose:** Dynamic pricing calculation

**Usage:**
```java
PricingStrategy strategy = new EarlyBirdPricingStrategy();
BigDecimal price = strategy.calculatePrice(basePrice, bookingDate, quantity);
```

### 7. Builder Pattern ✅
**Location:** `com.aiu.trips.builder`

**Components:**
- `IActivityBuilder` - Builder interface
- `EventBuilder` - Event builder
- `TripBuilder` - Trip builder
- `IActivityDirector` - Director interface
- `ActivityDirector` - Build orchestrator

**Purpose:** Construct complex Activity objects step-by-step

**Usage:**
```java
IActivityBuilder builder = new EventBuilder();
IActivityDirector director = new ActivityDirector();
director.setBuilder(builder);
ActivityDTO event = director.makeNormalEvent(data);
```

### 8. Decorator Pattern ✅
**Location:** `com.aiu.trips.decorator`

**Components:**
- `ITicketService` - Component interface
- `BaseTicketService` - Base implementation
- `TicketServiceDecorator` - Abstract decorator
- `SignedQrDecorator` - Adds QR signing
- `AuditLogDecorator` - Adds logging

**Purpose:** Add features to ticket service dynamically

**Usage:**
```java
ITicketService base = new BaseTicketService();
ITicketService signed = new SignedQrDecorator(base);
ITicketService audited = new AuditLogDecorator(signed);
TicketDTO ticket = audited.generateTicket(bookingId);
```

### 9. Bridge Pattern ✅
**Location:** `com.aiu.trips.bridge`

**Components:**
- `NotificationChannel` - Implementation interface
- `EmailChannel` - Email notifications
- `InAppChannel` - In-app notifications
- `NotificationMessage` - Abstraction
- `NewEventMessage` - New event notification
- `EventUpdateMessage` - Update notification
- `ReminderMessage` - Reminder notification

**Purpose:** Decouple notification channels from messages

**Usage:**
```java
NotificationChannel channel = new EmailChannel();
NotificationMessage message = new NewEventMessage(channel, "Event Name", date);
message.send("user@email.com");
```

### 10. Adapter Pattern ✅
**Location:** `com.aiu.trips.adapter`

**Components:**
- `IEmailService` - Target interface
- `SmtpEmailAdapter` - Adapts JavaMailSender

**Purpose:** Adapt third-party email service to our interface

**Usage:**
```java
IEmailService emailService = new SmtpEmailAdapter();
emailService.sendEmail("to@email.com", "Subject", "Body");
```

### 11. Abstract Factory Pattern ✅
**Location:** Integrated with Builder and Factory patterns

**Purpose:** Create families of related objects (Events and Trips)

**Note:** Partially implemented via ActivityDirector which acts as an abstract factory

## 📋 Remaining Implementation Tasks

### Phase 1: Repository Layer
1. Create repository interfaces:
   - `IUserModel` extends `IBaseModel<User>`
   - `IActivityModel` extends `IBaseModel<Activity>`
   - `IBookingModel` extends `IBaseModel<Booking>`
   - `INotificationModel` extends `IBaseModel<Notification>`
   - `IReportModel` extends `IBaseModel<Report>`
   - `IFeedbackModel` extends `IBaseModel<Feedback>`

2. Implement repository classes using Spring Data JPA

### Phase 2: Service Layer
1. Create service interfaces (from Controller.pu):
   - `IAuthenticationUserManagement`
   - `IActivityManagement`
   - `IBookingTicketingSystem`
   - `INotificationSystem`
   - `IReportsAnalytics`

2. Implement service classes integrating design patterns

3. Create Memento Caretakers:
   - `ActivityHistoryCaretaker`
   - `BookingHistoryCaretaker`

### Phase 3: Controller Layer
1. Create Command implementations:
   - `RegisterCommand`
   - `LoginCommand`
   - `CreateEventCommand`
   - `UpdateEventCommand`
   - `DeleteEventCommand`
   - `BookEventCommand`
   - `SendNotificationCommand`
   - `GenerateReportCommand`

2. Create `SystemController` using Command Pattern

### Phase 4: Frontend (Next.js)
1. Pages to create:
   - `/login` - Login page
   - `/register` - Registration page
   - `/dashboard` - User dashboard
   - `/events` - Events listing
   - `/events/[id]` - Event details
   - `/admin/events` - Event management
   - `/bookings` - My bookings
   - `/tickets` - My tickets
   - `/notifications` - Notifications
   - `/reports` - Analytics dashboard
   - `/feedback` - Submit feedback

2. Components to create:
   - `EventCard` - Event display card
   - `BookingForm` - Booking form
   - `QRCodeDisplay` - QR code viewer
   - `NotificationList` - Notifications list
   - `ReportChart` - Charts for analytics
   - `FeedbackForm` - Feedback form

3. API client layer:
   - Create `apiClient.ts` with methods for all endpoints
   - Handle JWT token management
   - Error handling and retry logic

### Phase 5: Testing
1. Unit tests for:
   - All design pattern implementations
   - Service layer methods
   - Controller commands

2. Integration tests for:
   - Complete user workflows
   - API endpoints
   - Database operations

### Phase 6: Docker & Deployment
1. Update `docker-compose.yml`:
   - PostgreSQL configuration
   - Backend service with environment variables
   - Frontend service
   - Volume mappings

2. Create production Dockerfiles:
   - Multi-stage build for backend
   - Optimized Next.js build for frontend

3. Environment configuration:
   - `.env.example` with all required variables
   - Separate configs for dev/prod

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    SystemController                          │
│  ┌────────────────────────┐  ┌─────────────────────────┐   │
│  │ ControllerCommandInvoker│  │   RequestHandler Chain  │   │
│  └────────────────────────┘  └─────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                    Service Layer                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ Activity Mgmt│  │   Booking    │  │ Notification │      │
│  │  - Builder   │  │ - Strategy   │  │   - Bridge   │      │
│  │  - State     │  │ - Decorator  │  │   - Adapter  │      │
│  │  - Memento   │  │ - Memento    │  │              │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                  Repository Layer                            │
│  ┌──────────────────────────────────────────────────────┐   │
│  │           ModelFactory (Factory Pattern)             │   │
│  └──────────────────────────────────────────────────────┘   │
│     ┌──────┐  ┌────────┐  ┌─────────┐  ┌──────────┐        │
│     │ User │  │Activity│  │ Booking │  │Notification│       │
│     │Model │  │ Model  │  │  Model  │  │   Model    │       │
│     └──────┘  └────────┘  └─────────┘  └──────────┘        │
└─────────────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                    Data Layer (JPA)                          │
│    PostgreSQL Database                                       │
└─────────────────────────────────────────────────────────────┘
```

## 📝 Key Implementation Notes

1. **Pattern Integration:**
   - Commands use Services
   - Services use Factories, Builders, and Strategies
   - Repositories are managed by ModelFactory

2. **Error Handling:**
   - Use existing `GlobalExceptionHandler`
   - Custom exceptions for domain logic
   - Proper HTTP status codes

3. **Security:**
   - JWT tokens via `JwtUtil`
   - Chain of Responsibility for auth/authz
   - Rate limiting via `RateLimitHandler`

4. **Performance:**
   - Lazy loading for JPA relationships
   - Caching for frequently accessed data
   - Database indexing on foreign keys

5. **Testing:**
   - Mock pattern dependencies
   - Test each pattern in isolation
   - Integration tests for workflows

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Node.js 18+
- Docker & Docker Compose
- PostgreSQL (or use Docker)

### Development Setup
1. Backend:
   ```bash
   cd Project/backend
   mvn clean install
   mvn spring-boot:run
   ```

2. Frontend:
   ```bash
   cd Project/frontend
   npm install
   npm run dev
   ```

3. Docker (Full Stack):
   ```bash
   cd Project
   docker-compose up -d
   ```

## 📚 Resources

- PlantUML Diagrams: `Milestones/PM_3/Class Diagram/After DP/`
- Design Pattern Reference: See this document above
- Spring Boot Docs: https://spring.io/projects/spring-boot
- Next.js Docs: https://nextjs.org/docs

## 🎯 Success Criteria

- ✅ All 11 design patterns implemented
- ✅ Entity models match Data_Layer.pu
- ✅ Enums match specifications
- ⏳ All service interfaces implemented
- ⏳ Controllers using Command pattern
- ⏳ Frontend pages complete
- ⏳ Docker deployment working
- ⏳ Tests passing
- ⏳ Documentation complete

## 🔄 Next Immediate Steps

1. **Create Repository Interfaces** (1-2 hours)
2. **Implement Service Layer** (3-4 hours)
3. **Create Command Implementations** (2-3 hours)
4. **Build REST Controllers** (2-3 hours)
5. **Frontend Development** (4-6 hours)
6. **Testing** (2-3 hours)
7. **Documentation** (1-2 hours)

**Estimated Total Remaining Time:** 15-23 hours

---

*This guide is a living document. Update as implementation progresses.*
