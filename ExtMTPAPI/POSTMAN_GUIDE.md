# Postman Collection - Complete Guide

## Import the Collection

### Step 1: Open Postman

### Step 2: Import Collection
1. Click **Import** button (top left)
2. Select **File** tab
3. Choose: `ExtMTPush_Complete_Postman_Collection.json`
4. Click **Import**

### Step 3: Collection Loaded
You should see: **"ExtMTPush Complete API Collection"** in your Collections sidebar

---

## Collection Structure

### 📁 ExtMTPush Complete API Collection

#### 🟢 **API 1 - Single SMS**
- `API 1 - Single SMS (GET)` - Send SMS using GET with query parameters
- `API 1 - Single SMS (POST JSON)` - Send SMS using POST with JSON body

#### 🔵 **API 2 - Multiple SMS**
- `API 2 - Multiple SMS (GET)` - Bulk SMS using GET
- `API 2 - Multiple SMS (POST JSON)` - Bulk SMS using POST

#### 🟣 **API 3 - Unicode/Chinese SMS**
- `API 3 - Chinese SMS (GET)` - Unicode SMS using GET
- `API 3 - Chinese SMS (POST JSON)` - Unicode SMS using POST

#### 🟠 **API 4 - Credit Check**
- `API 4 - Check Credit (GET)` - Simple text response
- `API 4 - Check Credit Details (JSON)` - Detailed JSON response

#### 🔧 **Utilities**
- `Health Check` - Check if application is running
- `Cache - Evict All` - Clear all caches
- `Cache - Evict Route Cache` - Clear route cache

---

## Collection Variables

The collection has 3 pre-configured variables:

| Variable | Default Value | Description |
|----------|---------------|-------------|
| `{{base_url}}` | `http://localhost:8083/ExtMTPush` | Base API URL |
| `{{shortcode}}` | `66399` | Service shortcode |
| `{{custid}}` | `CUST001` | Customer ID |

### How to Edit Variables:
1. Click on the collection name
2. Click **Variables** tab
3. Edit **Current Value** column
4. Click **Save**

---

## Running Requests

### Quick Test - API 1 (Single SMS)

1. Expand the collection
2. Click **"API 1 - Single SMS (POST JSON)"**
3. You should see:
   - Method: `POST`
   - URL: `{{base_url}}/extmtpush`
   - Body: JSON with all parameters
4. Click **Send** button
5. Check response:

**Success Response:**
```
MT Receive Result : returnCode = 0,messageID = MSG_1734429600,MSISDN = 60192782366,returnMsg = Success
 ------------- 
```

**Error Response:**
```
MT Receive Result : returnCode = 2,messageID = ,MSISDN = ,returnMsg = IP not authorized
 ------------- 
```

---

## Testing All 4 APIs

### Test 1: Single SMS (GET Method)

**Request:** `API 1 - Single SMS (GET)`

**What it does:** Send SMS to one mobile number

**Parameters:** All in URL query string
- rmsisdn: `60192782366` (single number)
- mtid: Auto-generated with `{{$timestamp}}`
- dataStr: `Test message single`

**Expected Response:**
```
MT Receive Result : returnCode = 0,messageID = MSG_xxx,MSISDN = 60192782366,returnMsg = Success
```

---

### Test 2: Bulk SMS (GET Method)

**Request:** `API 2 - Multiple SMS (GET)`

**What it does:** Send same SMS to multiple numbers

**Key Parameter:**
- rmsisdn: `60192782366,60122786404,60122879404` (comma-separated)

**Expected Response:**
```
MT Receive Result : returnCode = 0,messageID = MSG_xxx,MSISDN = 60192782366,60122786404,60122879404,returnMsg = Success
```

**Database Result:** 3 separate SMS records inserted

---

### Test 3: Chinese/Unicode SMS (GET Method)

**Request:** `API 3 - Chinese SMS (GET)`

**What it does:** Send SMS with Chinese characters

**Key Parameters:**
- dataEncoding: `8` (Unicode)
- productType: `10` (Unicode message)
- dataStr: Hex-encoded Chinese text

**Expected Response:**
```
MT Receive Result : returnCode = 0,messageID = MSG_xxx,MSISDN = 60192782366,returnMsg = Success
```

---

### Test 4: Check Credit (GET Method)

**Request:** `API 4 - Check Credit (GET)`

**What it does:** Query customer credit balance

**Parameters:**
- custid: `{{custid}}` (CUST001)

**Expected Response:**
```
Customer: CUST001, Balance: 1000.00, Has Credit: YES
```

**JSON Alternative:** Use `API 4 - Check Credit Details (JSON)` for structured response

---

## POST vs GET Requests

### Both methods supported for SMS APIs!

#### GET Method (Query Parameters)
```
GET http://localhost:8083/ExtMTPush/extmtpush?shortcode=66399&custid=CUST001&...
```
- All parameters in URL
- Easy to test in browser
- Limited by URL length

#### POST Method (JSON Body)
```
POST http://localhost:8083/ExtMTPush/extmtpush
Content-Type: application/json

{
  "shortcode": "66399",
  "custid": "CUST001",
  "rmsisdn": "60192782366",
  "mtid": "MSG_123",
  "productType": 4,
  "dataEncoding": 0,
  "dataStr": "Hello",
  "dnRep": 1
}
```
- Cleaner structure
- Better for complex data
- No URL length limit

---

## Using Dynamic Variables

### Postman Built-in Variables

The collection uses `{{$timestamp}}` for unique message IDs:

```json
"mtid": "MSG_{{$timestamp}}"
```

This generates: `MSG_1734429600` (Unix timestamp)

### Other Available Variables:
- `{{$timestamp}}` - Current Unix timestamp
- `{{$randomInt}}` - Random integer
- `{{$guid}}` - UUID/GUID
- `{{$randomUUID}}` - Random UUID

