/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.academic.city.msc.industrial.mobileclickers.ejb.exception;

/**
 *
 * @author Ivo Neskovic <ivo.neskovic@gmail.com>
 */
public class CourseException extends Exception {

    /**
     * Creates a new instance of <code>CourseException</code> without detail message.
     */
    public CourseException() {
    }


    /**
     * Constructs an instance of <code>CourseException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public CourseException(String msg) {
        super(msg);
    }
}
