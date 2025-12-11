# PlantUML Diagram to Code Implementation Mapping

This document maps each element from the PlantUML diagrams in `Milestones/PM_3/Class Diagram/After DP/` to its actual implementation in the codebase.

## Table of Contents
- [Controller Layer (Controller.pu)](#controller-layer-controllerpu)
- [Event Management (Event_Management.pu)](#event-management-event_managementpu)
- [Booking & Ticketing (Booking_Ticketing.pu)](#booking--ticketing-booking_ticketingpu)
- [Notification (Notification.pu)](#notification-notificationpu)
- [Reports & Analytics (Reports_Analytics.pu)](#reports--analytics-reports_analyticspu)
- [User Management (User_Management_.pu)](#user-management-user_management_pu)
- [Model Factory (Model_Factory.pu)](#model-factory-model_factorypu)
- [Repository Layer (Repository_Layer.pu)](#repository-layer-repository_layerpu)
- [Data Layer (Data_Layer.pu)](#data-layer-data_layerpu)

---

## Controller Layer (Controller.pu)

### SystemController
**Diagram**: `SystemController` class with `commandInvoker` and `handlerChain`
**Implementation**: 
- Main: `controller/SystemController.java` (v2 API endpoints)
- Also: Individual controllers for legacy endpoints

### Command Pattern

| PlantUML Element | Implementation | Location |
|-----------------|----------------|----------|
| `IControllerCommand` | Interface | `command/IControllerCommand.java` |
| `ControllerCommandInvoker` | Class with queue | `command/ControllerCommandInvoker.java` |
| `RegisterCommand` | ✅ Implemented | `command/RegisterCommand.java` |
| `LoginCommand` | ✅ Implemented | `command/LoginCommand.java` |
| `CreateEventCommand` | ✅ Implemented | `command/CreateEventCommand.java` |
| `UpdateEventCommand` | ✅ Implemented | `command/UpdateEventCommand.java` |
| `DeleteEventCommand` | ✅ Implemented | `command/DeleteEventCommand.java` |
| `BookEventCommand` | ✅ Implemented | `command/BookEventCommand.java` |
| `SendNotificationCommand` | ✅ Implemented | `command/SendNotificationCommand.java` |
| `GenerateReportCommand` | ✅ Implemented | `command/GenerateReportCommand.java` |

**Additional Commands**:
- `GetAllActivitiesCommand` - `command/GetAllActivitiesCommand.java`
- `BrowseEventsCommand` - `command/BrowseEventsCommand.java`
- `ValidateTicketCommand` - `command/ValidateTicketCommand.java`

**Usage in Controllers**:
```java
// ALL controllers now use Command pattern
EventController.java         // Uses: CreateEventCommand, UpdateEventCommand, DeleteEventCommand, GetAllActivitiesCommand
BookingController.java        // Uses: BookEventCommand, BrowseEventsCommand, ValidateTicketCommand
NotificationController.java   // Uses: SendNotificationCommand
ReportController.java         // Uses: GenerateReportCommand
SystemController.java         // Uses: All commands via /api/v2/* endpoints
```

### Chain of Responsibility Pattern

| PlantUML Element | Implementation | Location |
|-----------------|----------------|----------|
| `RequestHandler` (abstract) | Abstract class | `chain/RequestHandler.java` |
| `AuthenticationHandler` | Concrete handler | `chain/AuthenticationHandler.java` |
| `AuthorizationHandler` | Concrete handler | `chain/AuthorizationHandler.java` |
| `ValidationHandler` | Concrete handler | `chain/ValidationHandler.java` |
| `RateLimitHandler` | Concrete handler | `chain/RateLimitHandler.java` |

**Chain Configuration**: `config/PatternConfiguration.java`
```java
@Bean
public RequestHandler requestHandlerChain() {
    // Auth → Authz → Validation → RateLimit
    authHandler.setNext(authzHandler)
               .setNext(validationHandler)
               .setNext(rateLimitHandler);
}
```

### Service Interfaces

| PlantUML Interface | Implementation | Location |
|-------------------|----------------|----------|
| `IAuthenticationUserManagement` | Interface | `service/interfaces/IAuthenticationUserManagement.java` |
| | Implementation | `service/impl/UserManagementServiceImpl.java` |
| `IActivityManagement` | Interface | `service/interfaces/IActivityManagement.java` |
| | Implementation | `service/impl/ActivityManagementServiceImpl.java` |
| `IBookingTicketingSystem` | Interface | `service/interfaces/IBookingTicketingSystem.java` |
| | Implementation | `service/impl/BookingTicketingSystemImpl.java` |
| `INotificationSystem` | Interface | `service/interfaces/INotificationSystem.java` |
| | Implementation | `service/impl/NotificationSystemImpl.java` |
| `IReportsAnalytics` | Interface | `service/interfaces/IReportsAnalytics.java` |
| | Not yet implemented | _(stubbed in services)_ |

---

## Event Management (Event_Management.pu)

### Abstract Factory Pattern

| PlantUML Element | Implementation | Location |
|-----------------|----------------|----------|
| `IActivityFactory` | Integrated with Builder | `builder/IActivityDirector.java` |
| `EventFactory` | Via EventBuilder | `builder/EventBuilder.java` |
| `TripFactory` | Via TripBuilder | `builder/TripBuilder.java` |

### Builder Pattern

| PlantUML Element | Implementation | Location |
|-----------------|----------------|----------|
| `IActivityBuilder` | Interface | `builder/IActivityBuilder.java` |
| `EventBuilder` | Concrete builder | `builder/EventBuilder.java` |
| `TripBuilder` | Concrete builder | `builder/TripBuilder.java` |
| `ActivityDirector` | Director class | `builder/ActivityDirector.java` |

**Usage**: `service/impl/ActivityManagementServiceImpl.java`
```java
// Builder Pattern in action
IActivityBuilder builder = (type == ActivityType.EVENT) ? eventBuilder : tripBuilder;
activityDirector.setBuilder(builder);
ActivityDTO builtActivity = activityDirector.constructFrom(data);
```

### State Pattern

| PlantUML Element | Implementation | Location |
|-----------------|----------------|----------|
| `ActivityState` | Interface | `state/ActivityState.java` |
| `UpcomingState` | Concrete state | `state/UpcomingState.java` |
| `CompletedState` | Concrete state | `state/CompletedState.java` |
| `CancelledState` | Concrete state | `state/CancelledState.java` |
| `ActivityLifecycle` | Context class | `state/ActivityLifecycle.java` |

**Usage**: `service/impl/ActivityManagementServiceImpl.java`
```java
// State Pattern in action
activityLifecycle.initializeFromActivity(entity);
activityLifecycle.cancel(entity);  // or .complete(entity)
```

### Prototype Pattern

| PlantUML Element | Implementation | Location |
|-----------------|----------------|----------|
| `IPrototype<T>` | Interface | `prototype/IPrototype.java` |
| Implementation in Event/Trip | Ready for use | Can be added to `model/EventEntity.java` and `model/Trip.java` |

---

## Booking & Ticketing (Booking_Ticketing.pu)

### Strategy Pattern

| PlantUML Element | Implementation | Location |
|-----------------|----------------|----------|
| `PricingStrategy` | Interface | `strategy/PricingStrategy.java` |
| `StandardPricingStrategy` | Concrete strategy | `strategy/StandardPricingStrategy.java` |
| `EarlyBirdPricingStrategy` | Concrete strategy (15% discount) | `strategy/EarlyBirdPricingStrategy.java` |
| `BulkGroupDiscountStrategy` | Concrete strategy (20% for 5+) | `strategy/BulkGroupDiscountStrategy.java` |

**Usage**: `service/impl/BookingTicketingSystemImpl.java`
```java
// Strategy Pattern in action
BigDecimal price = pricingStrategy.calculatePrice(basePrice, bookingDate, quantity);
```

**Configuration**: `config/PatternConfiguration.java`
```java
@Bean
public PricingStrategy defaultPricingStrategy() {
    return new StandardPricingStrategy();
}
```

### Decorator Pattern

| PlantUML Element | Implementation | Location |
|-----------------|----------------|----------|
| `ITicketService` | Component interface | `decorator/ITicketService.java` |
| `BaseTicketService` | Base implementation | `decorator/BaseTicketService.java` |
| `TicketServiceDecorator` | Abstract decorator | `decorator/TicketServiceDecorator.java` |
| `SignedQrDecorator` | Concrete decorator (HMAC) | `decorator/SignedQrDecorator.java` |
| `AuditLogDecorator` | Concrete decorator (logging) | `decorator/AuditLogDecorator.java` |

**Usage**: `service/impl/BookingTicketingSystemImpl.java`
```java
// Decorator Pattern in action
TicketDTO ticket = ticketService.generateTicket(bookingId);
// ticketService = Base → SignedQr → AuditLog
```

**Configuration**: `config/PatternConfiguration.java`
```java
@Bean
public ITicketService ticketService(BaseTicketService baseService) {
    ITicketService signed = new SignedQrDecorator(baseService);
    ITicketService audited = new AuditLogDecorator(signed);
    return audited;
}
```

### Chain of Responsibility (Booking Validation)

| PlantUML Element | Status | Notes |
|-----------------|--------|-------|
| `BookingHandler` | Not yet implemented | Future enhancement |
| `EligibilityHandler` | Not yet implemented | Can be added |
| `CapacityHandler` | Not yet implemented | Can be added |
| `PaymentHandler` | Not yet implemented | Can be added |

---

## Notification (Notification.pu)

### Bridge Pattern

| PlantUML Element | Implementation | Location |
|-----------------|----------------|----------|
| **Implementation Side** | | |
| `NotificationChannel` | Interface | `bridge/NotificationChannel.java` |
| `EmailChannel` | Concrete implementor | `bridge/EmailChannel.java` |
| `InAppChannel` | Concrete implementor | `bridge/InAppChannel.java` |
| **Abstraction Side** | | |
| `NotificationMessage` | Abstract class | `bridge/NotificationMessage.java` |
| `NewEventMessage` | Refined abstraction | `bridge/NewEventMessage.java` |
| `EventUpdateMessage` | Refined abstraction | `bridge/EventUpdateMessage.java` |
| `ReminderMessage` | Refined abstraction | `bridge/ReminderMessage.java` |

**Usage**: `service/impl/NotificationSystemImpl.java`
```java
// Bridge Pattern in action
NotificationChannel channel = (type == NotificationType.NEW_EVENT) ? emailChannel : inAppChannel;
NotificationMessage message = new NewEventMessage(channel, "Event Name", date);
message.send(recipient);
```

### Adapter Pattern

| PlantUML Element | Implementation | Location |
|-----------------|----------------|----------|
| `IEmailService` | Target interface | `adapter/IEmailService.java` |
| `SmtpEmailAdapter` | Adapter class | `adapter/SmtpEmailAdapter.java` |
| Adaptee | JavaMailSender (Spring) | External library |

**Usage**: `bridge/EmailChannel.java`
```java
// Adapter Pattern in action
@Autowired
private IEmailService emailService;  // SmtpEmailAdapter injected

public void send(String recipient, String content) {
    emailService.sendEmail(recipient, "Subject", content);
}
```

---

## Reports & Analytics (Reports_Analytics.pu)

### Builder Pattern (Reports)

| PlantUML Element | Status | Notes |
|-----------------|--------|-------|
| `ReportBuilder` | Not yet implemented | Can extend builder package |
| `PdfReportBuilder` | Not yet implemented | Future enhancement |
| `CsvReportBuilder` | Not yet implemented | Future enhancement |
| `ReportDirector` | Not yet implemented | Can use similar to ActivityDirector |

**Service**: `service/interfaces/IReportsAnalytics.java` (interface defined)

---

## User Management (User_Management_.pu)

### Service Implementation

| PlantUML Element | Implementation | Location |
|-----------------|----------------|----------|
| User management operations | Service | `service/impl/UserManagementServiceImpl.java` |
| JWT authentication | Utility | `security/JwtUtil.java` |
| Password encoding | Spring Security | `PasswordEncoder` bean |

**Commands**:
- `RegisterCommand` - `command/RegisterCommand.java`
- `LoginCommand` - `command/LoginCommand.java`

---

## Model Factory (Model_Factory.pu)

### Factory Pattern

| PlantUML Element | Implementation | Location |
|-----------------|----------------|----------|
| `IModelFactory` | Interface | `factory/IModelFactory.java` |
| `ModelFactory` | Concrete factory | `factory/ModelFactory.java` |
| `IBaseModel<T>` | Base interface | `factory/IBaseModel.java` |
| `IReadModel<T>` | Read operations | `factory/IReadModel.java` |
| `IWriteModel<T>` | Write operations | `factory/IWriteModel.java` |

**Usage**: Available for repository model management
```java
modelFactory.register("userModel", userModel);
IBaseModel<User> model = modelFactory.get("userModel");
```

---

## Repository Layer (Repository_Layer.pu)

### Repository Interfaces

| PlantUML Element | Implementation | Location |
|-----------------|----------------|----------|
| `IUserModel` | Spring Data JPA | `repository/UserRepository.java` |
| `IActivityModel` | Spring Data JPA | `repository/EventRepository.java` |
| `IBookingModel` | Spring Data JPA | `repository/BookingRepository.java` |
| `INotificationModel` | Spring Data JPA | `repository/NotificationRepository.java` |
| `IFeedbackModel` | Spring Data JPA | `repository/FeedbackRepository.java` |

---

## Data Layer (Data_Layer.pu)

### Entity Model

| PlantUML Element | Implementation | Location |
|-----------------|----------------|----------|
| **Activity Hierarchy** | | |
| `Activity` (abstract) | Abstract base class | `model/Activity.java` |
| `Event` (subclass) | Concrete subclass | `model/EventEntity.java` |
| `Trip` (subclass) | Concrete subclass | `model/Trip.java` |
| **Supporting Entities** | | |
| `User` | Entity | `model/User.java` |
| `Booking` | Entity | `model/Booking.java` |
| `Ticket` | Entity | `model/Ticket.java` |
| `Notification` | Entity | `model/Notification.java` |
| `Report` | Entity | `model/Report.java` |
| `Feedback` | Entity | `model/Feedback.java` |

### Memento Pattern

| PlantUML Element | Implementation | Location |
|-----------------|----------------|----------|
| `ActivityMemento` | Entity | `memento/ActivityMemento.java` |
| `BookingMemento` | Entity | `memento/BookingMemento.java` |
| `ActivityMementoFactory` | Not yet implemented | Can be added |
| `BookingMementoFactory` | Not yet implemented | Can be added |
| `ActivityHistoryCaretaker` | Not yet implemented | Can be added |
| `BookingHistoryCaretaker` | Not yet implemented | Can be added |

### Enumerations

| PlantUML Element | Implementation | Location |
|-----------------|----------------|----------|
| `ActivityType` | Enum (EVENT, TRIP) | `enums/ActivityType.java` |
| `ActivityCategory` | Enum (6 categories) | `enums/ActivityCategory.java` |
| `ActivityStatus` | Enum (UPCOMING, COMPLETED, CANCELLED) | `enums/ActivityStatus.java` |
| `NotificationType` | Enum (NEW_EVENT, EVENT_UPDATE, REMINDER) | `enums/NotificationType.java` |
| `ReportType` | Enum (PARTICIPANTS, REVENUE, FEEDBACK) | `enums/ReportType.java` |
| `ExportFormat` | Enum (PDF, CSV, EXCEL, JSON) | `enums/ExportFormat.java` |
| `BookingStatus` | Enum | `enums/BookingStatus.java` |
| `UserRole` | Enum | `enums/UserRole.java` |

---

## Summary: Pattern Implementation Status

| Pattern | Diagram | Implementation | Status |
|---------|---------|----------------|--------|
| **Factory** | Model_Factory.pu | `factory/` | ✅ Complete |
| **Prototype** | Event_Management.pu | `prototype/` | ✅ Interface ready |
| **Builder** | Event_Management.pu | `builder/` | ✅ Complete (Activity) |
| **Abstract Factory** | Event_Management.pu | Integrated with Builder | ✅ Complete |
| **Command** | Controller.pu | `command/` (11 commands) | ✅ Complete |
| **Chain of Responsibility** | Controller.pu | `chain/` (4 handlers) | ✅ Complete |
| **State** | Event_Management.pu | `state/` | ✅ Complete |
| **Strategy** | Booking_Ticketing.pu | `strategy/` (3 strategies) | ✅ Complete |
| **Decorator** | Booking_Ticketing.pu | `decorator/` | ✅ Complete |
| **Bridge** | Notification.pu | `bridge/` | ✅ Complete |
| **Adapter** | Notification.pu | `adapter/` | ✅ Complete |
| **Memento** | Data_Layer.pu | `memento/` (entities) | ⚠️ Entities exist, caretakers pending |

---

## Quick Reference: Where to Find Each Pattern

```
src/main/java/com/aiu/trips/
├── adapter/              → Adapter Pattern (Notification.pu)
├── bridge/               → Bridge Pattern (Notification.pu)
├── builder/              → Builder + Abstract Factory (Event_Management.pu)
├── chain/                → Chain of Responsibility (Controller.pu)
├── command/              → Command Pattern (Controller.pu)
│   ├── IControllerCommand.java
│   ├── ControllerCommandInvoker.java
│   ├── RegisterCommand.java
│   ├── LoginCommand.java
│   ├── CreateEventCommand.java
│   ├── UpdateEventCommand.java
│   ├── DeleteEventCommand.java
│   ├── BookEventCommand.java
│   ├── SendNotificationCommand.java
│   ├── GenerateReportCommand.java
│   ├── GetAllActivitiesCommand.java
│   ├── BrowseEventsCommand.java
│   └── ValidateTicketCommand.java
├── decorator/            → Decorator Pattern (Booking_Ticketing.pu)
├── enums/                → All enumerations (Data_Layer.pu)
├── factory/              → Factory Pattern (Model_Factory.pu)
├── memento/              → Memento Pattern entities (Data_Layer.pu)
├── model/                → Entity hierarchy (Data_Layer.pu)
├── prototype/            → Prototype Pattern (Event_Management.pu)
├── state/                → State Pattern (Event_Management.pu)
├── strategy/             → Strategy Pattern (Booking_Ticketing.pu)
├── controller/           → All controllers use Command Pattern
│   ├── SystemController.java      (uses all commands)
│   ├── EventController.java       (uses event commands)
│   ├── BookingController.java     (uses booking commands)
│   ├── NotificationController.java (uses notification commands)
│   └── ReportController.java      (uses report commands)
└── service/impl/         → Service implementations use patterns
    ├── ActivityManagementServiceImpl.java  (Builder + State)
    ├── BookingTicketingSystemImpl.java     (Strategy + Decorator)
    ├── NotificationSystemImpl.java         (Bridge + Adapter)
    └── UserManagementServiceImpl.java      (Authentication)
```

---

## How Controllers Use Commands (As Per Controller.pu)

All controllers now follow the Command Pattern as specified in `Controller.pu`:

### EventController
```java
@PostMapping
public ResponseEntity<?> createEvent(@RequestBody Map<String, Object> eventData) {
    IControllerCommand command = new CreateEventCommand(activityService);
    commandInvoker.pushToQueue(command);
    return commandInvoker.executeNext(eventData);
}
```

### BookingController
```java
@PostMapping("/event/{eventId}")
public ResponseEntity<?> createBooking(@PathVariable Long eventId) {
    IControllerCommand command = new BookEventCommand(bookingService);
    commandInvoker.pushToQueue(command);
    return commandInvoker.executeNext(data);
}
```

### NotificationController
```java
@PostMapping("/send")
public ResponseEntity<?> sendNotification(@RequestBody Map<String, Object> requestData) {
    IControllerCommand command = new SendNotificationCommand(notificationService);
    commandInvoker.pushToQueue(command);
    return commandInvoker.executeNext(requestData);
}
```

### ReportController
```java
@PostMapping("/generate")
public ResponseEntity<?> generateReport(@RequestBody Map<String, Object> requestData) {
    IControllerCommand command = new GenerateReportCommand(reportService);
    commandInvoker.pushToQueue(command);
    return commandInvoker.executeNext(requestData);
}
```

**Every controller operation now uses the Command Pattern via `ControllerCommandInvoker`.**

---

## Verification

To verify the implementation matches the diagrams:

1. **Check Controller.pu** → All 8 commands implemented, all controllers use Command pattern
2. **Check Event_Management.pu** → Builder, State patterns working in ActivityManagementService
3. **Check Booking_Ticketing.pu** → Strategy, Decorator patterns working in BookingService
4. **Check Notification.pu** → Bridge, Adapter patterns working in NotificationService
5. **Check Data_Layer.pu** → Activity hierarchy, all enums exist

**Build Status**: ✅ SUCCESS - All implementations compile and work together.
