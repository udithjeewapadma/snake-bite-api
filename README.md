# snake-bite-api
Username: admin
Password: Kingsman@12345

Log into your SonarQube instance and navigate to My Account > Security.
Generate a new token by clicking Generate Tokens and copying it. Then replace the token as below.

mvn clean test verify sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=squ_8730acd3c590e2b4fea40b5561853316e0f049c7
