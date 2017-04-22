package example.location;

import example.common.InvalidDataException;
import example.common.Point3D;

public interface Locatable {

    double distance(Point3D loc) throws InvalidDataException;

    double distance(double x, double y, double z) throws InvalidDataException;

    Point3D getLocation();

    double getLocationX();

    double getLocationY();

    double getLocationZ();

    void setLocation(Point3D loc) throws InvalidDataException;

    void setLocation(double x, double y, double z) throws InvalidDataException;
}
