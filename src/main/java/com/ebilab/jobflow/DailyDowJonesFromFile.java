package com.ebilab.jobflow;

import com.ebilab.modelgen.dmdl.csv.AbstractDailyDowJonesCsvInputDescription;

public class DailyDowJonesFromFile extends AbstractDailyDowJonesCsvInputDescription {

    @Override
    public String getBasePath() {
        return "daily_daw_jones";
    }

    @Override
    public String getResourcePattern() {
        return "*.csv";
    }

}
