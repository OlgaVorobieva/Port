package main.web;

import main.entity.ModelingResult;
import main.entity.Ship;
import main.service.Service2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
@RequestMapping("/servicetwo")
public class Service2Controller
{
    @GetMapping("/timetable")
    public ResponseEntity<String> saveTimeTableJSON()
    {
        try
        {
            Service2.saveTimeTableJSON();
            return new ResponseEntity<>("Okay", HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/timetable2/{fileName}")
    public ResponseEntity<ArrayList<Ship>> getTimeTableFromFile(@PathVariable("fileName") String fileName)
    {
        try
        {
            ArrayList<Ship> result = Service2.getTimeTableFromFile(fileName);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping(value = "/result",consumes = "application/json")
    public ResponseEntity<String> saveResult(@RequestBody ModelingResult result)
    {
        try
        {
            Service2.saveModelingResult(result);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
