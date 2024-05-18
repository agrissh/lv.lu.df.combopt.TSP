package lv.lu.df.combopt.domain;

import ai.timefold.solver.core.api.domain.variable.AbstractVariableListener;
import ai.timefold.solver.core.api.domain.variable.VariableListener;
import ai.timefold.solver.core.api.score.director.ScoreDirector;
import freemarker.core.CustomAttribute;

public class VolumeVariableListener implements VariableListener<CVRPsolution, CVRPcustomer> {
    @Override
    public void beforeVariableChanged(ScoreDirector<CVRPsolution> scoreDirector, CVRPcustomer cvrPcustomer) {

    }

    @Override
    public void afterVariableChanged(ScoreDirector<CVRPsolution> scoreDirector, CVRPcustomer customer) {
        if (customer.getVehicle() == null) {
            scoreDirector.beforeVariableChanged(customer, "vehicleVolume");
            customer.setVehicleVolume(0);
            scoreDirector.afterVariableChanged(customer, "vehicleVolume");
            return;
        }

        CVRPcustomer prevCustomer = customer.getPrevious();
        Integer volume = 0;
        if (prevCustomer != null) {
            volume = prevCustomer.getVehicleVolume();
        }

        CVRPcustomer shadowCustomer = customer;
        //PICKUP
        volume = volume + shadowCustomer.getDemand();

        //&& !Objects.equals(shadowVisit.getUndeliveredVolume(), undeliveredVolume
        while (shadowCustomer != null) {
            scoreDirector.beforeVariableChanged(shadowCustomer, "vehicleVolume");
            shadowCustomer.setVehicleVolume(volume);
            scoreDirector.afterVariableChanged(shadowCustomer, "vehicleVolume");
            shadowCustomer = shadowCustomer.getNext();
            if (shadowCustomer != null) {
                volume = volume + shadowCustomer.getDemand();
            }
        }
    }


    @Override
    public void beforeEntityAdded(ScoreDirector<CVRPsolution> scoreDirector, CVRPcustomer cvrPcustomer) {

    }

    @Override
    public void afterEntityAdded(ScoreDirector<CVRPsolution> scoreDirector, CVRPcustomer cvrPcustomer) {

    }

    @Override
    public void beforeEntityRemoved(ScoreDirector<CVRPsolution> scoreDirector, CVRPcustomer cvrPcustomer) {

    }

    @Override
    public void afterEntityRemoved(ScoreDirector<CVRPsolution> scoreDirector, CVRPcustomer cvrPcustomer) {

    }
}
