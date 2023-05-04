package main.entity;

public class ModelingResult
{
    private Ship[] processedList;
    private int bulkCraneCount;
    private int liquidCraneCount;
    private int containerCraneCount;
    private int processedCount;
    private double penaltySum = 0;
    private  int minDelay = 0;
    private  int maxDelay = 0;
    private int averageDelay = 0;
    private int averageQueueLength = 0; //todo: implement

    public ModelingResult()
    {
    }

    public ModelingResult(int bulk, int liquid, int container, Ship[] processed)
    {
        bulkCraneCount = bulk;
        liquidCraneCount = liquid;
        containerCraneCount = container;
        processedList = processed;
        processedCount = processedList.length;

        if (processedCount > 0)
        {
            int sumDelay = 0;
            minDelay = processedList[0].getDelayUnload();
            maxDelay = minDelay;
            for (Ship ship : processedList)
            {
                penaltySum += ship.getPenalty();
                sumDelay += ship.getDelayUnload();
                if (maxDelay < ship.getDelayUnload())
                {
                    maxDelay = ship.getDelayUnload();
                }
                if (minDelay > ship.getDelayUnload())
                {
                    minDelay = ship.getDelayUnload();
                }
            }
            averageDelay = sumDelay/processedCount;
        }
    }

    public int getBulkCraneCount()
    {
        return bulkCraneCount;
    }

    public void setBulkCraneCount(int bulkCraneCount)
    {
        this.bulkCraneCount = bulkCraneCount;
    }

    public int getLiquidCraneCount()
    {
        return liquidCraneCount;
    }

    public void setLiquidCraneCount(int liquidCraneCount)
    {
        this.liquidCraneCount = liquidCraneCount;
    }

    public int getContainerCraneCount()
    {
        return containerCraneCount;
    }

    public void setContainerCraneCount(int containerCraneCount)
    {
        this.containerCraneCount = containerCraneCount;
    }

    public Ship[] getProcessedList()
    {
        return processedList;
    }

    public int getProcessedCount()
    {
        return processedCount;
    }

    public double getPenaltySum()
    {
        return penaltySum;
    }

    public int getMinDelay()
    {
        return minDelay;
    }

    public int getMaxDelay()
    {
        return maxDelay;
    }

    public int getAverageDelay()
    {
        return averageDelay;
    }

    public int getAverageQueueLength()
    {
        return averageQueueLength;
    }

    public void setProcessedList(Ship[] processedList)
    {
        this.processedList = processedList;
    }

    public void setProcessedCount(int processedCount)
    {
        this.processedCount = processedCount;
    }

    public void setPenaltySum(double penaltySum)
    {
        this.penaltySum = penaltySum;
    }

    public void setMinDelay(int minDelay)
    {
        this.minDelay = minDelay;
    }

    public void setMaxDelay(int maxDelay)
    {
        this.maxDelay = maxDelay;
    }

    public void setAverageDelay(int averageDelay)
    {
        this.averageDelay = averageDelay;
    }

    public void setAverageQueueLength(int averageQueueLength)
    {
        this.averageQueueLength = averageQueueLength;
    }
}
