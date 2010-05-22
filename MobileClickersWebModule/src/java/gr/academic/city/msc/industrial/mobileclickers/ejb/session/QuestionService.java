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
import gr.academic.city.msc.industrial.mobileclickers.entity.QuestionCodeGenerator;
import gr.academic.city.msc.industrial.mobileclickers.entity.Status;
import gr.academic.city.msc.industrial.mobileclickers.entity.SubmitedAnswer;
import gr.academic.city.msc.industrial.mobileclickers.entity.Tag;
import java.awt.Color;
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
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

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

    public String issueQuestion(long lecturerID, long questionID, long courseID) throws QuestionException {
        if (lecturerID < 0
                || questionID < 0
                || courseID < 0) {
            throw new QuestionException("Provide all details! (lecturer, question)");
        }

        Lecturer lecturer = em.find(Lecturer.class, lecturerID);
        Question question = em.find(Question.class, questionID);
        Course course = em.find(Course.class, courseID);

        AnsweredQuestion answeredQuestion = new AnsweredQuestion();
        answeredQuestion.setIssueDate(new Date());
        answeredQuestion.setIssuer(lecturer);
        answeredQuestion.setQuestion(question);
        answeredQuestion.setStatus(Status.ACTIVE);

        answeredQuestion.setQuestionCode(generateQuestionCode(lecturer, course));

        em.persist(answeredQuestion);

        question.getAnsweredQuestions().add(answeredQuestion);
        em.merge(question);

        return answeredQuestion.getQuestionCode();
    }

    public List<Question> getQuestionsForCourse(long courseID) throws QuestionException {
        List<Question> questions = em.find(Course.class, courseID).getQuestions();

        if (questions.size() == 0) {
            throw new QuestionException("Course does not have any questions!");
        }

        return questions;
    }

    public List<Question> filterQuestions(long courseID, long tagID) throws QuestionException {
        Course course = em.find(Course.class, courseID);
        Tag tag = em.find(Tag.class, tagID);

        if (course == null
                || tag == null) {
            throw new QuestionException("Course/Tag does not exist!");
        }

        List<Question> questions = new ArrayList<Question>();

        for (Question question : course.getQuestions()) {
            if (question.getTags().contains(tag)) {
                questions.add(question);
            }
        }
        return questions;
    }

    private String generateQuestionCode(Lecturer lecturer, Course course) {
        Query query = em.createQuery("SELECT q FROM QuestionCodeGenerator q WHERE q.lecturer = :lecturer AND q.course = :course", QuestionCodeGenerator.class);
        query.setParameter("lecturer", lecturer).setParameter("course", course);

        QuestionCodeGenerator questionCodeGenerator;
        if (query.getResultList().size() == 0) {
            //first time
            questionCodeGenerator = new QuestionCodeGenerator();
            questionCodeGenerator.setLecturer(lecturer);
            questionCodeGenerator.setCourse(course);
            questionCodeGenerator.setCounter(1);
            em.persist(questionCodeGenerator);
        } else {
            questionCodeGenerator = (QuestionCodeGenerator) query.getResultList().get(0);
            questionCodeGenerator.setCounter(questionCodeGenerator.getCounter() + 1);
            em.merge(questionCodeGenerator);
        }
        return "" + lecturer.getFirstName().charAt(0) + lecturer.getLastName().charAt(0) + course.getCode() + questionCodeGenerator.getCounter();
    }

    public void answerQuestion(String questionCode, String answerChar, String uniqueAnswerCode) throws QuestionException {
        if (questionCode == null
                || questionCode.equals("")
                || answerChar == null
                || answerChar.equals("")
                || uniqueAnswerCode == null
                || uniqueAnswerCode.equals("")) {
            throw new QuestionException("Please provide all details!");
        }


        Query query = em.createQuery("SELECT a FROM AnsweredQuestion a WHERE a.questionCode = :questionCode", AnsweredQuestion.class);
        query.setParameter("questionCode", questionCode);

        AnsweredQuestion answeredQuestion = (AnsweredQuestion) query.getResultList().get(0);

        if (!isSubmitedAnswerUnique(answeredQuestion, uniqueAnswerCode)) {
            throw new QuestionException("You have already answered this question!");
        }

        Question question = answeredQuestion.getQuestion();
        List<Answer> possibleAnswers = question.getPossibleAnswers();
        Answer answer = null;
        for (int i = 0; i < possibleAnswers.size(); i++) {
            if (possibleAnswers.get(i).getLetter() == answerChar.charAt(0)) {
                answer = possibleAnswers.get(i);
            }
        }

        SubmitedAnswer submitedAnswer = new SubmitedAnswer();
        submitedAnswer.setAnswer(answer);
        submitedAnswer.setUniqueSubmissionCode(uniqueAnswerCode);

        em.persist(submitedAnswer);

        answeredQuestion.getSubmitedAnswers().add(submitedAnswer);

        em.merge(answeredQuestion);
    }

    public int getNumberOfAnswers(String questionCode) throws QuestionException {
        Query query = em.createQuery("SELECT a FROM AnsweredQuestion a WHERE a.questionCode = :questionCode", AnsweredQuestion.class);
        query.setParameter("questionCode", questionCode);

        if (query.getResultList().size() == 0) {
            throw new QuestionException("Question with specified code does not exist!");
        }

        AnsweredQuestion answeredQuestion = (AnsweredQuestion) query.getResultList().get(0);
        Question question = answeredQuestion.getQuestion();

        return question.getPossibleAnswers().size();
    }

    public void generateStatisticsForIssuedQuestion(long issuedQuestionID) throws IOException {
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
            while (it.hasNext()) {
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
            new File("docroot" + fileSeparator + "charts" + fileSeparator).mkdirs();
            FileOutputStream fos = new FileOutputStream(new File("docroot" + fileSeparator + "charts" + fileSeparator + answeredQuestion.getQuestionCode() + ".png"));
            fos.write(imageData);
            fos.flush();
            fos.close();
        }
    }

    public byte[] getStatisticsForIssuedQuestion(String questionCode) throws IOException {
        Query query = em.createQuery("SELECT a FROM AnsweredQuestion a WHERE a.questionCode = :questionCode", AnsweredQuestion.class);
        query.setParameter("questionCode", questionCode);

        AnsweredQuestion answeredQuestion = (AnsweredQuestion) query.getResultList().get(0);

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
            while (it.hasNext()) {
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
            return imageData;
        } else {
            return answeredQuestion.getResultsChart();
        }
    }

    public byte[] generateStatisticsForQuestion(long questionID) throws IOException {
        Question question = em.find(Question.class, questionID);
        List<AnsweredQuestion> answeredQuestions = question.getAnsweredQuestions();

        TimeSeries ts = new TimeSeries("Correct Answers", Day.class);

        for (AnsweredQuestion answeredQuestion : answeredQuestions) {
            List<SubmitedAnswer> submitedAnswers = answeredQuestion.getSubmitedAnswers();
            List<SubmitedAnswer> correctAnswers = new ArrayList<SubmitedAnswer>();

            for (SubmitedAnswer submitedAnswer : submitedAnswers) {
                if (submitedAnswer.getAnswer().equals(question.getCorrectAnswer())) {
                    correctAnswers.add(submitedAnswer);
                }
            }
            double value;
            double correct = correctAnswers.size();
            double submited = submitedAnswers.size();
            if (submitedAnswers.size() == 0) {
                value = 0;
            } else {
                value = (correct / submited) * 100.0;
            }
            ts.addOrUpdate(new Day(answeredQuestion.getIssueDate()), value);
        }
        TimeSeriesCollection tsc = new TimeSeriesCollection(ts);
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Success Rate", "Date", "No. Correct Answers (%)", tsc, true, true, false);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(true);
            renderer.setBaseShapesFilled(true);
        }

        //DateAxis axis = (DateAxis) plot.getDomainAxis();
        //axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));

        byte[] imageData = ChartUtilities.encodeAsPNG(chart.createBufferedImage(500, 270));

        return imageData;
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

    public void stopQuestion(long issuedQuestionID) throws QuestionException {
        try {
            AnsweredQuestion answeredQuestion = em.find(AnsweredQuestion.class, issuedQuestionID);

            if (answeredQuestion == null
                    || answeredQuestion.getStatus() == Status.INACTIVE) {
                throw new QuestionException("Question already stoped!");
            }

            answeredQuestion.setResultsChart(getStatisticsForIssuedQuestion(answeredQuestion.getQuestionCode()));
            answeredQuestion.setStatus(Status.INACTIVE);
            em.merge(answeredQuestion);
        } catch (IOException ex) {
            Logger.getLogger(QuestionService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isSubmitedAnswerUnique(AnsweredQuestion answeredQuestion, String uniqueAnswerCode) {
        List<SubmitedAnswer> submitedAnswers = answeredQuestion.getSubmitedAnswers();

        for (SubmitedAnswer submitedAnswer : submitedAnswers) {
            if (submitedAnswer.getUniqueSubmissionCode().equals(uniqueAnswerCode)) {
                return false;
            }
        }
        return true;
    }
}
