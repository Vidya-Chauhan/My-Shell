#!/bin/sh
set -e

# Compile source code
mkdir -p out
javac -d out src/main/java/*.java

# Run program
java -cp out Main
