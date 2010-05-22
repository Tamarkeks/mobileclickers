/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.academic.city.msc.industrial.mobileclickers.jsf.managed;

import gr.academic.city.msc.industrial.mobileclickers.ejb.exception.QuestionException;
import gr.academic.city.msc.industrial.mobileclickers.ejb.session.QuestionService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Ivo Neskovic <ivo.neskovic@gmail.com>
 */
@ManagedBean(name="issuedQuestionManagedBean")
@RequestScoped
public class IssuedQuestionManagedBean {
    @EJB
    private QuestionService questionService;
    private long issuedQuestionID;
    private String errorMessage;
    /** Creates a new instance of IssuedQuestionManagedBean */
    public IssuedQuestionManagedBean() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public long getIssuedQuestionID() {
        return issuedQuestionID;
    }

    public void setIssuedQuestionID(long issuedQuestionID) {
        this.issuedQuestionID = issuedQuestionID;
    }

    public String stopQuestionActionHandler() {
        errorMessage = null;
        try {
            questionService.stopQuestion(issuedQuestionID);
        } catch (QuestionException ex) {
            errorMessage = ex.getMessage();
            return null;
        }
        return null;
    }
}
