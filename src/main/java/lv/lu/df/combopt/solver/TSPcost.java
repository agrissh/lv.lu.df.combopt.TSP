package lv.lu.df.combopt.solver;

import ai.timefold.solver.core.api.score.buildin.simple.SimpleScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import ai.timefold.solver.core.api.score.stream.Joiners;
import lv.lu.df.combopt.domain.TSPsalesman;
import lv.lu.df.combopt.domain.TSPvisit;

public class TSPcost implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                distanceToNextVisit(constraintFactory)
        };
    }

    public Constraint distanceToNextVisit(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(TSPvisit.class)
                .join(TSPsalesman.class, Joiners.equal(TSPvisit::getSalesman, s -> s))
                .penalize(SimpleScore.ONE, (v,s) -> (v.getNext() != null ?
                        (int) Math.round(100 * v.distanceToNext()) :
                        (int) Math.round(100 * v.distanceTo(s.getVisitList().get(0)))))
                .asConstraint("distanceToNextVisit");
    }
}
