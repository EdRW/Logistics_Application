package example.movement;

import example.common.InvalidDataException;
import example.common.Point3D;
import example.location.Locatable;

public interface Movable extends Locatable {

    boolean atDestination();

    Point3D getDestination();

    double getDestinationX();

    double getDestinationY();

    double getDestinationZ();

    double getMaxSpeed();

    double getSpeed();

    void setDestination(double x, double y, double z) throws InvalidDataException;

    void setDestination(Point3D aPoint) throws InvalidDataException;

    void setMaxSpeed(double ms) throws InvalidDataException;

    void setSpeed(double s) throws InvalidDataException;

    void update(double millis) throws InvalidDataException;
}
