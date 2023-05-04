package main.service;
import main.entity.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public abstract class CraneManager implements Runnable
{
    public final int PERFORMANCE = 1; // in min = 1 t
    protected int craneCount_;
    ConcurrentLinkedQueue<Ship> queue_;
    Map<String, Ship> inProgress_;
    ConcurrentLinkedQueue<Ship> processedQueue_;
    Ship.Type_of_cargo type_;
    int maxQueueLength_ = 0;

    public CraneManager(int craneCount, ConcurrentLinkedQueue<Ship> queue, Map<String, Ship> inProgressQueue, ConcurrentLinkedQueue<Ship> processedQueue)
    {
        craneCount_ = craneCount;
        queue_ = queue;
        inProgress_ = inProgressQueue;
        processedQueue_ = processedQueue;
    }

    protected abstract ArrayList<Crane> CreateCranes();

    @Override
    public void run()
    {
        ArrayList<Crane> cranes = CreateCranes();
        //int processTime = 0;
        Random random = new Random();

        LocalDateTime startRealTime = LocalDateTime.now();
        LocalDateTime endRealTime = startRealTime.plusSeconds((long)(Constants.PROCESS_HOURS*0.6));//?
        System.out.println("Type:" + type_ + " END Real TIME: " + endRealTime);

        while (endRealTime.isAfter(LocalDateTime.now()))
        {
            // check for unloaded ships
            ArrayList<String> toRemove = new ArrayList<>();

            for (ConcurrentHashMap.Entry<String, Ship> entry : inProgress_.entrySet())
            {
                if (entry.getValue().getRemainingWeight() <= 0)
                {
                    toRemove.add(entry.getKey());
                }
            }

            // remove unloaded ships from queue
            for (var name : toRemove)
            {
                Ship completedShip = inProgress_.remove(name);
                processedQueue_.add(completedShip);
                System.out.println("Ship " + name + " moved to Processed");
            }

            //assign available Cranes and move to inProgressQueue
            if (cranes.stream().anyMatch(Crane::isAvailable)) //crane available
            {
                Ship ship1 = queue_.poll();
                if(ship1 != null)
                {
                    long delay = Duration.between(ship1.getRealArrivalTime(), LocalDateTime.now()).toMillis() / Constants.COEFFICIENT;

                    System.out.println("Start CraneManager " + type_ + " processing for Ship " + ship1.getName() + " delayStart (min) " + delay);

                    ship1.setStartDateTime(ship1.getModelingArrivalTime().plusMinutes(delay));
                    ship1.setRemainingWeight(ship1.getWeight());
                    ship1.setDelayUnload(random.nextInt(Constants.UNLOAD_DELAY_MINUTES));//время задержки окончания разгрузки судна

                    inProgress_.put(ship1.getName(), ship1);

                    List<Crane> freeCranes = cranes.stream().filter(Crane::isAvailable).collect(Collectors.toList());

                    int normalUnload = ship1.getWeight() / PERFORMANCE;
                    ship1.setUnloadMinutes(freeCranes.size() > 1 ? normalUnload / 2 : normalUnload);
                    freeCranes.get(0).setShip(ship1.getName());

                    if (freeCranes.size() > 1) // 2 cranes can be assigned
                    {
                        freeCranes.get(1).setShip(ship1.getName());
                    }
                }
                // queueLengthBag.Add(dryQueue.Count);
            }
            if(queue_.size() > maxQueueLength_)
            {
                maxQueueLength_ = queue_.size();
            }
         }

        for (Crane crane : cranes)
        {
            crane.setIsStopped();
        }
    }
}
