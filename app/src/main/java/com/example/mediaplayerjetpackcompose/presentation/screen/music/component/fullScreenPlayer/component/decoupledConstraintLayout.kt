package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component

import android.content.res.Configuration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension

fun decoupledConstraintLayout(
  orientation: Int,
): ConstraintSet {
  return ConstraintSet {

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
        width = Dimension.fillToConstraints
      }
      constrain(pagerArtWork) {
        top.linkTo(headerRef.bottom)
        start.linkTo(startGuideLine)
        end.linkTo(endGuideLine)
        bottom.linkTo(songDetail.top, margin = 10.dp)
        height = Dimension.fillToConstraints
      }
      constrain(songDetail) {
        start.linkTo(startGuideLine)
        end.linkTo(endGuideLine)
        bottom.linkTo(slider.top, margin = 15.dp)
        width = Dimension.fillToConstraints
      }
      constrain(slider) {
        start.linkTo(startGuideLine)
        end.linkTo(endGuideLine)
        bottom.linkTo(controllerRef.top, margin = 5.dp)
        width = Dimension.fillToConstraints
        height = Dimension.wrapContent
      }
      constrain(controllerRef) {
        start.linkTo(startGuideLine)
        end.linkTo(endGuideLine)
        bottom.linkTo(volumeSlider.top, margin = 5.dp)
      }

      constrain(volumeSlider) {
        start.linkTo(startGuideLine)
        end.linkTo(endGuideLine)
        bottom.linkTo(bottomGuideline)
      }

    } else {

      val bottomGuideline = createGuidelineFromBottom(30.dp)
      val topGuideline = createGuidelineFromTop(40.dp)
      val endGuideLine = createGuidelineFromEnd(40.dp)

      constrain(headerRef) {
        top.linkTo(topGuideline)
        start.linkTo(pagerArtWork.end, margin = 20.dp)
        end.linkTo(endGuideLine)
        width = Dimension.fillToConstraints
      }
      constrain(pagerArtWork) {
        top.linkTo(parent.top)
        start.linkTo(parent.start, margin = 5.dp)
        bottom.linkTo(parent.bottom)
      }
      constrain(songDetail) {
        start.linkTo(slider.start)
        end.linkTo(slider.end)
        bottom.linkTo(slider.top, margin = 5.dp)
        width = Dimension.fillToConstraints
      }
      constrain(slider) {
        start.linkTo(pagerArtWork.end, margin = 30.dp)
        end.linkTo(parent.end, margin = 30.dp)
        bottom.linkTo(controllerRef.top)
        width = Dimension.fillToConstraints
      }
      constrain(controllerRef) {
        start.linkTo(pagerArtWork.end)
        end.linkTo(parent.end)
        bottom.linkTo(volumeSlider.top, margin = 15.dp)
      }

      constrain(volumeSlider) {
        start.linkTo(slider.start)
        end.linkTo(slider.end)
        bottom.linkTo(bottomGuideline)
        width = Dimension.preferredWrapContent
      }
    }
  }
}