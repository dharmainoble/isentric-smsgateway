//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.isentric.bulkgateway.infobip.webservice;


import com.isentric.bulkgateway.tga.webservice.QSResponse;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.wsdl.Skeleton;

import javax.xml.namespace.QName;
import java.rmi.RemoteException;
import java.util.*;

public class InfobipwsSoapBindingSkeleton implements InfobipDipInterface, Skeleton {
    private InfobipDipInterface impl;
    private static Map _myOperations = new Hashtable();
    private static Collection _myOperationsList = new ArrayList();

    static {
        ParameterDesc[] _params = new ParameterDesc[]{new ParameterDesc(new QName("", "in0"), (byte)1, new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false)};
        OperationDesc _oper = new OperationDesc("InfobipDipping", _params, new QName("", "InfobipDippingReturn"));
        _oper.setReturnType(new QName("http://webservice.tga.bg.isentric.com", "QSResponse"));
        _oper.setElementQName(new QName("urn:webservice.Infobip.bg.isentric.com", "InfobipDipping"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("InfobipDipping") == null) {
            _myOperations.put("InfobipDipping", new ArrayList());
        }

        ((List)_myOperations.get("InfobipDipping")).add(_oper);
        _params = new ParameterDesc[]{new ParameterDesc(new QName("", "in0"), (byte)1, new QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), String.class, false, false)};
        _oper = new OperationDesc("InfobipDippingSkipFilter", _params, new QName("", "InfobipDippingSkipFilterReturn"));
        _oper.setReturnType(new QName("http://webservice.tga.bg.isentric.com", "QSResponse"));
        _oper.setElementQName(new QName("urn:webservice.Infobip.bg.isentric.com", "InfobipDippingSkipFilter"));
        _oper.setSoapAction("");
        _myOperationsList.add(_oper);
        if (_myOperations.get("InfobipDippingSkipFilter") == null) {
            _myOperations.put("InfobipDippingSkipFilter", new ArrayList());
        }

        ((List)_myOperations.get("InfobipDippingSkipFilter")).add(_oper);
    }

    public static List getOperationDescByName(String methodName) {
        return (List)_myOperations.get(methodName);
    }

    public static Collection getOperationDescs() {
        return _myOperationsList;
    }

    public InfobipwsSoapBindingSkeleton() {
        this.impl = new InfobipwsSoapBindingImpl();
    }

    public InfobipwsSoapBindingSkeleton(InfobipDipInterface impl) {
        this.impl = impl;
    }

    public QSResponse InfobipDipping(String in0) throws RemoteException {
        QSResponse ret = this.impl.InfobipDipping(in0);
        return ret;
    }

    public QSResponse InfobipDippingSkipFilter(String in0) throws RemoteException {
        QSResponse ret = this.impl.InfobipDippingSkipFilter(in0);
        return ret;
    }
}
