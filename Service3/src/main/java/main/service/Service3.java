package main.service;

import main.entity.*;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Service3
{
    static ConcurrentLinkedQueue<Ship>  bulkQueue = new ConcurrentLinkedQueue<Ship>();
    static ConcurrentLinkedQueue<Ship>  liquidQueue = new ConcurrentLinkedQueue<Ship>();
    static ConcurrentLinkedQueue<Ship>  containerQueue = new ConcurrentLinkedQueue<Ship>();

    static Map<String,Ship> inProgressBulk = new ConcurrentHashMap<>();
    static Map<String,Ship> inProgressLiquid = new ConcurrentHashMap<>();
    static Map<String,Ship> inProgressContainer = new ConcurrentHashMap<>();

    // public static ConcurrentBag<int> queueLengthBag = new ConcurrentBag<int>();
    static ConcurrentLinkedQueue<Ship> processedQueue = new ConcurrentLinkedQueue<>();

    public void simulate()
    {
        //ArrayList<Ship> ships = Service2.getTimeTableFromFile(Constants.TIME_TABLE_JSON_TXT);
/*        ResponseEntity<List<User>> responseEntity =
                restTemplate.exchange(
                        BASE_URL,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<User>>() {}
                );
        List<User> users = responseEntity.getBody();
        return users.stream()
                .map(User::getName)
                .collect(Collectors.toList());*/

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8082/servicetwo/timetable2/TimeTableJSON.txt";
        ResponseEntity<ArrayList<Ship>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null,  new ParameterizedTypeReference<ArrayList<Ship>>() {});
        if(responseEntity.getStatusCode() == HttpStatus.OK)
        {
            ArrayList<Ship> ships = responseEntity.getBody();

            int dryCount = 1;
            int liquidCount = 1;
            int containerCount = 1;

            var penalties = simulateForCranes(ships, dryCount, liquidCount, containerCount);
            while (penalties[0] > Constants.CRANE_COST || penalties[1] > Constants.CRANE_COST || penalties[2] > Constants.CRANE_COST)
            {
                if (penalties[0] > Constants.CRANE_COST)
                {
                    dryCount += 1;
                }
                if (penalties[1] > Constants.CRANE_COST)
                {
                    liquidCount += 1;
                }
                if (penalties[2] > Constants.CRANE_COST)
                {
                    containerCount += 1;
                }
                penalties = simulateForCranes(ships, dryCount, liquidCount, containerCount);

            }

            ModelingResult result = new ModelingResult(dryCount, liquidCount, containerCount, processedQueue.toArray(new Ship[processedQueue.size()]));
            //Service2.saveModelingResult(result);

            url = "http://localhost:8082/servicetwo/result";
            //HttpHeaders headers = new HttpHeaders();
            //headers.setContentType(MediaType.APPLICATION_JSON);

            restTemplate.postForObject(url, result, ResponseEntity.class);

        }
        else
        {
                System.out.println("Service2 Error: "+ responseEntity.getStatusCode());
        }

    }

    private double[] simulateForCranes(ArrayList<Ship> ships, int dryCount, int liquidCount, int containerCount)
    {
        try
        {
            cleanQueues();
            CalculateRealArrival(ships);

            System.out.println("Start Modeling *************************");
            Utils.printTimeTable(ships);

            LocalDateTime startRealDateTime = LocalDateTime.now();
            LocalDateTime endModelingDateTime = ships.get(0).getModelingArrivalTime().plusHours(Constants.PROCESS_HOURS);
            //System.out.println("Start Modeling datetime  " + startModelingDateTime);
            //System.out.println("End Modeling datetime  " + endModelingDateTime);

            ArrayList<Thread> craneManagers = GenerateCranesManager(dryCount, liquidCount, containerCount);

            /* ships arrival emulation */
            for(int i = 0; i < ships.size(); i++)
            {
                Ship currentShip = ships.get(i);

                currentShip.setRealArrivalTime(LocalDateTime.now());
                if(currentShip.getType() == Ship.Type_of_cargo.BULK)
                {
                    bulkQueue.add(currentShip);
                }
                else if(currentShip.getType() == Ship.Type_of_cargo.LIQUID)
                {
                    liquidQueue.add(currentShip);
                }
                else
                {
                    containerQueue.add(currentShip);
                }
                System.out.println("added " + currentShip.getType() +" ship named - " + currentShip.getName() +" Arrival modeling time - " + currentShip.getModelingArrivalTime());

                Duration duration;
                if (i < ships.size() - 1)
                {
//                    if (endModelingDateTime.isBefore(ships.get(i+1).getModelingArrivalTime()))
//                        break;
                    duration = Duration.between(currentShip.getModelingArrivalTime(), ships.get(i+1).getModelingArrivalTime());
                    System.out.println("pause to next ship " + duration.toMinutes());
                    Utils.pause(duration.toMinutes()*Constants.COEFFICIENT);
                }
            }

            craneManagers.get(0).join();
            craneManagers.get(1).join();
            craneManagers.get(2).join();


        }
        catch ( InterruptedException e)
        {
            e.printStackTrace();
        }

        var result = calculateModelingResult(dryCount, liquidCount, containerCount);
        return  result;
    }

    private double[] calculateModelingResult(int dryCount, int liquidCount, int containerCount)
    {
        System.out.println();
        System.out.println("Modeling Results *************************");
        System.out.println("Cranes: Bulk - "+ dryCount + " Liquid - "+ liquidCount + " Container - " + containerCount);

        System.out.println("Processed ");
        double bulkPenalty = 0, containerPenalty = 0, liquidPenalty = 0;

        for(Ship ship: processedQueue)
        {
            Duration duration = Duration.between(ship.getModelingArrivalTime(), ship.getStartDateTime());
            var delay = duration.toHours();
            //ship.EndDateTime = ship.StartDateTime.AddMinutes(ship.UnloadMinutes + ship.DelayUnload);
            ship.setPenalty(delay * Constants.PENALTY_HOUR);
            switch (ship.getType())
            {
                case BULK: bulkPenalty += ship.getPenalty();
                case LIQUID: liquidPenalty += ship.getPenalty();
                case CONTAINER: containerPenalty += ship.getPenalty();
            }
            System.out.println(ship.getName() +" type = "+ship.getType() + " ModelingArrival= " + ship.getModelingArrivalTime()+" Start= " + ship.getStartDateTime() +" Weight= "+ ship.getWeight()
                    + " Unload(min)= " + ship.getUnloadMinutes() +" UnloadDelay= " +ship.getDelayUnload() + " Hours= " + (ship.getUnloadMinutes() + ship.getDelayUnload()) / 60
                    + " Penalty= " + ship.getPenalty());
        }

        System.out.println("In Bulk Queue");
        printQueue(bulkQueue);
        System.out.println("In Liquid Queue");
        printQueue(liquidQueue);
        System.out.println("In Container Queue");
        printQueue(containerQueue);

        System.out.println("Not Processed Bulk");
        inProgressBulk.forEach((k, v) -> System.out.println(k +"  Real arrival " +v.getModelingArrivalTime() ));
        System.out.println("Not Processed Liquid");
        inProgressLiquid.forEach((k, v) -> System.out.println(k +"  Real arrival " +v.getModelingArrivalTime() ));
        System.out.println("Not Processed Container");
        inProgressContainer.forEach((k, v) -> System.out.println(k +"  Real arrival " +v.getModelingArrivalTime() ));

        System.out.println("Penalty: Bulk "+ bulkPenalty +" Liquid " + liquidPenalty +" Container "+ containerPenalty);
        //  System.out.println("Average Queue length " + queueLengthBag.Average());
        return new double[] {bulkPenalty, liquidPenalty, containerPenalty};
    }

    private void printQueue(ConcurrentLinkedQueue<Ship> queue) {
        for(Ship ship : queue)
        {
            System.out.println( ship.getName()+ " Arrival= "+ ship.getArrivalTime() +" RealArrival= " + ship.getModelingArrivalTime() + " Start= "
                    + ship.getStartDateTime() + " Weight= "+ ship.getWeight()+" Penalty= " + ship.getPenalty());
        }
    }

    private static void cleanQueues()
    {
        processedQueue.clear();
        bulkQueue.clear();
        liquidQueue.clear();
        containerQueue.clear();
       /* while (queueLengthBag.Count > 0)
        {
            queueLengthBag.TryTake(out _);
        }*/
        inProgressBulk.clear();
        inProgressContainer.clear();
        inProgressLiquid.clear();
    }

    private static void CalculateRealArrival(ArrayList<Ship> ships)
    {
        for(Ship ship: ships)
        {
            int delayDays =  ThreadLocalRandom.current().nextInt(Constants.MIN_ARRIVAL_DELAY, Constants.MAX_ARRIVAL_DELAY);
            ship.setModelingArrivalTime(ship.getArrivalTime().plusDays(delayDays));
            ship.setStartDateTime(ship.getModelingArrivalTime());
        }
        ships.sort(Comparator.comparing(Ship::getModelingArrivalTime));

    }

    private ArrayList<Thread> GenerateCranesManager(int dryCount, int liquidCount, int containerCount) throws InterruptedException
    {
        ArrayList<Thread> craneManagers = new ArrayList<>();

        Thread t = new Thread(new BulkCraneManager(dryCount, bulkQueue, inProgressBulk, processedQueue));
        craneManagers.add(t);

        Thread t2 = new Thread(new LiquidCraneManager(liquidCount, liquidQueue, inProgressLiquid, processedQueue));
        craneManagers.add(t2);

        Thread t3 = new Thread(new ContainerCraneManager(containerCount, containerQueue, inProgressContainer, processedQueue));
        craneManagers.add(t3);

        t.start();
        t2.start();
        t3.start();

       return craneManagers;
    }

}
