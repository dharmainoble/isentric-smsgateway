//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.manager;


import com.isentric.bulkgateway.dto.JMSQueueObject;
import jakarta.jms.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public class JMSQueueManager {
    private static int connectionCount = 0;
    private static Context jndiContext = null;
    private static ConnectionFactory connectionFactory = null;
    private static Destination queueDestination = null;
    private static String queueName = null;
    private static Hashtable<String, JMSQueueManager> queuePool = new Hashtable();
    public static final String ORP_CONNECTION_FACTORY_CONTEXT = "ConnectionFactory";

    private JMSQueueManager() {
    }

    public static Context initJNDIContext() {
        if (jndiContext == null) {
            try {
                jndiContext = new InitialContext();
            } catch (NamingException ne) {
                ne.printStackTrace();
                return null;
            }

            return jndiContext;
        } else {
            return jndiContext;
        }
    }

    public static synchronized ConnectionFactory initConnectionFactory(Context inJndiContext) {
        if (connectionFactory == null) {
            try {
                connectionFactory = (ConnectionFactory)inJndiContext.lookup("ConnectionFactory");
            } catch (NamingException var2) {
                return null;
            }

            return connectionFactory;
        } else {
            return connectionFactory;
        }
    }

    public static synchronized Connection initConnection(ConnectionFactory inConnectionFactory) {
        Connection thisConnection = null;

        try {
            thisConnection = inConnectionFactory.createConnection();
            ++connectionCount;
            Connection var2 = thisConnection;
            return var2;
        } catch (JMSException jmse) {
            jmse.printStackTrace();
        } finally {
            if (thisConnection == null) {
                try {
                    thisConnection = inConnectionFactory.createConnection();
                    ++connectionCount;
                    return thisConnection;
                } catch (JMSException jmse) {
                    jmse.printStackTrace();
                }
            }

        }

        return thisConnection;
    }

    public static Session initSession(Connection inConnection) {
        try {
            Session thisSession = inConnection.createSession(false, 1);
            return thisSession;
        } catch (JMSException var2) {
            return null;
        }
    }

    public static Destination initQueueDestination(Context inJndiContext, String inQueueDestinationContext) {
        try {
            queueDestination = (Destination)inJndiContext.lookup(inQueueDestinationContext);
            queueName = inQueueDestinationContext;
        } catch (NamingException var3) {
            return null;
        }

        return queueDestination;
    }

    public static JMSQueueManager getInstance(String inQueueDestinationContext) {
        if (queuePool.containsKey(inQueueDestinationContext)) {
            return (JMSQueueManager)queuePool.get(inQueueDestinationContext);
        } else {
            System.out.println("No JMSQueueManager is found, creating new instance manager.");
            JMSQueueManager queueManager = new JMSQueueManager();
            queuePool.put(inQueueDestinationContext, queueManager);
            return queueManager;
        }
    }

    public JMSQueueObject getInit(String inQueueDestinationContext) {
        Context jndiContext = initJNDIContext();
        ConnectionFactory jmsQueueConnectionFactory = initConnectionFactory(jndiContext);
        Connection jmsQueueConnection = initConnection(jmsQueueConnectionFactory);
        Session jmsQueueSession = initSession(jmsQueueConnection);
        Destination jsmQueueDestination = initQueueDestination(jndiContext, inQueueDestinationContext);
        JMSQueueObject queueObject = new JMSQueueObject();
        queueObject.setJndiContext(jndiContext);
        queueObject.setJmsConnectionFactory(jmsQueueConnectionFactory);
        queueObject.setJmsConnection(jmsQueueConnection);
        queueObject.setJmsSession(jmsQueueSession);
        queueObject.setJmsDestination(jsmQueueDestination);
        return queueObject;
    }

    public MessageProducer getMessageProducer(JMSQueueObject queueObject) throws JMSException {
        MessageProducer thisMessageProducer = queueObject.getJmsSession().createProducer(queueObject.getJmsDestination());
        return thisMessageProducer;
    }

    public ObjectMessage getObjectMessage(JMSQueueObject queueObject) throws JMSException {
        ObjectMessage objectMessage = queueObject.getJmsSession().createObjectMessage();
        return objectMessage;
    }

    public void getDestroy(JMSQueueObject queueObject) throws JMSException {
        if (queueObject.getJmsSession() != null) {
            queueObject.getJmsSession().close();
        }

        if (queueObject.getJmsConnection() != null) {
            queueObject.getJmsConnection().close();
            --connectionCount;
        }

    }

    public static void printConnectionCount() {
        System.out.println("JMS connection=" + connectionCount);
    }

    public static String getQueueDestinationName() {
        return queueName;
    }
}
