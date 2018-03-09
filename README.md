# AsakusaProject
## ビルド＆デプロイ
### eclipse使用
1. Jinrikisha（人力車）->Asakusaバッチアプリケーションを生成
2. Jinrikisha（人力車）->Asakusaバッチアプリケーションを生成->Asakusa開発環境の構成->バッチアプリケーションの配備

この手順で${ASAKUSA_HOME}以下に配備されます。

### shellコマンド
```sh
# プロジェクトディレクトリへ移動
cd mhir/wks/AsakusaProject/
./gradlew vanillaCompileBatchapps
./gradlew assemble
# Asakusaバッチを実行サーバへコピー
export TARGET_HOST=52.243.39.179
scp ~/mhir/wks/AsakusaProject/build/asakusafw-AsakusaProject.tar.gz nakazawa@${TARGET_HOST}:
# デプロイ
ssh nakazawa@${TARGET_HOST}
cd ${ASAKUSA_HOME}
tar -xzf ../asakusafw-AsakusaProject.tar.gz
java -jar ${ASAKUSA_HOME}/tools/bin/setup.jar

```

## データ配置
```sh
# 実行サーバ
export TARGET_HOST=52.243.39.179
# データを配置するディレクトリを予め作成。
ssh nakazawa@${TARGET_HOST}
mkdir -p ~/target/testing/directio/daily_rate_usd_jpy
mkdir -p ~/target/testing/directio/daily_daw_jones
mkdir -p ~/target/testing/directio/daily_nikkei_300
mkdir -p ~/target/testing/directio/twitter
logout
# ローカルのデータ格納ディレクトリへ移動
cd ~/Downloads/nikkei_for_asakusa
scp USD_JPY_for_asakusa.csv nakazawa@${TARGET_HOST}:target/testing/directio/daily_rate_usd_jpy/
scp Dow_Jones_for_asakusa.csv nakazawa@${TARGET_HOST}:target/testing/directio/daily_daw_jones/
scp nikkei_for_asakusa.csv nakazawa@${TARGET_HOST}:target/testing/directio/daily_nikkei_300/
scp Tweet.txt nakazawa@${TARGET_HOST}:target/testing/directio/twitter/
```

## 実行

```sh
ssh nakazawa@${TARGET_HOST}
${ASAKUSA_HOME}/yaess/bin/yaess-batch.sh vanilla.CombineBatch
```

## 結果データ

日別データ
~/target/testing/directio/result/summary/summary_data.csv

## 中間データ
日別単語別計算結果詳細
~/target/testing/directio/result/daywordcount/dwcm-{日付}.csv

日別特徴単語（TF-IDF）
~/target/testing/directio/result/disptfidf/disptfidf.csv

![設計フロー](./設計フロー.png)

![実装フロー](./CombineBatch.png)
