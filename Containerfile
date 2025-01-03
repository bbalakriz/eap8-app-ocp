## EAP 8 builder image
FROM registry.redhat.io/jboss-eap-8/eap8-openjdk17-builder-openshift-rhel8:latest AS builder

# set environment variables
ENV GALLEON_PROVISION_FEATURE_PACKS=org.jboss.eap:wildfly-ee-galleon-pack,org.jboss.eap.cloud:eap-cloud-galleon-pack,org.jboss.eap:eap-datasources-galleon-pack
ENV GALLEON_PROVISION_LAYERS=base-server,bean-validation,cdi,ejb,cloud-server,core-tools,cloud-server,datasources-web-server
ENV GALLEON_PROVISION_CHANNELS=org.jboss.eap.channels:eap-8.0
# ENV GALLEON_MAVEN_ARGS="-X -e"  # for debugging

# add custom Maven settings
# COPY settings.xml /home/jboss/.m2/settings.xml

# execute the s2i assemble script from the base image
RUN /usr/local/s2i/assemble

## EAP 8 runtime image
FROM registry.redhat.io/jboss-eap-8/eap8-openjdk17-runtime-openshift-rhel8:latest AS runtime

# copy the EAP server provisioned using galleon from the builder image and set ownership and permissions for $JBOSS_HOME
COPY --from=builder --chown=jboss:root $JBOSS_HOME $JBOSS_HOME

# copy the application war (renamed as ROOT.war) and the IBM MQ 9.3.0.2 resource adapter archive
COPY --chown=jboss:root target/ROOT.war $JBOSS_HOME/standalone/deployments/

# add an application user for ejb remoting
RUN $JBOSS_HOME/bin/add-user.sh -a -u 'quickstartUser' -p 'quickstartPwd1!'

#ENV CONFIG_IS_FINAL=true # in case the configuration doesn't need any deployment change

# ensure appropriate permissions for $JBOSS_HOME
RUN chmod -R ug+rwX $JBOSS_HOME