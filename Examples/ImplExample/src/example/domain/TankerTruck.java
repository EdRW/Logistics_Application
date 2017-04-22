package example.domain;

import example.common.CannotFitException;
import example.common.InvalidDataException;
import example.common.Point3D;
import example.common.TruckImplFactory;
import example.truck.Truck;

public class TankerTruck implements Truck {

    private Truck myTruck; // Delegate - will refer to some implementation object
    private static final double TANKER_LOAD_RATE_LIMIT = 2000.0;

    public TankerTruck(double lX, double lY, double lZ, double dX, double dY, double dZ,
            double spd, double mxSpd, double mlw) throws InvalidDataException {
        myTruck = TruckImplFactory.createTruck(lX, lY, lZ, dX, dY, dZ, spd, mxSpd, mlw);
    }

    public TankerTruck(Point3D loc, Point3D dest, double spd, double mxSpd, double mlw) throws InvalidDataException {
        this(loc.getX(), loc.getY(), loc.getZ(), dest.getX(), dest.getY(), dest.getZ(), spd, mxSpd, mlw);
    }

    public void load(double amount) throws InvalidDataException, CannotFitException {
        if (amount < 0) {
            throw new InvalidDataException("Negative load amount: " + amount);
        }
        if (amount > TANKER_LOAD_RATE_LIMIT) {
            throw new InvalidDataException("Loading " + amount + " at one time exceeds the TankerTruck load rate limit of " + TANKER_LOAD_RATE_LIMIT);
        }
        if ((myTruck.getCurrentLoadWeight() + amount) > myTruck.getMaxLoadWeight()) {
            throw new CannotFitException("Additional load of " + amount + " will make the load weight " +
                    (myTruck.getCurrentLoadWeight() + amount) + " which exceeds the max load weight of " + myTruck.getMaxLoadWeight());
        }

        setCurrentLoadWeight(myTruck.getCurrentLoadWeight() + amount);
    }

    public void unLoad(double amount) throws InvalidDataException, CannotFitException {
        myTruck.unLoad(amount);
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
            return "I am TankerTruck " + getIdentifier() + ".\n\tI am at " +
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