### Example: Custom Message ID
Edit the Body and change:
```json
"mtid": "MSG_{{$timestamp}}"
```
To:
```json
"mtid": "TEST_{{$randomInt}}"
```

---

## Running Multiple Tests

### Method 1: Manual Sequential Testing

1. Click `API 1 - Single SMS (POST JSON)`
2. Click **Send**
3. Wait for response
4. Click `API 2 - Multiple SMS (POST JSON)`
5. Click **Send**
6. Repeat for all APIs

### Method 2: Collection Runner

1. Click **"..."** next to collection name
2. Click **Run collection**
3. Select requests to run
4. Set iterations (e.g., 10)
5. Click **Run ExtMTPush Complete API Collection**
6. View results

**Note:** Collection Runner executes all requests automatically

---

## Customizing Requests

### Change Mobile Number

**Single SMS:**
1. Open request body
2. Find: `"rmsisdn": "60192782366"`
3. Change to your number
4. Click **Send**

**Multiple SMS:**
1. Open request body
2. Find: `"rmsisdn": "60192782366,60122786404,60122879404"`
3. Add/remove numbers (comma-separated)
4. Click **Send**

### Change Message Content

1. Open request body
2. Find: `"dataStr": "Test message"`
3. Change to your message
4. Click **Send**

### Change Customer ID

**Option 1: Per Request**
- Edit `"custid"` in request body

**Option 2: Collection Variable**
1. Click collection name
2. Click **Variables** tab
3. Change `custid` current value
4. All requests use new value

---

## Troubleshooting

### Error: "Could not get any response"

**Cause:** Application not running

**Solution:**
```bash
cd /home/arun/Documents/rec/ExtMTPush-SpringBoot
java -jar target/extmtpush-springboot-1.0.0.jar &
sleep 20
```

Then test with **Health Check** request

---

### Error: returnCode = 2 (IP not authorized)

**Cause:** Your IP not in database

**Solution:**
```bash
cd /home/arun/Documents/rec/ExtMTPush-SpringBoot
./setup_test_data.sh
```

Restart app and test again

---

### Error: returnCode = 3 (Duplicate MTID)

**Cause:** Message ID already used

**Solution:** The `{{$timestamp}}` variable should prevent this. If it happens:
1. Wait 1 second
2. Click **Send** again
3. New timestamp will be generated

---

### Error: returnCode = 5 (Insufficient credit)

**Cause:** No credit for customer

**Solution:**
```bash
mysql -uroot -parun bulk_config -e "UPDATE customer_credit SET credit_balance = 10000.00 WHERE custid = 'CUST001';"
```

---

## Verifying Results

### Check Response

**Success:**
- Status: `200 OK`
- Body contains: `returnCode = 0`
- Body contains: `returnMsg = Success`

**Error:**
- Body contains: `returnCode = X` (where X > 0)
- Body contains: `returnMsg = Error description`

### Check Database

After sending SMS, verify in database:

**Using Command Line:**
```bash
mysql -uroot -parun extmt -e "SELECT mtid, rmsisdn, data_str, received_date FROM extmtpush_receive_bulk WHERE custid='CUST001' ORDER BY received_date DESC LIMIT 5;" 2>&1 | grep -v Warning
```

**Expected Output:**
```
mtid                rmsisdn         data_str        received_date
MSG_1734429600      60192782366     Test message    2025-12-17 10:30:00
```

---

## Advanced Usage

### Pre-request Scripts

Add logging before each request:

1. Click on a request
2. Go to **Pre-request Script** tab
3. Add:
```javascript
console.log("Sending SMS to:", pm.request.body.raw);
console.log("Timestamp:", new Date().toISOString());
```

### Tests/Assertions

Add automatic validation:

1. Click on a request
2. Go to **Tests** tab
3. Add:
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response contains success", function () {
    pm.expect(pm.response.text()).to.include("returnCode = 0");
});
```

Now when you send the request, tests run automatically!

---

## Save Sample Responses

1. Send a request
2. Click **Save Response** button
3. Name it (e.g., "Success Response")
4. Next time, you can view examples under request

---

## Export/Share Collection

### Export:
1. Click **"..."** next to collection
2. Click **Export**
3. Choose **Collection v2.1**
4. Save file

### Share:
- Send exported JSON file to team members
- Or use Postman Workspaces for team collaboration

---

## Quick Reference

### Essential Shortcuts

| Action | Shortcut |
|--------|----------|
| Send request | `Ctrl/Cmd + Enter` |
| Open new tab | `Ctrl/Cmd + T` |
| Save request | `Ctrl/Cmd + S` |
| Search collection | `Ctrl/Cmd + F` |

---

## Summary

✅ **11 Ready-to-Run Requests:**
- 2 for API 1 (Single SMS)
- 2 for API 2 (Multiple SMS)
- 2 for API 3 (Unicode SMS)
- 2 for API 4 (Credit Check)
- 3 Utility endpoints

✅ **Both GET and POST supported** for SMS APIs

✅ **Auto-generated unique IDs** with `{{$timestamp}}`

✅ **Pre-configured variables** for easy customization

✅ **Complete documentation** for all features

---

## Next Steps

1. **Import collection** into Postman
2. **Test Health Check** to verify app is running
3. **Run API 1** (Single SMS) first
4. **Check response** for `returnCode = 0`
5. **Run remaining APIs** one by one
6. **Verify in database** using provided command

---

**File:** `ExtMTPush_Complete_Postman_Collection.json`  
**Location:** `/home/arun/Documents/rec/ExtMTPush-SpringBoot/`  
**Created:** December 17, 2025  
**Status:** ✅ Ready to import and run

