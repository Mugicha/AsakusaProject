package com.ebilab.jobflow;

import com.asakusafw.vocabulary.batch.Batch;
import com.asakusafw.vocabulary.batch.Batch.Parameter;
import com.asakusafw.vocabulary.batch.BatchDescription;

/**
 * Twitter文章TF-IDF解析バッチ。
 * @author nakazawasugio
 *
 */
@Batch(
        name = "TwTfIdfBatch",
        comment = "日毎の出現単語特徴抽出",
        parameters = {
            @Parameter(key = "limit", comment = "tf-idf出力件数", required = false)
        },
        strict = true
)
public class TwTfIdfBatch extends BatchDescription {

    @Override
    protected void describe() {
        run(TwTfIdfJob.class).soon();
    }

}
