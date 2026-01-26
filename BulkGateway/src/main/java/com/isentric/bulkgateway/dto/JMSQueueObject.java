//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.dto;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import jakarta.jms.Session;

import javax.naming.Context;

public class JMSQueueObject {
    private Context jndiContext = null;
    private ConnectionFactory jmsConnectionFactory = null;
    private Connection jmsConnection = null;
    private Session jmsSession = null;
    private Destination jmsDestination = null;

    public void setJndiContext(Context inJndiContext) {
        this.jndiContext = inJndiContext;
    }

    public Context getJndiContext() {
        return this.jndiContext;
    }

    public void setJmsConnectionFactory(ConnectionFactory inJmsConnectionFactory) {
        this.jmsConnectionFactory = inJmsConnectionFactory;
    }

    public ConnectionFactory getJmsConnectionFactory() {
        return this.jmsConnectionFactory;
    }

    public void setJmsConnection(Connection inJmsConnection) {
        this.jmsConnection = inJmsConnection;
    }

    public Connection getJmsConnection() {
        return this.jmsConnection;
    }

    public void setJmsSession(Session inJmsSession) {
        this.jmsSession = inJmsSession;
    }

    public Session getJmsSession() {
        return this.jmsSession;
    }

    public void setJmsDestination(Destination inJmsDestination) {
        this.jmsDestination = inJmsDestination;
    }

    public Destination getJmsDestination() {
        return this.jmsDestination;
    }

    //public String toString() {
    //    return (new ToStringBuilder(this)).append("jndiContext", this.jndiContext).append("jmsConnection", this.jmsConnection).append("jmsConnectionFactory", this.jmsConnectionFactory).append("jmsSession", this.jmsSession).append("jmsDestination", this.jmsDestination).toString();
    //}
}
