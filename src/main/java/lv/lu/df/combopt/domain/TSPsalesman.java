package lv.lu.df.combopt.domain;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.variable.PlanningListVariable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@PlanningEntity
@Getter @Setter @NoArgsConstructor
public class TSPsalesman {

    private String name;

    @PlanningListVariable(valueRangeProviderRefs = {"visits"})
    private List<TSPvisit> visitList = new ArrayList<>();

    public Double totalDistanceTraveled() {
        Double totalDistance = 0.0;
        for (TSPvisit visit: visitList) {
            if (visit.getNext() != null) {
                totalDistance = totalDistance + visit.distanceToNext();
            } else {
                totalDistance = totalDistance + visit.distanceTo(visitList.get(0));
            }
        }
        return totalDistance;
    }

}
