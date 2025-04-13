#!/bin/bash
set -e

echo "Starting WildFly in the background..."
$JBOSS_HOME/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 &
WILDFLY_PID=$!

echo "Waiting for WildFly to start..."
for i in {1..60}; do
  if $JBOSS_HOME/bin/jboss-cli.sh --connect --command=":read-attribute(name=server-state)" | grep -q "running"; then
    echo "WildFly started successfully"
    break
  fi

  if [ $i -eq 60 ]; then
    echo "Timed out waiting for WildFly to start"
    exit 1
  fi

  echo "Waiting for WildFly to start... ($i/60)"
  sleep 2
done

echo "Setting up storage directory..."
mkdir -p /opt/jboss/wildfly/storage
chown jboss:jboss /opt/jboss/wildfly/storage

echo "Downloading PostgreSQL driver..."
cd /tmp
wget -O "postgresql-${POSTGRESQL_VERSION}.jar" "http://search.maven.org/remotecontent?filepath=org/postgresql/postgresql/${POSTGRESQL_VERSION}/postgresql-${POSTGRESQL_VERSION}.jar"

echo "Deploying PostgreSQL driver..."
$JBOSS_HOME/bin/jboss-cli.sh --connect --command="deploy /tmp/postgresql-${POSTGRESQL_VERSION}.jar"

echo "Configuring datasource with environment variables..."
$JBOSS_HOME/bin/jboss-cli.sh --connect --command="data-source add \
  --name=${DATASOURCE_NAME} \
  --driver-name=postgresql-${POSTGRESQL_VERSION}.jar \
  --driver-class=org.postgresql.Driver \
  --jndi-name=${DATASOURCE_JNDI} \
  --connection-url=${DB_HOST} \
  --user-name=${DB_USER} \
  --password=${DB_PASS} \
  --validate-on-match=true \
  --valid-connection-checker-class-name=${VALID_CONNECTION_CHECKER}"

echo "Configuring HTTP listener..."
$JBOSS_HOME/bin/jboss-cli.sh --connect --commands="\
  /subsystem=undertow/server=default-server/http-listener=default:write-attribute(name=redirect-socket, value=https),\
  /subsystem=undertow/server=default-server/http-listener=default:write-attribute(name=enable-http2, value=true),\
  /subsystem=undertow/server=default-server/http-listener=default:write-attribute(name=read-timeout, value=0),\
  /subsystem=undertow/server=default-server/http-listener=default:write-attribute(name=write-timeout, value=0)"

echo "Stopping WildFly to apply changes..."
$JBOSS_HOME/bin/jboss-cli.sh --connect --command=:shutdown
wait $WILDFLY_PID

echo "Cleaning up temporary files..."
rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/ $JBOSS_HOME/standalone/log/*

echo "Starting WildFly with the applied configuration..."
exec $JBOSS_HOME/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0