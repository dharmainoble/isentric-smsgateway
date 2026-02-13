# ExtMTPush HTML Form - Quick Start Guide

## What's Been Created

A professional HTML web form for the ExtMTPush SMS Gateway API with the following components:

### 1. **HTML Interface** (`extmtpush.html`)
   - Location: `/ExtMTPAPI/src/main/resources/templates/extmtpush.html`
   - Modern, responsive design
   - Form inputs for required fields only
   - Auto-fills default values for other fields

### 2. **Updated Controller** (`SmsController.java`)
   - Changed to support both HTML templates and API responses
   - `GET /extmtpush` → Returns the HTML form
   - `GET /extmtpush/api` → Returns JSON/Text response
   - `POST /extmtpush` → Accepts JSON and returns response

### 3. **Configuration Updates**
   - Added Thymeleaf dependency to `pom.xml`
   - Enabled template rendering in `application.properties`

## How to Use

### Step 1: Build the Project
```bash
cd /home/arun/IdeaProjects/isentric-smsgateway/ExtMTPAPI
mvn clean install
```

### Step 2: Run the Application
The application starts automatically on port **8083** with context path **/ExtMTPush**

### Step 3: Access the Form
Open your web browser and go to:
```
http://localhost:8083/ExtMTPush/extmtpush
```

### Step 4: Fill the Form
Required fields (shown with red asterisk):
- **Shortcode**: Enter SMS shortcode (e.g., `10086`)
- **Customer ID**: Enter customer ID (e.g., `isentric_demo`)
- **Recipient MSISDN**: Enter phone number (e.g., `601126141207`)
- **SMS ISDN (Sender)**: Enter sender ID (e.g., `62003`)
- **Message Text**: Enter your SMS message (e.g., `Test on 13Feb`)

### Step 5: Submit
Click the **"Send SMS"** button

The form will:
1. Auto-generate a unique MTID with current timestamp
2. Set all other fields to their default values
3. Send the request to the backend
4. Display the response below the form

## Form Features

✅ **Responsive Design**
- Works on desktop, tablet, and mobile browsers
- Modern gradient background and clean UI

✅ **Auto-Generated Fields**
- MTID is auto-generated with timestamp (format: `MSG_1707747024000`)
- All default values are automatically set

✅ **Built-in Validation**
- Required field validation
- Clear error messages
- Field highlighting on focus

✅ **Response Display**
- Shows server response in a formatted box
- Loading spinner during submission
- Success/error styling

✅ **User-Friendly Controls**
- "Send SMS" button to submit
- "Clear" button to reset form

## Data Fields Reference

| Field | UI Input | Default Value | Type |
|-------|----------|----------------|------|
| shortcode | Yes | - | String |
| custid | Yes | - | String |
| rmsisdn | Yes | - | String |
| smsisdn | Yes | - | String |
| dataStr | Yes | - | String |
| mtid | Auto | MSG_timestamp | String |
| mtprice | No | "000" | String |
| productCode | No | "" | String |
| productType | No | "10" | Integer |
| keyword | No | "" | String |
| dataEncoding | No | "8" | Integer |
| dataUrl | No | "" | String |
| dnRep | No | "0" | Integer |
| groupTag | No | "10" | String |
| cFlag | No | "0" | String |

## API Endpoints

### 1. GET HTML Form
```
GET /extmtpush
```
Returns the HTML form page.

### 2. GET with Query Parameters
```
GET /extmtpush/api?shortcode=10086&custid=isentric_demo&rmsisdn=601126141207&smsisdn=62003&dataStr=Test
```
Returns API response in text format.

### 3. POST with JSON
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
Returns API response.

## Troubleshooting

### Issue: Form page shows blank or 404
**Solution**: Ensure Thymeleaf dependency is installed and properties are configured correctly.
Check that `spring.thymeleaf.enabled=true` in `application.properties`

### Issue: Form submission returns error
**Solution**: Check browser console for error messages. Verify:
- All required fields are filled
- Server is running on port 8083
- Backend service is responding

### Issue: Response not showing
**Solution**: Check that:
- JavaScript is enabled in browser
- Network tab shows successful POST request (200/201 status)
- Server logs for any backend errors

## Files Modified/Created

```
ExtMTPAPI/
├── pom.xml (added Thymeleaf dependency)
├── src/main/
│   ├── java/com/isentric/smsserver/
│   │   └── controller/SmsController.java (updated)
│   └── resources/
│       ├── application.properties (updated)
│       └── templates/
│           └── extmtpush.html (created)
└── EXTMTPUSH_HTML_SETUP.md (documentation)
```

## Support

For issues or questions, check:
1. Server logs for error messages
2. Browser console for JavaScript errors
3. Network tab to inspect requests/responses
4. Application properties configuration

