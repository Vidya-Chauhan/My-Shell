# #!/bin/sh
# #
# # Use this script to run your program LOCALLY.
# #
# # Note: Changing this script WILL NOT affect how CodeCrafters runs your program.
# #
# # Learn more: https://codecrafters.io/program-interface

# set -e # Exit early if any commands fail

# # Copied from .codecrafters/compile.sh
# #
# # - Edit this to change how your program compiles locally
# # - Edit .codecrafters/compile.sh to change how your program compiles remotely
# (
#   cd "$(dirname "$0")" # Ensure compile steps are run within the repository directory
#   mvn -q -B package -Ddir=/tmp/codecrafters-build-shell-java
# )

# # Copied from .codecrafters/run.sh
# #
# # - Edit this to change how your program runs locally
# # - Edit .codecrafters/run.sh to change how your program runs remotely
# exec java -jar /tmp/codecrafters-build-shell-java/codecrafters-shell.jar "$@"


#!/bin/sh

set -e

# Compile Java source files to /tmp
mkdir -p /tmp/codecrafters-build-shell-java/out

javac -d /tmp/codecrafters-build-shell-java/out -cp lib/* src/main/java/*.java

# Create the jar file in the expected location
jar cfe /tmp/codecrafters-build-shell-java/codecrafters-shell.jar Main -C /tmp/codecrafters-build-shell-java/out .

# Run the jar
exec java -cp /tmp/codecrafters-build-shell-java/codecrafters-shell.jar:lib/* Main
