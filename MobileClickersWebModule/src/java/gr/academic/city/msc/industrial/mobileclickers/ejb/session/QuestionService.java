/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.academic.city.msc.industrial.mobileclickers.ejb.session;

import gr.academic.city.msc.industrial.mobileclickers.ejb.exception.QuestionException;
import gr.academic.city.msc.industrial.mobileclickers.entity.Answer;
import gr.academic.city.msc.industrial.mobileclickers.entity.Question;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ivo Neskovic <ivo.neskovic@gmail.com>
 */
@Stateless
public class QuestionService {
    @PersistenceContext
    private EntityManager em;

    //answer sent from server in a (char letter, answer) tuple
    public void createQuestion(String questionText, Map<Character, String> possibleAnswers, char correctAnswer) throws QuestionException {
        //checks
        if (questionText == null
                || questionText.equals("")
                || possibleAnswers.size() == 0) {
            throw new QuestionException ("Please provide all details!");
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

        em.persist(question);
    }
 
}
