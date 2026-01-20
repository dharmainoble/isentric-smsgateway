# ExtMTPush SMS Gateway - Spring Boot

A production-ready SMS Gateway application built with Spring Boot 3.2.1, supporting multiple SMS protocols and real-time message processing.

## 🚀 Features

- **4 SMS APIs**
  - Single SMS sending
  - Bulk SMS (multiple recipients)
  - Unicode/Chinese text support
  - Credit balance management

- **Dual Database Support**
  - Avatar Database (SMS records)
  - General Database (configuration)

- **Production Ready**
  - WAR deployment to Tomcat
  - Standalone execution
  - RESTful APIs
  - SOAP Web Services support

- **Advanced Features**
  - IP authorization
  - Credit system
  - Blacklist management
  - Sender ID validation
  - Destination validation
  - Message ID tracking
  - Delivery notifications

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.x
- MySQL 8.0+
- Tomcat 10.1.x (for WAR deployment)

## 🛠️ Tech Stack

- **Spring Boot**: 3.2.1
- **Java**: 17
- **Database**: MySQL 8.0
- **Build Tool**: Maven
- **Packaging**: WAR (also supports standalone JAR)
- **Cache**: Caffeine
- **ORM**: JPA/Hibernate

## 📦 Installation

### 1. Clone the Repository
```bash
git clone <repository-url>
cd ExtMTPush-SpringBoot
```

### 2. Configure Database
Update `src/main/resources/application.properties` with your database credentials:

```properties
# Avatar Database
spring.datasource.avatar.jdbc-url=jdbc:mysql://your-server:3306/extmt
spring.datasource.avatar.username=your-username
spring.datasource.avatar.password=your-password

# General Database
spring.datasource.general.jdbc-url=jdbc:mysql://your-server:3306/bulk_config
spring.datasource.general.username=your-username
spring.datasource.general.password=your-password
```

### 3. Setup Database Tables
```bash
chmod +x setup_test_data.sh
./setup_test_data.sh
```

### 4. Build the Project
```bash
mvn clean package -DskipTests
```

This creates:
- `target/extmtpush-springboot-1.0.0.war` (61 MB)

## 🚀 Deployment

### Option 1: Tomcat Deployment (Production)

```bash
# Copy WAR to Tomcat
cp target/extmtpush-springboot-1.0.0.war $CATALINA_HOME/webapps/ExtMTPush.war

# Start Tomcat
$CATALINA_HOME/bin/startup.sh
```

**Access URL:** `http://localhost:8080/ExtMTPush`

### Option 2: Standalone Execution (Development)

```bash
java -jar target/extmtpush-springboot-1.0.0.war
```

**Access URL:** `http://localhost:8083/ExtMTPush`

## 🎯 API Documentation

### API 1: Send SMS to Single Number

**Endpoint:** `POST /extmtpush`

**Request:**
```json
{
  "shortcode": "66399",
  "custid": "CUST001",
  "rmsisdn": "60192782366",
  "smsisdn": "62003",
  "mtid": "MSG_123456",
  "productType": 4,
  "dataEncoding": 0,
  "dataStr": "Hello World",
  "dnRep": 1
}
```

**Response:**
```
MT Receive Result : returnCode = 0,messageID = MSG_123456,MSISDN = 60192782366,returnMsg = Success
```

### API 2: Send Bulk SMS (Multiple Numbers)

**Request:**
```json
{
  "shortcode": "66399",
  "custid": "CUST001",
  "rmsisdn": "60192782366,60122786404,60122879404",
  "mtid": "MSG_BULK_123",
  "productType": 4,
  "dataEncoding": 0,
  "dataStr": "Bulk message",
  "dnRep": 0
}
```

### API 3: Send Unicode SMS (Chinese/Arabic)

**Request:**
```json
{
  "shortcode": "66399",
  "custid": "CUST001",
  "rmsisdn": "60192782366",
  "mtid": "MSG_UNICODE_123",
  "productType": 10,
  "dataEncoding": 8,
  "dataStr": "4f60597d4e16754c",
  "dnRep": 0
}
```

### API 4: Check Credit Balance

**Endpoint:** `GET /CheckSMSUserCredit?custid=CUST001`

**Response:**
```
Customer: CUST001, Balance: 1000.00, Has Credit: YES
```

## 📊 Return Codes

| Code | Status | Description |
|------|--------|-------------|
| 0 | ✅ Success | SMS inserted successfully |
| 1 | ❌ Error | Invalid parameters |
| 2 | ❌ Error | IP not authorized |
| 3 | ❌ Error | Duplicate message ID |
| 4 | ❌ Error | Destination validation failed |
| 5 | ❌ Error | Insufficient credit |
| 6 | ❌ Error | Blacklisted number |
| 7 | ❌ Error | Invalid masking ID |
| 99 | ❌ Error | Internal server error |

