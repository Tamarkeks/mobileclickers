package gr.academic.city.msc.industrial.mobileclickers.mobilemodule.generated.ws;

import javax.xml.rpc.JAXRPCException;
import javax.xml.namespace.QName;
import javax.microedition.xml.rpc.Operation;
import javax.microedition.xml.rpc.Type;
import javax.microedition.xml.rpc.ComplexType;
import javax.microedition.xml.rpc.Element;

public class MobileClickersWSService_Stub implements MobileClickersWSService, javax.xml.rpc.Stub {

    private String[] _propertyNames;
    private Object[] _propertyValues;

    public MobileClickersWSService_Stub() {
        _propertyNames = new String[] { ENDPOINT_ADDRESS_PROPERTY };
        _propertyValues = new Object[] { "http://localhost:8080/MobileClickersWebModule/MobileClickersWSService" };
    }

    public void _setProperty( String name, Object value ) {
        int size = _propertyNames.length;
        for (int i = 0; i < size; ++i) {
            if( _propertyNames[i].equals( name )) {
                _propertyValues[i] = value;
                return;
            }
        }
        String[] newPropNames = new String[size + 1];
        System.arraycopy(_propertyNames, 0, newPropNames, 0, size);
        _propertyNames = newPropNames;
        Object[] newPropValues = new Object[size + 1];
        System.arraycopy(_propertyValues, 0, newPropValues, 0, size);
        _propertyValues = newPropValues;

        _propertyNames[size] = name;
        _propertyValues[size] = value;
    }

    public Object _getProperty(String name) {
        for (int i = 0; i < _propertyNames.length; ++i) {
            if (_propertyNames[i].equals(name)) {
                return _propertyValues[i];
            }
        }
        if (ENDPOINT_ADDRESS_PROPERTY.equals(name) || USERNAME_PROPERTY.equals(name) || PASSWORD_PROPERTY.equals(name)) {
            return null;
        }
        if (SESSION_MAINTAIN_PROPERTY.equals(name)) {
            return new Boolean(false);
        }
        throw new JAXRPCException("Stub does not recognize property: " + name);
    }

    protected void _prepOperation(Operation op) {
        for (int i = 0; i < _propertyNames.length; ++i) {
            op.setProperty(_propertyNames[i], _propertyValues[i].toString());
        }
    }

    public int getNumberOfAnswer(String questionCode) throws java.rmi.RemoteException {
        Object inputObject[] = new Object[] {
            questionCode
        };

        Operation op = Operation.newInstance( _qname_operation_getNumberOfAnswer, _type_getNumberOfAnswer, _type_getNumberOfAnswerResponse );
        _prepOperation( op );
        op.setProperty( Operation.SOAPACTION_URI_PROPERTY, "" );
        Object resultObj;
        try {
            resultObj = op.invoke( inputObject );
        } catch( JAXRPCException e ) {
            Throwable cause = e.getLinkedCause();
            if( cause instanceof java.rmi.RemoteException ) {
                throw (java.rmi.RemoteException) cause;
            }
            throw e;
        }

        return ((Integer )((Object[])resultObj)[0]).intValue();
    }

    public int submitAnswer(String questionCode, String answer, String uniqueSubmissionCode) throws java.rmi.RemoteException {
        Object inputObject[] = new Object[] {
            questionCode,
            answer,
            uniqueSubmissionCode
        };

        Operation op = Operation.newInstance( _qname_operation_submitAnswer, _type_submitAnswer, _type_submitAnswerResponse );
        _prepOperation( op );
        op.setProperty( Operation.SOAPACTION_URI_PROPERTY, "" );
        Object resultObj;
        try {
            resultObj = op.invoke( inputObject );
        } catch( JAXRPCException e ) {
            Throwable cause = e.getLinkedCause();
            if( cause instanceof java.rmi.RemoteException ) {
                throw (java.rmi.RemoteException) cause;
            }
            throw e;
        }

        return ((Integer )((Object[])resultObj)[0]).intValue();
    }

    protected static final QName _qname_operation_getNumberOfAnswer = new QName( "http://webservices.ejb.mobileclickers.industrial.msc.city.academic.gr/", "getNumberOfAnswer" );
    protected static final QName _qname_operation_submitAnswer = new QName( "http://webservices.ejb.mobileclickers.industrial.msc.city.academic.gr/", "submitAnswer" );
    protected static final QName _qname_getNumberOfAnswerResponse = new QName( "http://webservices.ejb.mobileclickers.industrial.msc.city.academic.gr/", "getNumberOfAnswerResponse" );
    protected static final QName _qname_submitAnswerResponse = new QName( "http://webservices.ejb.mobileclickers.industrial.msc.city.academic.gr/", "submitAnswerResponse" );
    protected static final QName _qname_getNumberOfAnswer = new QName( "http://webservices.ejb.mobileclickers.industrial.msc.city.academic.gr/", "getNumberOfAnswer" );
    protected static final QName _qname_submitAnswer = new QName( "http://webservices.ejb.mobileclickers.industrial.msc.city.academic.gr/", "submitAnswer" );
    protected static final Element _type_submitAnswer;
    protected static final Element _type_submitAnswerResponse;
    protected static final Element _type_getNumberOfAnswer;
    protected static final Element _type_getNumberOfAnswerResponse;

    static {
        _type_submitAnswerResponse = new Element( _qname_submitAnswerResponse, _complexType( new Element[] {
            new Element( new QName( "", "return" ), Type.INT )}), 1, 1, false );
        _type_getNumberOfAnswerResponse = new Element( _qname_getNumberOfAnswerResponse, _complexType( new Element[] {
            new Element( new QName( "", "return" ), Type.INT )}), 1, 1, false );
        _type_getNumberOfAnswer = new Element( _qname_getNumberOfAnswer, _complexType( new Element[] {
            new Element( new QName( "", "questionCode" ), Type.STRING, 0, 1, false )}), 1, 1, false );
        _type_submitAnswer = new Element( _qname_submitAnswer, _complexType( new Element[] {
            new Element( new QName( "", "questionCode" ), Type.STRING, 0, 1, false ),
            new Element( new QName( "", "answer" ), Type.STRING, 0, 1, false ),
            new Element( new QName( "", "uniqueSubmissionCode" ), Type.STRING, 0, 1, false )}), 1, 1, false );
    }

    private static ComplexType _complexType( Element[] elements ) {
        ComplexType result = new ComplexType();
        result.elements = elements;
        return result;
    }
}
