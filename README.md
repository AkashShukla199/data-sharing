# Data Sharing Application

A Spring Boot application that connects to two different data sources (Excel and SAP), reads data from Excel, and sends it to SAP using column names as fields.

## Features

- Automatically monitors a directory for new Excel files
- Reads data from Excel files using column names as fields
- Sends data to SAP (mock implementation)
- Tracks the status of each record in a database
- Provides REST API endpoints for manual triggering and monitoring
- Scheduled job for automatic processing

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- For SAP connectivity in a real-world scenario, you would need:
  - SAP JCo libraries (not included due to licensing restrictions)
  - SAP system access credentials

## Project Structure

```
data-sharing/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── datasharing/
│   │   │               ├── config/           # Configuration classes
│   │   │               ├── controller/       # REST controllers
│   │   │               ├── model/            # Data models
│   │   │               ├── repository/       # Data repositories
│   │   │               ├── service/          # Business logic
│   │   │               ├── util/             # Utility classes
│   │   │               └── DataSharingApplication.java
│   │   └── resources/
│   │       ├── application.yml               # Application configuration
│   │       └── samples/                      # Sample data files
│   └── test/                                 # Test classes
└── pom.xml                                   # Maven configuration
```

## Configuration

The application is configured through `src/main/resources/application.yml`. Key configuration properties include:

### Excel Configuration

```yaml
app:
  excel:
    input-directory: ${user.home}/data-sharing/excel/input
    processed-directory: ${user.home}/data-sharing/excel/processed
    error-directory: ${user.home}/data-sharing/excel/error
    polling-interval: 60000  # 1 minute in milliseconds
```

### SAP Configuration

```yaml
app:
  sap:
    destination: DEV
    ashost: your-sap-host.example.com
    sysnr: "00"
    client: "100"
    user: sapuser
    passwd: sappassword
    lang: EN
```

## Getting Started

1. Clone the repository
2. Configure the application in `src/main/resources/application.yml`
3. Build the application:
   ```
   mvn clean package
   ```
4. Run the application:
   ```
   java -jar target/data-sharing-0.0.1-SNAPSHOT.jar
   ```
   
   Or with Spring Boot Maven plugin:
   ```
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

## Usage

### Directory Setup

The application will automatically create the following directories:
- `${user.home}/data-sharing/excel/input` - Place Excel files here for processing
- `${user.home}/data-sharing/excel/processed` - Successfully processed files are moved here
- `${user.home}/data-sharing/excel/error` - Files with errors are moved here

### Excel File Format

Excel files should have a header row with column names. These column names will be used as field names when sending data to SAP.

Example Excel structure:
| ProductID | ProductName | Category    | Price   | Quantity | Supplier   | Description              |
|-----------|-------------|-------------|---------|----------|------------|--------------------------|
| P001      | Laptop      | Electronics | 1299.99 | 10       | TechCorp   | High-performance laptop  |
| P002      | Smartphone  | Electronics | 799.99  | 20       | MobileTech | Latest smartphone model  |

### REST API Endpoints

- `POST /api/data-transfer/process` - Manually trigger the data transfer process
- `GET /api/data-transfer/records` - Get all data records
- `GET /api/data-transfer/records?status=PENDING` - Get records with a specific status
- `GET /api/data-transfer/stats` - Get statistics about data records

### Scheduled Processing

The application automatically checks for new Excel files at the interval specified in the configuration (default: every minute).

## Development

### Running with Sample Data

Run the application with the `dev` profile to automatically generate sample data:

```
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### H2 Database Console

The H2 database console is available at `http://localhost:8080/h2-console` with the following default settings:
- JDBC URL: `jdbc:h2:mem:datasharing`
- Username: `sa`
- Password: (empty)

## Notes on SAP Integration

This application includes a mock implementation of SAP connectivity. In a real-world scenario, you would need to:

1. Download SAP JCo libraries from the SAP Support Portal
2. Install the libraries in your local Maven repository
3. Configure the SAP connection properties in `application.yml`
4. Modify the `SapService` class to use the actual SAP JCo API

## License

This project is licensed under the MIT License - see the LICENSE file for details.
