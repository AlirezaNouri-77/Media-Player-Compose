package com.example.mediaplayerjetpackcompose.presentation.screenComponent

import androidx.compose.foundation.Image
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.mediaplayerjetpackcompose.domain.model.bottomNavigation.BottomNavigationModel

@Composable
fun BottomNavigationBar(
  currentRoute: String,
  onClick: (String) -> Unit,
) {
  
  val bottomNavigationItemList: List<BottomNavigationModel> = remember {
	listOf(
	  BottomNavigationModel.Video,
	  BottomNavigationModel.Music
	)
  }
  
  NavigationBar {
	bottomNavigationItemList.forEach {
	  NavigationBarItem(
		selected = currentRoute == it.route.route,
		alwaysShowLabel = false,
		onClick = {
		  if (currentRoute != it.route.route) {
			onClick.invoke(it.route.route)
		  }
		},
		icon = {
		  Image(painter = painterResource(id = it.icon), contentDescription = "")
		},
		label = { Text(text = it.title) }
	  )
	}
  }
  
}