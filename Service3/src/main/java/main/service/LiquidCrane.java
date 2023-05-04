package main.service;

import  main.entity.*;
import static main.service.Service3.inProgressLiquid;

class LiquidCrane extends Crane
{
    protected LiquidCrane(int number)
    {
        super(number);
    }

    @Override
    public void run()
    {
        while (!isStopped)//todo: prevent infinite cycle
        {
            if (!shipName.isEmpty())
            {
                Ship ship = inProgressLiquid.get(shipName);
                if (ship != null)
                {
                    System.out.println("LiquidCraneThread " + number + " try to start unloading " + shipName +"  weight " + ship.getWeight());
                    Utils.pause((long) (ship.getUnloadMinutes() + ship.getDelayUnload()) * Constants.COEFFICIENT);
                    ship.setRemainingWeight(0);

                    System.out.println("LiquidCraneThread " + number + " end unloading " + shipName + " minutes = " + ship.getUnloadMinutes() +
                            " delay unload = "+ ship.getDelayUnload());
                } else {
                    System.out.println("Ship with name " + shipName + " not found");
                }
                setShip("");
            }
        }
    }
}