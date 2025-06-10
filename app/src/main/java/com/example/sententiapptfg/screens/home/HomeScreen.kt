package com.example.sententiapptfg.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.sententiapptfg.data.Article
import com.example.sententiapptfg.data.Quote
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.sententiapptfg.R
import com.example.sententiapptfg.data.ArticleCategory
import com.example.sententiapptfg.navigation.Footer
import com.example.sententiapptfg.navigation.Menu
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
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
                    title = { Text("Inicio") },
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
                HomeBody(navController = navController, modifier = Modifier)
                Footer()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBody(modifier: Modifier, navController: NavHostController){
    /*LOADING DATA*/
    val goldenColor = Color(red = 225, green = 165, blue = 75)
    val articles = testArticles()
    val categories = articles.map { it.category }.distinct()
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ArticleCategory?>(null) }
    val filteredArticles = articles.filter { article ->
        (selectedCategory == null || article.category == selectedCategory) &&
                article.title.contains(searchQuery, ignoreCase = true)
    }
    Column (
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        /*LOGO*/
        Row(
            horizontalArrangement = Arrangement.Center
        ){
            Image(painterResource(id = R.drawable.logotipo),
                contentDescription = "logo",
                modifier = Modifier.height(150.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        /*SEARCH BAR*/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(20.dp))
                .border(1.dp, Color.Gray, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar...") },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 1,
                singleLine = true
            )

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.Gray,
                modifier = Modifier
                    .size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        /*CATEGORIES*/
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { selectedCategory = null },
                    label = { Text("Todos") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = goldenColor,
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFFF0F0F0),
                        labelColor = Color.Black
                    )
                )
            }

            items(categories) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category.name) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = goldenColor,
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFFF0F0F0),
                        labelColor = Color.Black
                    )
                )
            }
        }
        /*ARTICLES*/
        Column {
            for (article in filteredArticles) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.1f)) // fallback si no carga la imagen
                        .clickable { navController.navigate("articles/${article.id}") }
                        .shadow(4.dp, RoundedCornerShape(12.dp))
                ) {
                    val imageResId = context.resources.getIdentifier(article.imagePath, "drawable", context.packageName)
                    val mainImage = if (imageResId != 0) imageResId else R.drawable.mockup

                    Image(
                        painter = painterResource(mainImage),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(12.dp))
                    )

                    /*Overlay*/
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                                startY = 50f
                            ))
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal= 16.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        /*ICON IMAGE*/
                        Image(
                            painter = painterResource(mainImage),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        /*TITLE AND DATE*/
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(end = 4.dp)
                        ) {
                            Text(
                                text = article.title,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                lineHeight = 18.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = article.date,
                                color = goldenColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun testArticles() : List<Article>{
    val testQuote = Quote(id = "0", latinQuote = "Sentencia en latín", spanishQuote = "Sentencia en español", englishQuote = "Sentencia en inglés", author = "author", likes = 0, favorites = 0, dislikes = 0, funny = 0)
    val testQuote2 = Quote(id = "3", latinQuote = "Lorem Ipsum etc", spanishQuote = "Sentencia en español", englishQuote = "Sentencia en inglés", author = "Miguel", likes = 3, favorites = 2, dislikes = 1, funny = 0)
    val testList = listOf(testQuote, testQuote2)
    val article1 = Article(id = "0", title = "Día Internacional de la Educación", description = "Descripción del artículo día internacional de la educación", quotes = testList, date = "24 de Enero de 2024", imagePath = "mockup", category = ArticleCategory.Amor)
    val article2 = Article(id = "1", title = "Día Internacional de la Codicia", description = "Descripción del artículo día internacional de la codicia", quotes = testList, date = "29 de Febrero de 2024", imagePath = "mockup",  category = ArticleCategory.Codicia)
    val article3 = Article(id = "3", title = "Día Internacional de la Educación con un titulo mas largo", description = "Descripción del artículo día internacional de la educación", quotes = testList, date = "24 de Enero de 2024", imagePath = "mockup", category = ArticleCategory.Amor)
    val article4 = Article(id = "345", title = "Día Internacional de la Educación", description = "Descripción del artículo día internacional de la educación", quotes = testList, date = "24 de Enero de 2024", imagePath = "mockup", category = ArticleCategory.Amor)
    val article5 = Article(id = "1", title = "Día Internacional de la Codicia con un titulo mas largo", description = "Descripción del artículo día internacional de la codicia", quotes = testList, date = "29 de Febrero de 2024", imagePath = "mockup",  category = ArticleCategory.Codicia)
    val article6 = Article(id = "1", title = "Día Internacional de los Defectos", description = "Descripción del artículo día internacional de la codicia", quotes = testList, date = "29 de Febrero de 2024", imagePath = "mockup",  category = ArticleCategory.Defectos)
    val articlesList = listOf(article1,article2,article3,article4,article5,article6)
    return articlesList
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen(rememberNavController())
}