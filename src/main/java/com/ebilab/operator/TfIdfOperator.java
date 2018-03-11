package com.ebilab.operator;

import java.text.MessageFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asakusafw.runtime.core.BatchContext;
import com.asakusafw.runtime.core.Result;
import com.asakusafw.runtime.value.StringOption;
import com.asakusafw.vocabulary.model.Key;
import com.asakusafw.vocabulary.operator.CoGroup;
import com.asakusafw.vocabulary.operator.GroupSort;
import com.asakusafw.vocabulary.operator.Logging;
import com.asakusafw.vocabulary.operator.MasterJoinUpdate;
import com.asakusafw.vocabulary.operator.Summarize;
import com.ebilab.modelgen.tfidf.model.DayCountModel;
import com.ebilab.modelgen.tfidf.model.DayNumModel;
import com.ebilab.modelgen.tfidf.model.DayWordCountModel;
import com.ebilab.modelgen.tfidf.model.WordCountDayTotalModel;
import com.ebilab.modelgen.tfidf.model.WordCountModel;

/**
 * TF-IDF計算用演算子
 * @author nakazawasugio
 *
 */
public abstract class TfIdfOperator {

    static Logger LOG = LoggerFactory.getLogger(TfIdfOperator.class);

    /** 出力単語数指定キー */
    static private final String OUTPUT_LIMIT = "limit";
    /** 性能検証用DBアクセスフラグ */
    static final String USE_DB = "useDb";

    /** 日別単語別カウント */
    private final DayWordCountModel wordCount = new DayWordCountModel();

    /**
     * 日付、単語で集計しカウントを計算。
     * 引数 USE_DB を true にすると性能検証用にSELECT文を発行します。（遅くなるので要注意）
     * @param wordCountList
     * @param wordCountResult
     */
    @CoGroup
    public void sumDayWord(@Key(group = { "word", "date" }) List<DayWordCountModel> wordCountList,
            Result<DayWordCountModel> wordCountResult) {
        StringOption date = wordCountList.get(0).getDateOption();
        StringOption word = wordCountList.get(0).getWordOption();

        int count = 0;
        for (DayWordCountModel wc : wordCountList) {
            count += wc.getCount();
        }

        wordCount.setDateOption(date);
        wordCount.setWordOption(word);
        wordCount.setCount(count);

        wordCountResult.add(wordCount);
    }

    @Summarize
    public abstract DayCountModel summarizeDay(DayWordCountModel wc);

    @Summarize
    public abstract DayNumModel summarizeDayNum(DayCountModel wc);

    @Summarize
    public abstract WordCountModel summarizeWord(DayWordCountModel wc);

    /**
     * 単語別カウントに日数をセット。
     * @param master
     * @param tx
     */
    @MasterJoinUpdate
    public void updateJoinDayNum(@Key(group = { }) DayNumModel master, @Key(group = { }) WordCountDayTotalModel tx) {
        tx.setTotalNumOfDay(master.getNumOfDay());
    }

    @MasterJoinUpdate
    public void updateJoinDay(@Key(group = "date") DayCountModel master, @Key(group = "date") DayWordCountModel tx) {
        tx.setTf(((double) tx.getCount() / (double) master.getTotalCount()));
    }

    @MasterJoinUpdate
    public void updateJoinWord(@Key(group = "word") WordCountDayTotalModel master,
            @Key(group = "word") DayWordCountModel tx) {
        tx.setIdf(Math.log((double) master.getTotalNumOfDay() / (double) master.getNumOfDay()));
//        tx.setIdf(Math.log((double) master.getTotalNumOfDay() / (double) master.getNumOfDay()) + 1);
        tx.setTfIdf(tx.getTf() * tx.getIdf());
//        if("[ー]".equalsIgnoreCase(tx.getWordAsString())){
//            System.out.println("total = " + master.getTotalNumOfDay() + " num = " + master.getNumOfDay());
//        }
    }

    @GroupSort
    public void top(@Key(group = "date", order = "tf_idf DESC") List<DayWordCountModel> in,
            Result<DayWordCountModel> out) {
        int limit = 100;
        if (BatchContext.get(OUTPUT_LIMIT) != null) {
            limit = Integer.parseInt(BatchContext.get(OUTPUT_LIMIT));
        }
        int i = 0;
        for (DayWordCountModel dwcm : in) {
            out.add(dwcm);
            i++;
            if (i > limit)
                break;
        }
    }
    @Logging(Logging.Level.ERROR)
    public String error(WordCountModel in) {
        return MessageFormat.format("word = {0} numDay = {1} total = {2}", in.getWordAsString(),in.getNumOfDay(),in.getTotalCount());
    }

}
