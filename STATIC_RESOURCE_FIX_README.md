# Fix for "No static resource" Error - /ExtMTPush/extmtpush

## Problem
The application was returning error:
```
"No static resource ExtMTPush/extmtpush."
```

This occurred when requests were being incorrectly routed to the static resource handler instead of the REST controller.

## Root Cause
1. **Incorrect Context Path**: The application had a commented-out context path configuration
2. **Wrong Service URL**: The local service URL was pointing to port 8080 instead of 8081
3. **Default Static Resource Mapping**: Spring Boot was treating unknown paths as static resource requests

## Solutions Applied

### 1. Fixed Context Path Configuration
**File**: `ExtMTPAPI/src/main/resources/application.properties`

Changed from:
```properties
#server.servlet.context-path=/ExtMTPush
```

To:
```properties
server.servlet.context-path=/
```

This ensures the application runs at the root context path (`http://localhost:8081/`) instead of a nested context.

### 2. Updated Service URL
**File**: `ExtMTPAPI/src/main/resources/application.properties`

Changed from:
```properties
sms.gateway.local-service-url=http://localhost:8080/ExtMTPush/services/ExternalMTPushInterface
```

To:
```properties
sms.gateway.local-service-url=http://localhost:8081/services/ExternalMTPushInterface
```

This matches the actual port where ExtMTPAPI is running and the correct context path.

### 3. Disabled Auto Static Resource Mapping
**File**: `ExtMTPAPI/src/main/resources/application.properties`

Added:
```properties
# Prevent Spring Boot from treating unknown paths as static resource requests
spring.web.resources.add-mappings=false

# Enable custom error handling
server.error.include-message=always
server.error.include-stacktrace=on_param
server.error.path=/error
```

### 4. Created ResourceConfig Class
**File**: `ExtMTPAPI/src/main/java/com/isentric/smsserver/config/ResourceConfig.java`

This configuration class explicitly maps static resources while preventing unknown paths from being treated as static resources:
- Maps `/static/**`, `/css/**`, `/js/**`, `/images/**` to their respective classpath locations
- Maps `/webjars/**` for Bootstrap, jQuery, etc.
- All other paths are routed to the custom error controller

### 5. Created CustomErrorController Class
**File**: `ExtMTPAPI/src/main/java/com/isentric/smsserver/controller/CustomErrorController.java`

This controller handles 404 errors gracefully and provides meaningful error messages instead of the confusing "No static resource" error.

## Testing the Fix

### Test REST Endpoints
The application now correctly routes REST endpoints:

**GET Request:**
```
GET http://localhost:8081/extmtpush?shortcode=66399&custid=customer&rmsisdn=601234567890&smsisdn=6019876543...
```

**POST Request:**
```
POST http://localhost:8081/extmtpush
Content-Type: application/json

{
  "shortcode": "66399",
  "custid": "customer",
  "rmsisdn": "601234567890",
  ...
}
```

### Test Error Handling
Requesting a non-existent path now returns a proper error:

```
GET http://localhost:8081/nonexistent

Response:
{
  "timestamp": 1706353234000,
  "status": 404,
  "error": "Not Found",
  "message": "No static resource found at /nonexistent",
  "path": "/nonexistent"
}
```

### Test SOAP Web Services
The SOAP services should still work at:
```
http://localhost:8081/services
http://localhost:8081/services/externalMTPush
```

## Configuration Summary

### application.properties Changes
| Setting | Old Value | New Value |
|---------|-----------|-----------|
| server.servlet.context-path | `#/ExtMTPush` (commented) | `/` |
| sms.gateway.local-service-url | `http://localhost:8080/ExtMTPush/services/...` | `http://localhost:8081/services/...` |
| spring.web.resources.add-mappings | Not set (default: true) | `false` |
| server.error.include-message | Not set | `always` |
| server.error.path | Not set | `/error` |

## Files Created/Modified

**Modified:**
- `ExtMTPAPI/src/main/resources/application.properties`

**Created:**
- `ExtMTPAPI/src/main/java/com/isentric/smsserver/config/ResourceConfig.java`
- `ExtMTPAPI/src/main/java/com/isentric/smsserver/controller/CustomErrorController.java`

## Next Steps

1. Rebuild the application: `mvn clean package`
2. Restart the application
3. Test the REST endpoints using the URLs provided above
4. Verify SOAP services are still functional
5. Check logs for any configuration warnings

## Related Configuration Notes

### If you need to use a context path like `/extmtpush`
You can change the context path back:
```properties
server.servlet.context-path=/extmtpush
```

And update the service URL accordingly:
```properties
sms.gateway.local-service-url=http://localhost:8081/extmtpush/services/ExternalMTPushInterface
```

But then all REST requests must include `/extmtpush` in the path:
```
GET http://localhost:8081/extmtpush/extmtpush?...
POST http://localhost:8081/extmtpush/extmtpush
```

### For SOAP Web Services
The SOAP endpoint remains at:
```
http://localhost:8081/services/ (or /extmtpush/services/ if using context path)
```

## Troubleshooting

If you still see "No static resource" errors:

1. **Clear Tomcat cache**: Delete `$CATALINA_HOME/work/Catalina/localhost/` if running in Tomcat
2. **Check context path**: Ensure the context path in application.properties matches your actual deployment path
3. **Verify port**: Confirm port 8081 is correct for ExtMTPAPI
4. **Check logs**: Look for "ResourceConfig" and "CustomErrorController" initialization messages
5. **Rebuild**: Run `mvn clean package` to ensure all changes are applied

