package com.ebilab.jobflow;

import java.util.Arrays;
import java.util.List;

import com.ebilab.modelgen.dmdl.csv.AbstractSummaryDataCsvOutputDescription;

/**
 * TF-IDF計算済みファイル。
 * 日、単語で集約し計算結果を格納したファイル。ファイル単位は日単位。
 * @author nakazawasugio
 *
 */
public class SummaryDataToFile extends AbstractSummaryDataCsvOutputDescription {

    @Override
    public String getBasePath() {
        return "result/summary";
    }

    @Override
    public String getResourcePattern() {
        return "summary_data.csv";
    }

    /**
     * 日付順（昇順）で出力。
     */
    @Override
    public List<String> getOrder() {
        return Arrays.asList("+date");
    }

}
