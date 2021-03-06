package com.redhat.gpe.eapclass.basicmessaging;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.JMSContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SimpleJMSTest {

    private static final String DEFAULT_USERNAME = "guest";
    private static final String DEFAULT_PASSWORD = "guest!";

    JMSContext contextObj = null;
    Queue gpteQueue = null;

    @Before
    public void setup() throws Exception {

        String userName = System.getProperty("messaging.user", DEFAULT_USERNAME);
        String password = System.getProperty("messaging.password", DEFAULT_PASSWORD);

        Hashtable<Object, Object> environment = new Hashtable<>();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        environment.put(Context.PROVIDER_URL, "http-remoting://127.0.0.1:8080" );
        InitialContext initialContext = new InitialContext(environment);

        ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("jms/RemoteConnectionFactory");
        contextObj = cf.createContext(userName, password);
        gpteQueue = (Queue) initialContext.lookup("jms/gpteQueue");

        System.out.println("setup() JMS objects found on remote broker:\t"+contextObj+"\t"+gpteQueue);
    }

    @Test
    public void sendJMSMessage() throws Exception {
        String content = "Hello from Red Hat GPTE";
        contextObj.createProducer().send(gpteQueue, content);
    }

    @After
    public void shutdown() throws Exception {
        if(contextObj != null)
            contextObj.close();
    }

}
