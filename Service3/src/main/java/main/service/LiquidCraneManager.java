package main.service;

import main.entity.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

class LiquidCraneManager extends CraneManager
{
    LiquidCraneManager(int craneCount, ConcurrentLinkedQueue<Ship> queue, Map<String,Ship> inProgressQueue,
                       ConcurrentLinkedQueue<Ship> processedQueue)
    {
        super(craneCount, queue, inProgressQueue, processedQueue);
        type_ = Ship.Type_of_cargo.LIQUID;
    }


    @Override
    protected ArrayList<Crane> CreateCranes()
    {
        ArrayList<Crane> cranes = new ArrayList<>();
        for (int i = 0; i < craneCount_; i++)
        {
            cranes.add(new LiquidCrane(i));
        }
        return cranes;
    }
}