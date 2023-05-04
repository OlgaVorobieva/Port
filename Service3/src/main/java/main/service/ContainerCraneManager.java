package main.service;

import main.entity.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

class ContainerCraneManager extends CraneManager
{
    ContainerCraneManager(int craneCount, ConcurrentLinkedQueue<Ship> queue, Map<String,Ship> inProgressQueue,
                          ConcurrentLinkedQueue<Ship> processedQueue)
    {
        super(craneCount, queue, inProgressQueue, processedQueue);
        type_ = Ship.Type_of_cargo.CONTAINER;
    }


    @Override
    protected ArrayList<Crane> CreateCranes()
    {
        ArrayList<Crane> cranes = new ArrayList<>();
        for (int i = 0; i < craneCount_; i++)
        {
            cranes.add(new ContainerCrane(i));
        }
        return cranes;
    }
}