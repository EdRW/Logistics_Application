package truck;

import truck.utils.InvalidArgumentException;

/** This interface will need to be implemented by any class that wants to be
 *  considered a "Container" object. This interface requires the implementor
 *  to support identification and weight data.
 */

public interface Container
{
    
    /**
     * Returns the String identifier.
     *
     * @see #setIdentifier(java.lang.String) 
     */
    public String getIdentifier();
    
    /**
     * Accepts a new String identifier value and uses it to set the Container's identifier.
     *
     * @param idIn The new String identifier
     * @throws InvalidArgumentException If the identifier value provided is null or empty
     * @see #getIdentifier() 
     */
    public void setIdentifier(String idIn) throws InvalidArgumentException;

    /**
     * Returns the double weight of the Container.
     *
     * @see #setWeight(double) 
     */
    public double getWeight();
    
    /**
     * Accepts a new double value that is used to set the weight of the Container.
     *
     * @param weightIn The new double weight of the Container
     * @throws InvalidArgumentException If the weight value provided is zero or less
     * @see #getWeight() 
     */
    public void setWeight(double weightIn) throws InvalidArgumentException;
}
