package example.common;

import example.movement.Movable;
import example.movement.MovableImpl;

public class MovableImplFactory {
    // No error checking in here - let the Impl's do that - they should know now.
    
    // This one uses Point3D's
    public static Movable createMovable(Point3D loc, Point3D dest, double spd, double mxSpd) throws InvalidDataException {
        // Not much logic here - only one kind of Impl is available currently
        return new MovableImpl(loc, dest, spd, mxSpd);
    }

    // Overloaded to use doubles
    public static Movable createMovable(double lX, double lY, double lZ, double dX, double dY, double dZ,
            double spd, double mxSpd) throws InvalidDataException {
        // Not much logic here - only one kind of Impl is available currently
        return new MovableImpl(lX, lY, lZ, dX, dY, dZ, spd, mxSpd);
    }
}
