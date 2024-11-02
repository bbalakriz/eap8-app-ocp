Reference: https://github.com/wildfly/quickstart/tree/main/ejb-remote

Local testing
==============
1. Start EAP 8 on the local machine
```
cd /Users/bbalasub/Mywork/Setup/eap8/bin
./standalone.sh
```

2. Clone this repository. Package the application and deploy to 
```
mvn clean package
cp target/eap8-simple.war ~/Mywork/Setup/eap8/standalone/deployments
```

3. Check EAP 8 logs to see that the application has been deployed successfully and the JNDI names of the deployed EJB is shown clearly. Check if the application is accessible at the following URL:
```
curl -v http://localhost:8080/eap8-simple/TestServlet
```

4.  Test the remote EJB access from the local client using the integration tests
```
mvn verify -Pintegration-testing
```

Deploying on EAP 8 in OpenShift
=================================
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
http://localhost:8080/eap8-simple/TestServlet
```

4. Run remote EJB integration testing
```
mvn verify -Pintegration-testing
```

