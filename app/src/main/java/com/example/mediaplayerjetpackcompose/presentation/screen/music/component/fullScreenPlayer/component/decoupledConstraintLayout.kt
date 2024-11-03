package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.fullScreenPlayer.component

import android.content.res.Configuration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension

fun decoupledConstraintLayout(
  orientation:Int,
): ConstraintSet {
  return ConstraintSet {

    val headerRef = createRefFor("header")
    val songDetail = createRefFor("songDetail")
    val slider = createRefFor("slider")
    val controllerRef = createRefFor("controllerRef")
    val pagerArtWork = createRefFor("pagerArtwork")

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {

      val bottomGuideline = createGuidelineFromBottom(70.dp)
      val startGuideLine = createGuidelineFromStart(10.dp)
      val endGuideLine = createGuidelineFromEnd(10.dp)

      constrain(headerRef) {
        top.linkTo(parent.top, margin = 5.dp)
        start.linkTo(startGuideLine)
        end.linkTo(endGuideLine)
        width = Dimension.fillToConstraints
      }
      constrain(pagerArtWork) {
        top.linkTo(headerRef.bottom, margin = 40.dp)
        start.linkTo(startGuideLine)
        end.linkTo(endGuideLine)
        bottom.linkTo(songDetail.top, margin = 10.dp)
      }
      constrain(songDetail) {
        start.linkTo(parent.start, margin = 25.dp)
        end.linkTo(parent.end, margin = 25.dp)
        bottom.linkTo(slider.top, margin = 10.dp)
        width = Dimension.fillToConstraints
      }
      constrain(slider) {
        start.linkTo(songDetail.start)
        end.linkTo(songDetail.end)
        bottom.linkTo(controllerRef.top, margin = 30.dp)
        width = Dimension.fillToConstraints
      }
      constrain(controllerRef) {
        start.linkTo(parent.start, margin = 10.dp)
        end.linkTo(parent.end, margin = 10.dp)
        bottom.linkTo(bottomGuideline)
      }

    } else {

      val bottomGuideline = createGuidelineFromBottom(0.2f)
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
        bottom.linkTo(bottomGuideline)
      }

    }
  }
}