#!/bin/bash

./setup-test-env.sh

echo "Clearing previous results..."
rm -rf report
rm results

echo "Running Apache JMeter tests..."
java -jar /home/amphyx/apache-jmeter-5.6.3/apache-jmeter-5.6.3/bin/ApacheJMeter.jar -n -t Assertion.jmx -l ./results -e -o ./report

./teardown-test-env.sh
