package com.ebilab.flowpart;

import com.asakusafw.vocabulary.flow.FlowDescription;
import com.asakusafw.vocabulary.flow.FlowPart;
import com.asakusafw.vocabulary.flow.In;
import com.asakusafw.vocabulary.flow.Out;
import com.asakusafw.vocabulary.flow.Source;
import com.asakusafw.vocabulary.flow.util.CoreOperatorFactory;
import com.asakusafw.vocabulary.flow.util.CoreOperatorFactory.Extend;
import com.asakusafw.vocabulary.flow.util.CoreOperators;
import com.ebilab.modelgen.tfidf.model.DayWordCountModel;
import com.ebilab.modelgen.tfidf.model.WordCountDayTotalModel;
import com.ebilab.operator.TfIdfOperatorFactory;
import com.ebilab.operator.TfIdfOperatorFactory.SummarizeDay;
import com.ebilab.operator.TfIdfOperatorFactory.SummarizeDayNum;
import com.ebilab.operator.TfIdfOperatorFactory.SummarizeWord;
import com.ebilab.operator.TfIdfOperatorFactory.UpdateJoinDay;
import com.ebilab.operator.TfIdfOperatorFactory.UpdateJoinDayNum;
import com.ebilab.operator.TfIdfOperatorFactory.UpdateJoinWord;

/**
 * TF-IDFの計算
 * IN 必須項目　date word count（日、単語でユニークの必要はなし）
 * OUT には計算項目がセットされる。（結果は日、単語でユニークとなっている）
 * @author nakazawasugio
 *
 */
@FlowPart
public class TfIdfFlowPart extends FlowDescription {

    /** 日別単語別カウント(日、単語でユニークでなくともよい) */
    private final In<DayWordCountModel> in;

    /** 計算済み日別単語別カウント */
    private final Out<DayWordCountModel> out;
    
    public TfIdfFlowPart(In<DayWordCountModel> in, Out<DayWordCountModel> out) {
        this.in = in;
        this.out = out;
    }

    @Override
    protected void describe() {
        TfIdfOperatorFactory tfIdfOperator = new TfIdfOperatorFactory();
        CoreOperatorFactory core = new CoreOperatorFactory();

        //  日、単語ごとの出現数を算出。TFの分子。
        Source<DayWordCountModel> sumDayWordResult = tfIdfOperator.sumDayWord(in).wordCountResult;
        //  日で集計し、日毎の単語数を算出。TFの分母。
        SummarizeDay sumDay = tfIdfOperator.summarizeDay(sumDayWordResult);
        //  全体の日数をカウント。IDFの分子。
        SummarizeDayNum sumDayNum = tfIdfOperator.summarizeDayNum(sumDay.out);
        //  単語で集計し、単語ごとの出現日数を算出。IDFの分母。
        SummarizeWord sumWord = tfIdfOperator.summarizeWord(sumDayWordResult);
        //  日数を追加。カラム追加してIDFの分子、分母を同じ行に格納。
        Extend<WordCountDayTotalModel> extend = core.extend(sumWord.out, WordCountDayTotalModel.class);
        UpdateJoinDayNum addedDayNum = tfIdfOperator.updateJoinDayNum(sumDayNum.out, extend.out);
        //  日付別合計を結合。TFの計算
        UpdateJoinDay calcTf = tfIdfOperator.updateJoinDay(sumDay.out, sumDayWordResult);
        //  単語別合計を結合。IDF、TF-IDFの計算。
        UpdateJoinWord calcIDF = tfIdfOperator.updateJoinWord(addedDayNum.updated, calcTf.updated);
        out.add(calcIDF.updated);

        //  この処理ではmissedは発生しない。
        CoreOperators.stop(calcTf.missed);
        CoreOperators.stop(calcIDF.missed);
        CoreOperators.stop(addedDayNum.missed);
    }

}