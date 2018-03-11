package com.ebilab.jobflow;

import com.ebilab.modelgen.tfidf.csv.AbstractDayWordCountModelCsvInputDescription;

public class DayWordCountModelFromFile extends AbstractDayWordCountModelCsvInputDescription {

    @Override
    public String getBasePath() {
        return "result/daywordcount";
    }

    @Override
    public String getResourcePattern() {
        return "dwcm-*";
    }

}
