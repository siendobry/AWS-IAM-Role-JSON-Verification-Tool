## Tool for verifying correctness of JSON documents containing AWS IAM Role Policies

Program can run in two modes: 
- robust: checks for correctness of the whole document
- regular: checks only if document is not null, PolicyDocument is not null, Statement list is not null and if any of Resource field contain asterisk

Usage: (Java 17 SDK in required)
In order to use this project navigate to root directory, run the command `gradlew run --args='{path-to-document}'` to run in regular mode, `gradlew run --args='{--robust/-r} {path-to-document}'` to run in robust mode or just run it inside an IDE and adjust parameters inside the main method in the Main class.
