package com.ebilab.jobflow;

import com.asakusafw.vocabulary.batch.Batch;
import com.asakusafw.vocabulary.batch.Batch.Parameter;
import com.asakusafw.vocabulary.batch.BatchDescription;

/**
 * 日別為替関連情報作成バッチ。
 * @author nakazawasugio
 *
 */
@Batch(
        name = "CombineBatch",
        comment = "日別為替関連情報作成",
//        parameters = {
//            @Parameter(key = "limit", comment = "tf-idf出力件数", required = false)
//        },
        strict = true
)
public class CombineDataBatch extends BatchDescription {

    @Override
    protected void describe() {
        run(CombineDataJob.class).soon();
    }

}
