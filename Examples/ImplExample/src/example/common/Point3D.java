package example.common;

import java.awt.*;

public class Point3D extends Point.Double {

    public double z;

    public Point3D(double xIn, double yIn, double zIn) {
        setCoordinates(xIn, yIn, zIn);
    }

    public Point3D(double xIn, double yIn) {
        setCoordinates(xIn, yIn, 0.0);
    }

    public Point3D(Point3D aPoint) {
        setCoordinates(aPoint.getX(), aPoint.getY(), aPoint.getZ());
    }

    public Point3D() {
    }

    public double getZ() {
        return z;
    }

    public void setCoordinates(double xIn, double yIn, double zIn) {
        x = xIn;
        y = yIn;
        z = zIn;

    }

    public void setLocation(Point3D aPoint) {
        setCoordinates(aPoint);
    }

    public void setLocation(double xIn, double yIn, double zIn) {
        setCoordinates(xIn, yIn, zIn);
    }

    public void setCoordinates(Point3D aPoint) {
        setCoordinates(aPoint.getX(), aPoint.getY(), aPoint.getZ());
    }

    public String toString() {
        return String.format("[%-1.2f, %-1.2f, %-1.2f]", x, y, z);
    }

    public double distance(double xIn, double yIn, double zIn) {
        xIn -= getX();
        yIn -= getY();
        zIn -= getZ();

        return Math.sqrt(xIn * xIn + yIn * yIn + zIn * zIn);
    }

    public double distance(Point3D aPoint) {
        return distance(aPoint.getX(), aPoint.getY(), aPoint.getZ());
    }

    public boolean equals(Point3D aPoint3D) {
        if ((getX() == aPoint3D.getX()) &&
                (getY() == aPoint3D.getY()) &&
                (getZ() == aPoint3D.getZ())) {
            return true;
        } else {
            return false;
        }
    }
}
