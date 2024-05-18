package lv.lu.df.combopt.domain;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.variable.InverseRelationShadowVariable;
import ai.timefold.solver.core.api.domain.variable.NextElementShadowVariable;
import ai.timefold.solver.core.api.domain.variable.PreviousElementShadowVariable;
import ai.timefold.solver.core.api.domain.variable.ShadowVariable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@PlanningEntity
@Getter
@Setter
@NoArgsConstructor
public class CVRPcustomer {
    private String name;

    private CVRPlocation location;

    private Integer demand;

    @InverseRelationShadowVariable(sourceVariableName = "customerList")
    private CVRPvehicle vehicle;

    @NextElementShadowVariable(sourceVariableName = "customerList")
    private CVRPcustomer next;

    @PreviousElementShadowVariable(sourceVariableName = "customerList")
    private CVRPcustomer previous;

    @ShadowVariable(variableListenerClass = VolumeVariableListener.class, sourceVariableName = "vehicle")
    @ShadowVariable(variableListenerClass = VolumeVariableListener.class, sourceVariableName = "previous")
    private Integer vehicleVolume = 0;

    public Double distanceToNext() {
        return (next != null ? location.distanceTo(next.getLocation()) : null);
    }
}
