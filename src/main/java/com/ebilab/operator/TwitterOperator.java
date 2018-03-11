package com.ebilab.operator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asakusafw.runtime.core.BatchContext;
import com.asakusafw.runtime.core.Result;
import com.asakusafw.vocabulary.model.Key;
import com.asakusafw.vocabulary.operator.Extract;
import com.asakusafw.vocabulary.operator.GroupSort;
import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import com.atilika.kuromoji.ipadic.Tokenizer.Builder;
import com.ebilab.modelgen.tfidf.model.DayWordCountModel;
import com.ebilab.modelgen.twitter.model.DispDayWordModel;
import com.ebilab.modelgen.twitter.model.TwitterRec;

/**
 * Twitter文書TF-IDF計算オペレータ。
 * @author nakazawasugio
 *
 */
public abstract class TwitterOperator {

    static Logger LOG = LoggerFactory.getLogger(TwitterOperator.class);

    /** 出力単語数指定キー */
    static private final String OUTPUT_LIMIT = "limit";

    /** 日別単語別カウント */
    private final DayWordCountModel wordCount = new DayWordCountModel();

    /** 日付別単語列 */
    private final DispDayWordModel ddwm = new DispDayWordModel();

    /** 日付文字列変換用　日付 */
    Date date = new Date();

    /** 日付文字列変換用　フォーマット */
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    /** 形態素解析 */
    private Tokenizer tokenizer;
    {
        try {
            LOG.info("*** create Tokenizer ***");
            //  ユーザ辞書
            String[] userDics = { "円高,円高,エンダカ,名詞", //
                    "円安,円安,エンヤス,名詞", //
                    "ドル高,ドル高,ドルダカ,名詞", //
                    "ドル安,ドル安,ドルヤス,名詞", //
                    "羽生結弦,羽生結弦,ハニュウユヅル,名詞", // 
                    "小平奈緒,小平奈緒,コダイラナオ,名詞", //
                    "葛西紀明,葛西紀明,カサイノリアキ,名詞", //
            };
            List<String> userDicList = Arrays.asList(userDics);
            Path path = Files.createTempFile(Paths.get("/tmp"), "userDic", ".csv");
            Files.write(path, userDicList, StandardOpenOption.TRUNCATE_EXISTING);
            path.toFile().deleteOnExit();
            //  kuromoji初期化
            Builder builder = new Tokenizer.Builder();
            this.tokenizer = builder.userDictionary(path.toFile().getAbsolutePath()).build();
        } catch (IOException e) {
            LOG.warn("unused user dictionary on kuromoji.");
            e.printStackTrace();
        }
    }
    /**
     * Twitter入力から日別単語別に分割。
     * StringTokenizerで分割された各単語<br>
     *      （１）文章の中のダブルコーテーションは全て削除<br>
     *      （２）単語の最後のピリオドは削除。<br>
     *      （３）単語の中のカンマは空白。<br>
     * @param rec
     * @param dayWordCountResult
     */
    @Extract
    public void split(TwitterRec rec, Result<DayWordCountModel> dayWordCountResult) {
        //  日付
        String dayStr = extractDayStr(rec);
        if (dayStr == null) {
            return;
        }
        //  最初にStringTokenizerで分割して塊ごとに日本語＆ダブルバイトあれば形態素解析
        StringTokenizer st = new StringTokenizer(extractSentence(rec));
        while (st.hasMoreTokens()) {
            String word = st.nextToken();
            word = word.replaceAll("\\.$", "");
            word = word.replaceAll(",", " ");
            if (includeDoubleByteChar(word) && ("ja".equalsIgnoreCase(extractLangStr(rec)))) {
                List<Token> tokens = tokenizer.tokenize(word);
                for (Token token : tokens) {
                    String subWord = convJapaneseWord(token);
                    if (subWord != null) {
                        wordCount.setWordAsString(subWord);
                        wordCount.setDateAsString(dayStr);
                        wordCount.setCount(1);
                        dayWordCountResult.add(wordCount);
                    }
                }
            } else {
                wordCount.setWordAsString(word);
                wordCount.setDateAsString(dayStr);
                wordCount.setCount(1);
                dayWordCountResult.add(wordCount);
            }
        }
    }

