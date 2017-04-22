package example.common;

import example.truck.Truck;
import example.truck.TruckImpl;

public class TruckImplFactory {
    // No error checking in here - let the Impl's do that - they should know now.
    
    // This one uses Point3D's
    public static Truck createTruck(Point3D loc, Point3D dest, double spd, double mxSpd, double mlw) throws InvalidDataException {
        // Not much logic here - only one kind of Impl is available currently
        return new TruckImpl(loc, dest, spd, mxSpd, mlw);
    }

    // Overloaded to use doubles
    public static Truck createTruck(double lX, double lY, double lZ, double dX, double dY, double dZ,
            double spd, double mxSpd, double mlw) throws InvalidDataException {
        // Not much logic here - only one kind of Impl is available currently
        return new TruckImpl(lX, lY, lZ, dX, dY, dZ, spd, mxSpd, mlw);
    }
}
