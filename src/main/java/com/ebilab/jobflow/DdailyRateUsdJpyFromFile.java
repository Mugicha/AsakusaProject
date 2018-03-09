package com.ebilab.jobflow;

import com.ebilab.modelgen.dmdl.csv.AbstractDailyRateUsdJpyCsvInputDescription;

public class DdailyRateUsdJpyFromFile extends AbstractDailyRateUsdJpyCsvInputDescription {

    @Override
    public String getBasePath() {
        return "daily_rate_usd_jpy";
    }

    @Override
    public String getResourcePattern() {
        return "*.csv";
    }

}
