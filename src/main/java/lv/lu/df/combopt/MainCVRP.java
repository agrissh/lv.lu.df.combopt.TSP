package lv.lu.df.combopt;

import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.config.solver.SolverConfig;
import ai.timefold.solver.core.config.solver.termination.TerminationConfig;
import lv.lu.df.combopt.domain.*;
import lv.lu.df.combopt.solver.CVRPcost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MainCVRP {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainCVRP.class);
    public static void main(String[] args) {
        LOGGER.info("CVRP Optimizer started");

        //CVRPsolution problem = getSimpleProblem();
        //CVRPsolution problem = CVRPsolution.readFromCSV("data/matrix_4.csv","data/customers_1.csv");
        //CVRPsolution problem2 = CVRPsolution.readFromCSV("data/matrix_4.csv","data/customers_2.csv");
        //CVRPsolution problem3 = CVRPsolution.readFromCSV("data/matrix_4.csv","data/customers_3.csv");
        CVRPsolution problem4 = CVRPsolution.readFromCSV("data/matrix_4.csv","data/customers_4.csv");

        SolverFactory<CVRPsolution> solverFactory = SolverFactory.create(
                new SolverConfig()
                        .withSolutionClass(CVRPsolution.class)
                        .withEntityClasses(CVRPvehicle.class, CVRPcustomer.class)
                        .withConstraintProviderClass(CVRPcost.class)
                        .withTerminationConfig(new TerminationConfig()
                                .withSecondsSpentLimit(10L))
        );

        Solver<CVRPsolution> solver = solverFactory.buildSolver();

        //CVRPsolution solution = solver.solve(problem);
        //solution.print();

        //CVRPsolution solution2 = solver.solve(problem2);
        //solution2.print();


        //CVRPsolution solution3 = solver.solve(problem3);
        //solution3.print();

        CVRPsolution solution4 = solver.solve(problem4);
        solution4.print();

        LOGGER.info("CVRP Optimizer ended");
    }

    public static CVRPsolution getSimpleProblem() {
        CVRPsolution problem = new CVRPsolution();
        problem.setSolutionId("TEST");

        CVRPlocation depot = new CVRPlocation();
        depot.setId("DEPOT");

        CVRPvehicle vehicle = new CVRPvehicle();
        vehicle.setPlateNr("Agris1");
        vehicle.setCapacity(12);
        vehicle.setDepot(depot);

        CVRPvehicle vehicle2 = new CVRPvehicle();
        vehicle2.setPlateNr("Agris2");
        vehicle2.setCapacity(4);
        vehicle2.setDepot(depot);

        CVRPcustomer c1 = new CVRPcustomer();
        c1.setName("C1");
        c1.setDemand(10);
        CVRPlocation loc1 = new CVRPlocation();
        loc1.setId("C1");
        c1.setLocation(loc1);


        CVRPcustomer c2 = new CVRPcustomer();
        c2.setName("C2");
        c2.setDemand(2);
        CVRPlocation loc2 = new CVRPlocation();
        loc2.setId("C2");
        c2.setLocation(loc2);

        CVRPcustomer c3 = new CVRPcustomer();
        c3.setName("C3");
        c3.setDemand(2);
        CVRPlocation loc3 = new CVRPlocation();
        loc3.setId("C3");
        c3.setLocation(loc3);

        CVRPcustomer c4 = new CVRPcustomer();
        c4.setName("C4");
        c4.setDemand(2);
        CVRPlocation loc4 = new CVRPlocation();
        loc4.setId("C4");
        c4.setLocation(loc4);

        loc1.getDistanceMap().put(loc1, 0.0);
        loc1.getDistanceMap().put(loc2, 1.0);
        loc1.getDistanceMap().put(loc3, 1.4);
        loc1.getDistanceMap().put(loc4, 1.0);
        loc1.getDistanceMap().put(depot, 0.0);

        depot.getDistanceMap().put(loc1, 0.0);
        depot.getDistanceMap().put(loc2, 1.0);
        depot.getDistanceMap().put(loc3, 1.4);
        depot.getDistanceMap().put(loc4, 1.0);
        depot.getDistanceMap().put(depot, 0.0);

        loc2.getDistanceMap().put(loc1, 1.1);
        loc2.getDistanceMap().put(loc2, 0.0);
        loc2.getDistanceMap().put(loc3, 1.0);
        loc2.getDistanceMap().put(loc4, 1.4);
        loc2.getDistanceMap().put(depot, 1.1);

        loc3.getDistanceMap().put(loc1, 1.4);
        loc3.getDistanceMap().put(loc2, 1.1);
        loc3.getDistanceMap().put(loc3, 0.0);
        loc3.getDistanceMap().put(loc4, 1.0);
        loc3.getDistanceMap().put(depot, 1.4);

        loc4.getDistanceMap().put(loc1, 1.0);
        loc4.getDistanceMap().put(loc2, 1.4);
        loc4.getDistanceMap().put(loc3, 1.1);
        loc4.getDistanceMap().put(loc4, 0.0);
        loc4.getDistanceMap().put(depot, 1.0);

        problem.getVehicleList().add(vehicle);
        problem.getVehicleList().add(vehicle2);
        problem.getCustomerList().addAll(List.of(c1,c2,c3,c4));
        problem.getLocationList().addAll(List.of(depot, loc1, loc2, loc3, loc4));

        return problem;
    }
}
