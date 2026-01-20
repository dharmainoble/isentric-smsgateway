# BulkGateway API - Postman Collection

This folder contains Postman collection and environment files for testing the BulkGateway REST API.

## Files

| File | Description |
|------|-------------|
| `BulkGateway_API_Collection.postman_collection.json` | Complete API collection with all endpoints |
| `BulkGateway_Local.postman_environment.json` | Environment variables for local testing |

## How to Import into Postman

### Step 1: Import Collection
1. Open Postman
2. Click **Import** button (top-left)
3. Drag and drop `BulkGateway_API_Collection.postman_collection.json` or click **Upload Files**
4. Click **Import**

### Step 2: Import Environment
1. Click **Import** button again
2. Upload `BulkGateway_Local.postman_environment.json`
3. Click **Import**

### Step 3: Select Environment
1. Click the environment dropdown (top-right corner)
2. Select **BulkGateway - Local**

## Collection Structure

```
BulkGateway API
├── Health & Info
│   ├── Health Check (GET /health)
│   ├── Application Info (GET /info)
│   └── Home (GET /)
│
├── Messages - INSERT Operations
│   ├── Create Single Message (POST /messages)
│   └── Create Bulk Messages (POST /messages/bulk)
│
├── Messages - SELECT Operations
│   ├── Get All Messages (GET /messages)
│   ├── Get Message by ID (GET /messages/{id})
│   ├── Get Messages by Sender (GET /messages/sender/{sender})
│   ├── Get Messages by Recipient (GET /messages/recipient/{recipient})
│   ├── Get Messages by Status (GET /messages/status/{status})
│   └── Count Messages by Status (GET /messages/count/status/{status})
│
├── Messages - UPDATE Operations
│   ├── Update Message (PUT /messages/{id})
│   ├── Update Message Status (PATCH /messages/{id}/status)
│   ├── Update Message Status to DELIVERED
│   └── Update Message Status to FAILED
│
├── Messages - DELETE Operations
│   └── Delete Message (DELETE /messages/{id})
│
└── Test Scenarios
    ├── Scenario 1 - Insert SMS Message
    ├── Scenario 2 - Insert Email Notification
    ├── Scenario 3 - Insert Push Notification
    ├── Scenario 4 - Bulk Marketing Campaign
    └── Scenario 5 - Validation Error Test
```

## Quick Start Testing

### 1. Start the Application
```bash
cd /home/arun/Documents/rec/BulkGatewayWar
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

### 2. Test INSERT (Create Message)
- Open **Messages - INSERT Operations** → **Create Single Message**
- Click **Send**
- Expected Response (201 Created):
```json
{
    "success": true,
    "message": "Message created successfully",
    "data": {
        "id": 6,
        "sender": "john@example.com",
        "recipient": "jane@example.com",
        "content": "Hello Jane! This is a test message from Postman.",
        "status": "PENDING",
        "createdAt": "2026-01-12T10:30:00",
        "updatedAt": "2026-01-12T10:30:00"
    }
}
```

### 3. Test SELECT (Get All Messages)
- Open **Messages - SELECT Operations** → **Get All Messages**
- Click **Send**
- You'll see all messages in the database including auto-inserted sample data

### 4. Test BULK INSERT
- Open **Messages - INSERT Operations** → **Create Bulk Messages**
- Click **Send**
- Multiple messages will be inserted at once

## Message Status Values

| Status | Description |
|--------|-------------|
| `PENDING` | Message created, waiting to be sent |
| `SENT` | Message has been sent |
| `DELIVERED` | Message delivered successfully |
| `FAILED` | Message delivery failed |

## Environment Variables

| Variable | Default Value | Description |
|----------|---------------|-------------|
| `baseUrl` | `http://localhost:8081/api` | Base URL of the API |
| `messageId` | `1` | Default message ID for testing |

## Sample Request Bodies

### Single Message Insert
```json
{
    "sender": "john@example.com",
    "recipient": "jane@example.com",
    "content": "Hello Jane! This is a test message."
}
```

### Bulk Message Insert
```json
[
    {
        "sender": "sender1@example.com",
        "recipient": "recipient1@example.com",
        "content": "Message 1"
    },
    {
        "sender": "sender2@example.com",
        "recipient": "recipient2@example.com",
        "content": "Message 2"
    }
]
```

## Troubleshooting

### Connection Refused
- Make sure the Spring Boot application is running
- Check if port 8081 is available
- Verify the baseUrl in environment settings

### 404 Not Found
- Check if context path `/api` is included in the URL
- Verify the endpoint path is correct

### 400 Bad Request
- Check request body JSON format
- Ensure required fields (sender, recipient, content) are provided

