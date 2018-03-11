package com.ebilab.jobflow;

import com.asakusafw.vocabulary.flow.Export;
import com.asakusafw.vocabulary.flow.FlowDescription;
import com.asakusafw.vocabulary.flow.Import;
import com.asakusafw.vocabulary.flow.In;
import com.asakusafw.vocabulary.flow.JobFlow;
import com.asakusafw.vocabulary.flow.Out;
import com.asakusafw.vocabulary.flow.Source;
import com.ebilab.flowpart.TfIdfFlowPartFactory;
import com.ebilab.modelgen.tfidf.model.DayWordCountModel;
import com.ebilab.modelgen.twitter.model.DispDayWordModel;
import com.ebilab.modelgen.twitter.model.TwitterRec;
import com.ebilab.operator.TfIdfOperatorFactory;
import com.ebilab.operator.TfIdfOperatorFactory.Top;
import com.ebilab.operator.TwitterOperatorFactory;
import com.ebilab.operator.TwitterOperatorFactory.ReformatTop;

/**
 * Twitter文章TF-IDF解析バッチ。
 * @author nakazawasugio
 *
 */
@JobFlow(name = "TwTfIdfJob")
public class TwTfIdfJob extends FlowDescription {

    private final In<TwitterRec> in;

    private final Out<DayWordCountModel> out;

    private final Out<DispDayWordModel> outDispTfIdf;

    public TwTfIdfJob(@Import(name = "in", description = TwitterRecFromFile.class) In<TwitterRec> in,
            @Export(name = "out", description = DayWordCountModelToFile.class) Out<DayWordCountModel> out,
            @Export(name = "outDispTfIdf", description = DispTfIdfToFile.class) Out<DispDayWordModel> outDispTfIdf) {
        this.in = in;
        this.out = out;
        this.outDispTfIdf = outDispTfIdf;
    }

    @Override
    protected void describe() {
        TwitterOperatorFactory twOperator = new TwitterOperatorFactory();
        TfIdfOperatorFactory tfIdfOperator = new TfIdfOperatorFactory();
        TfIdfFlowPartFactory tfIdfFlowPart = new TfIdfFlowPartFactory();
        //  tweat文章から単語分割
        Source<DayWordCountModel> splitResult = twOperator.split(in).dayWordCountResult;
        //  TF-IDF計算
        TfIdfFlowPartFactory.TfIdfFlowPart fp = tfIdfFlowPart.create(splitResult);
        out.add(fp.out);
        //  トップN件に絞る。
        Top top = tfIdfOperator.top(fp.out);
        //  トップN件の日を行としたスタイル
        ReformatTop reformat = twOperator.reformatTop(top.out);
        outDispTfIdf.add(reformat.out);
    }
}
