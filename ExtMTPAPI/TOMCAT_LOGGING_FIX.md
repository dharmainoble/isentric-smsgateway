# ✅ FIXED: Tomcat Logging Error

## Problem Resolved

**Error:** `FileNotFoundException: logs/extmtpush.log (No such file or directory)`

**Cause:** The application was trying to write log files to relative paths (`logs/extmtpush.log`) which don't exist in Tomcat's working directory.

**Solution:** Updated logging configuration to use system-appropriate directories.

---

## Changes Made

### 1. Updated `application.properties`

**Before:**
```properties
logging.file.name=logs/extmtpush.log
```

**After:**
```properties
logging.file.name=${catalina.base:${java.io.tmpdir}}/logs/extmtpush/extmtpush.log
```

### 2. Updated `logback-spring.xml`

**Before:**
```xml
<file>logs/extmtpush.log</file>
<file>logs/sms-traffic.log</file>
```

**After:**
```xml
<property name="LOG_DIR" value="${catalina.base:-${java.io.tmpdir}}/logs/extmtpush" />
<file>${LOG_DIR}/extmtpush.log</file>
<file>${LOG_DIR}/sms-traffic.log</file>
```

---

## How It Works

### For Tomcat Deployment:
- Logs go to: `$CATALINA_BASE/logs/extmtpush/`
- Example: `/opt/tomcat/logs/extmtpush/extmtpush.log`

### For Standalone Execution:
- Logs go to: `${java.io.tmpdir}/logs/extmtpush/`
- Example (Linux): `/tmp/logs/extmtpush/extmtpush.log`
- Example (Windows): `C:\Users\USERNAME\AppData\Local\Temp\logs\extmtpush\extmtpush.log`

---

## Log Files Location

### In Tomcat:
```
$CATALINA_BASE/logs/extmtpush/
├── extmtpush.log              # Application logs
├── extmtpush.2025-12-17.0.log # Archived logs
└── sms-traffic.log            # SMS traffic logs
```

### View Logs in Tomcat:
```bash
# Real-time monitoring
tail -f $CATALINA_BASE/logs/extmtpush/extmtpush.log

# View SMS traffic
tail -f $CATALINA_BASE/logs/extmtpush/sms-traffic.log

# Last 100 lines
tail -100 $CATALINA_BASE/logs/extmtpush/extmtpush.log
```

---

## Deployment Instructions

### Step 1: Undeploy Old WAR (if exists)

**Option A: Using Tomcat Manager**
1. Go to: `http://localhost:8080/manager/html`
2. Find "ExtMTPush" application
3. Click "Undeploy"

**Option B: Manual**
```bash
# Stop Tomcat
$CATALINA_HOME/bin/shutdown.sh

# Remove old deployment
rm -rf $CATALINA_HOME/webapps/ExtMTPush*

# Start Tomcat
$CATALINA_HOME/bin/startup.sh
```

### Step 2: Deploy New WAR

```bash
# Copy fixed WAR
cp /home/arun/Documents/rec/ExtMTPush.war $CATALINA_HOME/webapps/

# Tomcat will auto-deploy
# Wait 20-30 seconds for deployment
```

### Step 3: Verify Deployment

**Check Tomcat logs:**
```bash
tail -f $CATALINA_HOME/logs/catalina.out
```

**Look for:**
```
INFO [main] org.springframework.boot.web.embedded.tomcat.TomcatWebServer - Tomcat started on port(s): 8080
INFO [main] com.isentric.smsserver.ExtMtPushApplication - Started ExtMtPushApplication
```

**Test application:**
```bash
curl http://localhost:8080/ExtMTPush/actuator/health
```

**Expected response:**
```json
{"status":"UP"}
```

---

## Troubleshooting

### If Still Getting Log Errors:

**Check directory permissions:**
```bash
# Ensure Tomcat can write to logs directory
chmod 755 $CATALINA_BASE/logs
ls -la $CATALINA_BASE/logs/
```

**Manually create log directory:**
```bash
mkdir -p $CATALINA_BASE/logs/extmtpush
chown tomcat:tomcat $CATALINA_BASE/logs/extmtpush
```

### If Deployment Fails:

**Check Tomcat logs:**
```bash
tail -100 $CATALINA_HOME/logs/catalina.out
```

**Check application logs:**
```bash
tail -100 $CATALINA_BASE/logs/extmtpush/extmtpush.log
```

### If Port 8080 is in Use:

**Change Tomcat port:**
Edit `$CATALINA_HOME/conf/server.xml`:
```xml
<Connector port="8083" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" />
```

---

## Testing After Deployment

### Test Health Check:
```bash
curl http://localhost:8080/ExtMTPush/actuator/health
```

### Test SMS API:
```bash
curl -X POST http://localhost:8080/ExtMTPush/extmtpush \
  -H "Content-Type: application/json" \
  -d '{
    "shortcode": "66399",
    "custid": "CUST001",
    "rmsisdn": "60192782366",
    "mtid": "TEST_'$(date +%s)'",
    "productType": 4,
    "dataEncoding": 0,
    "dataStr": "Test from Tomcat",
    "dnRep": 1
  }'
```

### Test Credit API:
```bash
curl http://localhost:8080/ExtMTPush/CheckSMSUserCredit?custid=CUST001
```

---

## Log Management

### View Application Logs:
```bash
# Latest logs
tail -f $CATALINA_BASE/logs/extmtpush/extmtpush.log

# Search for errors
grep ERROR $CATALINA_BASE/logs/extmtpush/extmtpush.log

# Search for specific message
grep "returnCode" $CATALINA_BASE/logs/extmtpush/sms-traffic.log
```

### Log Rotation:
- **Automatic:** Logs rotate daily
- **Max size:** 10MB per file
- **Retention:** 30 days for app logs, 90 days for SMS traffic
- **Old logs:** `extmtpush.2025-12-17.0.log`

### Clean Old Logs:
```bash
# Remove logs older than 30 days
find $CATALINA_BASE/logs/extmtpush -name "*.log" -mtime +30 -delete
```

---

## Updated Postman Collection

**Base URL changed from standalone to Tomcat:**

**Before (Standalone):**
```
http://localhost:8083/ExtMTPush
```

**After (Tomcat):**
```
http://localhost:8080/ExtMTPush
```

**Update in Postman:**
1. Click collection → Variables
2. Change `base_url` to: `http://localhost:8080/ExtMTPush`
3. Save

---

## Summary

✅ **Problem Fixed:** Logging paths now work in Tomcat  
✅ **WAR Rebuilt:** New version ready for deployment  
✅ **Location:** `/home/arun/Documents/rec/ExtMTPush.war`  
✅ **Logs Directory:** `$CATALINA_BASE/logs/extmtpush/`  
✅ **Tested:** Build successful  

---

## Quick Deployment Commands

```bash
# 1. Stop Tomcat
$CATALINA_HOME/bin/shutdown.sh

# 2. Remove old deployment
rm -rf $CATALINA_HOME/webapps/ExtMTPush*

# 3. Copy new WAR
cp /home/arun/Documents/rec/ExtMTPush.war $CATALINA_HOME/webapps/

# 4. Start Tomcat
$CATALINA_HOME/bin/startup.sh

# 5. Monitor deployment
tail -f $CATALINA_HOME/logs/catalina.out

# 6. Test
curl http://localhost:8080/ExtMTPush/actuator/health
```

---

**Date:** December 17, 2025  
**Status:** ✅ Fixed and Ready  
**File:** `/home/arun/Documents/rec/ExtMTPush.war`  
**Size:** 61 MB

