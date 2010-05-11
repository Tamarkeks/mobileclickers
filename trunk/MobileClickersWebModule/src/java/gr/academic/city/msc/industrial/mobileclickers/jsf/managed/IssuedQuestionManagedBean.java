/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.academic.city.msc.industrial.mobileclickers.jsf.managed;

import gr.academic.city.msc.industrial.mobileclickers.ejb.session.QuestionService;
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
    /** Creates a new instance of IssuedQuestionManagedBean */
    public IssuedQuestionManagedBean() {
    }

    public long getIssuedQuestionID() {
        return issuedQuestionID;
    }

    public void setIssuedQuestionID(long issuedQuestionID) {
        this.issuedQuestionID = issuedQuestionID;
    }

    public String stopQuestionActionHandler() {
        questionService.stopQuestion(issuedQuestionID);
        return null;
    }
}
