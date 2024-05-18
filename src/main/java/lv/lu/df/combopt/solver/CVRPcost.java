package lv.lu.df.combopt.solver;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.buildin.simple.SimpleScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import ai.timefold.solver.core.api.score.stream.Joiners;
import lv.lu.df.combopt.domain.CVRPcustomer;
import lv.lu.df.combopt.domain.CVRPvehicle;
import lv.lu.df.combopt.domain.TSPsalesman;
import lv.lu.df.combopt.domain.TSPvisit;

public class CVRPcost implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                distanceToNextCustomer(constraintFactory),
                distanceToFirstCustomer(constraintFactory),
                overFilledVehicle(constraintFactory)
        };
    }

    public Constraint distanceToNextCustomer(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(CVRPcustomer.class)
                .join(CVRPvehicle.class, Joiners.equal(CVRPcustomer::getVehicle, v -> v))
                .penalize(HardSoftScore.ONE_SOFT, (c, v) -> (c.getNext() != null ?
                        (int) Math.round(100 * c.distanceToNext()) :
                        (int) Math.round(100 * c.getLocation().distanceTo(v.getDepot()))))
                .asConstraint("distanceToNextCustomer");
    }

    public Constraint distanceToFirstCustomer(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(CVRPvehicle.class)
                .filter(v -> !v.getCustomerList().isEmpty())
                .join(CVRPcustomer.class, Joiners.equal(v -> v.getCustomerList().get(0), c -> c))
                .penalize(HardSoftScore.ONE_SOFT, (v, c) -> (
                        (int) Math.round(100 * v.getDepot().distanceTo(c.getLocation()))))
                .asConstraint("distanceToFirstCustomer");
    }

    public Constraint overFilledVehicle(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(CVRPcustomer.class)
                .join(CVRPvehicle.class, Joiners.equal(CVRPcustomer::getVehicle, v -> v))
                .filter((c,v) -> c.getVehicleVolume() > v.getCapacity())
                .penalize(HardSoftScore.ONE_HARD, (c,v) -> - v.getCapacity() + c.getVehicleVolume())
                .asConstraint("overFilledVehicle");
    }
}
