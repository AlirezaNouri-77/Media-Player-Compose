package com.example.mediaplayerjetpackcompose.presentation.screen.music.component.topBar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mediaplayerjetpackcompose.R

@Composable
fun SearchSection(
  textFieldValue: String,
  onTextFieldChange: (String) -> Unit,
  onDismiss: () -> Unit,
) {
  OutlinedTextField(
    value = textFieldValue,
    onValueChange = { value ->
      onTextFieldChange.invoke(value)
    },
    modifier = Modifier
      .fillMaxWidth()
      .height(50.dp)
      .padding(horizontal = 15.dp),
    singleLine = true,
    maxLines = 1,
    placeholder = {
      Text(text = "Enter a music name")
    },
    trailingIcon = {
      if (textFieldValue.isNotEmpty()) {
        Icon(
          imageVector = Icons.Rounded.Clear,
          contentDescription = "Clear Search Field",
          modifier = Modifier
            .size(20.dp)
            .clickable {
              onTextFieldChange.invoke("")
            },
        )
      }
    },
    leadingIcon = {
      IconButton(onClick = { onDismiss() }, modifier = Modifier.size(35.dp)) {
        Icon(painter = painterResource(id = R.drawable.icon_right), contentDescription = "")
      }
    },
    shape = RoundedCornerShape(15.dp),
    colors = OutlinedTextFieldDefaults.colors(
      focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
      unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
      focusedTextColor = MaterialTheme.colorScheme.onPrimary,
      cursorColor = MaterialTheme.colorScheme.onPrimary,
      focusedBorderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f),
      unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f),
    )
  )
}