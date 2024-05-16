package lv.lu.df.combopt;

import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.config.solver.SolverConfig;
import ai.timefold.solver.core.config.solver.termination.TerminationConfig;
import lv.lu.df.combopt.domain.TSPsalesman;
import lv.lu.df.combopt.domain.TSPsolution;
import lv.lu.df.combopt.domain.TSPvisit;
import lv.lu.df.combopt.solver.TSPcost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        LOGGER.info("TSP Optimizer started");

        TSPsolution problem = TSPsolution.readFromCSV("data/distance_matrix_1.csv");
        TSPsolution problem2 = TSPsolution.readFromCSV("data/distance_matrix_2.csv");
        TSPsolution problem3 = TSPsolution.readFromCSV("data/distance_matrix_3.csv");
        TSPsolution problem4 = TSPsolution.readFromCSV("data/matrix_4.csv");

        /* SolverFactory<TSPsolution> solverFactory = SolverFactory.create(
                new SolverConfig()
                        .withSolutionClass(TSPsolution.class)
                        .withEntityClasses(TSPsalesman.class, TSPvisit.class)
                        .withConstraintProviderClass(TSPcost.class)
                        .withTerminationConfig(new TerminationConfig()
                                .withSecondsSpentLimit(30L))
        );
        */

        SolverFactory<TSPsolution> solverFactoryFromXML = SolverFactory
                .createFromXmlResource("SolverConfig.xml");

        Solver<TSPsolution> solver = solverFactoryFromXML.buildSolver();
        TSPsolution solution = solver.solve(problem);
        LOGGER.info(solution.getSalesmanList().get(0).totalDistanceTraveled().toString());
        TSPsolution solution2 = solver.solve(problem2);
        LOGGER.info(solution2.getSalesmanList().get(0).totalDistanceTraveled().toString());
        TSPsolution solution3 = solver.solve(problem3);
        LOGGER.info(solution3.getSalesmanList().get(0).totalDistanceTraveled().toString());
        TSPsolution solution4 = solver.solve(problem4);
        LOGGER.info(solution4.getSalesmanList().get(0).totalDistanceTraveled().toString());

        //solution.print();

        LOGGER.info("TSP Optimizer ended");
    }

    public static TSPsolution getSimpleProblem() {
        TSPsolution problem = new TSPsolution();
        problem.setSolutionId("TEST");

        TSPsalesman salesman = new TSPsalesman();
        salesman.setName("Agris");

        TSPvisit v1 = new TSPvisit();
        v1.setName("V1");

        TSPvisit v2 = new TSPvisit();
        v2.setName("V2");

        TSPvisit v3 = new TSPvisit();
        v3.setName("V3");

        TSPvisit v4= new TSPvisit();
        v4.setName("V4");

        v1.getDistanceMap().put(v1, 0.0);
        v1.getDistanceMap().put(v2, 1.0);
        v1.getDistanceMap().put(v3, 1.4);
        v1.getDistanceMap().put(v4, 1.0);

        v2.getDistanceMap().put(v1, 1.1);
        v2.getDistanceMap().put(v2, 0.0);
        v2.getDistanceMap().put(v3, 1.0);
        v2.getDistanceMap().put(v4, 1.4);

        v3.getDistanceMap().put(v1, 1.4);
        v3.getDistanceMap().put(v2, 1.1);
        v3.getDistanceMap().put(v3, 0.0);
        v3.getDistanceMap().put(v4, 1.0);

        v4.getDistanceMap().put(v1, 1.0);
        v4.getDistanceMap().put(v2, 1.4);
        v4.getDistanceMap().put(v3, 1.1);
        v4.getDistanceMap().put(v4, 0.0);

        problem.getSalesmanList().add(salesman);
        problem.getVisitList().addAll(List.of(v1,v2,v3,v4));

        return problem;
    }
}
