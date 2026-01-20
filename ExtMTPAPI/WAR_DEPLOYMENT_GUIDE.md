# WAR Deployment Guide for ExtMTPush

## ✅ WAR File Created Successfully

**File:** `extmtpush-springboot-1.0.0.war`  
**Location:** `/home/arun/Documents/rec/ExtMTPush-SpringBoot/target/`  
**Size:** 61 MB  
**Status:** Ready for deployment

---

## Changes Made for WAR Packaging

### 1. Updated pom.xml
- Changed `<packaging>jar</packaging>` → `<packaging>war</packaging>`
- Added `spring-boot-starter-tomcat` with `<scope>provided</scope>`

### 2. Updated ExtMtPushApplication.java
- Extended `SpringBootServletInitializer`
- Added `configure()` method for servlet container initialization
- Can still run as standalone JAR with `main()` method

---

## Deployment Options

### Option 1: Deploy to External Tomcat Server

#### Prerequisites:
- Tomcat 9.x or 10.x installed
- Java 17 installed
- MySQL database accessible from server

#### Deployment Steps:

1. **Stop Tomcat (if running)**
   ```bash
   $CATALINA_HOME/bin/shutdown.sh
   ```

2. **Copy WAR file to Tomcat webapps**
   ```bash
   cp target/extmtpush-springboot-1.0.0.war $CATALINA_HOME/webapps/ExtMTPush.war
   ```
   
   Note: Rename to `ExtMTPush.war` to match context path

3. **Update Database Configuration**
   
   The application.properties uses remote database:
   ```
   jdbc:mysql://192.168.26.201:3306/extmt
   jdbc:mysql://192.168.26.201:3306/bulk_config
   ```
   
   Ensure this server is accessible from deployment server.

4. **Start Tomcat**
   ```bash
   $CATALINA_HOME/bin/startup.sh
   ```

5. **Verify Deployment**
   ```bash
   # Check logs
   tail -f $CATALINA_HOME/logs/catalina.out
   
   # Test health endpoint
   curl http://localhost:8080/ExtMTPush/actuator/health
   ```

#### Expected URL:
- **Base URL:** `http://localhost:8080/ExtMTPush`
- **API Endpoint:** `http://localhost:8080/ExtMTPush/extmtpush`
- **Health Check:** `http://localhost:8080/ExtMTPush/actuator/health`

---

### Option 2: Run as Standalone (Still Supported)

The WAR can still run as standalone application:

```bash
java -jar target/extmtpush-springboot-1.0.0.war
```

This will:
- Use embedded Tomcat
- Run on port 8083 (as configured)
- Context path: /ExtMTPush

---

## Configuration for Production

### Database Configuration

Current settings (in application.properties):
```properties
# Avatar DB (extmt)
spring.datasource.avatar.jdbc-url=jdbc:mysql://192.168.26.201:3306/extmt
spring.datasource.avatar.username=admin
spring.datasource.avatar.password=Isentric20@!

# General DB (bulk_config)
spring.datasource.general.jdbc-url=jdbc:mysql://192.168.26.201:3306/bulk_config
spring.datasource.general.username=admin
spring.datasource.general.password=Isentric20@!
```

**For Production:**
- Ensure database server `192.168.26.201` is accessible
- Create databases if not exists: `extmt`, `bulk_config`
- Run setup script on production database
- Verify credentials are correct

### External Configuration (Recommended)

Instead of modifying application.properties inside WAR, use external config:

#### Method 1: Environment Variables
```bash
export SPRING_DATASOURCE_AVATAR_URL=jdbc:mysql://prod-db:3306/extmt
export SPRING_DATASOURCE_AVATAR_USERNAME=prod_user
export SPRING_DATASOURCE_AVATAR_PASSWORD=prod_pass
```

#### Method 2: External application.properties
```bash
# Create external config
mkdir -p /opt/extmtpush/config
cp src/main/resources/application.properties /opt/extmtpush/config/

# Edit production values
vim /opt/extmtpush/config/application.properties

# Run with external config
java -jar extmtpush-springboot-1.0.0.war --spring.config.location=/opt/extmtpush/config/
```

