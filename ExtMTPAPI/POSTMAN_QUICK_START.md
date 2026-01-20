# Postman Quick Start - 3 Simple Steps

## Step 1: Import Collection (30 seconds)

1. Open Postman
2. Click **Import** button
3. Select file: `ExtMTPush_Complete_Postman_Collection.json`
4. Click **Import**

✅ Done! You should see **"ExtMTPush Complete API Collection"** in sidebar

---

## Step 2: Start Application (20 seconds)

```bash
cd /home/arun/Documents/rec/ExtMTPush-SpringBoot
java -jar target/extmtpush-springboot-1.0.0.jar &
sleep 20
```

✅ App running on: `http://localhost:8083`

---

## Step 3: Test APIs (Click & Send!)

### Test 1: Health Check
1. Expand collection
2. Click **"Health Check"**
3. Click **Send**
4. Should see: `"status":"UP"`

### Test 2: Single SMS
1. Click **"API 1 - Single SMS (POST JSON)"**
2. Click **Send**
3. Should see: `returnCode = 0`

### Test 3: Check Credit
1. Click **"API 4 - Check Credit (GET)"**
2. Click **Send**
3. Should see: `Balance: 1000.00`

---

## ✅ Success!

All 4 APIs are now runnable in Postman:

- ✅ API 1: Single SMS (2 variants: GET & POST)
- ✅ API 2: Multiple SMS (2 variants: GET & POST)
- ✅ API 3: Unicode SMS (2 variants: GET & POST)
- ✅ API 4: Credit Check (2 variants: Simple & JSON)

---

## Variables (Already Configured!)

| Variable | Value | Change it? |
|----------|-------|------------|
| `{{base_url}}` | `http://localhost:8083/ExtMTPush` | If using different port |
| `{{shortcode}}` | `66399` | If using different shortcode |
| `{{custid}}` | `CUST001` | If using different customer |

**To change:**
1. Click collection name
2. Click **Variables** tab
3. Edit **Current Value**
4. **Save**

---

## Each Request Has:

✅ **Pre-filled parameters** - Just click Send!  
✅ **Auto-generated MTID** - Uses `{{$timestamp}}` for uniqueness  
✅ **Complete documentation** - Hover over parameters to see descriptions  
✅ **Sample responses** - Know what to expect

---

## Troubleshooting

### "Could not get any response"
→ Start the application (see Step 2 above)

### "returnCode = 2" (IP not authorized)
→ Run: `./setup_test_data.sh`

### "returnCode = 5" (Insufficient credit)
→ Run: `mysql -uroot -parun bulk_config -e "UPDATE customer_credit SET credit_balance = 10000.00 WHERE custid = 'CUST001';"`

---

## What's in the Collection?

```
📁 ExtMTPush Complete API Collection
  ├─ 🟢 API 1 - Single SMS (GET)
  ├─ 🟢 API 1 - Single SMS (POST JSON)
  ├─ 🔵 API 2 - Multiple SMS (GET)
  ├─ 🔵 API 2 - Multiple SMS (POST JSON)
  ├─ 🟣 API 3 - Chinese SMS (GET)
  ├─ 🟣 API 3 - Chinese SMS (POST JSON)
  ├─ 🟠 API 4 - Check Credit (GET)
  ├─ 🟠 API 4 - Check Credit Details (JSON)
  ├─ 🔧 Health Check
  ├─ 🔧 Cache - Evict All
  └─ 🔧 Cache - Evict Route Cache
```

**Total: 11 ready-to-run requests!**

---

## Need More Help?

See **POSTMAN_GUIDE.md** for:
- Detailed documentation
- Advanced features
- Customization options
- Testing strategies

---

**Status:** ✅ All APIs Ready in Postman  
**File:** `ExtMTPush_Complete_Postman_Collection.json`  
**Time to Import:** 30 seconds  
**Time to Test:** 2 minutes

