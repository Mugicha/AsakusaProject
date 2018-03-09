package com.ebilab.jobflow;

import com.asakusafw.vocabulary.flow.Export;
import com.asakusafw.vocabulary.flow.FlowDescription;
import com.asakusafw.vocabulary.flow.Import;
import com.asakusafw.vocabulary.flow.In;
import com.asakusafw.vocabulary.flow.JobFlow;
import com.asakusafw.vocabulary.flow.Out;
import com.asakusafw.vocabulary.flow.Source;
import com.ebilab.flowpart.CombineDataFlowPartFactory;
import com.ebilab.flowpart.TfIdfFlowPartFactory;
import com.ebilab.modelgen.dmdl.model.DailyDowJones;
import com.ebilab.modelgen.dmdl.model.DailyNikkei300;
import com.ebilab.modelgen.dmdl.model.DailyRateUsdJpy;
import com.ebilab.modelgen.dmdl.model.SummaryData;
import com.ebilab.modelgen.tfidf.model.DayWordCountModel;
import com.ebilab.modelgen.twitter.model.DispDayWordModel;
import com.ebilab.modelgen.twitter.model.TwitterRec;
import com.ebilab.operator.CombineDataOperatorFactory;
import com.ebilab.operator.CombineDataOperatorFactory.ReformatTop50;
import com.ebilab.operator.TfIdfOperatorFactory;
import com.ebilab.operator.TfIdfOperatorFactory.Top;
import com.ebilab.operator.TwitterOperatorFactory;
import com.ebilab.operator.TwitterOperatorFactory.ReformatTop;

/**
 * 日別データ結合。
 * @author nakazawasugio
 *
 */
@JobFlow(name = "CombineDataJob")
public class CombineDataJob extends FlowDescription {

	private final In<DailyRateUsdJpy> inDailyRateUsdJpy;
	private final In<DailyDowJones> inDailyDowJones;
	private final In<DailyNikkei300> inDailyNikkei300;
    private final In<TwitterRec> inTwitterRec;

    private final Out<DayWordCountModel> out;

    private final Out<DispDayWordModel> outDispTfIdf;
    
	private final Out<SummaryData> outSummaryData;

    public CombineDataJob(
    		@Import(name = "inDdailyRateUsdJpy", description = DdailyRateUsdJpyFromFile.class) In<DailyRateUsdJpy> inDdailyRateUsdJpy,
    		@Import(name = "inDailyDowJones", description = DailyDowJonesFromFile.class) In<DailyDowJones> inDailyDowJones,
    		@Import(name = "inDailyNikkei300", description = DailyNikkei300FromFile.class) In<DailyNikkei300> inDailyNikkei300,
    		@Import(name = "inTwitterRec", description = TwitterRecFromFile.class) In<TwitterRec> inTwitterRec,
            @Export(name = "out", description = DayWordCountModelToFile.class) Out<DayWordCountModel> out,
            @Export(name = "outDispTfIdf", description = DispTfIdfToFile.class) Out<DispDayWordModel> outDispTfIdf,
            @Export(name = "outSummaryData", description = SummaryDataToFile.class) Out<SummaryData> outSummaryData
    		) {
        this.inDailyRateUsdJpy = inDdailyRateUsdJpy;
        this.inDailyDowJones = inDailyDowJones;
        this.inDailyNikkei300 = inDailyNikkei300;
        this.inTwitterRec = inTwitterRec;
        this.out = out;
        this.outDispTfIdf = outDispTfIdf;
        this.outSummaryData = outSummaryData;
    }

    @Override
    protected void describe() {
        TwitterOperatorFactory twOperator = new TwitterOperatorFactory();
        TfIdfOperatorFactory tfIdfOperator = new TfIdfOperatorFactory();
        TfIdfFlowPartFactory tfIdfFlowPart = new TfIdfFlowPartFactory();

    	CombineDataFlowPartFactory combineDataFlowPart = new CombineDataFlowPartFactory();
    	CombineDataOperatorFactory combineDataOperator = new CombineDataOperatorFactory();

    	//	Twitter処理
        //  tweat文章から単語分割
        Source<DayWordCountModel> splitResult = twOperator.split(inTwitterRec).dayWordCountResult;
        //  TF-IDF計算
        TfIdfFlowPartFactory.TfIdfFlowPart fp = tfIdfFlowPart.create(splitResult);
        out.add(fp.out);
        //  トップN件に絞る。
        Top top = tfIdfOperator.top(fp.out);
        //  トップN件の日を行としたスタイル
        ReformatTop reformat = twOperator.reformatTop(top.out);
        outDispTfIdf.add(reformat.out);
        //	twitter縦横変換
        ReformatTop50 reformat50 = combineDataOperator.reformatTop50(top.out);
        
        //	CombineData処理
		CombineDataFlowPartFactory.CombineDataFlowPart cbfp = combineDataFlowPart
				.create(
						inDailyRateUsdJpy,
						inDailyDowJones, 
						inDailyNikkei300,
						reformat50.out);
		outSummaryData.add(cbfp.summaryData);
    }
}