## 🧪 Testing

### Using Postman

Import the collection:
```bash
ExtMTPush_Complete_Postman_Collection.json
```

The collection includes:
- All 4 API endpoints (GET & POST variants)
- Auto-generated unique message IDs
- Pre-configured test data
- Health check and cache management

### Using cURL

**Test Single SMS:**
```bash
curl -X POST http://localhost:8083/ExtMTPush/extmtpush \
  -H "Content-Type: application/json" \
  -d '{
    "shortcode": "66399",
    "custid": "CUST001",
    "rmsisdn": "60192782366",
    "mtid": "TEST_'$(date +%s)'",
    "productType": 4,
    "dataEncoding": 0,
    "dataStr": "Test",
    "dnRep": 1
  }'
```

**Check Health:**
```bash
curl http://localhost:8083/ExtMTPush/actuator/health
```

## 📁 Project Structure

```
ExtMTPush-SpringBoot/
├── src/
│   ├── main/
│   │   ├── java/com/isentric/smsserver/
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── service/         # Business logic
│   │   │   ├── repository/      # Data access
│   │   │   ├── model/           # JPA entities
│   │   │   ├── dto/             # Data transfer objects
│   │   │   ├── config/          # Configuration
│   │   │   └── util/            # Utilities
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── logback-spring.xml
│   │       └── xsd/
│   └── test/
├── target/
│   └── extmtpush-springboot-1.0.0.war
├── ExtMTPush_Complete_Postman_Collection.json
├── pom.xml
└── README.md
```

## 🗄️ Database Schema

### Required Tables

1. **cpip** - IP authorization
2. **customer_credit** - Credit management
3. **bulk_destination_sms** - Destination validation
4. **blacklist** - Blacklist management
5. **masking_id** - Sender ID validation
6. **extmt_mtid** - Message ID tracking
7. **extmtpush_receive_bulk** - SMS records

Run `setup_test_data.sh` to create all tables automatically.

## ⚙️ Configuration

### Application Properties

Key configurations in `application.properties`:

```properties
# Server
server.port=8083
server.servlet.context-path=/ExtMTPush

# Database (Dual datasource)
spring.datasource.avatar.jdbc-url=jdbc:mysql://localhost:3306/extmt
spring.datasource.general.jdbc-url=jdbc:mysql://localhost:3306/bulk_config

# JPA
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false

# Cache
spring.cache.type=caffeine
spring.cache.caffeine.spec=maximumSize=10000,expireAfterWrite=3600s

# Logging
logging.level.com.isentric.smsserver=INFO
```

## 🔧 Utilities

### Setup Script
```bash
./setup_test_data.sh          # Create database tables and test data
```

### Testing Scripts
```bash
./start_and_test_apis.sh      # Start app and test all APIs
./final_api_test.sh           # Comprehensive API test
```

### Maintenance
```bash
./fix_masking_id.sh           # Fix sender ID validation issues
```

## 📝 Documentation

- **POSTMAN_COMPLETE.md** - Complete Postman guide
- **API_TESTING_GUIDE.md** - API reference
- **WAR_DEPLOYMENT_GUIDE.md** - Deployment guide
- **FINAL_SOLUTION.md** - Complete setup guide

## 🐛 Troubleshooting

### Connection Refused
**Problem:** Can't connect to the application

**Solution:**
```bash
# Check if running
ps aux | grep extmtpush

# Start if not running
java -jar target/extmtpush-springboot-1.0.0.war &
```

### Return Code 2 (IP Not Authorized)
**Solution:**
```bash
./setup_test_data.sh
```

### Return Code 5 (Insufficient Credit)
**Solution:**
```bash
mysql -u root -p bulk_config -e "UPDATE customer_credit SET credit_balance = 10000.00 WHERE custid = 'CUST001';"
```

## 🔐 Security

- IP-based authorization
- Credit validation
- Sender ID validation
- Blacklist management
- Input validation
- SQL injection prevention

## 📈 Performance

- Connection pooling (HikariCP)
- Caffeine cache
- Async processing support
- Optimized database queries

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👥 Authors

- **Isentric Team** - Initial work

## 🙏 Acknowledgments

- Spring Boot framework
- MySQL database
- Caffeine cache
- All contributors

## 📞 Support

For support, email support@isentric.com or open an issue in the repository.

## 🗺️ Roadmap

- [ ] Add API rate limiting
- [ ] Implement Redis cache
- [ ] Add message scheduling
- [ ] WebSocket support for real-time updates
- [ ] Admin dashboard
- [ ] Message templates
- [ ] Multi-language support

---

**Version:** 1.0.0  
**Last Updated:** December 17, 2025  
**Status:** Production Ready ✅

