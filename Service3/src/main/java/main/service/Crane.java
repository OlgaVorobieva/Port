package main.service;

public abstract class Crane implements Runnable
{
    protected String shipName;
    Thread myThread;
    protected int number;
    volatile boolean isStopped;

    protected Crane(int number)
    {
        shipName = "";
        this.number = number;
        isStopped = false;

        myThread = new Thread(this);
        myThread.start();
    }

    public synchronized boolean isAvailable()
    {
        return (shipName == null || shipName.isEmpty());
    }

    public synchronized void setShip(String shipName)
    {
        this.shipName = shipName;
    }

    public synchronized String getShip()
    {
        return shipName;
    }

    public void setIsStopped()
    {
        isStopped = true;
    }

    @Override
    public abstract void run();
}