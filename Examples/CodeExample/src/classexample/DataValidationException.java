/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classexample;

/**
 *
 * @author hieldc
 */
class DataValidationException extends Exception {

    // When writing custom classes, DO NOT include a constructor that requires no
    // parameters. That would allow people to create an exception object with NO
    // ERROR MESSAGE at all - which is not a good thing. Just include ONE
    // constructor as you see below, that requires the creater to provide an
    // error message to describe the exception condition.
    public DataValidationException(String msg) {
        super(msg);
    }
}
