package com.example.sententiapptfg.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun Menu(navController: NavHostController, closeDrawer: () -> Unit){
    val goldenColor = Color(red = 225, green = 165, blue = 75)

    Column(modifier = Modifier.fillMaxHeight()
        .width(280.dp)
        .background(Color.White)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close menu",
                tint = Color.Black,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { closeDrawer() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Menú",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }

        Divider()

        MenuItem("Inicio", onClick = {
            navController.navigate("home") {
                popUpTo("home") { inclusive = true }
            }
            closeDrawer()
        }, icon = Icons.Default.Home, color = goldenColor)

        MenuItem("Mis Sentencias", onClick = {
            navController.navigate("myquotes") {
                popUpTo("home")
            }
            closeDrawer()
        }, icon = Icons.Default.FormatQuote, color = goldenColor)
    }
}

@Composable
fun MenuItem(label: String, onClick: () -> Unit, icon: ImageVector, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = label, tint = color)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, fontSize = 18.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun MenuPreview(){
    Menu(rememberNavController(), closeDrawer = {} )
}
