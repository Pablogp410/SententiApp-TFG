package com.example.sententiapptfg.screens.myquotes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sententiapptfg.navigation.Footer
import com.example.sententiapptfg.navigation.Menu
import com.example.sententiapptfg.screens.UserInteractions
import com.example.sententiapptfg.screens.article.AddQuote
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyQuotesScreen(navController: NavHostController){
    val scrollState = rememberScrollState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Menu(
                navController = navController,
                closeDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = { Text("Mis Sentencias") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Open menu")
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = Color.Black
                    )
                )
            },
            containerColor = Color.White
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MyQuotesBody(navController = navController, modifier = Modifier)
                Footer()
            }
        }
    }

}

@Composable
fun MyQuotesBody(navController: NavHostController, modifier: Modifier) {
    /*VARIABLES*/
    val filters = listOf("Me gusta", "Favoritas", "Divertidas", "No me gusta")
    var selectedFilter by remember { mutableStateOf<String?>(null) }
    val goldenColor = Color(red = 225, green = 165, blue = 75)
    val filteredQuotes = when (selectedFilter) {
        "Me gusta" -> UserInteractions.likedQuotes
        "Favoritas" -> UserInteractions.favoriteQuotes
        "Divertidas" -> UserInteractions.funnyQuotes
        "No me gusta" -> UserInteractions.dislikedQuotes
        null -> UserInteractions.allQuotes
        else -> emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /*Text(
            text = "Mis sentencias",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp
        )*/

        Spacer(modifier = Modifier.height(16.dp))

        /*FILTERS*/
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedFilter == null,
                    onClick = { selectedFilter = null },
                    label = { Text("Todas") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = goldenColor,
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFFF0F0F0),
                        labelColor = Color.Black
                    )
                )
            }
            items(filters) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = goldenColor,
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFFF0F0F0),
                        labelColor = Color.Black
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredQuotes.isEmpty()) {
            Text("No has interactuado con ninguna sentencia todavía.")
        } else {
            filteredQuotes.forEach { quote ->
                AddQuote(quote.id)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MyQuotesPreview(){
    MyQuotesScreen(rememberNavController())
}