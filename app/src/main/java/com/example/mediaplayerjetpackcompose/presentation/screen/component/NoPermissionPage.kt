package com.example.mediaplayerjetpackcompose.presentation.screen.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.mediaplayerjetpackcompose.R
import com.example.mediaplayerjetpackcompose.presentation.checkPermission
import com.example.mediaplayerjetpackcompose.ui.theme.MediaPlayerJetpackComposeTheme

@Composable
fun NoPermissionPage(
  onGrant: () -> Unit,
  currentLocalLifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  context: Context = LocalContext.current,
) {

  DisposableEffect(
    key1 = currentLocalLifecycleOwner,
  ) {
    val observer = LifecycleEventObserver { _, event ->
      if (event == Lifecycle.Event.ON_RESUME) {
        if (checkPermission(context)) {
          onGrant.invoke()
        }
      }
    }
    currentLocalLifecycleOwner.lifecycle.addObserver(observer)
    onDispose {
      currentLocalLifecycleOwner.lifecycle.removeObserver(observer)
    }
  }

  Surface(
    color = MaterialTheme.colorScheme.primary,
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 15.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
        text = "Simple Media Player",
        fontSize = 35.sp, fontWeight = FontWeight.Bold,
      )
      Spacer(modifier = Modifier.height(90.dp))
      Image(
        painter = painterResource(id = R.drawable.warning_icon),
        contentDescription = "",
        Modifier.aspectRatio(3f)
      )
      Spacer(modifier = Modifier.height(20.dp))
      Text(
        text = "This app need to storage permission for Work properly",
        fontSize = 18.sp
      )
      Spacer(modifier = Modifier.height(20.dp))
      Button(onClick = { openPermissionSetting(context) }) {
        Text(text = "Go to setting")
      }
    }
  }

}

fun openPermissionSetting(context: Context) {
  val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
  val uri = Uri.fromParts("package", context.packageName, null)
  intent.setData(uri)
  context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
fun PreviewNoPermissionPage() {
  MediaPlayerJetpackComposeTheme {
    NoPermissionPage(onGrant = {})
  }
}