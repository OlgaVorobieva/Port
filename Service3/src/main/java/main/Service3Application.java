package main;

import main.service.Service3;

public class Service3Application
{
    public static void main(String[] args)
    {
        //SpringApplication.run(Service3Application.class, args);
        Service3 service3 = new Service3();
        service3.simulate();

    }
}
