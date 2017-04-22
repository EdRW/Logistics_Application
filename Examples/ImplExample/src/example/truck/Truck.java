package example.truck;

import example.common.CannotFitException;
import example.common.InvalidDataException;
import example.identification.Identifiable;
import example.movement.Movable;

public interface Truck extends Movable, Identifiable {

    double getMaxLoadWeight();

    void setCurrentLoadWeight(double d);

    double getCurrentLoadWeight();

    void load(double amount) throws InvalidDataException, CannotFitException;

    void unLoad(double amount) throws InvalidDataException, CannotFitException;
}
