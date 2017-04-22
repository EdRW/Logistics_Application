package example.domain;

import example.common.CannotFitException;
import example.common.InvalidDataException;
import example.common.Point3D;
import example.common.TruckImplFactory;
import example.truck.Truck;

public class ContainerTruck implements Truck {

    private Truck myTruck; // Delegate - will refer to some implementation object

    public ContainerTruck(double lX, double lY, double lZ, double dX, double dY, double dZ,
            double spd, double mxSpd, double mlw) throws InvalidDataException {
        myTruck = TruckImplFactory.createTruck(lX, lY, lZ, dX, dY, dZ, spd, mxSpd, mlw);
    }

    public ContainerTruck(Point3D loc, Point3D dest, double spd, double mxSpd, double mlw) throws InvalidDataException {
        this(loc.getX(), loc.getY(), loc.getZ(), dest.getX(), dest.getY(), dest.getZ(), spd, mxSpd, mlw);
    }

    private void verifyLoadValue(double amount) throws InvalidDataException {
        if (amount != getMaxLoadWeight()) {
            throw new InvalidDataException("A Container Truck can only be *fully* loaded (" + getMaxLoadWeight() +
                    ")- not partially (" + amount + ")");
        }
    }

    public void load(double amount) throws InvalidDataException, CannotFitException {
        verifyLoadValue(amount);
        setCurrentLoadWeight(amount);
    }

    public void unLoad(double amount) throws InvalidDataException, CannotFitException {
        if (amount != getCurrentLoadWeight()) {
            throw new InvalidDataException("A Container Truck can only be *fully* unloaded - not ");
        }
        setCurrentLoadWeight(0.0);
    }

    public boolean atDestination() {
        return myTruck.atDestination();
    }

    public Point3D getDestination() {
        return myTruck.getDestination();
    }

    public double getDestinationX() {
        return myTruck.getDestinationX();
    }

    public double getDestinationY() {
        return myTruck.getDestinationY();
    }

    public double getDestinationZ() {
        return myTruck.getDestinationZ();
    }

    public double getMaxSpeed() {
        return myTruck.getMaxSpeed();
    }

    public double getSpeed() {
        return myTruck.getSpeed();
    }

    public void setDestination(double x, double y, double z) throws InvalidDataException {
        myTruck.setDestination(x, y, z);
    }

    public void setDestination(Point3D aPoint) throws InvalidDataException {
        myTruck.setDestination(aPoint);
    }

    public void setMaxSpeed(double ms) throws InvalidDataException {
        myTruck.setMaxSpeed(ms);
    }

    public void setSpeed(double s) throws InvalidDataException {
        myTruck.setSpeed(s);
    }

    public double distance(Point3D loc) throws InvalidDataException {
        return myTruck.distance(loc);
    }

    public double distance(double x, double y, double z) throws InvalidDataException {
        return myTruck.distance(x, y, z);
    }

    public Point3D getLocation() {
        return myTruck.getLocation();
    }

    public double getLocationX() {
        return myTruck.getLocationX();
    }

    public double getLocationY() {
        return myTruck.getLocationY();
    }

    public double getLocationZ() {
        return myTruck.getLocationZ();
    }

    public void setLocation(Point3D loc) throws InvalidDataException {
        myTruck.setLocation(loc);
    }

    public void setLocation(double x, double y, double z) throws InvalidDataException {
        myTruck.setLocation(x, y, z);
    }

    public String getIdentifier() {
        return myTruck.getIdentifier();
    }

    public double getMaxLoadWeight() {
        return myTruck.getMaxLoadWeight();
    }

    public void setCurrentLoadWeight(double d) {
        myTruck.setCurrentLoadWeight(d);
    }

    public double getCurrentLoadWeight() {
        return myTruck.getCurrentLoadWeight();
    }

    public void update(double millis) throws InvalidDataException {
        myTruck.update(millis);
    }

    public String toString() {
        try {
            return "I am ContainerTruck " + getIdentifier() + ".\n\tI am at " +
                    getLocation() + " and am heading to " + getDestination() +
                    ".\n\tMy load is " + getCurrentLoadWeight() + " and my max. load is " +
                    getMaxLoadWeight() + ".\n\tDistance to my destination is " +
                    String.format("%4.2f", distance(getDestination())) + ". " +
                    (atDestination() ? "I am there!" : "I'm not there yet");
        } catch (InvalidDataException ex) {
            return ex.getMessage();
        }
    }

}
