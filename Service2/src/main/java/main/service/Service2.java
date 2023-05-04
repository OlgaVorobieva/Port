package main.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import main.entity.ModelingResult;
import main.entity.Ship;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Service2
{
    public static String saveTimeTableJSON()
    {
        String resultJson = "";

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/serviceone/timetable";
        ResponseEntity<ArrayList<Ship>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null,  new ParameterizedTypeReference<ArrayList<Ship>>() {});
        if(responseEntity.getStatusCode() == HttpStatus.OK)
        {
            ArrayList<Ship> ships = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            //Set pretty printing of json
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            try
            {
                resultJson = objectMapper.writeValueAsString(ships);
            }
            catch (JsonProcessingException e)
            {
                e.printStackTrace();
            }

            try (FileWriter writer = new FileWriter(Constants.TIME_TABLE_JSON_TXT, false))
            {
                writer.write(resultJson);
                writer.flush();
            }
            catch (IOException ex)
            {

                System.out.println(ex.getMessage());
            }
        }
        return resultJson;
    }


    public static ArrayList<Ship> getTimeTableFromFile(String fileName)
    {
        StringBuilder builder = new StringBuilder();

        try (FileReader reader = new FileReader(fileName))
        {
            Scanner scan = new Scanner(reader);

            while (scan.hasNextLine())
            {
                builder.append(scan.nextLine());
            }

        }
        catch (IOException ex)
        {

            System.out.println(ex.getMessage());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        ArrayList<Ship> ships = new ArrayList<>();
        try
        {
            ships = objectMapper.readValue(builder.toString(), new TypeReference<ArrayList<Ship>>(){});
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        return ships;
    }

    public static void saveModelingResult(ModelingResult result)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        String resultJson = "";
        try
        {
            resultJson = objectMapper.writeValueAsString(result);
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }

        try(FileWriter writer = new FileWriter(Constants.FINAL_RESULT_JSON_TXT, false))
        {
            writer.write(resultJson);
            writer.flush();
        }
        catch(IOException ex)
        {

            System.out.println(ex.getMessage());
        }

    }
}