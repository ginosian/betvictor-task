#!/bin/sh
# If variables.env exists, load the environment variables.
if [ -f /app/variables.env ]; then
  echo "Loading environment variables from /app/variables.env"
  # This command exports each line (ignoring comments) as an environment variable.
  export $(grep -v '^#' /app/variables.env | xargs)
fi

echo "Starting application..."
exec java -jar /app/app.jar
