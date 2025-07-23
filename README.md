# snake-bite-api
Username: admin
Password: Kingsman@12345

Log into your SonarQube instance and navigate to My Account > Security.
Generate a new token by clicking Generate Tokens and copying it. Then replace the token as below.

mvn clean test verify sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=squ_843c4546ab2a4973b2c4e16ad1ba1eff92fce97a
