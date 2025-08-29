package com.example.core.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.core.model.datastore.SortType

@Composable
fun SortDropDownMenu(
    modifier: Modifier = Modifier,
    isExpand: Boolean,
    sortTypeList: List<SortType>,
    isSortDescending: Boolean,
    currentSortType: SortType,
    onSortClick: (SortType) -> Unit,
    onOrderClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    DropdownMenu(
        modifier = modifier,
        expanded = isExpand,
        shape = RoundedCornerShape(10.dp),
        onDismissRequest = { onDismiss() },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
    ) {
        sortTypeList.forEachIndexed { _, sortType ->
            DropdownMenuItem(
                modifier = Modifier.background(
                    if (currentSortType == sortType) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f) else Color.Transparent,
                    shape = RoundedCornerShape(10.dp),
                ),
                text = {
                    Text(text = sortType.getString())
                },
                onClick = {
                    onSortClick(sortType)
                },
                colors = MenuDefaults.itemColors(
                    textColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        }
        Spacer(Modifier.height(5.dp))
        DropdownMenuItem(
            text = {
                Text(text = if (isSortDescending) "Dec" else "Acs")
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = if (isSortDescending) R.drawable.icon_sort_desc else R.drawable.icon_sort_asce),
                    contentDescription = "",
                )
            },
            colors = MenuDefaults.itemColors(
                textColor = MaterialTheme.colorScheme.onPrimary,
            ),
            onClick = { onOrderClick() },
        )
    }
}
