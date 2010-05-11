/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.academic.city.msc.industrial.mobileclickers.ejb.session;

import gr.academic.city.msc.industrial.mobileclickers.ejb.exception.QuestionException;
import gr.academic.city.msc.industrial.mobileclickers.entity.Answer;
import gr.academic.city.msc.industrial.mobileclickers.entity.AnsweredQuestion;
import gr.academic.city.msc.industrial.mobileclickers.entity.ChartType;
import gr.academic.city.msc.industrial.mobileclickers.entity.Course;
import gr.academic.city.msc.industrial.mobileclickers.entity.Lecturer;
import gr.academic.city.msc.industrial.mobileclickers.entity.Question;
import gr.academic.city.msc.industrial.mobileclickers.entity.Status;
import gr.academic.city.msc.industrial.mobileclickers.entity.SubmitedAnswer;
import gr.academic.city.msc.industrial.mobileclickers.entity.Tag;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author Ivo Neskovic <ivo.neskovic@gmail.com>
 */
@Stateless
public class QuestionService {

    @PersistenceContext
    private EntityManager em;
    @EJB
    private SystemWideServices systemWideServices;

    //answer sent from server in a (char letter, answer) tuple
    public void createQuestion(String questionText, Map<Character, String> possibleAnswers, char correctAnswer, long courseID, List<String> tagStrings, String chartType) throws QuestionException {
        //checks
        if (questionText == null
                || questionText.equals("")
                || possibleAnswers.size() == 0
                || tagStrings.size() == 0) {
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

        Set<Tag> tags = new HashSet<Tag>();

        for (int i = 0; i < tagStrings.size(); i++) {
            String tagName = tagStrings.get(i);
            Tag tag = getTagFromName(tagName);

            if (tag == null) {
                tags.add(systemWideServices.createTag(tagName));
            } else {
                tags.add(tag);
            }
        }
        question.setTags(tags);

        question.setChart(ChartType.valueOf(chartType));

        em.persist(question);

        course.getQuestions().add(question);
        em.merge(course);
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
        answeredQuestion.setStatus(Status.ACTIVE);

        em.persist(answeredQuestion);

        answeredQuestion.setQuestionCode(answeredQuestion.getId().toString());
        em.persist(answeredQuestion);

        question.getAnsweredQuestions().add(answeredQuestion);
        em.merge(question);

        return answeredQuestion.getId().toString();
    }

    public List<Question> getQuestionsForCourse(long courseID) {
        return em.find(Course.class, courseID).getQuestions();
    }

    public List<Question> filterQuestions(long courseID, long tagID) {
        Course course = em.find(Course.class, courseID);
        Tag tag = em.find(Tag.class, tagID);

        List<Question> questions = new ArrayList<Question>();

        for (Question question : course.getQuestions()) {
            if (question.getTags().contains(tag)) {
                questions.add(question);
            }
        }
        return questions;
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

    public void generateImageStatisticsForQuestion(long issuedQuestionID) throws IOException {
        AnsweredQuestion answeredQuestion = em.find(AnsweredQuestion.class, issuedQuestionID);

        if (answeredQuestion.getStatus() == Status.ACTIVE) {
            List<SubmitedAnswer> submitedAnswers = answeredQuestion.getSubmitedAnswers();

            Map<Character, Integer> countedAnswers = countAnswers(submitedAnswers);
            float totalAnswers = submitedAnswers.size();

            Set<Character> keySet = countedAnswers.keySet();
            Iterator it = keySet.iterator();

            DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
            while (it.hasNext()) {
                Character next = (Character) it.next();
                categoryDataset.addValue((countedAnswers.get(next) / totalAnswers) * 100.0, next, "");
            }

            it = keySet.iterator();
            DefaultPieDataset pieDataset = new DefaultPieDataset();
            while(it.hasNext()) {
                Character next = (Character) it.next();
                pieDataset.setValue(next, (countedAnswers.get(next) / totalAnswers) * 100.0);
            }

            JFreeChart chart = null;
            switch (answeredQuestion.getQuestion().getChart()) {
                case BAR:
                    chart = ChartFactory.createBarChart("Submited Answers", "Options", "Answers (%)", categoryDataset, PlotOrientation.VERTICAL, true, true, false);
                    break;
                case BAR3D:
                    chart = ChartFactory.createBarChart3D("Submited Answers", "Options", "Answers (%)", categoryDataset, PlotOrientation.VERTICAL, true, true, false);
                    break;
                case PIE:
                    chart = ChartFactory.createPieChart("Submited Answers", pieDataset, true, true, false);
                    break;
                case PIE3D:
                    chart = ChartFactory.createPieChart3D("Submited Answers", pieDataset, true, true, false);
                    break;
                case LINE:
                    chart = ChartFactory.createLineChart("Submited Answers", "Options", "Answers (%)", categoryDataset, PlotOrientation.VERTICAL, true, true, false);
                    break;
            }

            byte[] imageData = ChartUtilities.encodeAsPNG(chart.createBufferedImage(500, 270));

            String fileSeparator = System.getProperty("file.separator");
            //FileOutputStream fos = new FileOutputStream(new File("images" + fileSeparator + "charts" + fileSeparator + answeredQuestion.getQuestionCode()));
            FileOutputStream fos = new FileOutputStream(new File("docroot" + fileSeparator + answeredQuestion.getQuestionCode() + ".png"));
            fos.write(imageData);
            fos.flush();
            fos.close();
        }
    }

    private Map<Character, Integer> countAnswers(List<SubmitedAnswer> submitedAnswers) {
        Map<Character, Integer> countedAnswers = new HashMap<Character, Integer>();

        for (int i = 0; i < submitedAnswers.size(); i++) {
            SubmitedAnswer submitedAnswer = submitedAnswers.get(i);

            if (countedAnswers.get(submitedAnswer.getAnswer().getLetter()) == null) {
                countedAnswers.put(submitedAnswer.getAnswer().getLetter(), 1);
            } else {
                int occurence = countedAnswers.get(submitedAnswer.getAnswer().getLetter());
                countedAnswers.remove(submitedAnswer.getAnswer().getLetter());
                countedAnswers.put(submitedAnswer.getAnswer().getLetter(), occurence + 1);
            }
        }
        return countedAnswers;
    }

    private Tag getTagFromName(String tagName) {
        Query query = em.createQuery("SELECT t FROM Tag t where t.name = :tagName", Tag.class).setParameter("tagName", tagName);

        if (query.getResultList().size() == 0) {
            return null;
        } else {
            return (Tag) query.getResultList().get(0);
        }
    }

    public Question getQuestionFromID(long questionID) {
        return em.find(Question.class, questionID);
    }

    public void stopQuestion(long issuedQuestionID) {
        try {
            AnsweredQuestion answeredQuestion = em.find(AnsweredQuestion.class, issuedQuestionID);
            generateImageStatisticsForQuestion(answeredQuestion.getId());
            answeredQuestion.setStatus(Status.INACTIVE);
            em.merge(answeredQuestion);
        } catch (IOException ex) {
            Logger.getLogger(QuestionService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
