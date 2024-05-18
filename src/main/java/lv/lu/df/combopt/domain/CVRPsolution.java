package lv.lu.df.combopt.domain;

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.buildin.simple.SimpleScore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PlanningSolution
@Getter
@Setter
@NoArgsConstructor
public class CVRPsolution {
    private static final Logger LOGGER = LoggerFactory.getLogger(CVRPsolution.class);
    private String solutionId;
    @PlanningScore
    private HardSoftScore score;

    @PlanningEntityCollectionProperty
    private List<CVRPvehicle> vehicleList = new ArrayList<>();

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "customers")
    private List<CVRPcustomer> customerList = new ArrayList<>();

    @ProblemFactCollectionProperty
    private List<CVRPlocation> locationList = new ArrayList<>();

    public void print() {
        LOGGER.info("TOTAL: " + this.totalDistance());
        vehicleList.forEach(v -> {
            LOGGER.info(v.getPlateNr() + "("+v.getCapacity()+", "+v.totalDistanceTraveled()+") :");
            v.getCustomerList().forEach(c -> LOGGER.info("    " + c.getName() + "("+c.getDemand()+", "+c.getVehicleVolume()+")->"
                    + (c.getNext() != null ? c.getNext().getName() : "null") ));
        });
    }

    public static final CVRPsolution readFromCSV(String matrix_path, String customer_path) {
        CVRPsolution solution = new CVRPsolution();
        solution.setSolutionId(customer_path);

        try {
            List<String> lines = Files.readAllLines(Paths.get(matrix_path));

            Map<String, CVRPlocation> locations = new HashMap<>();

            CVRPlocation depot = new CVRPlocation();
            locations.put("55.66816421.161899", depot);
            solution.getLocationList().add(depot);
            for (int i = 0; i < 11; i++) {
                CVRPvehicle vehicle = new CVRPvehicle();
                vehicle.setPlateNr("Agris" + i);
                vehicle.setCapacity(499);
                vehicle.setDepot(depot);
                solution.vehicleList.add(vehicle);
            }

            for (String line: lines) {
                String[] items = line.split(",");
                String locationID = items[0] + items[1];
                CVRPlocation location = locations.get(locationID);
                if (location == null) {
                    location = new CVRPlocation();
                    location.setId(locationID);
                    locations.put(locationID, location);
                    solution.locationList.add(location);
                }

                String toLocationID = items[2] + items[3];
                CVRPlocation toLocation = locations.get(toLocationID);
                if (toLocation == null) {
                    toLocation = new CVRPlocation();
                    toLocation.setId(toLocationID);
                    locations.put(toLocationID, toLocation);
                    solution.locationList.add(toLocation);
                }

                Double distance = Double.parseDouble(items[4]);
                location.getDistanceMap().put(toLocation, distance);
            }

            List<String> lines_cust = Files.readAllLines(Paths.get(customer_path));
            for (String line: lines_cust) {
                String[] items = line.split(",");

                String locationID = items[0].replaceAll("0+$", "").replaceAll("\\.$", "\\.0")
                        + items[1].replaceAll("0+$", "").replaceAll("\\.$", "\\.0");

                CVRPcustomer customer = new CVRPcustomer();
                solution.getCustomerList().add(customer);
                customer.setName(locationID);
                CVRPlocation loc = locations.get(locationID);
                customer.setLocation(loc);
                //LOGGER.info(locationID + " ? " + (loc == null ? "null" : loc.getId()));

                Integer demand = Integer.parseInt(items[2]);
                customer.setDemand(demand);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        return solution;
    }

    public Double totalDistance() {
        Double totalDistance = 0.0;
        for (CVRPvehicle v: vehicleList) {
            totalDistance = totalDistance + v.totalDistanceTraveled();
        }
        return totalDistance;
    }

    public void writeToCSV(File file) {
        try {
            FileWriter f = new FileWriter(file);

            for (CVRPvehicle v: this.vehicleList) {
                f.write(v.getPlateNr() + ",");
                for (CVRPcustomer c: v.getCustomerList()) {
                    f.write(c.getName() + ",");
                }
                f.write(System.lineSeparator());
            }

            f.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
