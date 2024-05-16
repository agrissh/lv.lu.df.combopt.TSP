package lv.lu.df.combopt.domain;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.variable.InverseRelationShadowVariable;
import ai.timefold.solver.core.api.domain.variable.NextElementShadowVariable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@PlanningEntity
@Getter @Setter @NoArgsConstructor
public class TSPvisit {

    private String name;

    private Map<TSPvisit, Double> distanceMap = new HashMap<>();

    @InverseRelationShadowVariable(sourceVariableName = "visitList")
    private TSPsalesman salesman;

    @NextElementShadowVariable(sourceVariableName = "visitList")
    private TSPvisit next;

    public Double distanceToNext() {
        return (next != null ? distanceMap.get(next) : null);
    }

    public Double distanceTo(TSPvisit visit) {
        return distanceMap.get(visit);
    }

}
