Postman / curl examples and verification SQL for SMSMessageController (/api/v1/messages/send)

1) Postman
- Import `BulkGateway_postman_SMSMessageController.json` into Postman.
- Set the `baseUrl` environment variable to your running app URL, for example `http://localhost:8090`.
- Use the request "Send SMS (POST /api/v1/messages/send)".
- The sample JSON body matches `SMSMessageDTO` fields.

2) curl

Run this command (adjust host/port if different):

```
curl -X POST \
  http://localhost:8090/api/v1/messages/send \
  -H 'Content-Type: application/json' \
  -d '{
  "guid": "test-guid-123",
  "groupId": "api_request",
  "ip": "127.0.0.1",
  "smsc": "smpp",
  "telco": "maxis",
  "smppName": "smpp1",
  "smppConfig": "default",
  "moid": "moid123",
  "sender": "API",
  "recipient": "60123456789",
  "senderType": 0,
  "keyword": "promo",
  "message": "Hello from API (POST)",
  "messageType": 0,
  "date": null,
  "price": "0.00",
  "callbackURL": "http://example.com/callback",
  "shortcode": "10086",
  "userGroup": "default",
  "queueSequence": 0,
  "cFlag": 0
}'
```

3) SQL verification (MySQL)

Assuming `tbl_smpp_in` is in the `bulkgateway` schema (as in original code), run:

```
USE bulkgateway;
SELECT row_id, guid, groupId, ip, sender, recipient, messageType, date, userGroup FROM tbl_smpp_in WHERE guid = 'test-guid-123' ORDER BY row_id DESC LIMIT 1;
```

Notes
- The controller uses `SMSMessageService` to insert data; ensure the application is running and that the service implementation persists the DTO into the `tbl_smpp_in` table. If the service is not wired or returns an error, the response will reflect that.
- The sample body sets `date` to null; the server code often uses `now()` in SQL inserts, so the resulting DB `date` may be the current server time.
- If your app is configured with a different persistence unit or table name, adjust the SQL accordingly.

