/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.academic.city.msc.industrial.mobileclickers.ejb.exception;

/**
 *
 * @author Ivo Neskovic <ivo.neskovic@gmail.com>
 */
public class QuestionException extends Exception {

    /**
     * Creates a new instance of <code>QuestionException</code> without detail message.
     */
    public QuestionException() {
    }


    /**
     * Constructs an instance of <code>QuestionException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public QuestionException(String msg) {
        super(msg);
    }
}
