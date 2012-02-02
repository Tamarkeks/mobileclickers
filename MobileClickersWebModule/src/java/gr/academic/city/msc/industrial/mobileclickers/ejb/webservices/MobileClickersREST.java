/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.academic.city.msc.industrial.mobileclickers.ejb.webservices;

import gr.academic.city.msc.industrial.mobileclickers.ejb.exception.QuestionException;
import gr.academic.city.msc.industrial.mobileclickers.ejb.session.QuestionService;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Ivo Neskovic <ineskovic@6pmplc.com>
 */
@Stateless
@Path("question")
public class MobileClickersREST {

    @EJB
    private QuestionService questionService;

    @POST
    @Path("{questionCode}/{answer}/{uniqueSubmissionCode}")
    public Response submitAnswer(@PathParam("questionCode") String questionCode, @PathParam("answer") String answer, @PathParam("uniqueSubmissionCode") String uniqueSubmissionCode) {
        try {
            questionService.answerQuestion(questionCode, answer, uniqueSubmissionCode);
            return Response.ok().build();
        } catch (QuestionException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("{questionCode}")
    public Response getNumberOfAnswer(@PathParam("questionCode") String questionCode) {
        try {
            int count = questionService.getNumberOfAnswers(questionCode);
            return Response.ok().entity(Integer.toString(count)).build();
        } catch (QuestionException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
}
