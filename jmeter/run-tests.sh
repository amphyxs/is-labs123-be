#!/bin/bash

# Step 1: Launch Docker Compose profile for postgres-test-db
echo "Launching PostgreSQL test database via Docker Compose..."
sudo docker compose -f ../docker-compose.yml --profile postgres-test-db up -d

# Step 2: Wait for the PostgreSQL database to become available on port 5432
echo "Waiting for PostgreSQL to be available on port 5432..."
until nc -z -v -w30 localhost 5432; do
  echo "Waiting for PostgreSQL to start..."
  sleep 5
done

echo "PostgreSQL is up and running on port 5432."

# Step 3: Execute SQL script to initialize test data
echo "Executing initital SQL scripts"
docker exec -i postgres_test_db psql -U testuser -d testdb -f /init/ddl.sql
docker exec -i postgres_test_db psql -U testuser -d testdb -f /init/db-functions.sql
docker exec -i postgres_test_db psql -U testuser -d testdb -f /init/start-data-for-jmeter-tests.sql

echo "Clearing previous results..."
rm -rf report
rm results

# Step 4: Run Apache JMeter tests
echo "Running Apache JMeter tests..."
java -jar /home/amphyx/apache-jmeter-5.6.3/apache-jmeter-5.6.3/bin/ApacheJMeter.jar -n -t Assertion.jmx -l ./results -e -o ./report

# Optional: Bring down the Docker containers after the tests
echo "Shutting down Docker containers..."
sudo docker compose -f ../docker-compose.yml --profile postgres-test-db down
