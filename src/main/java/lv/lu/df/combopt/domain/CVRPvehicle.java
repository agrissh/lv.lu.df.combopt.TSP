package lv.lu.df.combopt.domain;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.variable.PlanningListVariable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@PlanningEntity
@Getter
@Setter
@NoArgsConstructor
public class CVRPvehicle {
    private String plateNr;

    private CVRPlocation depot;

    private Integer capacity;

    @PlanningListVariable(valueRangeProviderRefs = {"customers"})
    private List<CVRPcustomer> customerList = new ArrayList<>();

    public Double totalDistanceTraveled() {
        Double totalDistance = customerList.isEmpty() ? 0.0 : depot.distanceTo(customerList.get(0).getLocation());
        for (CVRPcustomer customer: customerList) {
            if (customer.getNext() != null) {
                totalDistance = totalDistance + customer.distanceToNext();
            } else {
                totalDistance = totalDistance + customer.getLocation().distanceTo(depot);
            }
        }
        return totalDistance;
    }
}
