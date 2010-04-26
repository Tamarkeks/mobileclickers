/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.academic.city.msc.industrial.mobileclickers.ejb.webservices;

import gr.academic.city.msc.industrial.mobileclickers.ejb.session.QuestionService;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author Ivo Neskovic <ivo.neskovic@gmail.com>
 */
@WebService()
public class MobileClickersWS {
    @EJB
    private QuestionService questionService;
    /**
     * Web service operation
     */
    @WebMethod(operationName = "submitAnswer")
    public void submitAnswer(@WebParam(name = "questionCode")
    String questionCode, @WebParam(name = "answer")
    String answer, @WebParam(name = "uniqueSubmissionCode")
    String uniqueSubmissionCode) {
        questionService.answerQuestion(questionCode, answer, uniqueSubmissionCode);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getNumberOfAnswer")
    public int getNumberOfAnswer(@WebParam(name = "questionCode")
    String questionCode) {
        return questionService.getNumberOfAnswers(questionCode);
    }

}
