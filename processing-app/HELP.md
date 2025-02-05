## Tech Stack
- **Spring Boot 3.2.2** (Web, Validation, AOP, Kafka)
- **Lombok** 
- **Kafka** 
- **Docker** 
- **Gradle** 

## Running the Application
### Using Docker
Ensure Docker is installed and run:

```docker-compose up --build ```

### Without Docker
#### Run the application using Gradle:

```./gradlew bootRun```

### Configuration

#### Modify the following environment variables inn variables.env to configure Kafka when running with docker:

##### KAFKA_BOOTSTRAP_SERVER=<Kafka Bootstrap Server>
##### KAFKA_USERNAME=<Kafka Username>
##### KAFKA_PASSWORD=<Kafka Password>


## Future Improvements
##### Enhance exception handling, cover different scenarios.
##### Implement a retry mechanism for failed text fetch requests and Kafka message producing.
##### Store credentials in Google Cloud Secret Manager.
##### Stream logs to cloud to monitor
##### Make correlationId appear in logs and in request/response headers
##### Use Mapstruct for conversions
##### Write tests!



