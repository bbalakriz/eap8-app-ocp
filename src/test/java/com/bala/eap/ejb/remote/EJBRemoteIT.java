
package com.bala.eap.ejb.remote;

import com.bala.eap.ejb.RemoteCalculator;
import org.junit.Assert;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public class EJBRemoteIT {

    private static final String DEFAULT_SERVER_HOST = "http://localhost:8080";

    private String getProviderURl() {
        final String serverHost = System.getProperty("server.host");
        return "remote+" + (serverHost != null ? serverHost : DEFAULT_SERVER_HOST);
    }

    private String getEJBBaseJndiName() {
        return "ejb:/ROOT";
    }

    @Test
    public void testCalculator() throws NamingException {
        final Hashtable<String, String> jndiProperties = new Hashtable<>();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        // use HTTP upgrade, an initial upgrade requests is sent to upgrade to the
        // remoting protocol
        jndiProperties.put(Context.PROVIDER_URL, getProviderURl());
        jndiProperties.put(Context.SECURITY_PRINCIPAL, "quickstartUser");
        jndiProperties.put(Context.SECURITY_CREDENTIALS, "quickstartPwd1!");

        jndiProperties.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
        jndiProperties.put("jboss.naming.client.connect.options.org.xnio.Options.SSL_STARTTLS", "false");
        jndiProperties.put("jboss.naming.client.connect.options.org.xnio.Options.SSL_ENABLED", "false");
        jndiProperties.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");


        final Context context = new InitialContext(jndiProperties);
        // The JNDI lookup name for a stateless session bean has the syntax of:
        // ejb:<appName>/<moduleName>/<distinctName>/<beanName>!<viewClassName>
        //
        // <appName> The application name is the name of the EAR that the EJB is
        // deployed in
        // (without the .ear). If the EJB JAR is not deployed in an EAR then this is
        // blank. The app name can also be specified in the EAR's application.xml
        //
        // <moduleName> By the default the module name is the name of the EJB JAR file
        // (without the
        // .jar suffix). The module name might be overridden in the ejb-jar.xml
        //
        // <distinctName> : EAP allows each deployment to have an (optional) distinct
        // name.
        // This example does not use this so leave it blank.
        //
        // <beanName> : The name of the session been to be invoked.
        //
        // <viewClassName>: The fully qualified classname of the remote interface. Must
        // include
        // the whole package name.
      
        final RemoteCalculator statelessRemoteCalculator = (RemoteCalculator) context
                .lookup(getEJBBaseJndiName() + "/CalculatorBean!" + RemoteCalculator.class.getName());

        System.out.println(statelessRemoteCalculator!=null? statelessRemoteCalculator.toString(): "statelessRemoteCalculator is null");                                    
        System.out.println("Obtained a remote stateless calculator for invocation");

        // invoke on the remote calculator
        int a = 204;
        int b = 340;
        System.out.println("Adding " + a + " and " + b + " via the remote stateless calculator deployed on the server");

        int sum = statelessRemoteCalculator.add(a, b);
        System.out.println("Remote calculator returned sum = " + sum);

        Assert.assertEquals("Unexpected remote stateless calculator sum", (a + b), sum);
        // try one more invocation, this time for subtraction
        int num1 = 3434;
        int num2 = 2332;
        System.out.println("Subtracting " + num2 + " from " + num1
                + " via the remote stateless calculator deployed on the server");

        int difference = statelessRemoteCalculator.subtract(num1, num2);
        System.out.println("Remote calculator returned difference = " + difference);
        Assert.assertEquals("Unexpected remote stateless calculator difference", (num1 - num2), difference);
        context.close();
    }

    // @Test
    // import org.jboss.as.quickstarts.ejb.remote.stateful.RemoteCounter;

    // public void testCounter() throws NamingException {
    // final Hashtable<String, String> jndiProperties = new Hashtable<>();
    // jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,
    // "org.wildfly.naming.client.WildFlyInitialContextFactory");
    // //use HTTP upgrade, an initial upgrade requests is sent to upgrade to the
    // remoting protocol
    // jndiProperties.put(Context.PROVIDER_URL, getProviderURl());
    // final Context context = new InitialContext(jndiProperties);
    // // The JNDI lookup name for a stateless session bean has the syntax of:
    // // ejb:<appName>/<moduleName>/<distinctName>/<beanName>!<viewClassName>
    // //
    // // <appName> The application name is the name of the EAR that the EJB is
    // deployed in
    // // (without the .ear). If the EJB JAR is not deployed in an EAR then this is
    // // blank. The app name can also be specified in the EAR's application.xml
    // //
    // // <moduleName> By the default the module name is the name of the EJB JAR
    // file (without the
    // // .jar suffix). The module name might be overridden in the ejb-jar.xml
    // //
    // // <distinctName> : EAP allows each deployment to have an (optional) distinct
    // name.
    // // This example does not use this so leave it blank.
    // //
    // // <beanName> : The name of the session been to be invoked.
    // //
    // // <viewClassName>: The fully qualified classname of the remote interface.
    // Must include
    // // the whole package name.
    // final RemoteCounter statefulRemoteCounter = (RemoteCounter)
    // context.lookup(getEJBBaseJndiName() + "/CounterBean!" +
    // RemoteCounter.class.getName() + "?stateful");
    // System.out.println("Obtained a remote stateful counter for invocation");
    // // invoke on the remote counter bean
    // final int NUM_TIMES = 5;
    // System.out.println("Counter will now be incremented " + NUM_TIMES + "
    // times");
    // for (int i = 0; i < NUM_TIMES; i++) {
    // System.out.println("Incrementing counter");
    // statefulRemoteCounter.increment();
    // System.out.println("Count after increment is " +
    // statefulRemoteCounter.getCount());
    // }
    // Assert.assertEquals("Unexpected remote stateful counter value after
    // increments", NUM_TIMES, statefulRemoteCounter.getCount());
    // // now decrementing
    // System.out.println("Counter will now be decremented " + NUM_TIMES + "
    // times");
    // for (int i = NUM_TIMES; i > 0; i--) {
    // System.out.println("Decrementing counter");
    // statefulRemoteCounter.decrement();
    // System.out.println("Count after decrement is " +
    // statefulRemoteCounter.getCount());
    // }
    // Assert.assertEquals("Unexpected remote stateful counter value after
    // decrements", 0, statefulRemoteCounter.getCount());
    // context.close();
    // }
}