#### Method 3: Tomcat context.xml
Create `$CATALINA_HOME/conf/Catalina/localhost/ExtMTPush.xml`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Context>
    <Environment name="spring.datasource.avatar.url" 
                 value="jdbc:mysql://prod-db:3306/extmt" 
                 type="java.lang.String"/>
    <Environment name="spring.datasource.avatar.username" 
                 value="prod_user" 
                 type="java.lang.String"/>
    <Environment name="spring.datasource.avatar.password" 
                 value="prod_pass" 
                 type="java.lang.String"/>
</Context>
```

---

## Database Setup on Production Server

### 1. Create Databases
```sql
CREATE DATABASE IF NOT EXISTS extmt;
CREATE DATABASE IF NOT EXISTS bulk_config;
```

### 2. Run Setup Script

Transfer and run the setup script on production:

```bash
# On production server
scp setup_test_data.sh user@prod-server:/tmp/
ssh user@prod-server

# Update credentials in script
vim /tmp/setup_test_data.sh
# Change DB_USER and DB_PASS

# Run setup
chmod +x /tmp/setup_test_data.sh
./setup_test_data.sh
```

### 3. Verify Tables

```bash
mysql -u admin -p extmt -e "SHOW TABLES;"
mysql -u admin -p bulk_config -e "SHOW TABLES;"
```

Expected tables:
- **extmt:** extmtpush_receive_bulk, extmt_mtid
- **bulk_config:** cpip, customer_credit, bulk_destination_sms, blacklist, masking_id

---

## Testing After Deployment

### 1. Health Check
```bash
curl http://your-server:8080/ExtMTPush/actuator/health
```

Expected: `{"status":"UP"}`

### 2. Test SMS API
```bash
curl -X POST http://your-server:8080/ExtMTPush/extmtpush \
  -H "Content-Type: application/json" \
  -d '{
    "shortcode": "66399",
    "custid": "CUST001",
    "rmsisdn": "60192782366",
    "mtid": "TEST_'$(date +%s)'",
    "productType": 4,
    "dataEncoding": 0,
    "dataStr": "Production test",
    "dnRep": 1
  }'
```

Expected: `returnCode = 0`

### 3. Check Credit API
```bash
curl http://your-server:8080/ExtMTPush/CheckSMSUserCredit?custid=CUST001
```

### 4. Verify Database
```bash
mysql -u admin -p extmt -e "
  SELECT COUNT(*) as total 
  FROM extmtpush_receive_bulk;
"
```

---

## Postman Collection Update

The Postman collection needs URL update for production:

### Update Collection Variables:
1. Open Postman
2. Click on collection → Variables
3. Change `base_url`:
   - **Old:** `http://localhost:8083/ExtMTPush`
   - **New:** `http://your-server:8080/ExtMTPush`
4. Save

Or create a new Environment:
- **Name:** Production
- **Variable:** `base_url`
- **Value:** `http://prod-server:8080/ExtMTPush`

---

## Port Configuration

### Current Configuration:

#### Standalone Mode (java -jar):
- **Port:** 8083 (from application.properties)
- **URL:** `http://localhost:8083/ExtMTPush`

