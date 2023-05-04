package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class Service2Application
{
    public static void main(String[] args)
    {

        SpringApplication app = new SpringApplication(Service2Application.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8082"));
        app.run(args);

        //SpringApplication.run(Service2Application.class, args);

    }
}
