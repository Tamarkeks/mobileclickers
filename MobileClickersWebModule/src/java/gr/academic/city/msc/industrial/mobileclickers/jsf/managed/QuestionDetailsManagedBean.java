/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.academic.city.msc.industrial.mobileclickers.jsf.managed;

import gr.academic.city.msc.industrial.mobileclickers.ejb.session.QuestionService;
import gr.academic.city.msc.industrial.mobileclickers.entity.Question;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Ivo Neskovic <ivo.neskovic@gmail.com>
 */
@ManagedBean(name = "questionDetailsManagedBean")
@SessionScoped
public class QuestionDetailsManagedBean implements Serializable {

    @EJB
    private QuestionService questionService;
    private long questionID;
    private Question question;

    /** Creates a new instance of QuestionDetailsManagedBean */
    public QuestionDetailsManagedBean() {
    }

    public long getQuestionID() {
        return questionID;
    }

    public void setQuestionID(long questionID) {
        this.questionID = questionID;
        question = questionService.getQuestionFromID(questionID);
    }

    public Question getQuestion() {
        question = questionService.getQuestionFromID(questionID);
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
