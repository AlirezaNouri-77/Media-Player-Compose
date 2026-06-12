package com.example.core.model

data class CurrentVideoInfo(
    var title: String,
    var videoID: String,
    var videoUri: String,
    var duration: Long,
) {
    companion object {
        var Empty = CurrentVideoInfo(title = "", videoID = "", videoUri = "", duration = 0)
    }
}
