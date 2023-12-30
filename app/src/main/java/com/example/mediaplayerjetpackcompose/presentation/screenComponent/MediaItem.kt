package com.example.mediaplayerjetpackcompose.presentation.screenComponent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mediaplayerjetpackcompose.data.convertByteToReadableSize
import com.example.mediaplayerjetpackcompose.data.convertMilliSecondToTime
import com.example.mediaplayerjetpackcompose.domain.model.VideoMediaModel

@Composable
fun MediaItem(
  item: VideoMediaModel,
  onItemClick: (VideoMediaModel) -> Unit,
) {
  Surface(
	onClick = { onItemClick.invoke(item) },
	modifier = Modifier
	  .fillMaxWidth()
	  .padding(5.dp),
  ) {
	Row(
	  verticalAlignment = Alignment.CenterVertically,
	  horizontalArrangement = Arrangement.Start,
	) {
	  Image(
		contentScale = ContentScale.FillBounds,
		modifier = Modifier
		  .size(width = 150.dp, height = 90.dp)
		  .background(color = Color.Black, shape = RoundedCornerShape(15.dp)),
		bitmap = item.image!!.asImageBitmap(),
		contentDescription = "",
	  )
	  Spacer(modifier = Modifier.width(10.dp))
	  Column {
		Text(
		  text = item.name,
		  fontSize = 16.sp,
		  fontWeight = FontWeight.Medium,
		)
		Spacer(modifier = Modifier.height(3.dp))
		Text(
		  modifier = Modifier.fillMaxWidth(),
		  textAlign = TextAlign.End,
		  text = item.size.convertByteToReadableSize(),
		  fontSize = 14.sp,
		)
		Text(
		  modifier = Modifier.fillMaxWidth(),
		  textAlign = TextAlign.End,
		  text = item.duration.convertMilliSecondToTime(),
		  fontSize = 14.sp,
		)
	  }
	}
  }
  
}