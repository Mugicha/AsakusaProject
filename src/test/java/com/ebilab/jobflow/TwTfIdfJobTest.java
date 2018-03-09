package com.ebilab.jobflow;

import org.junit.Test;

import com.asakusafw.testdriver.JobFlowTester;
import com.ebilab.modelgen.tfidf.model.DayWordCountModel;
import com.ebilab.modelgen.twitter.model.TwitterRec;

/**
 * Twitter解析ジョブテスト。
 * @author nakazawasugio
 *
 */
public class TwTfIdfJobTest {

    @Test
    public void testDescribe() {
        String inDataSheet = "TwTfIdfJobTestData/twitter_rec.xls";
        String outDataSheet = "TwTfIdfJobTestData/day_word_count_model.xls";
        //	同率が多いため実行ごとに順序が入れ替わるのでテストなし。
//        String outDispTfIdfDataSheet = "TwTfIdfJobTest/disp_day_word_model.xls";

        JobFlowTester tester = new JobFlowTester(getClass());

        tester.input("in", TwitterRec.class).prepare(inDataSheet + "#input");
        tester.output("out", DayWordCountModel.class).verify(outDataSheet + "#output", outDataSheet + "#rule");
        //	同率が多いため実行ごとに順序が入れ替わるのでテストなし。
//        tester.output("outDispTfIdf", DispDayWordModel.class).verify(outDispTfIdfDataSheet + "#output", outDispTfIdfDataSheet + "#rule");

        tester.runTest(TwTfIdfJob.class);
    }
}
