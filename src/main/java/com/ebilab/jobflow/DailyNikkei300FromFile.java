package com.ebilab.jobflow;

import com.ebilab.modelgen.dmdl.csv.AbstractDailyNikkei300CsvInputDescription;

public class DailyNikkei300FromFile extends AbstractDailyNikkei300CsvInputDescription {

    @Override
    public String getBasePath() {
        return "daily_nikkei_300";
    }

    @Override
    public String getResourcePattern() {
        return "*.csv";
    }

}
