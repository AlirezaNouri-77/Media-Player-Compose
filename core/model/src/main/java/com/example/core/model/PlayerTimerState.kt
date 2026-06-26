package com.example.core.model

data class PlayerTimerState(
    val timerTimeLeft: Long,
    val isPauseEndOfMedia: Boolean,
    val playerTimerState: PlayerTimers,
) {
    companion object {
        val Initial = PlayerTimerState(
            timerTimeLeft = 0L,
            isPauseEndOfMedia = false,
            playerTimerState = PlayerTimers.INITIAL,
        )
    }
}

enum class PlayerTimers(val time: Int) {
    INITIAL(0),
    FIVE_MINUTE(5),
    TEN_MINUTE(10),
    TWELVE_MINUTE(20),
    THIRTY_MINUTE(30),
    FORTY_FIVE_MINUTE(45),
    ONE_HOUR(60),
    END_OFF_SONG(0),
    ;

    fun asString(timer: PlayerTimers): String {
        return when (timer) {
            FIVE_MINUTE -> "5 Minute"
            TEN_MINUTE -> "10 Minute"
            TWELVE_MINUTE -> "20 Minute"
            THIRTY_MINUTE -> "30 Minute"
            FORTY_FIVE_MINUTE -> "45 Minute"
            ONE_HOUR -> "1 Hour"
            END_OFF_SONG -> "Stop at end of song"
            else -> ""
        }
    }
}
