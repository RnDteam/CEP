package Controllers;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Entities.Vehicle;
import Enums.VehicleType;

@RestController
public class VehicleController {

//    private static final String template = "Hello, %s!";
//    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/getVehivleType")
    public VehicleType vehivleType(@RequestParam(value="manufactur", defaultValue="BMW") String manufactur,@RequestParam(value="model", defaultValue="R3") String model,@RequestParam(value="year", defaultValue="2015") int year) {
        return new Vehicle(manufactur,model,year).getType();
    }
}