#### Tomcat Deployment:
- **Port:** 8080 (Tomcat's default)
- **URL:** `http://localhost:8080/ExtMTPush`

### To Change Tomcat Port:

Edit `$CATALINA_HOME/conf/server.xml`:
```xml
<Connector port="8083" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" />
```

---

## Troubleshooting

### Issue 1: WAR Not Deploying

**Check Tomcat logs:**
```bash
tail -f $CATALINA_HOME/logs/catalina.out
```

**Common causes:**
- Java version mismatch (needs Java 17)
- Database not accessible
- Port conflict

### Issue 2: Database Connection Failed

**Error:** `Unable to obtain JDBC Connection`

**Solution:**
1. Verify database server is accessible:
   ```bash
   telnet 192.168.26.201 3306
   ```

2. Check credentials:
   ```bash
   mysql -h 192.168.26.201 -u admin -p extmt
   ```

3. Check firewall rules

### Issue 3: Context Path Not Working

**Problem:** Accessing `http://localhost:8080/ExtMTPush` returns 404

**Solution:**
1. Ensure WAR is named correctly: `ExtMTPush.war`
2. Wait for deployment to complete
3. Check Tomcat webapps directory:
   ```bash
   ls -la $CATALINA_HOME/webapps/
   ```

### Issue 4: Application Properties Not Loading

**Problem:** Using wrong database

**Solution:**
- Place `application.properties` in:
  - `$CATALINA_HOME/conf/` OR
  - `/opt/extmtpush/config/` with spring.config.location

---

## Monitoring in Production

### 1. Application Logs

**Location:** `logs/extmtpush.log` (relative to working directory)

For Tomcat, configure in application.properties:
```properties
logging.file.name=/var/log/extmtpush/extmtpush.log
```

### 2. Tomcat Logs

**Location:** `$CATALINA_HOME/logs/`

- `catalina.out` - Main log
- `localhost.YYYY-MM-DD.log` - Application deployment logs

### 3. Health Endpoint

Monitor via:
```bash
curl http://localhost:8080/ExtMTPush/actuator/health
```

Set up monitoring tools (Nagios, Zabbix) to check this endpoint.

### 4. Metrics Endpoint

```bash
curl http://localhost:8080/ExtMTPush/actuator/metrics
```

---

## Security Considerations

### 1. Disable Actuator in Production

In `application.properties`:
```properties
# Disable all actuator endpoints in production
management.endpoints.web.exposure.include=health

# Or completely disable
# management.endpoints.enabled-by-default=false
```

### 2. Enable HTTPS

Configure Tomcat SSL in `server.xml`:
```xml
<Connector port="8443" protocol="org.apache.coyote.http11.Http11NioProtocol"
           maxThreads="150" SSLEnabled="true">
    <SSLHostConfig>
        <Certificate certificateKeystoreFile="conf/keystore.jks"
                     certificateKeystorePassword="your-password"
                     type="RSA" />
    </SSLHostConfig>
</Connector>
```

### 3. Database Security

- Use separate database users for each application
- Grant only required permissions
- Use SSL for database connections
- Keep credentials in external config, not in WAR

---

## Backup and Rollback

### Backup Current WAR

Before deploying new version:
```bash
cp $CATALINA_HOME/webapps/ExtMTPush.war \
   $CATALINA_HOME/webapps/ExtMTPush.war.backup.$(date +%Y%m%d)
```

### Rollback

If deployment fails:
```bash
# Stop Tomcat
$CATALINA_HOME/bin/shutdown.sh

# Remove failed deployment
rm -rf $CATALINA_HOME/webapps/ExtMTPush*

# Restore backup
cp $CATALINA_HOME/webapps/ExtMTPush.war.backup.YYYYMMDD \
   $CATALINA_HOME/webapps/ExtMTPush.war

# Start Tomcat
$CATALINA_HOME/bin/startup.sh
```

---

## Performance Tuning

### Tomcat Configuration

Edit `$CATALINA_HOME/bin/setenv.sh`:
```bash
export CATALINA_OPTS="$CATALINA_OPTS -Xms1024m"
export CATALINA_OPTS="$CATALINA_OPTS -Xmx2048m"
export CATALINA_OPTS="$CATALINA_OPTS -XX:+UseG1GC"
```

### Application Configuration

In `application.properties`:
```properties
# Database connection pool
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5

# Cache settings
spring.cache.caffeine.spec=maximumSize=10000,expireAfterWrite=3600s
```

---

## Summary

✅ **WAR File Ready:** `extmtpush-springboot-1.0.0.war` (61 MB)  
✅ **Can Deploy to:** Tomcat 9.x, 10.x  
✅ **Can Run Standalone:** Yes (java -jar)  
✅ **Database:** MySQL on 192.168.26.201  
✅ **Context Path:** /ExtMTPush  
✅ **Configuration:** Supports external config  

---

## Quick Deployment Checklist

- [ ] Copy WAR to Tomcat webapps as `ExtMTPush.war`
- [ ] Verify database server accessible
- [ ] Run database setup script
- [ ] Configure credentials (if needed)
- [ ] Start Tomcat
- [ ] Check health endpoint
- [ ] Test SMS API
- [ ] Update Postman collection URL
- [ ] Monitor logs
- [ ] Set up backups

---

**Status:** ✅ WAR File Created and Ready for Deployment  
**Location:** `/home/arun/Documents/rec/ExtMTPush-SpringBoot/target/extmtpush-springboot-1.0.0.war`  
**Size:** 61 MB  
**Created:** December 17, 2025

