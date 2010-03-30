/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.academic.city.msc.industrial.mobileclickers.ejb.exception;

/**
 *
 * @author Ivo Neskovic <ivo.neskovic@gmail.com>
 */
public class AccountException extends Exception {

    /**
     * Creates a new instance of <code>AccountException</code> without detail message.
     */
    public AccountException() {
    }


    /**
     * Constructs an instance of <code>AccountException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public AccountException(String msg) {
        super(msg);
    }
}
