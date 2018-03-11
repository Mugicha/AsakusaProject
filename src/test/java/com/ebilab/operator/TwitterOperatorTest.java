package com.ebilab.operator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asakusafw.runtime.testing.MockResult;
import com.ebilab.modelgen.tfidf.model.DayWordCountModel;
import com.ebilab.modelgen.twitter.model.TwitterRec;

/**
 * Twitter文書解析用演算子テスト。
 * @author nakazawasugio
 *
 */
public class TwitterOperatorTest {

    static Logger LOG = LoggerFactory.getLogger(TwitterOperatorTest.class);

    private TwitterOperatorImpl operator = new TwitterOperatorImpl();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSplit() {
        TwitterRec rec1 = new TwitterRec();
        rec1.setContentAsString("[Wed Jan 17 11:15:02 +0000 2018]@hitsuzi34 おめでとうございます！当選しました！！ でか焼鳥無料クーポンはメッセージでお送りします♪ また明日も参加をお待ちしています(^^) https://t.co/xqeHBh7npP");
        TwitterRec rec2 = new TwitterRec();
        rec2.setContentAsString("[Wed Jan 17 11:15:02 +0000 2018]RT @OM_Officiel: Bonne nuit à tous. 😴 🤗 https://t.co/LsAUgaLSxJ");
        TwitterRec rec3 = new TwitterRec();
        rec3.setContentAsString("[Wed Jan 17 11:15:02 +0000 2018]RT @awtar_sarab: سأغيب الي ان تأتي تسأل عني  وساقولك لك بانني مريضة  مريضة بكلماتك التي تدلقها كل يوم بين شفاه النساء وهن يتفاخرن بك  وانا…");
        TwitterRec rec4 = new TwitterRec();
        rec4.setContentAsString("[Wed Jan 17 11:15:02 +0000 2018]RT @hwon_sbS2: 브이앱에서 덷거미 연성하는 k아이돌 https://t.co/ZVemhouYcH");

        MockResult<DayWordCountModel> result = new MockResult<DayWordCountModel>() {

            @Override
            protected DayWordCountModel bless(DayWordCountModel result) {
                DayWordCountModel copy = new DayWordCountModel();
                copy.copyFrom(result);
                return copy;
            }
        };

        operator.split(rec1, result);
        operator.split(rec2, result);
        operator.split(rec3, result);
        operator.split(rec4, result);

        List<DayWordCountModel> r = result.getResults();
        //  日本語　名詞、動詞抽出
        String[] expect = {
                //
                "@","hitsuzi","34", "当選", "する", "焼鳥", "無料", "クーポン", "メッセージ", "お送り", "する", "♪", "明日", "参加", "お待ち", "する", "いる",
                "(^^)", "https","://","t",".","co","/","xqeHBh","7","npP", "RT", "@","OM","_","Officiel",":", "Bonne", "nuit", "à", "tous", //"😴", "🤗",
                "https","://","t",".","co","/","LsAUgaLSxJ", "RT", "@","awtar","_","sarab",":", "RT", "@","hwon","_","sbS","2",":", "k",//"아이돌",
                "https","://","t",".","co","/","ZVemhouYcH",
        //
        };
        //  言語識別をなし（日本語だけ）にしたので以下は没
//        "@hitsuzi34", "当選", "する", "焼鳥", "無料", "クーポン", "メッセージ", "お送り", "する", "♪", "明日", "参加", "お待ち", "する", "いる",
//        "(^^)", "https://t.co/xqeHBh7npP", "RT", "@OM_Officiel:", "Bonne", "nuit", "à", "tous", "😴", "🤗",
//        "https://t.co/LsAUgaLSxJ", "RT", "@awtar_sarab:", "سأغيب", "الي", "ان", "تأتي", "تسأل", "عني",
//        "وساقولك", "لك", "بانني", "مريضة", "مريضة", "بكلماتك", "التي", "تدلقها", "كل", "يوم", "بين", "شفاه",
//        "النساء", "وهن", "يتفاخرن", "بك", "وانا…", "RT", "@hwon_sbS2:", "브이앱에서", "덷거미", "연성하는", "k아이돌",
//        "https://t.co/ZVemhouYcH",
//
        //  日本語　名詞のみ抽出の場合
//        String[] a = { "@hitsuzi34", "当選", "焼鳥", "無料", "クーポン", "メッセージ", "お送り", "♪", "明日", "参加", "お待ち", "(^^)",
//                "https://tco/xqeHBh7npP", "RT", "@OM_Officiel:", "Bonne", "nuit", "à", "tous", "😴", "🤗",
//                "https://tco/LsAUgaLSxJ", "RT", "@awtar_sarab:", "سأغيب", "الي", "ان", "تأتي", "تسأل", "عني",
//                "وساقولك", "لك", "بانني", "مريضة", "مريضة", "بكلماتك", "التي", "تدلقها", "كل", "يوم", "بين", "شفاه",
//                "النساء", "وهن", "يتفاخرن", "بك", "وانا…", "RT", "@hwon_sbS2:", "브이앱에서", "덷거미", "연성하는", "k아이돌",
//                "https://tco/ZVemhouYcH", };
        //  実績値の配列
        StringBuffer sb = new StringBuffer();
        r.forEach(d -> sb.append("\"" + d.getWordAsString() + "\","));
        LOG.debug(sb.toString());

        for (int i = 0; i < expect.length; i++) {
        	LOG.info(r.get(i).getWordAsString());
            assertThat(r.get(i).getWordAsString(), is(expect[i]));
        }
    }
    @Test
    public void testReformatTop(){
        
    }
}
