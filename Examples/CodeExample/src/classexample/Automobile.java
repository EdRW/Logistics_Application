/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classexample;

import java.awt.Point;


// The requirements of this class is to allow a user of the class to build an 
// Automobile object by providing the data you see in the constructor declaration.
//
// If any bad data is passed the the constructor it is expected that the Automobile
// creation will fail and an exception will be thrown.
//
// The only publically settable piece of data after an Automobile object has been
// created is the "speed" and the "location" - so they have public "set" methods.
// All the other "set" methods are private.
//
// There is a toString that creates and returns a String summary of the Automobile data.
//
public class Automobile {
    
    private String make; // ford, bmw, toyota, etc. Should never be null or empty
    private String model; // RAV4, focus, versa, etc. Should never be null or empty
    private double maxSpeed; // Should never be less than zero.
    private double speed; // Should never be less than zero or greater than maxSpeed
    private Point location;// Should not be null
    
    public Automobile(String makeIn, String modelIn, double maxSpeedIn, double speedIn, Point locationIn) 
            throws DataValidationException {
        //
        // In this c'tor I pass ALL the parameters on to the related "set" method. 
        // The "set" method is the single point of modification. All the error 
        // checking and the setting logic will reside in the "set"s and no where else.
        // Each of these "set" methods will throw an exception if they are passed
        // bad data. 
        // Since the c'tor did not *create" the data we pass to the "set" methods,
        // the c'tor has no way to "fix" the data. So any exceptions from the "set"
        // methods are "propagated" - passed up the call stack back to the caller
        // of the c'tor. That is what the "throws DataValidationException" is for
        // above on the constructro declaration line.
        //
        setMake(makeIn);
        setModel(modelIn);
        setMaxSpeed(maxSpeedIn);
        setSpeed(speedIn);
        setLocation(locationIn);
    }
    
    private void setMake(String makeIn) throws DataValidationException {
        //
        // In this "set" method we verify that the String parameter is not null
        // and that it is not empty. If either occurs, we throw a DataValidationException.
        //
        // We do NOT try to catch the error here. You should only catch the error
        // if you are going to do something about the error situation. Since this
        // method did not *create" the data parameter, we have no knowledge of
        // how to fix the error. So any exceptions are "propagated" - passed up 
        // the call stack back to the caller (the c'tor). That is what the 
        // "throws DataValidationException" is for above on the method declaration line.
        //
        // This "set" method is "private" because once the "make" is set, it 
        // should not be publically re-settable.
        //
        if (makeIn == null || makeIn.isEmpty()) 
            throw new DataValidationException((makeIn == null ? "Null" : "Empty") + " String passed into Automobile.setMake(String)");
        make = makeIn;
    }
    
    private void setModel(String modelIn) throws DataValidationException {
        //
        // In this "set" method we verify that the String parameter is not null
        // and that it is not empty. If either occurs, we throw a DataValidationException.
        //
        // We do NOT try to catch the error here. You should only catch the error
        // if you are going to do something about the error situation. Since this
        // method did not *create" the data parameter, we have no knowledge of
        // how to fix the error. So any exceptions are "propagated" - passed up 
        // the call stack back to the caller (the c'tor). That is what the 
        // "throws DataValidationException" is for above on the method declaration line.
        //
        // This "set" method is "private" because once the "make" is set, it 
        // should not be publically re-settable.
        //
        if (modelIn == null || modelIn.isEmpty()) 
            throw new DataValidationException((modelIn == null ? "Null" : "Empty") + " String passed into Automobile.setModel(String)");
        model = modelIn;
    }
    
    private void setMaxSpeed(double maxSpeedIn) throws DataValidationException {
        //
        // In this "set" method we verify that the double parameter is not negative.
        // If it is, we throw a DataValidationException.
        //
        // We do NOT try to catch the error here. You should only catch the error
        // if you are going to do something about the error situation. Since this
        // method did not *create" the data parameter, we have no knowledge of
        // how to fix the error. So any exceptions are "propagated" - passed up 
        // the call stack back to the caller (the c'tor). That is what the 
        // "throws DataValidationException" is for above on the method declaration line.
        //
        // This "set" method is "private" because once the "make" is set, it 
        // should not be publically re-settable.
        //
        if (maxSpeedIn < 0) 
            throw new DataValidationException("Invalid Max. Speed value passed into Automobile.setMaxSpeed(double): " + maxSpeedIn);
        maxSpeed = maxSpeedIn;
    }
    
    public void setSpeed(double speedIn) throws DataValidationException {
        //
        // In this "set" method we verify that the double parameter is not negative,
        // and that it is not greater than the Max Speed of the vehicle. If it is, 
        // we throw a DataValidationException.
        //
        // We do NOT try to catch the error here. You should only catch the error
        // if you are going to do something about the error situation. Since this
        // method did not *create" the data parameter, we have no knowledge of
        // how to fix the error. So any exceptions are "propagated" - passed up 
        // the call stack back to the caller (the c'tor). That is what the 
        // "throws DataValidationException" is for above on the method declaration line.
        //
        // This "set" method is "public" because it can be called publically to 
        // change the speed of the Automobile at any time.
        //
        if (speedIn < 0 || speedIn > getMaxSpeed()) 
            throw new DataValidationException((speedIn < 0 ? "Negative" : "Speed greater than Max Speed") + " passed into Automobile.setSpeed(double): " + speedIn);
        speed = speedIn;
    } 
    
    public void setLocation(Point locationIn) throws DataValidationException {
        //
        // In this "set" method we verify that the Point parameter is not null. 
        // If it is, we throw a DataValidationException.
        //
        // We do NOT try to catch the error here. You should only catch the error
        // if you are going to do something about the error situation. Since this
        // method did not *create" the data parameter, we have no knowledge of
        // how to fix the error. So any exceptions are "propagated" - passed up 
        // the call stack back to the caller (the c'tor). That is what the 
        // "throws DataValidationException" is for above on the method declaration line.
        //
        // This "set" method is "public" because it can be called publically to 
        // change the location of the Automobile at any time.
        //
        // Note that we do not *directly* set the Point reference passed in to our
        // "location" reference. Since a Point is an object reference, then some 
        // external entity passed in a reference to an existing Point object as 
        // the parameter. We DO NOT want external entities holding refefrences to
        // our data members so we set "location" to a COPY of the Point passed in.
        //
        if (locationIn == null) 
            throw new DataValidationException("Null Location Point passed into Automobile.setLocation(Point)");
        location = new Point(locationIn); // Make a copy!
    }
    
    public String getMake() {
        // This "get" accessor is public - Strings and primitive types are 
        // safe to publically return.
        return make;
    }

    public String getModel() {
        // This "get" accessor is public - Strings and primitive types are 
        // safe to publically return.
        return model;
    }

    public double getMaxSpeed() {
        // This "get" accessor is public - Strings and primitive types are 
        // safe to publically return.
        return maxSpeed;
    }

    public double getSpeed() {
        // This "get" accessor is public - Strings and primitive types are 
        // safe to publically return.
        return speed;
    }

    private Point getLocation() {
        // This "get" accessor is private - we DO NOT ever want to release a reference
        // to an object we own publically because then it can be modified without our knowledge.
        // This "get" method is for "internal" use only.
        return location;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Automobile Make: %s, Model:  %s", getMake(), getModel()));
        sb.append(String.format(", Current Speed: %.1f, Max Speed:  %.1f", getSpeed(), getMaxSpeed()));
        sb.append(String.format(", Current Location: (%d, %d)", getLocation().x, getLocation().y));
        
        return sb.toString();
        
    }
}
