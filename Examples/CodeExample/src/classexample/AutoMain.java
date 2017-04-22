/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classexample;

import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hieldc
 */
public class AutoMain {

    public static void main(String[] args) {

        System.out.println("1) Create a valid Automobile, and print out the toString summary");
        try {
            Automobile myAuto = new Automobile("Ford", "Focus", 112.8, 37.4, new Point(13, 47));
            System.out.println("SUCCESS: " + myAuto);
        } catch (DataValidationException ex) {
            System.out.println("FAILURE: " + ex.getMessage());
        }

        System.out.println("\n2) Try to create an Automobile with a null Make");
        try {
            Automobile myAuto = new Automobile(null, "Focus", 112.8, 37.4, new Point(13, 47));
            System.out.println("FAILURE (Missed the Error): " + myAuto);
        } catch (DataValidationException ex) {
            System.out.println("SUCCESS (Trapped the Error): " + ex.getMessage());
        }

        System.out.println("\n3) Try to create an Automobile with an empty Make");
        try {
            Automobile myAuto = new Automobile("", "Focus", 112.8, 37.4, new Point(13, 47));
            System.out.println("FAILURE (Missed the Error): " + myAuto);
        } catch (DataValidationException ex) {
            System.out.println("SUCCESS (Trapped the Error): " + ex.getMessage());
        }

        System.out.println("\n4) Try to create an Automobile with a null Model");
        try {
            Automobile myAuto = new Automobile("Ford", null, 112.8, 37.4, new Point(13, 47));
            System.out.println("FAILURE (Missed the Error): " + myAuto);
        } catch (DataValidationException ex) {
            System.out.println("SUCCESS (Trapped the Error): " + ex.getMessage());
        }

        System.out.println("\n5) Try to create an Automobile with an empty Model");
        try {
            Automobile myAuto = new Automobile("Ford", "", 112.8, 37.4, new Point(13, 47));
            System.out.println("FAILURE (Missed the Error): " + myAuto);
        } catch (DataValidationException ex) {
            System.out.println("SUCCESS (Trapped the Error): " + ex.getMessage());
        }

        System.out.println("\n6) Try to create an Automobile with a negative Max Speed");
        try {
            Automobile myAuto = new Automobile("Ford", "Focus", -112.8, 37.4, new Point(13, 47));
            System.out.println("FAILURE (Missed the Error): " + myAuto);
        } catch (DataValidationException ex) {
            System.out.println("SUCCESS (Trapped the Error): " + ex.getMessage());
        }

        System.out.println("\n7) Try to create an Automobile with a negative Speed");
        try {
            Automobile myAuto = new Automobile("Ford", "Focus", 112.8, -37.4, new Point(13, 47));
            System.out.println("FAILURE (Missed the Error): " + myAuto);
        } catch (DataValidationException ex) {
            System.out.println("SUCCESS (Trapped the Error): " + ex.getMessage());
        }

        System.out.println("\n8) Try to create an Automobile with a Speed greated than the Max Speed");
        try {
            Automobile myAuto = new Automobile("Ford", "Focus", 112.8, 137.4, new Point(13, 47));
            System.out.println("FAILURE (Missed the Error): " + myAuto);
        } catch (DataValidationException ex) {
            System.out.println("SUCCESS (Trapped the Error): " + ex.getMessage());
        }

        System.out.println("\n9) Try to create an Automobile with a null Location");
        try {
            Automobile myAuto = new Automobile("Ford", "Focus", 112.8, 37.4, null);
            System.out.println("FAILURE (Missed the Error): " + myAuto);
        } catch (DataValidationException ex) {
            System.out.println("SUCCESS (Trapped the Error): " + ex.getMessage());
        }
        
        
        System.out.println("\n10) Change the Speed of an existing Automobile to a valid value");
        try {
            Automobile myAuto = new Automobile("Ford", "Focus", 112.8, 37.4, new Point(13, 47));
            System.out.println("Before Change: " + myAuto);
            myAuto.setSpeed(55.0);
            System.out.println("SUCCESS (Trapped the Error): " + myAuto);
        } catch (DataValidationException ex) {
            System.out.println(ex.getMessage());
        }
        
        System.out.println("\n11) Change the Speed of an existing Automobile to an invalid value");
        try {
            Automobile myAuto = new Automobile("Ford", "Focus", 112.8, 37.4, new Point(13, 47));
            System.out.println("Before Change: " + myAuto);
            myAuto.setSpeed(155.0);
            System.out.println("FAILURE (Missed the Error): " + myAuto);
        } catch (DataValidationException ex) {
            System.out.println("SUCCESS (Trapped the Error): " + ex.getMessage());
        }
        
        System.out.println("\n12) Change the Location of an existing Automobile to a null Point");
        try {
            Automobile myAuto = new Automobile("Ford", "Focus", 112.8, 37.4, new Point(13, 47));
            System.out.println("Before Change: " + myAuto);
            myAuto.setLocation(null);
            System.out.println("FAILURE (Missed the Error): " + myAuto);
        } catch (DataValidationException ex) {
            System.out.println("SUCCESS (Trapped the Error): " + ex.getMessage());
        } 

    }
}
