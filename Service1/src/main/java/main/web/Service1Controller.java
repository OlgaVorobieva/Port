package main.web;

import main.entity.Ship;
import main.service.Service1;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/serviceone")
public class Service1Controller
{
    @GetMapping("/timetable")
    public ResponseEntity<ArrayList<Ship>> getTimeTable()
    {
        ArrayList<Ship> result = Service1.getTimeTable(10);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
