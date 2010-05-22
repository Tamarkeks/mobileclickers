package gr.academic.city.msc.industrial.mobileclickers.mobilemodule.generated.ws;
import javax.xml.namespace.QName;

public interface MobileClickersWSService extends java.rmi.Remote {

    /**
     *
     */
    public int getNumberOfAnswer(String questionCode) throws java.rmi.RemoteException;

    /**
     *
     */
    public int submitAnswer(String questionCode, String answer, String uniqueSubmissionCode) throws java.rmi.RemoteException;

}
