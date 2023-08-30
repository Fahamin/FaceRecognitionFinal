package com.fahamin.facerecognition

import java.util.HashMap

data class ScanResultModel(
    var id: Int = 0,
    var resultData: HashMap<String, SimilarityClassifier.Recognition>? = null,
    var fileName: String = ""

)
