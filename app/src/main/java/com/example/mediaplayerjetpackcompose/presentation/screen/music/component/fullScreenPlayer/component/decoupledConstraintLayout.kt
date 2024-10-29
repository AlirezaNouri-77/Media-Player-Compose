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
    val titleRef = createRefFor("titleRef")
    val controllerRef = createRefFor("controllerRef")
    val pagerArtWork = createRefFor("pagerArtwork")

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {

      val bottomGuideline = createGuidelineFromBottom(70.dp)
      val topGuideline = createGuidelineFromTop(50.dp)
      val startGuideLine = createGuidelineFromStart(10.dp)
      val endGuideLine = createGuidelineFromEnd(10.dp)

      constrain(headerRef) {
        top.linkTo(topGuideline)
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
      constrain(titleRef) {
        start.linkTo(parent.start, margin = 20.dp)
        end.linkTo(parent.end, margin = 20.dp)
        bottom.linkTo(songDetail.top, margin = 10.dp)
      }
      constrain(songDetail) {
        start.linkTo(parent.start, margin = 20.dp)
        end.linkTo(parent.end, margin = 20.dp)
        bottom.linkTo(slider.top, margin = 10.dp)
        width = Dimension.fillToConstraints
      }
      constrain(slider) {
        start.linkTo(songDetail.start, margin = 10.dp)
        end.linkTo(songDetail.end, margin = 10.dp)
        bottom.linkTo(controllerRef.top, margin = 30.dp)
        width = Dimension.fillToConstraints
      }
      constrain(controllerRef) {
        start.linkTo(parent.start, margin = 10.dp)
        end.linkTo(parent.end, margin = 10.dp)
        bottom.linkTo(bottomGuideline)
      }

    } else {

      val bottomGuideline = createGuidelineFromBottom(20.dp)
      val topGuideline = createGuidelineFromTop(20.dp)
      val startGuideLine = createGuidelineFromStart(30.dp)
      val endGuideLine = createGuidelineFromEnd(40.dp)

      constrain(headerRef) {
        top.linkTo(topGuideline)
        start.linkTo(pagerArtWork.end)
        end.linkTo(endGuideLine)
        width = Dimension.fillToConstraints
      }
      constrain(pagerArtWork) {
        top.linkTo(parent.top)
        start.linkTo(startGuideLine)
        bottom.linkTo(parent.bottom)
      }
      constrain(titleRef) {
        top.linkTo(headerRef.bottom, margin = 30.dp)
        start.linkTo(pagerArtWork.end)
        end.linkTo(endGuideLine)
        width = Dimension.fillToConstraints
      }
      constrain(songDetail) {
        top.linkTo(titleRef.bottom)
        start.linkTo(titleRef.start, margin = 10.dp)
        end.linkTo(titleRef.end)
        width = Dimension.fillToConstraints
      }
      constrain(slider) {
        top.linkTo(songDetail.bottom, margin = 10.dp)
        start.linkTo(songDetail.start, margin = 10.dp)
        end.linkTo(songDetail.end, margin = 10.dp)
        bottom.linkTo(controllerRef.top)
        width = Dimension.fillToConstraints
      }
      constrain(controllerRef) {
        top.linkTo(slider.bottom)
        start.linkTo(slider.start)
        end.linkTo(slider.end)
        bottom.linkTo(parent.bottom, margin = 15.dp)
      }

    }
  }
}