package main.service;

import main.entity.*;

import static main.service.Service3.inProgressBulk;


class BulkCrane extends Crane
{
    protected BulkCrane(int number)
    {
        super(number);
    }

    @Override
    public void run()//todo: prevent infinite cycle
    {
        while (!isStopped)
        {
            if (!shipName.isEmpty())
            {
                Ship ship = inProgressBulk.get(shipName);
                if (ship != null)
                {
                    System.out.println("BulkCraneThread " + number + " try to start unloading " + shipName +"  weight " + ship.getWeight());
                    Utils.pause((long) (ship.getUnloadMinutes() + ship.getDelayUnload()) * Constants.COEFFICIENT);
                    ship.setRemainingWeight(0);

                    System.out.println("BulkCraneThread " + number + " end unloading " + shipName + " minutes = " + ship.getUnloadMinutes() +
                            " delay unload = "+ ship.getDelayUnload());
                }
                else
                {
                    System.out.println("Ship with name " + shipName + " not found");
                }
                setShip("");
            }
        }
    }
}