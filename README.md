References: 
============
Examples: 
1. Remote EJB: https://github.com/wildfly/quickstart/tree/main/ejb-remote
2. Client JNDI lookup: https://docs.redhat.com/en/documentation/red_hat_jboss_enterprise_application_platform/7.4/html-single/developing_jakarta_enterprise_beans_applications/index?extIdCarryOver=true&intcmp=7015Y000003t8u8QAA&sc_cid=701f2000000txokAAA#initial_context_lookup

Red Hat KBs:
1. https://access.redhat.com/solutions/3259861
2. https://access.redhat.com/solutions/2972861
3. https://access.redhat.com/solutions/266853
4. https://access.redhat.com/solutions/3167251

Wildfly Remote EJB in kubernetes clustered environment: https://groups.google.com/g/wildfly/c/XY_aDnU_AQk


Local testing
==============
1. Start EAP 8 on the local machine and add the user that's used for remote EJB authentication 
```
cd /Users/bbalasub/Mywork/Setup/eap8/bin
./add-user.sh -a -u 'quickstartUser' -p 'quickstartPwd1!' 
./standalone.sh
```

2. Clone this repository. Package the application and deploy to 
```
mvn clean package
cp target/ROOT.war ~/Mywork/Setup/eap8/standalone/deployments
```

3. Check EAP 8 logs to see that the application has been deployed successfully and the JNDI names of the deployed EJB is shown clearly. Check if the application is accessible at the following URL:
```
curl -v http://localhost:8080/TestServlet
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

3. Start the generated embedded EAP server locally to test EAP server readiness with the deployed EJB application
```
./target/server/bin/standalone.sh
```

4. In a new terminal, add a user to the EAP server for remote EJB authentication
```
./target/server/bin/add-user.sh -a -u 'quickstartUser' -p 'quickstartPwd1!' 
```

5. Run remote EJB integration testing
```
mvn verify -Pintegration-testing
```

6. Deploy EAP along with the application to OpenShift. Test if the deployment and pods are up and running with the EJB application deployed and shown in the log with all possible JNDI lookup names for the EJB. 
```
helm install eap8 -f charts/helm.yaml --repo https://jbossas.github.io/eap-charts/ eap8

```

7. Make sure that the deployed EAP8 server has non-HTTP route (TLS disabled)

8. Add a user to the EAP8 server deployed on OpenShift
```
oc exec <EAP8-POD-NAME> -n eap8 -- /opt/server/bin/add-user.sh -a -u 'quickstartUser' -p 'quickstartPwd1!'
```

9. Test the remote EJB invocation by running the following in the terminal.  
```
mvn verify -Pintegration-testing -Dserver.host=http://eap8-eap8.apps.cluster-m55vs.m55vs.sandbox2542.opentlc.com:80
```