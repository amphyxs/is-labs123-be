echo "Shutting down Docker containers..."
sudo docker compose -f ../docker-compose.yml --profile postgres-test-db down
