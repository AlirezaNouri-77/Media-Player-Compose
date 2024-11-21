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

      val topGuideline = createGuidelineFromTop(0.15f)
      val bottomGuideline = createGuidelineFromBottom(0.06f)
      val startGuideLine = createGuidelineFromStart(15.dp)
      val endGuideLine = createGuidelineFromEnd(15.dp)

      constrain(headerRef) {
        top.linkTo(parent.top, margin = 5.dp)
        start.linkTo(startGuideLine)
        end.linkTo(endGuideLine)
        width = Dimension.fillToConstraints
      }
      constrain(pagerArtWork) {
        top.linkTo(topGuideline)
        start.linkTo(startGuideLine)
        end.linkTo(endGuideLine)
        bottom.linkTo(songDetail.top, margin = 10.dp)
      }
      constrain(songDetail) {
        top.linkTo(pagerArtWork.bottom)
        start.linkTo(startGuideLine)
        end.linkTo(endGuideLine)
        bottom.linkTo(slider.top, margin = 25.dp)
        width = Dimension.fillToConstraints
      }
      constrain(slider) {
        start.linkTo(startGuideLine)
        end.linkTo(endGuideLine)
        bottom.linkTo(controllerRef.top, margin = 10.dp)
        width = Dimension.fillToConstraints
        height = Dimension.wrapContent
      }
      constrain(controllerRef) {
        start.linkTo(startGuideLine, margin = 10.dp)
        end.linkTo(endGuideLine, margin = 10.dp)
        bottom.linkTo(volumeSlider.top, margin = 10.dp)
      }

      constrain(volumeSlider) {
        start.linkTo(controllerRef.start)
        end.linkTo(controllerRef.end)
        bottom.linkTo(bottomGuideline)
      }

    } else {

      val bottomGuideline = createGuidelineFromBottom(30.dp)
      val topGuideline = createGuidelineFromTop(40.dp)
      val endGuideLine = createGuidelineFromEnd(40.dp)

      constrain(headerRef) {
        top.linkTo(topGuideline)
        start.linkTo(pagerArtWork.end)
        end.linkTo(endGuideLine)
        width = Dimension.fillToConstraints
      }
      constrain(pagerArtWork) {
        top.linkTo(parent.top)
        start.linkTo(parent.start, margin = 5.dp)
        bottom.linkTo(parent.bottom)
      }
      constrain(songDetail) {
        start.linkTo(slider.start, margin = 10.dp)
        end.linkTo(slider.end)
        bottom.linkTo(slider.top)
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
        start.linkTo(controllerRef.start)
        end.linkTo(controllerRef.end)
        bottom.linkTo(bottomGuideline)
        width = Dimension.fillToConstraints
      }
    }
  }
}