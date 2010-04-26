/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.academic.city.msc.industrial.mobileclickers.ejb.session;

import gr.academic.city.msc.industrial.mobileclickers.ejb.exception.QuestionException;
import gr.academic.city.msc.industrial.mobileclickers.entity.Answer;
import gr.academic.city.msc.industrial.mobileclickers.entity.AnsweredQuestion;
import gr.academic.city.msc.industrial.mobileclickers.entity.Course;
import gr.academic.city.msc.industrial.mobileclickers.entity.Lecturer;
import gr.academic.city.msc.industrial.mobileclickers.entity.Question;
import gr.academic.city.msc.industrial.mobileclickers.entity.SubmitedAnswer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Ivo Neskovic <ivo.neskovic@gmail.com>
 */
@Stateless
public class QuestionService {

    @PersistenceContext
    private EntityManager em;

    //answer sent from server in a (char letter, answer) tuple
    public void createQuestion(String questionText, Map<Character, String> possibleAnswers, char correctAnswer, long courseID) throws QuestionException {
        //checks
        if (questionText == null
                || questionText.equals("")
                || possibleAnswers.size() == 0) {
            throw new QuestionException("Please provide all details!");
        }

        Question question = new Question();

        question.setQuestion(questionText);

        List<Answer> answers = new ArrayList<Answer>();
        Set<Character> keys = possibleAnswers.keySet();

        Iterator it = keys.iterator();
        while (it.hasNext()) {
            Answer answer = new Answer();

            Character letter = (Character) it.next();

            answer.setLetter(letter);
            answer.setAnswerText(possibleAnswers.get(letter));

            answers.add(answer);

            if (correctAnswer == letter) {
                question.setCorrectAnswer(answer);
            }
        }

        question.setPossibleAnswers(answers);

        Course course = em.find(Course.class, courseID);
        question.setCourse(course);

        em.persist(question);
    }

    public String issueQuestion(long lecturerID, long questionID) throws QuestionException {
        if (lecturerID < 0
                || questionID < 0) {
            throw new QuestionException("Provide all details! (lecturer, question)");
        }

        Lecturer lecturer = em.find(Lecturer.class, lecturerID);
        Question question = em.find(Question.class, questionID);

        AnsweredQuestion answeredQuestion = new AnsweredQuestion();
        answeredQuestion.setIssueDate(new Date());
        answeredQuestion.setIssuer(lecturer);
        answeredQuestion.setQuestion(question);

        em.persist(answeredQuestion);

        answeredQuestion.setQuestionCode(answeredQuestion.getId().toString());
        em.persist(answeredQuestion);

        return answeredQuestion.getId().toString();
    }

    public List<Question> getQuestionsForCourse(long courseID) {
        return em.find(Course.class, courseID).getQuestions();
    }

    private String generateQuestionCode(long lecturerID, long questionID) {
        int code = (int) ((lecturerID + questionID + System.currentTimeMillis()) / 1000);
        return Integer.toString(code);
    }

    public void answerQuestion(String questionCode, String answerChar, String uniqueAnswerCode) {
        Query query = em.createQuery("SELECT a FROM AnsweredQuestion a WHERE a.questionCode = :questionCode", AnsweredQuestion.class);
        query.setParameter("questionCode", questionCode);

        AnsweredQuestion answeredQuestion = (AnsweredQuestion) query.getResultList().get(0);

        Question question = answeredQuestion.getQuestion();
        List<Answer> possibleAnswers = question.getPossibleAnswers();
        Answer answer = null;
        for (int i = 0; i < possibleAnswers.size(); i++) {
            if (possibleAnswers.get(i).getLetter() == answerChar.charAt(0)) {
                answer = possibleAnswers.get(i);
            }
        }

        //TODO: handle multiple answers!
        
        SubmitedAnswer submitedAnswer = new SubmitedAnswer();
        submitedAnswer.setAnswer(answer);
        submitedAnswer.setUniqueSubmissionCode(uniqueAnswerCode);

        em.persist(submitedAnswer);
        
        answeredQuestion.getSubmitedAnswers().add(submitedAnswer);

        em.merge(answeredQuestion);
    }

    public int getNumberOfAnswers(String questionCode) {
        Query query = em.createQuery("SELECT a FROM AnsweredQuestion a WHERE a.questionCode = :questionCode", AnsweredQuestion.class);
        query.setParameter("questionCode", questionCode);

        AnsweredQuestion answeredQuestion = (AnsweredQuestion) query.getResultList().get(0);
        Question question = answeredQuestion.getQuestion();

        return question.getPossibleAnswers().size();
    }
}
