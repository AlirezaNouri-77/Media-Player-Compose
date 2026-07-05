package com.shermanrex.feature.music_search.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shermanrex.core.designsystem.R
import com.shermanrex.core.designsystem.theme.MediaPlayerJetpackComposeTheme
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class)
@Composable
fun SearchTextFieldComponent(
    textFieldValue: String,
    onTextFieldChange: (String) -> Unit,
    onClear: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { value ->
                onTextFieldChange.invoke(value)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .padding(top = 8.dp)
                .focusRequester(focusRequester),
            singleLine = true,
            maxLines = 1,
            placeholder = {
                Text(text = "Enter a music name")
            },
            trailingIcon = {
                if (textFieldValue.isNotEmpty()) {
                    IconButton(
                        modifier = Modifier
                            .size(20.dp),
                        onClick = { onClear() },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = "Clear Search Field",
                        )
                    }
                }
            },
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.icon_search), contentDescription = "")
            },
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.45f),
                unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                cursorColor = MaterialTheme.colorScheme.onPrimary,
                focusedBorderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f),
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f),
            ),
        )
    }
}

@Preview
@Composable
private fun SearchTextFieldComponentPreview() {
    MediaPlayerJetpackComposeTheme {
        SearchTextFieldComponent(
            textFieldValue = "",
            onTextFieldChange = {},
            onClear = {},
        )
    }
}
