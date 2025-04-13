# Stage 1: Build the application with Maven
FROM maven:3.9.9-amazoncorretto-17-alpine AS builder

WORKDIR /app

# Create a volume for Maven dependencies
VOLUME /root/.m2

# Copy only the necessary files for dependency resolution
COPY pom.xml ./

# Download dependencies (cache step)
RUN mvn dependency:go-offline

# Copy the rest of the application code
COPY . .

# Run Maven build to create the WAR file
RUN mvn clean package

# Stage 2: Final WildFly image
FROM quay.io/wildfly/wildfly:35.0.1.Final-jdk21

RUN /opt/jboss/wildfly/bin/add-user.sh root Aavn123!@# --silent

EXPOSE 8080 9990

ENV DATASOURCE_NAME PostgresDS
ENV DATASOURCE_JNDI java:/PostgresDS
ENV POSTGRESQL_VERSION 42.6.0
ENV JBOSS_HOME /opt/jboss/wildfly
ENV VALID_CONNECTION_CHECKER org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLValidConnectionChecker

# Install PostgreSQL drivers and configure datasource
USER root

# Copy the WAR file from the builder stage
COPY --from=builder /app/target/*.war $JBOSS_HOME/standalone/deployments/

# Create a startup script that will configure WildFly with the environment variables
COPY --chown=jboss:jboss configure-wildfly.sh /opt/jboss/
RUN chmod +x /opt/jboss/configure-wildfly.sh

# Set the script as the entry point
CMD ["/opt/jboss/configure-wildfly.sh"]