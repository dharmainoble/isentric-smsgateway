// 
// Decompiled by Procyon v0.5.32
// 

package com.isentric.bulkgateway.utility;


import com.isentric.bulkgateway.bg.model.SMSMessageResponse;
import com.isentric.bulkgateway.dto.JMSQueueObject;
import com.isentric.bulkgateway.manager.JMSQueueManager;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageProducer;
import jakarta.jms.ObjectMessage;

import java.io.Serializable;

public class JmsUtil
{
    public boolean postQueue(final String queueContext, final SMSMessageResponse queueMsg) throws JMSException {
        final JMSQueueManager jmsQueueManager = JMSQueueManager.getInstance(queueContext);
        final JMSQueueObject queueObject = jmsQueueManager.getInit(queueContext);
        final MessageProducer jmsProducer = jmsQueueManager.getMessageProducer(queueObject);
        final ObjectMessage jmsObjectMessage = jmsQueueManager.getObjectMessage(queueObject);
        jmsObjectMessage.setObject((Serializable)queueMsg);
        jmsProducer.send((Message)jmsObjectMessage);
        jmsQueueManager.getDestroy(queueObject);
        return true;
    }
}
