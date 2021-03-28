package com.example.for62nd

// RecyclerViewの各項目のデータを保持する構造体のような役割。Javaでは普通にclassでやってたけどKotlinはそれ専用のdata classというものがあったのでそれ使った。
data class MemoStructure(var id: Int,var title: String,var detail: String)