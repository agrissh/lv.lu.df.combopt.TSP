package lv.lu.df.combopt.domain;

import ai.timefold.solver.persistence.common.api.domain.solution.SolutionFileIO;

import java.io.File;

public class TSPioSym implements SolutionFileIO<TSPsolution> {
    @Override
    public String getInputFileExtension() {
        return "csv";
    }

    @Override
    public TSPsolution read(File file) {
        return TSPsolution.readFromCSV(file.getPath(), false);
    }

    @Override
    public void write(TSPsolution tsPsolution, File file) {
        tsPsolution.writeToCSV(file);
    }
}
