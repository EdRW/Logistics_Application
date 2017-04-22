package example.movement;

import example.common.InvalidDataException;
import example.common.LocatableImplFactory;
import example.common.Point3D;
import example.location.Locatable;

public class MovableImpl implements Movable {

    private final Point3D destination = new Point3D();
    private double speed;
    private double maxSpeed;
    private Locatable myLocatable;

    public MovableImpl(Point3D loc, Point3D dest, double spd, double mxSpd) throws InvalidDataException {
        setLocatable(LocatableImplFactory.createLocatable(loc));
        setDestination(dest);
        setMaxSpeed(mxSpd);
        setSpeed(spd);
    }

    public MovableImpl(double lX, double lY, double lZ, double dX, double dY, double dZ,
            double spd, double mxSpd) throws InvalidDataException {
        setLocatable(LocatableImplFactory.createLocatable(new Point3D(lX, lY, lZ)));
        setDestination(dX, dY, dZ);
        setMaxSpeed(mxSpd);
        setSpeed(spd);
    }

    private void setLocatable(Locatable li) throws InvalidDataException {
        if (li == null) {
            throw new InvalidDataException("Null Locatable sent to setLocatable.");
        }
        myLocatable = li;
    }

    private Locatable getLocatable() {
        return myLocatable;
    }

    public Point3D getDestination() {
        if (destination == null) {
            return null;
        }
        return new Point3D(destination);
    }

    public double getDestinationX() {
        return destination.getX();
    }

    public double getDestinationY() {
        return destination.getY();
    }

    public double getDestinationZ() {
        return destination.getZ();
    }

    public void setDestination(double x, double y, double z) throws InvalidDataException {
        if (x < 0.0 || y < 0.0 || z < 0.0) {
            throw new InvalidDataException(
                    "Invalid X,Y,Z point sent to setDestination(x,y,z): (" + x + "," + y + "," + z + ")");
        }
        destination.setLocation(x, y, z);
    }

    public void setDestination(Point3D aPoint) throws InvalidDataException {
        if (aPoint == null) {
            throw new InvalidDataException("Null Point3D sent to setDestination(Point3D)");
        }
        setDestination(aPoint.getX(), aPoint.getY(), aPoint.getZ());
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double s) throws InvalidDataException {
        if (s < 0.0) {
            throw new InvalidDataException("Negative speed sent to setSpeed:" + s);
        }
        if (s > getMaxSpeed()) {
            throw new InvalidDataException("Attempt to set speed (" + s + ") greater than maxSpeed (" + getMaxSpeed() +
                    ") in setSpeed");
        }
        speed = s;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double ms) throws InvalidDataException {
        if (ms < 0.0) {
            throw new InvalidDataException("Negative maxSpeed sent to setMaxSpeed:" + ms);
        }
        if (ms < getSpeed()) {
            throw new InvalidDataException("Attempt to set maxSpeed less than speed in setMaxSpeed: " + ms);
        }
        maxSpeed = ms;
    }

    public boolean atDestination() {
        return myLocatable.getLocation().equals(destination);
    }

    // From Locatable interface
    public double distance(Point3D loc) throws InvalidDataException {
        return myLocatable.distance(loc);
    }

    public double distance(double x, double y, double z) throws InvalidDataException {
        return myLocatable.distance(x, y, z);
    }

    public Point3D getLocation() {
        return myLocatable.getLocation();
    }

    public double getLocationX() {
        return myLocatable.getLocationX();
    }

    public double getLocationY() {
        return myLocatable.getLocationY();
    }

    public double getLocationZ() {
        return myLocatable.getLocationZ();
    }

    public void setLocation(Point3D loc) throws InvalidDataException {
        myLocatable.setLocation(loc);
    }

    public void setLocation(double x, double y, double z) throws InvalidDataException {
        myLocatable.setLocation(x, y, z);
    }

    public void update(double millis) throws InvalidDataException {
        // This is a FAKE update method - NOT what you need for your project.
        double time = millis / 1000.0;

        double distanceTraveled = getSpeed() * time;
        double distance = getLocation().distance(getDestination());

        if (distance == 0.0) {
            return;
        }
        if (distanceTraveled >= distance) {
            setLocation(destination);
            System.out.println("\t*** I am at my destination!");
            return;
        }
        double delta = distanceTraveled / distance;

        double newX = getLocation().getX() + (getDestination().getX() - getLocation().getX()) * delta;
        double newY = getLocation().getY() + (getDestination().getY() - getLocation().getY()) * delta;
        double newZ = getLocation().getZ() + (getDestination().getZ() - getLocation().getZ()) * delta;

        getLocation().setLocation(newX, newY, newZ);

    }
}
