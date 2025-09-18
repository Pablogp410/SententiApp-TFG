package com.example.sententiapptfg.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.sententiapptfg.R
import com.example.sententiapptfg.screens.home.HomeScreen

@Composable
fun Footer(){
    val maxImageHeight = 20.dp
    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ){
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Image(
                painterResource(id = R.drawable.pagina_rota),
                contentDescription = "logo",
                modifier = Modifier.height(50.dp)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(id = R.drawable.iatext_pie),
                contentDescription = "logo",
                modifier = Modifier.heightIn(max = maxImageHeight)
            )
            Image(
                painterResource(id = R.drawable.logofooter2),
                contentDescription = "logo",
                modifier = Modifier.heightIn(max = maxImageHeight)
            )
            Image(
                painterResource(id = R.drawable.logofooter3),
                contentDescription = "logo",
                modifier = Modifier.heightIn(max = maxImageHeight)
            )
            Image(
                painterResource(id = R.drawable.logofooter4),
                contentDescription = "logo",
                modifier = Modifier.heightIn(max = maxImageHeight)
            )
            Image(
                painterResource(id = R.drawable.clariah_es_marca_vertical),
                contentDescription = "logo",
                modifier = Modifier.heightIn(max = maxImageHeight)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FooterPreview(){
    Footer()
}
