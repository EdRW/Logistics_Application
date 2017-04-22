package truck;

import java.util.HashMap;
import truck.utils.AlreadyLoadedException;
import truck.utils.CannotLoadException;
import truck.utils.CannotUnloadException;
import truck.utils.InvalidArgumentException;

/**
 * A class representing a Truck.<br>
 * Usage example:
 * <pre>
 * Truck myTruck = new Truck(1000.0);
 * </pre>
 *
 * @author Chris Hield
 * @see java.util.HashMap
 * @since Version 1.0
 */

public class Truck
{

    /**
     * The HashMap<String, Container> "cargoArea" represents the Truck's cargo area. The String "key" is
     * the Container's "identifier", the "value" is the Container itself.
     */
    private HashMap<String, Container> cargoArea = new HashMap<String, Container>();

    /**
     * The Max Weight of the Truck. Specified in the Truck's constructor.
     *
     * @see #getMaxWeight()
     * @see #setMaxWeight(double maxWeightIn)
     */
    private double maxWeight;

    /**
     * The current Weight of the Truck. Matches the total weight of the Containers in the Truck's cargo area.
     *
     * @see #getCurrentWeight()
     * @see #setCurrentWeight(double maxWeightIn)
     */
    private double currentWeight;

    /**
     * Constructor for the Truck - needs a "double" Max Weight parameter.
     *
     * @param maxWgtIn the requested Max Weight of the Truck's contents.
     * @throws InvalidArgumentException if the Max Weight parameter is less than or equal to zero
     * @see #setMaxWeight(double maxWeightIn)
     */
    public Truck(double maxWgtIn) throws InvalidArgumentException
    {
        setMaxWeight(maxWgtIn);
    }

    /**
     * Adds the provided Container to the Truck's cargo area. Adding a container will add to the Truck's weight.
     *
     * @param c The Container to add to the cargo area
     * @throws InvalidArgumentException If the Container parameter is NULL
     * @throws AlreadyLoadedException   If the specified Container is already loaded onto the Truck
     * @throws CannotLoadException      If the specified Container weights more than the Truck can hold
     * @see #setCurrentWeight(double currentWeightIn)
     * @see #cargoArea
     */
    public void addContainer(Container c) throws InvalidArgumentException, AlreadyLoadedException, CannotLoadException
    {
        if (c == null)
        {
            throw new InvalidArgumentException("Null Container passed to 'addContainer(Container)'");
        }
        if (isLoaded(c.getIdentifier()))
        {
            throw new AlreadyLoadedException("Container " + c.getIdentifier() + " is already loaded!");
        }
        if ((getCurrentWeight() + c.getWeight()) > getMaxWeight())
        {
            throw new CannotLoadException("Adding Container " + c.getIdentifier() + " will overload the truck!");
        }

        cargoArea.put(c.getIdentifier(), c);
        setCurrentWeight(getCurrentWeight() + c.getWeight());
    }

    /**
     * Removes the Container with the specified ID from the Truck's cargo area.
     * Removing a container will reduce the Truck's weight.
     *
     * @param id The ID of the Container to remove from the cargo area
     * @throws InvalidArgumentException If Container ID parameter is NULL or empty
     * @throws CannotUnloadException      If the specified Container is not loaded on the Truck
     * @see #setCurrentWeight(double currentWeightIn)
     * @see #cargoArea
     * @return The Container that was removed
     */
    public Container removeContainer(String id) throws InvalidArgumentException, CannotUnloadException
    {
        if (id == null || id.length() == 0)
        {
            throw new InvalidArgumentException("Invalid Container ID to 'removeContainer(Container)':" + id);
        }
        if (!isLoaded(id))
        {
            throw new CannotUnloadException("Container " + id + " is NOT loaded!");
        }

        setCurrentWeight(getCurrentWeight() - cargoArea.get(id).getWeight());
        return cargoArea.remove(id);

    }

    /**
     * Returns true if the Container with the specified Id IS loaded on the Truck, false if it is not.
     *
     * @param id The ID of the Container to remove from the cargo area
     * @see #cargoArea
     * @return true if the Container with the specified Id IS loaded on the Truck, false if it is not.

     */
    public boolean isLoaded(String id)
    {
        return cargoArea.containsKey(id);
    }

    /**
     * Returns number of Containers loaded into the Truck's cargo area.
     *
     * @see #cargoArea
     * @return the number of Containers loaded into the Truck's cargo area.
     */
    public int getNumContainers()
    {
        return cargoArea.size();
    }

    /**
     * Returns total weight of Containers loaded into the Truck's cargo area.
     *
     * @see #getCurrentWeight()
     * @return total weight of Containers loaded into the Truck's cargo area.
     */
    public double getLoadedWeight()
    {
        return getCurrentWeight();
    }

    /**
     * Accessor for "maxWeight" - returns the "maxWeight" value.
     *
     * @see #maxWeight
     * @see #setMaxWeight(double maxWeightIn)
     * @return returns the "maxWeight" value.
     */
    public double getMaxWeight()
    {
        return maxWeight;
    }

    /**
     * Modifier for "maxWeight" - sets the "maxWeight" to the specified value.
     *
     * @see #maxWeight
     * @see #getMaxWeight()
     * @throws InvalidArgumentException      If the specified weight is less than or equal to zero.
     * @param maxWeightIn The requested new "maxWeight" value.
     */
    private void setMaxWeight(double maxWeightIn) throws InvalidArgumentException
    {
        if (maxWeightIn <= 0.0)
        {
            throw new InvalidArgumentException("Invalid Max. Weight: " + maxWeightIn);
        }

        maxWeight = maxWeightIn;
    }

    /**
     * Accessor for "currentWeight" - returns the "currentWeight" value.
     *
     * @see #currentWeight
     * @see #setCurrentWeight(double currentWeightIn)
     * @return returns the "currentWeight" value.
     */
    public double getCurrentWeight()
    {
        return currentWeight;
    }

    /**
     * Modifier for "currentWeight" - sets the "currentWeight" to the specified value.
     *
     * @see #currentWeight
     * @see #getCurrentWeight()
     * @throws InvalidArgumentException      If the specified weight is less zero OR the specified weight is greater than the max weight
     * @param currentWeightIn The requested new "maxWeight" value.
     */
    public void setCurrentWeight(double currentWeightIn) throws InvalidArgumentException
    {
        if (currentWeightIn < 0.0 || currentWeightIn > getMaxWeight())
        {
            throw new InvalidArgumentException("Invalid Current Weight: " + currentWeightIn);
        }
        currentWeight = currentWeightIn;
    }

}
