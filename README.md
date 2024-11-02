1. Add EAP8 Helm charts to the OpenShift cluster
```
helm repo add jboss-eap https://jbossas.github.io/eap-charts/
```

2. Clone this repository. Package the application and provision a local EAP 8 server for testing
```
mvn clean package -Popenshift -DskipTests
```

3. Access the application at the following URL 
```
http://localhost:8080/eap8-simple-8.0.3/HelloWorld
```

4. Run remote EJB integration testing
```
mvn verify -Pintegration-testing
```

