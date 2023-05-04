package main.service;

import main.entity.*;

import static main.service.Service3.inProgressContainer;


class ContainerCrane extends Crane
{
    protected ContainerCrane(int number)
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
                Ship ship = inProgressContainer.get(shipName);
                if (ship != null)
                {
                    System.out.println("ContainerCraneThread " + number + " try to start unloading " + shipName +"  count " + ship.getWeight());
                    Utils.pause((long) (ship.getUnloadMinutes() + ship.getDelayUnload()) * Constants.COEFFICIENT);
                    ship.setRemainingWeight(0);

                    System.out.println("ContainerCraneThread " + number + " end unloading " + shipName + " minutes = " + ship.getUnloadMinutes() +
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