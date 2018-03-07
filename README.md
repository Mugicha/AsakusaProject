# AsakusaProject
## ビルド＆デプロイ
### eclipse使用
1. Jinrikisha（人力車）->Asakusaバッチアプリケーションを生成
2. Jinrikisha（人力車）->Asakusaバッチアプリケーションを生成->Asakusa開発環境の構成->バッチアプリケーションの配備

この手順で${ASAKUSA_HOME}以下に配備されます。

### shellコマンド
```shell:デプロイ
cd ~/$ASAKUSA_HOME
tar -xzf ~/TwTfIdf/build//asakusafw-TwTfIdf.tar.gz
java -jar $ASAKUSA_HOME/tools/bin/setup.jar```

## データ配置

## 実行
```shell:実行
{ASAKUSA_HOME}/yaess/bin/yaess-batch.sh vanilla.TwTfIdfBatch```
