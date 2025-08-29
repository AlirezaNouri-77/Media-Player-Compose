package com.example.feature.music_player.fullScreen.component

import android.content.res.Configuration
import androidx.compose.ui.unit.dp

fun decoupledConstraintLayout(
    orientation: Int,
): androidx.constraintlayout.compose.ConstraintSet {
    return androidx.constraintlayout.compose.ConstraintSet {
        val headerRef = createRefFor("header")
        val songDetail = createRefFor("songDetail")
        val slider = createRefFor("slider")
        val controllerRef = createRefFor("controllerRef")
        val pagerArtWork = createRefFor("pagerArtwork")
        val volumeSlider = createRefFor("volumeSlider")

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            val topGuideline = createGuidelineFromTop(10.dp)
            val bottomGuideline = createGuidelineFromBottom(20.dp)
            val startGuideLine = createGuidelineFromStart(15.dp)
            val endGuideLine = createGuidelineFromEnd(15.dp)

            constrain(headerRef) {
                top.linkTo(parent.top)
                start.linkTo(startGuideLine)
                end.linkTo(endGuideLine)
                width = androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
            }
            constrain(pagerArtWork) {
                top.linkTo(headerRef.bottom)
                start.linkTo(startGuideLine)
                end.linkTo(endGuideLine)
                bottom.linkTo(songDetail.top, margin = 10.dp)
                height = androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
            }
            constrain(songDetail) {
                start.linkTo(startGuideLine)
                end.linkTo(endGuideLine)
                bottom.linkTo(slider.top, margin = 28.dp)
                width = androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
            }
            constrain(slider) {
                start.linkTo(startGuideLine)
                end.linkTo(endGuideLine)
                bottom.linkTo(controllerRef.top, margin = 5.dp)
                width = androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
                height = androidx.constraintlayout.compose.Dimension.Companion.wrapContent
            }
            constrain(controllerRef) {
                start.linkTo(startGuideLine)
                end.linkTo(endGuideLine)
                bottom.linkTo(volumeSlider.top, margin = 5.dp)
                width = androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
                height = androidx.constraintlayout.compose.Dimension.Companion.wrapContent
            }

            constrain(volumeSlider) {
                start.linkTo(startGuideLine)
                end.linkTo(endGuideLine)
                bottom.linkTo(bottomGuideline)
            }
        } else {
            val bottomGuideline = createGuidelineFromBottom(30.dp)
            val topGuideline = createGuidelineFromTop(30.dp)
            val endGuideLine = createGuidelineFromEnd(30.dp)

            constrain(headerRef) {
                top.linkTo(topGuideline)
                start.linkTo(pagerArtWork.end, margin = 30.dp)
                end.linkTo(parent.end, margin = 30.dp)
                width = androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
            }
            constrain(pagerArtWork) {
                top.linkTo(parent.top)
                start.linkTo(parent.start, margin = 5.dp)
                bottom.linkTo(parent.bottom)
            }
            constrain(songDetail) {
                start.linkTo(slider.start)
                end.linkTo(slider.end)
                bottom.linkTo(slider.top, margin = 15.dp)
                width = androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
            }
            constrain(slider) {
                start.linkTo(pagerArtWork.end, margin = 30.dp)
                end.linkTo(parent.end, margin = 30.dp)
                bottom.linkTo(controllerRef.top)
                width = androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
            }
            constrain(controllerRef) {
                start.linkTo(pagerArtWork.end, margin = 30.dp)
                end.linkTo(parent.end, margin = 30.dp)
                bottom.linkTo(volumeSlider.top, margin = 15.dp)
                width = androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
            }

            constrain(volumeSlider) {
                start.linkTo(slider.start)
                end.linkTo(slider.end)
                bottom.linkTo(bottomGuideline)
                width = androidx.constraintlayout.compose.Dimension.Companion.preferredWrapContent
            }
        }
    }
}
