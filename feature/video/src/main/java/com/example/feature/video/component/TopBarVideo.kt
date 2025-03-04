package com.example.feature.video.component

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.feature.video.isPermissionGrant
import com.example.feature.video.util.Constant
import com.example.feature.video.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarVideo(
  modifier: Modifier = Modifier,
  onBackClick: () -> Unit,
  onSelectVideo: () -> Unit,
  context: Context,
) {

  TopAppBar(
    modifier = modifier,
    title = {
      Text(
        text = "Video",
        modifier = Modifier,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
      )
    },
    navigationIcon = {
      IconButton(
        modifier = Modifier
          .size(35.dp),
        onClick = { onBackClick.invoke() },
      ) {
        Icon(
          painter = painterResource(id = R.drawable.icon_back_24),
          contentDescription = "",
        )
      }
    },
    actions = {
      if (Build.VERSION.SDK_INT >= Constant.API_34_UPSIDE_DOWN_CAKE_ANDROID_14 && !context.isPermissionGrant(Manifest.permission.READ_MEDIA_VIDEO)) {
        TextButton(
          modifier = Modifier
            .wrapContentWidth(),
          onClick = {
            onSelectVideo()
          },
        ) {
          Text(text = "Select Videos", fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimary)
        }
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = Color.Transparent,
      titleContentColor = MaterialTheme.colorScheme.onPrimary,
      navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
    )
  )

}