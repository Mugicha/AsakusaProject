package com.ebilab.jobflow;

import com.ebilab.modelgen.twitter.line.AbstractTwitterRecLineInputDescription;

/**
 * Twitterファイル。
 * １行を１カラムとしてオペレータで分解する。
 * @author nakazawasugio
 *
 */
public class TwitterRecFromFile extends AbstractTwitterRecLineInputDescription {

    @Override
    public String getBasePath() {
        return "twitter";
    }

    @Override
    public String getResourcePattern() {
        return "*.txt";
    }
}
