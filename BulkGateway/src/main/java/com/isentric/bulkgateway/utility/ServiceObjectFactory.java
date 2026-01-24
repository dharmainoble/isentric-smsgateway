// 
// Decompiled by Procyon v0.5.32
// 

package com.isentric.bulkgateway.utility;

import org.apache.commons.pool.KeyedPoolableObjectFactory;

public class ServiceObjectFactory implements KeyedPoolableObjectFactory
{
    private static ServiceObjectFactory serviceObjectFactory;
    
    public static ServiceObjectFactory getServiceObjectFactoryInstance() {
        return ServiceObjectFactory.serviceObjectFactory;
    }
    
    public Object makeObject(final Object key) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        final Class serviceClass = Class.forName(key.toString());
        return serviceClass.newInstance();
    }
    
    public void activateObject(final Object key, final Object obj) {
    }
    
    public void passivateObject(final Object key, final Object obj) {
    }
    
    public void destroyObject(final Object key, final Object obj) {
        System.out.println(">> Object Key Destroy: " + key);
    }
    
    public boolean validateObject(final Object key, final Object obj) {
        return true;
    }
    
    static {
        ServiceObjectFactory.serviceObjectFactory = new ServiceObjectFactory();
    }
}
