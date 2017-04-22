package implementors;

import truck.Container;
import truck.utils.InvalidArgumentException;


/**
 * A class representing a shipping Crate.<br>
 * Usage example:
 * <pre>
 * Crate myCrate = new Crate("ABC123", 150.0);
 * </pre>
 *
 * @author Chris Hield
 * @since Version 1.0
 */
public class Crate implements Container
{
    /**
     * The identifier of the Crate. Specified in the Crate's constructor.
     *
     * @see #getIdentifier() 
     * @see #setIdentifier(java.lang.String) 
     */
    private String identifier;
    
    /**
     * The weight of the Crate. Specified in the Crate's constructor.
     *
     * @see #getWeight() 
     * @see #setWeight(double) 
     */
    private double weight;

    
    /**
     * Constructor for the Crate - needs a "String" identifier and a "double" Max Weight parameter.
     *
     * @param idIn the requested Crate identifier.
     * @param wgtIn the requested weight of the Crate.
     * @throws InvalidArgumentException if the identifier or the weight passed in is invalid
     * @see #setIdentifier(java.lang.String) 
     * @see #setWeight(double) 
     */
    public Crate(String idIn, double wgtIn) throws InvalidArgumentException
    {
        setIdentifier(idIn);
        setWeight(wgtIn);
    }
    
    /**
     * Returns the String identifier.
     *
     * @see #identifier
     * @see #setIdentifier(java.lang.String) 
     */
    public String getIdentifier()
    {
        return identifier;
    }

    /**
     * Accepts a new String identifier value and uses it to set the Container's identifier.
     *
     * @param idIn The new String identifier
     * @throws InvalidArgumentException If the identifier value provided is null or empty
     * @see #identifier
     * @see #getIdentifier() 
     */
    public final void setIdentifier(String idIn) throws InvalidArgumentException
    {
        if (idIn == null || idIn.isEmpty())
            throw new InvalidArgumentException("Invalid (empty or null) value passed to Crate 'setIdentifier'");
        identifier = idIn;
    }

    /**
     * Returns the double weight of the Container.
     *
     * @see #setWeight(double) 
     * @see #weight
     */
    public double getWeight()
    {
        return weight;
    }

    /**
     * Accepts a new double value that is used to set the weight of the Container.
     *
     * @param weightIn The new double weight of the Container
     * @throws InvalidArgumentException If the weight value provided is zero or less
     * @see #weight
     * @see #getWeight() 
     */
    public final void setWeight(double weightIn) throws InvalidArgumentException
    {
        if (weightIn <= 0.0)
            throw new InvalidArgumentException("Invalid (zero or less) value passed to Crate 'setWeight'");
        weight = weightIn;
    }
}
