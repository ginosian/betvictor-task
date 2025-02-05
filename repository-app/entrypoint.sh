#!/bin/sh

if [ -f /app/variables.env ]; then
  echo "Loading environment variables from /app/variables.env"
  export $(grep -v '^#' /app/variables.env | xargs)
fi

echo "Starting application..."
exec java -jar /app/app.jar
