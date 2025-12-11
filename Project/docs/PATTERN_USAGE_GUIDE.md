# Pattern Implementation Usage Guide

## Overview

All 11 design patterns are now **fully implemented and integrated** into the application. This guide shows how the patterns are used in the actual working system.

## Using the Patterns

### 1. SystemController - Command & Chain of Responsibility

The `SystemController` demonstrates real usage of Command Pattern and Chain of Responsibility:

```java
@RestController
@RequestMapping("/api/v2")
public class SystemController {
    
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> requestData, HttpServletRequest request) {
        // Chain of Responsibility processes the request
        handlerChain.handle(request);
        
        // Command Pattern executes the business logic
        IControllerCommand command = new RegisterCommand(authService);
        commandInvoker.pushToQueue(command);
        return commandInvoker.executeNext(requestData);
    }
}
```

**Available Endpoints:**
- `POST /api/v2/auth/register` - Register new user
- `POST /api/v2/auth/login` - Login
- `POST /api/v2/activities` - Create activity (uses Builder pattern)
- `GET /api/v2/activities` - List all activities
- `POST /api/v2/bookings` - Book an event (uses Strategy pattern)

### 2. ActivityManagementService - Builder & State Patterns

Creates activities using the Builder pattern:

```java
@Service
public class ActivityManagementServiceImpl implements IActivityManagement {
    
    @Override
    public ActivityDTO createActivity(ActivityDTO data, ActivityType type) {
        // Builder Pattern via Director
        IActivityBuilder builder = (type == ActivityType.EVENT) ? eventBuilder : tripBuilder;
        activityDirector.setBuilder(builder);
        
        ActivityDTO builtActivity = activityDirector.constructFrom(data);
        
        // State Pattern for lifecycle management
        activityLifecycle.initializeFromActivity(entity);
        
        return save(entity);
    }
}
```

### 3. BookingService - Strategy & Decorator Patterns

Booking uses pricing strategies and decorated ticket service:

```java
@Service
public class BookingTicketingSystemImpl implements IBookingTicketingSystem {
    
    @Override
    public BookingDTO bookEvent(Long studentId, Long eventId) {
        // Strategy Pattern for dynamic pricing
        BigDecimal price = pricingStrategy.calculatePrice(basePrice, bookingDate, 1);
        
        // Save booking...
        
        return booking;
    }
    
    @Override
    public TicketDTO generateTicket(Long bookingId) {
        // Decorator Pattern (signed & audited)
        return ticketService.generateTicket(bookingId);
    }
}
```

### 4. NotificationService - Bridge & Adapter Patterns

Sends notifications through different channels:

```java
@Service
public class NotificationSystemImpl implements INotificationSystem {
    
    @Override
    public void sendNotification(Long userId, String message, NotificationType type) {
        // Bridge Pattern - decouple channel from message
        NotificationChannel channel = (type == NotificationType.NEW_EVENT) ? emailChannel : inAppChannel;
        NotificationMessage notificationMessage = createMessage(channel, type, message);
        
        // Adapter Pattern wraps JavaMailSender
        notificationMessage.send(user.getEmail());
    }
}
```

### 5. Pattern Configuration

All patterns are wired in `PatternConfiguration`:

```java
@Configuration
public class PatternConfiguration {
    
    @Bean
    public RequestHandler requestHandlerChain() {
        // Build Chain: Auth → Authz → Validation → RateLimit
        authHandler.setNext(authzHandler);
        authzHandler.setNext(validationHandler);
        validationHandler.setNext(rateLimitHandler);
        return authHandler;
    }
    
    @Bean
    public ITicketService ticketService(BaseTicketService baseService) {
        // Stack Decorators: Base → Signed → Audited
        ITicketService signed = new SignedQrDecorator(baseService);
        ITicketService audited = new AuditLogDecorator(signed);
        return audited;
    }
    
    @Bean
    public PricingStrategy defaultPricingStrategy() {
        return new StandardPricingStrategy();
    }
}
```

## Testing the Implementation

### 1. Start the Application

```bash
cd Project/backend
mvn spring-boot:run
```

### 2. Test Pattern Usage

**Register a User (Command Pattern):**
```bash
curl -X POST http://localhost:8080/api/v2/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

**Create Activity (Builder Pattern):**
```bash
curl -X POST http://localhost:8080/api/v2/activities \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "name": "Tech Conference 2024",
    "description": "Annual tech conference",
    "location": "Convention Center",
    "activityDate": "2024-12-15T09:00:00",
    "capacity": 100,
    "price": 50.00,
    "type": "EVENT"
  }'
```

**Book Event (Strategy + Decorator Patterns):**
```bash
curl -X POST http://localhost:8080/api/v2/bookings \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "studentId": 1,
    "eventId": 1
  }'
```

## Pattern Integration Summary

| Pattern | Location | Used By | Purpose |
|---------|----------|---------|---------|
| **Command** | `command/` | `SystemController` | Request routing and execution |
| **Chain of Responsibility** | `chain/` | `SystemController` | Request processing pipeline |
| **Builder** | `builder/` | `ActivityManagementService` | Complex object construction |
| **State** | `state/` | `ActivityManagementService` | Activity lifecycle management |
| **Strategy** | `strategy/` | `BookingService` | Dynamic pricing |
| **Decorator** | `decorator/` | `BookingService` | Ticket enhancement (signing, logging) |
| **Bridge** | `bridge/` | `NotificationService` | Decouple channels from messages |
| **Adapter** | `adapter/` | `EmailChannel` | Email service integration |
| **Factory** | `factory/` | All services | Model/repository management |
| **Prototype** | `prototype/` | Activities | Object cloning |

## Key Benefits

1. **Extensibility**: Add new commands, handlers, strategies without modifying existing code
2. **Maintainability**: Clear separation of concerns
3. **Testability**: Each pattern component can be tested independently
4. **Flexibility**: Swap implementations at runtime (strategies, decorators)
5. **Reusability**: Pattern components can be reused across the application

## Next Steps

1. **Add More Commands**: Create commands for update, delete operations
2. **Add More Strategies**: Implement seasonal pricing, group discounts
3. **Add More Decorators**: Email notifications, SMS alerts
4. **Add More Handlers**: Logging, metrics collection
5. **Frontend Integration**: Build UI that uses these endpoints

## Documentation

- See `DESIGN_PATTERNS_IMPLEMENTATION_GUIDE.md` for detailed pattern documentation
- See `PATTERNS_IMPLEMENTATION_STATUS.md` for implementation status
- See JavaDoc in each class for usage examples
