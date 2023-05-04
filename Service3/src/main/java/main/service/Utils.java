package main.service;

import main.entity.Ship;
import java.util.ArrayList;

public class Utils
{
    public static void pause(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

    }

    public static void printTimeTable(ArrayList<Ship> ships)
    {
        for (Ship ship: ships)
        {
            System.out.println("Name: " + ship.getName()+" Time: " + ship.getArrivalTime() +" Real Time: " + ship.getModelingArrivalTime()
                    + " Type: " + ship.getType() +" Weight:  " +ship.getWeight() );
        }
    }
}
