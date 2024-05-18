package lv.lu.df.combopt.domain;

import ai.timefold.solver.persistence.common.api.domain.solution.SolutionFileIO;

import java.io.File;

public class CVRPio implements SolutionFileIO<CVRPsolution> {
    @Override
    public String getInputFileExtension() {
        return "csv";
    }

    @Override
    public CVRPsolution read(File file) {
        return CVRPsolution.readFromCSV("data/matrix_4.csv", file.getPath() );
    }

    @Override
    public void write(CVRPsolution solution, File file) {
        solution.writeToCSV(file);
    }
}
