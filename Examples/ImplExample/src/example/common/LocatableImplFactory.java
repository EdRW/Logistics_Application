package example.common;

import example.location.Locatable;
import example.location.LocatableImpl;

public class LocatableImplFactory 
{
    // No error checking in here - let the Impl's do that - they should know now.
    
    // This one uses Point3D's
    public static Locatable createLocatable(Point3D loc) throws InvalidDataException
    {
        // Not much logic here - only one kind of Impl is available currently
        return new LocatableImpl(loc);
    }
    
    // Overloaded to use doubles
    public static Locatable createLocatable(double x, double y, double z) throws InvalidDataException
    {
        // Not much logic here - only one kind of Impl is available currently
        return new LocatableImpl(x,y,z);
    }
}
