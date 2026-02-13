# ExtMTPush HTML Form Setup Documentation

## Overview
An HTML web form has been created for the `/extmtpush` endpoint that allows users to submit SMS messages through a user-friendly interface.

## Files Created/Modified

### 1. HTML Template
**File:** `/ExtMTPAPI/src/main/resources/templates/extmtpush.html`

Features:
- Modern, responsive design with gradient background
- Form fields for required parameters:
  - **Shortcode** (required) - SMS shortcode
  - **Customer ID** (required) - Customer identifier
  - **Recipient MSISDN** (required) - Phone number to receive SMS
  - **SMS ISDN/Sender** (required) - Sender ID/number
  - **Message Text** (required) - SMS message content (textarea)

- Hidden fields with default values:
  - `mtid` - Auto-generated Message ID (MSG_timestamp)
  - `mtprice` - Default: "000"
  - `productCode` - Default: ""
  - `productType` - Default: "10"
  - `keyword` - Default: ""
  - `dataEncoding` - Default: "8"
  - `dataUrl` - Default: ""
  - `dnRep` - Default: "0"
  - `groupTag` - Default: "10"
  - `cFlag` - Default: "0"

- Buttons:
  - **Send SMS** - Submits the form
  - **Clear** - Resets all form fields

- Response display area showing server response
- Loading spinner during submission
- Client-side validation
- Automatic MTID generation with timestamp

### 2. Controller Updates
**File:** `/ExtMTPAPI/src/main/java/com/isentric/smsserver/controller/SmsController.java`

Changes:
- Changed from `@RestController` to `@Controller` to support HTML template rendering
- Added `showForm(Model model)` method to serve HTML page
- Maintains existing API endpoints:
  - `GET /extmtpush/api` - Query-based API endpoint
  - `POST /extmtpush` - JSON-based API endpoint
- All endpoints return properly formatted responses

### 3. POM.xml Updates
**File:** `/ExtMTPAPI/pom.xml`

Added dependency:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

### 4. Application Properties Updates
**File:** `/ExtMTPAPI/src/main/resources/application.properties`

Changes:
- Enabled resource mapping: `spring.web.resources.add-mappings=true`
- Added Thymeleaf configuration:
  - `spring.thymeleaf.enabled=true`
  - `spring.thymeleaf.prefix=classpath:/templates/`
  - `spring.thymeleaf.suffix=.html`
  - `spring.thymeleaf.mode=HTML`
  - `spring.thymeleaf.cache=false` (for development)

## Usage

### Access the Form
Open your browser and navigate to:
```
http://localhost:8083/ExtMTPush/extmtpush
```

### Using the Form
1. Fill in the required fields:
   - Shortcode (e.g., 10086)
   - Customer ID (e.g., isentric_demo)
   - Recipient MSISDN (e.g., 601126141207)
   - SMS ISDN/Sender (e.g., 62003)
   - Message Text (e.g., "Test on 13Feb")

2. Click "Send SMS"

3. The form will:
   - Auto-generate an MTID with timestamp
   - Set all other fields to their default values
   - Submit the request to the backend
   - Display the server response

### API Endpoints Available

**GET Endpoint (Query Parameters):**
```
GET /extmtpush/api?shortcode=10086&custid=isentric_demo&rmsisdn=601126141207&smsisdn=62003&dataStr=Test
```

**POST Endpoint (JSON Body):**
```
POST /extmtpush
Content-Type: application/json

{
  "shortcode": "10086",
  "custid": "isentric_demo",
  "rmsisdn": "601126141207",
  "smsisdn": "62003",
  "mtid": "MSG_1707747024000",
  "mtprice": "000",
  "productCode": "",
  "productType": 10,
  "keyword": "",
  "dataEncoding": 8,
  "dataStr": "Test on 13Feb",
  "dataUrl": "",
  "dnRep": 0,
  "groupTag": "10",
  "cFlag": 0
}
```

## Data Sample
The form expects data in the following format:

```json
{
  "shortcode": "10086",
  "custid": "isentric_demo",
  "rmsisdn": "601126141207",
  "smsisdn": "62003",
  "mtid": "MSG_{{$timestamp}}",
  "mtprice": "000",
  "productCode": "",
  "productType": 10,
  "keyword": "",
  "dataEncoding": 8,
  "dataStr": "Test on 13Feb",
  "dataUrl": "",
  "dnRep": 0,
  "groupTag": "10"
}
```

## Build & Run

### Build
```bash
cd /home/arun/IdeaProjects/isentric-smsgateway/ExtMTPAPI
mvn clean install
```

### Run
The application will start on port 8083 with context path `/ExtMTPush`

Access the form at: `http://localhost:8083/ExtMTPush/extmtpush`

## Technical Details

### Frontend Features
- Responsive design that works on mobile and desktop
- Client-side form validation
- Real-time response display
- Auto-scrolling to response section
- Loading spinner during submission
- Error handling with distinct styling

### Backend Integration
- The form submits JSON to `POST /extmtpush`
- All required fields are validated
- Default values are automatically set for hidden fields
- MTID is auto-generated with timestamp to ensure uniqueness
- Responses are displayed in HTML format

## Notes
- The MTID is generated on the client side and regenerated for each submission
- All default values match the provided sample data
- The form works with both desktop and mobile browsers
- Response messages are displayed in a formatted box below the form
- Clearing the form also regenerates a new MTID

