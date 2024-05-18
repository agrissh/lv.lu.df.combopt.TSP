package lv.lu.df.combopt.domain;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@PlanningEntity
@Getter
@Setter
@NoArgsConstructor
public class CVRPlocation {
    private String id;

    private Map<CVRPlocation, Double> distanceMap = new HashMap<>();

    public Double distanceTo(CVRPlocation customer) {
        return distanceMap.get(customer);
    }
}
