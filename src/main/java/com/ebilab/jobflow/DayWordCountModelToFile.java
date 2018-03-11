package com.ebilab.jobflow;

import com.ebilab.modelgen.tfidf.csv.AbstractDayWordCountModelCsvOutputDescription;

/**
 * TF-IDF計算済みファイル。
 * 日、単語で集約し計算結果を格納したファイル。ファイル単位は日単位。
 * @author nakazawasugio
 *
 */
public class DayWordCountModelToFile extends AbstractDayWordCountModelCsvOutputDescription {

    @Override
    public String getBasePath() {
        return "result/daywordcount";
    }

    @Override
    public String getResourcePattern() {
        return "dwcm-{date}.csv";
    }

}
