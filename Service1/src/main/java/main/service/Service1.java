package main.service;

import main.entity.Ship;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Service1
{
    public static ArrayList<Ship> getTimeTable(int shipCount)
    {
        ArrayList<Ship> ships = new ArrayList<Ship>();

        LocalDateTime dateNow = LocalDateTime.now();
        LocalDateTime tempTime;
        Random random = new Random();

        for (int i = 0; i < shipCount; i++)
        {
            Ship tempShip;
            String tempName = "ship" + i;
            tempTime = dateNow.plusHours(random.nextInt(720));
            Ship.Type_of_cargo tempType = Ship.Type_of_cargo.getRandomType();
            if(tempType == Ship.Type_of_cargo.CONTAINER)
            {
                int tempCount =  ThreadLocalRandom.current().nextInt(1, 15);
                tempShip = new Ship(tempName, tempTime, Ship.Type_of_cargo.CONTAINER,tempCount);
            }
            else
            {
                int tempWeight = ThreadLocalRandom.current().nextInt(1000, 2000);
                tempShip = new Ship(tempName, tempTime, tempType, tempWeight);
            }

            ships.add(tempShip);
        }
        ships.sort(Comparator.comparing(Ship::getTime));

        Utils.printTimeTable(ships);

        return ships;
    }


}
