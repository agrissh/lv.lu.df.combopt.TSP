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
import lombok.Value;
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
@Getter @Setter @NoArgsConstructor
public class TSPsolution {
    private static final Logger LOGGER = LoggerFactory.getLogger(TSPsolution.class);
    private String solutionId;
    @PlanningScore
    private SimpleScore score;

    @PlanningEntityCollectionProperty
    private List<TSPsalesman> salesmanList = new ArrayList<>();

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "visits")
    private List<TSPvisit> visitList = new ArrayList<>();

    public void print() {
        salesmanList.forEach(s -> {
            LOGGER.info(s.getName() + ":");
            s.getVisitList().forEach(v -> LOGGER.info("    " + v.getName() + "->" + (v.getNext() != null ? v.getNext().getName() : "null") ));
        });
    }

    public static final TSPsolution readFromCSV(String path, Boolean assymetric) {
        TSPsolution solution = new TSPsolution();
        solution.setSolutionId(path);

        try {
            List<String> lines = Files.readAllLines(Paths.get(path));

            TSPsalesman salesman = new TSPsalesman();
            salesman.setName("Agris");
            solution.salesmanList.add(salesman);

            Map<String, TSPvisit> visits = new HashMap<>();
            for (String line: lines) {
                String[] items = line.split(",");
                String visitID = items[0] + items[1];
                TSPvisit visit = visits.get(visitID);
                if (visit == null) {
                    visit = new TSPvisit();
                    visit.setName(visitID);
                    visits.put(visitID, visit);
                    solution.visitList.add(visit);
                }

                String toVisitID = items[2] + items[3];
                TSPvisit toVisit = visits.get(toVisitID);
                if (toVisit == null) {
                    toVisit = new TSPvisit();
                    toVisit.setName(toVisitID);
                    visits.put(toVisitID, toVisit);
                    solution.visitList.add(toVisit);
                }

                Double distance = Double.parseDouble(items[4]);
                if (assymetric) {
                    visit.getDistanceMap().put(toVisit, distance);
                } else {
                    Double other_distance = toVisit.getDistanceMap().getOrDefault(visit, 0.0);
                    Double max_distance = Math.max(distance, other_distance);
                    visit.getDistanceMap().put(toVisit, max_distance);
                    toVisit.getDistanceMap().put(visit, max_distance);
                }
            }


        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        return solution;
    }

    public void writeToCSV(File file) {
        try {
            FileWriter f = new FileWriter(file);

            for (TSPsalesman s: this.salesmanList) {
                f.write(s.getName() + ",");
                for (TSPvisit v: s.getVisitList()) {
                    f.write(v.getName() + ",");
                }
                f.write(System.lineSeparator());
            }

            f.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