    /**
     * 日付の抽出。
     * 日付は最初のカラムとして[]で囲まれている。
     * @param rec
     * @return
     */
    private String extractDayStr(TwitterRec rec) {
        try {
            String content = rec.getContentAsString();
            String dateStr = content.substring(1, content.indexOf("]"));
            date.setTime(Date.parse(dateStr));
            return sdf.format(date);
        } catch (StringIndexOutOfBoundsException e) {
            LOG.error("not ] " + rec.getContentAsString());
        }catch (IllegalArgumentException iae){
            LOG.error("IllegalArgumentException " + rec.getContentAsString());
        }
        return null;
    }

    /**
     * 言語の抽出。
     * @param rec
     * @return
     */
    private String extractLangStr(TwitterRec rec) {
        //  日本語のみを対象とするのでいつでも ja
        return "ja";
        //        String content = rec.getContentAsString();
        //        return content.substring(content.indexOf("]") + 1, content.indexOf("]") + 3);
    }

    /**
     * 本文の抽出
     * @param content
     * @return
     */
    private String extractSentence(TwitterRec rec) {
        String content = rec.getContentAsString();
        content = content.replaceAll("\"", " ");
        //  日本語のみを対象とするのでいつでも（言語はなし） + 1
        return content.substring(content.indexOf("]") + 1, content.length());
        //        return content.substring(content.indexOf("]") + 3, content.length());
    }

    /**
     * 日本語の名詞、動詞の抽出。それ以外はnull
     * @param token
     * @return
     */
    private String convJapaneseWord(Token token) {

        LOG.debug(token.getAllFeaturesArray()[0] + " surface=" + token.getSurface() + " base=" + token.getBaseForm()
                + " doubleByteCheck=" + String.valueOf(token.getSurface().toCharArray()[0]).getBytes().length);
        //        System.out.println(token.getAllFeaturesArray()[0] + ":" + token.getSurface() + ":" + token.getBaseForm() + ":"
        //                + String.valueOf(token.getSurface().toCharArray()[0]).getBytes().length);

        if ("名詞".equalsIgnoreCase(token.getAllFeaturesArray()[0])
                || "動詞".equalsIgnoreCase(token.getAllFeaturesArray()[0])) {
            if ("*".equalsIgnoreCase(token.getBaseForm())) {
                if (String.valueOf(token.getSurface().toCharArray()[0]).getBytes().length > 1) {
                    //  全角あり辞書なし
                    return token.getSurface();
                } else {
                    //  半角のみ辞書なし
                    return token.getSurface();
                }
            } else {
                //  辞書あり
                return token.getBaseForm();
            }
        }
        return null;
    }

    /**
     * ダブルバイト文字が含まれるかの判定。
     * @param str
     * @return
     */
    private boolean includeDoubleByteChar(String str) {
        return true;
//        if (str.toCharArray().length > 0) {
//            if (String.valueOf(str.toCharArray()[0]).getBytes().length > 1) {
//                return true;
//            }
//        }
//        return false;
    }

    /**
     * TF-IDF日別レコード。
     * 日付ごとにTF-IDFの大きい順に並んだレコードを作成。単語数は引数で指定した値。デフォルトは１００。
     * 出力カラムは２つのみで２つ目のカラムにカンマ区切りで単語が連結される。
     * @param in
     * @param out
     */
    @GroupSort
    public void reformatTop(@Key(group = "date", order = { "tf_idf DESC", "word ASC" }) List<DayWordCountModel> in,
            Result<DispDayWordModel> out) {
        int limit = 100;
        if (BatchContext.get(OUTPUT_LIMIT) != null) {
            limit = Integer.parseInt(BatchContext.get(OUTPUT_LIMIT));
        }
        int i = 0;
        StringBuffer sb = new StringBuffer();
        for (DayWordCountModel dwcm : in) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(dwcm.getWordAsString());
            i++;
            if (i > limit)
                break;
        }
        ddwm.setDate(in.get(0).getDate());
        ddwm.setWordsAsString(sb.toString());
        out.add(ddwm);
    }

}
