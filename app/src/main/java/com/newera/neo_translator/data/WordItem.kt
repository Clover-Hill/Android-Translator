package com.newera.neo_translator.data

data class WordItem(
    val dict: Dict,
    val errorCode: String,
    val query: String,
    val basic: Basic,
    val l: String,
    val speakUrl: String,
    val tSpeakUrl: String,
    val translation: List<String>,
    val webdict: Webdict
)