package example.truck;

import example.common.CannotFitException;
import example.common.IdentifiableImplFactory;
import example.common.InvalidDataException;
import example.common.MovableImplFactory;
import example.common.Point3D;
import example.identification.Identifiable;
import example.movement.Movable;

public class TruckImpl implements Truck {

    private double maxLoadWeight;
    private double currentLoadWeight;
    private Movable myMovable;
    private Identifiable myIdentity;

    public TruckImpl(double lX, double lY, double lZ, double dX, double dY, double dZ,
            double spd, double mxSpd, double mlw) throws InvalidDataException {
        myMovable = MovableImplFactory.createMovable(lX, lY, lZ, dX, dY, dZ, spd, mxSpd);
        myIdentity = IdentifiableImplFactory.createIdentifiable();
        maxLoadWeight = mlw;

    }

    public TruckImpl(Point3D loc, Point3D dest, double spd, double mxSpd, double mlw) throws InvalidDataException {
        this(loc.getX(), loc.getY(), loc.getZ(), dest.getX(), dest.getY(), dest.getZ(), spd, mxSpd, mlw);
    }

    public void load(double amount) throws InvalidDataException, CannotFitException {
        if (amount < 0) {
            throw new InvalidDataException("Negative load amount: " + amount);
        }

        if ((currentLoadWeight + amount) > maxLoadWeight) {
            throw new CannotFitException("Additional load of " + amount + " will make the load weight " +
                    (currentLoadWeight + amount) + " which exceeds the max load weight of " + maxLoadWeight);
        }

        currentLoadWeight += amount;
    }

    public void unLoad(double amount) throws InvalidDataException, CannotFitException {
        if (amount < 0) {
            throw new InvalidDataException("Negative unLoad amount: " + amount);
        }

        if ((currentLoadWeight - amount) < 0.0) {
            throw new CannotFitException("UnLoading " + amount + " will make the load weight negative: " +
                    (currentLoadWeight + amount));
        }

        currentLoadWeight -= amount;
    }

    public boolean atDestination() {
        return myMovable.atDestination();
    }

    public Point3D getDestination() {
        return myMovable.getDestination();
    }

    public double getDestinationX() {
        return myMovable.getDestinationX();
    }

    public double getDestinationY() {
        return myMovable.getDestinationY();
    }

    public double getDestinationZ() {
        return myMovable.getDestinationZ();
    }

    public double getMaxSpeed() {
        return myMovable.getMaxSpeed();
    }

    public double getSpeed() {
        return myMovable.getSpeed();
    }

    public void setDestination(double x, double y, double z) throws InvalidDataException {
        myMovable.setDestination(x, y, z);
    }

    public void setDestination(Point3D aPoint) throws InvalidDataException {
        myMovable.setDestination(aPoint);
    }

    public void setMaxSpeed(double ms) throws InvalidDataException {
        myMovable.setMaxSpeed(ms);
    }

    public void setSpeed(double s) throws InvalidDataException {
        myMovable.setSpeed(s);
    }

    public double distance(Point3D loc) throws InvalidDataException {
        return myMovable.distance(loc);
    }

    public double distance(double x, double y, double z) throws InvalidDataException {
        return myMovable.distance(x, y, z);
    }

    public Point3D getLocation() {
        return myMovable.getLocation();
    }

    public double getLocationX() {
        return myMovable.getLocationX();
    }

    public double getLocationY() {
        return myMovable.getLocationY();
    }

    public double getLocationZ() {
        return myMovable.getLocationZ();
    }

    public void setLocation(Point3D loc) throws InvalidDataException {
        myMovable.setLocation(loc);
    }

    public void setLocation(double x, double y, double z) throws InvalidDataException {
        myMovable.setLocation(x, y, z);
    }

    public double getMaxLoadWeight() {
        return maxLoadWeight;
    }

    public void setCurrentLoadWeight(double mlw) {
        maxLoadWeight = mlw;
    }

    public double getCurrentLoadWeight() {
        return currentLoadWeight;
    }

    public String getIdentifier() {
        return myIdentity.getIdentifier();
    }

    public void update(double millis) throws InvalidDataException {
        myMovable.update(millis);
    }
}
