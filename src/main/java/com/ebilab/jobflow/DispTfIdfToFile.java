package com.ebilab.jobflow;

import java.util.Arrays;
import java.util.List;

import com.ebilab.modelgen.twitter.csv.AbstractDispDayWordModelCsvOutputDescription;

/**
 * TF-IDFトップN件を出力。
 * 日別に１レコード、TF-IDFの大きい順に単語をカンマ区切りで出力。
 * @author nakazawasugio
 *
 */
public class DispTfIdfToFile extends AbstractDispDayWordModelCsvOutputDescription {

    @Override
    public String getBasePath() {
        return "result/disptfidf";
    }

    @Override
    public String getResourcePattern() {
        return "disptfidf.csv";
    }

    @Override
    public List<String> getOrder() {
        return Arrays.asList("+date");
    }

}
