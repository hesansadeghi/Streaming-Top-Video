package com.example.streamingtopvideo.presentaion.main.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.streamingtopvideo.models.NavigationItem
import com.example.streamingtopvideo.presentaion.ui.theme.EbonyClay
import com.example.streamingtopvideo.presentaion.ui.theme.Platinum


@Composable
fun NavBar(
    navController: NavHostController,
    selectedMenu: String,
    isSelectedMenu: (String) -> Unit
) {

    val menu = listOf(
        NavigationItem.Home,
        NavigationItem.Favorite,
        NavigationItem.Downloads,
    )



    BottomAppBar(
        containerColor = EbonyClay,
    ) {


        menu.forEach { navigationItem ->

            val route = navigationItem.route

            NavigationBarItem(
                selected = selectedMenu == route,
                onClick = {
                    isSelectedMenu(route)
//                    selectedMenu = index
                    navController.navigate(route)
                },
                icon = {

                    Icon(
                        painterResource(navigationItem.icon),
                        contentDescription = navigationItem.title,
                        modifier = Modifier.size(
                            if (selectedMenu == route) {
                                30.dp
                            } else {
                                25.dp
                            }
                        )
                    )
                },
                label = {

                    Text(text = navigationItem.title)
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor = Color.White,
                    unselectedTextColor = Platinum,
                    selectedIconColor = Color.White,
                    unselectedIconColor = Platinum,
                    indicatorColor = Color.Transparent
                )
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    NavBar(rememberNavController(), NavigationItem.Home.route) {}
}