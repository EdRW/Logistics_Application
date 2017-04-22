/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import example.common.CannotFitException;
import example.common.InvalidDataException;
import example.common.Point3D;
import example.domain.ContainerTruck;
import example.domain.StandardCargoTruck;
import example.domain.TankerTruck;
import example.truck.Truck;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hieldc
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<Truck> allTrucks = new ArrayList<Truck>();

        try {
            allTrucks.add(
                    new StandardCargoTruck(new Point3D(1.1, 2.2, 3.3), new Point3D(333.3, 222.2, 111.1), 55.0, 95.0, 10000.0));
            allTrucks.add(
                    new StandardCargoTruck(new Point3D(4.4, 5.5, 6.6), new Point3D(444.4, 555.5, 666.6), 38.0, 69.0, 8500.0));
            allTrucks.add(
                    new ContainerTruck(new Point3D(1.2, 3.4, 5.6), new Point3D(12.3, 45.6, 78.9), 43.0, 82.0, 8000.0));
            allTrucks.add(
                    new ContainerTruck(new Point3D(9.8, 7.6, 5.4), new Point3D(22.3, 33.4, 44.5), 57.0, 89.0, 6500.0));
            allTrucks.add(
                    new TankerTruck(new Point3D(100.0, 200.0, 300.0), new Point3D(900.0, 800.0, 700.0), 47.0, 75.0, 8000.0));
            allTrucks.add(
                    new TankerTruck(new Point3D(9.8, 7.6, 5.4), new Point3D(9.8, 7.6, 5.4), 22.0, 93.0, 7200.0));

        } catch (InvalidDataException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("\n\n1) Truck List:");
        for (Truck t : allTrucks) {
            System.out.println(t);
        }

        System.out.println("\n\n2) Loading cargo with weight 2250.0 into each truck:");
        for (Truck t : allTrucks) {
            try {
                System.out.println("Loading " + t.getIdentifier());
                t.load(2250.0);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println("Done. My load is now " + t.getCurrentLoadWeight());
        }

        System.out.println("\n\n3) Calling 'update' on all trucks & printing final status:");
        for (Truck t : allTrucks) {
            try {
                t.update(1500.0);
            } catch (InvalidDataException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(t);
        }

    }
}
