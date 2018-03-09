package com.ebilab.jobflow;

import org.junit.Test;

import com.asakusafw.testdriver.BatchTester;
import com.ebilab.modelgen.dmdl.model.DailyDowJones;
import com.ebilab.modelgen.dmdl.model.DailyNikkei300;
import com.ebilab.modelgen.dmdl.model.DailyRateUsdJpy;
import com.ebilab.modelgen.dmdl.model.SummaryData;
import com.ebilab.modelgen.tfidf.model.DayWordCountModel;
import com.ebilab.modelgen.twitter.model.DispDayWordModel;
import com.ebilab.modelgen.twitter.model.TwitterRec;

public class CombineDataBatchTest {
//	@Test
//	public void test() {
//
//		String dataDir = "CombineDataBatchTestData/";
//		String inDdailyRateUsdJpy = dataDir + "daily_rate_usd_jpy.xls";
//		String inDailyDowJones = dataDir + "daily_dow_jones.xls";
//		String inDailyNikkei300 = dataDir + "daily_nikkei_300.xls";
//		String inTwitterRec = dataDir + "twitter_rec.xls";
//
//		String outSummaryData = dataDir + "summary_data.xls";
//		String out = dataDir + "day_word_count_model.xls";
//		String outDispTfIdf = dataDir + "disp_day_word_model.xls";
//
//		BatchTester tester = new BatchTester(getClass());
//
//		tester.jobflow("CombineDataJob").input("inDdailyRateUsdJpy", DailyRateUsdJpy.class).prepare(inDdailyRateUsdJpy + "#input");
//		tester.jobflow("CombineDataJob").input("inDailyDowJones", DailyDowJones.class).prepare(inDailyDowJones + "#input");
//		tester.jobflow("CombineDataJob").input("inDailyNikkei300", DailyNikkei300.class).prepare(inDailyNikkei300 + "#input");
//		tester.jobflow("CombineDataJob").input("inTwitterRec", TwitterRec.class).prepare(inTwitterRec + "#input");
//
//		tester.jobflow("CombineDataJob").output("outSummaryData", SummaryData.class).verify(outSummaryData + "#output", outSummaryData + "#rule");
//		tester.jobflow("CombineDataJob").output("out", DayWordCountModel.class).verify(out + "#output", outSummaryData + "#rule");
//		tester.jobflow("CombineDataJob").output("outDispTfIdf", DispDayWordModel.class).verify(outDispTfIdf + "#output", outSummaryData + "#rule");
//
//		tester.runTest(CombineDataBatch.class);
//	}
}
