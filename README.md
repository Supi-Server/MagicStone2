# 使用方法

1. Pluginsフォルダに.jarファイルを設置
2. plugins/MagicStoneに所定の`data_table.xlsx`を配置
3. 再起動

# config.yml

`data_table_path`: **data_table.xlsx**のファイルパス  
`blocks`: 魔法石が落ちるブロックのID (MaterialIDをいれること)  
`worlds`: 魔法石の落ちるワールド名

### 記入例

```
data_table_path: "plugins/MagicStone/data_table.xlsx"

blocks:
  - "STONE"
  - "ANDESITE"
  - "DIORITE"
  - "GRANITE"
  - "TUFF"
  - "CALCITE"
  - "DEEPSLATE"

worlds:
  - "shigen"
  - "world"
```

# コマンド

`/magicstone reload`: config.yml及びExcelを再読み込みします  
`/magicstone error`: エラー一覧の出力  
`/magicstone print [<Material>] [<Rank>] [<FortuneLevel>]`: 登録データの確認(任意引数未指定で全件表示)  
`/magicstone challenge <Material> <Rank> <FortuneLevel> [<hit_number>]`: テスト抽選  
`/magicstone get <id>`: 指定idの魔法石を取得します  
