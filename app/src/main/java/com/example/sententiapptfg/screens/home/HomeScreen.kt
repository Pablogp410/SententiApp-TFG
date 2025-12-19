package com.example.sententiapptfg.screens.home

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.navigation.NavHostController
import com.example.sententiapptfg.data.models.Quote
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.sententiapptfg.R
import com.example.sententiapptfg.data.SententiAppRepository
import com.example.sententiapptfg.data.SoapService
import com.example.sententiapptfg.data.models.Date
import com.example.sententiapptfg.navigation.Footer
import com.example.sententiapptfg.navigation.Menu
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    /*val viewModel = remember {
        HomeViewModel(SententiAppRepository(SoapService()))
    }*/

    val dates by viewModel.filteredDates
    val categories by viewModel.categories

    LaunchedEffect(Unit) {
        viewModel.loadDates()
        viewModel.loadCategories()
    }

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
                HomeBody(
                    dates = dates,
                    categories = categories,
                    onCategorySelected = { category ->
                        viewModel.loadDatesByCategory(category)
                    },
                    navController = navController,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.weight(1f))
                Footer()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBody(dates: List<Date>, categories: List<String>, onCategorySelected: (String?) -> Unit, navController: NavHostController, modifier: Modifier = Modifier) {
    val goldenColor = Color(red = 225, green = 165, blue = 75)
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val filteredDates = dates.filter { date ->
        date.tag.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = modifier.fillMaxWidth().padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /* LOGO */
        Row(horizontalArrangement = Arrangement.Center) {
            Image(
                painterResource(id = R.drawable.logotipo),
                contentDescription = "logo",
                modifier = Modifier.height(150.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        /* SEARCH BAR */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(1.dp, goldenColor, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 12.dp, vertical = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text("Buscar...", color = goldenColor, modifier=Modifier.fillMaxWidth())
                },
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    cursorColor = goldenColor,
                    focusedLabelColor = goldenColor,
                    unfocusedLabelColor = goldenColor
                ),
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(color = goldenColor),
            )
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = goldenColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        /* CATEGORIES */
        val categoriesPerPage = 3
        val totalPagesCategories = (categories.size + categoriesPerPage - 1) / categoriesPerPage
        var currentCategoryPage by remember { mutableIntStateOf(0) }

        val startIndex = currentCategoryPage * categoriesPerPage
        val endIndex = minOf(startIndex + categoriesPerPage, categories.size)
        val visibleCategories = categories.subList(startIndex, endIndex)

        val borderStroke = BorderStroke(1.dp, goldenColor)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // PREV BUTTON
            if (totalPagesCategories > 1) {
                IconButton(
                    onClick = {
                        currentCategoryPage = if (currentCategoryPage > 0) currentCategoryPage - 1 else totalPagesCategories - 1
                    }
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Previous", tint = goldenColor)
                }
            }

            LazyRow(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 0.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item {
                    val isSelected = selectedCategory == null
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedCategory = null
                            onCategorySelected(null)
                        },
                        label = { Text("Todos") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = goldenColor,
                            selectedLabelColor = Color.White,
                            containerColor = Color.Transparent,
                            labelColor = goldenColor
                        ),
                        border = if (isSelected) null else borderStroke
                    )
                }

                items(visibleCategories) { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedCategory = category
                            onCategorySelected(category)
                        },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = goldenColor,
                            selectedLabelColor = Color.White,
                            containerColor = Color.Transparent,
                            labelColor = goldenColor
                        ),
                        border = if (isSelected) null else borderStroke
                    )
                }
            }

            // NEXT BUTTON
            if (totalPagesCategories > 1) {
                IconButton(
                    onClick = {
                        currentCategoryPage = (currentCategoryPage + 1) % totalPagesCategories
                    }
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Next", tint = goldenColor)
                }
            }
        }



        /* DATES LIST */
        val pageSize = 10
        var currentPage by remember { mutableIntStateOf(0) }

        LaunchedEffect(selectedCategory) {
            currentPage = 0
        }

        val totalPages = (filteredDates.size + pageSize - 1) / pageSize
        val pagedDates = filteredDates.drop(currentPage * pageSize).take(pageSize)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            for (date in pagedDates) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.1f))
                        .clickable { navController.navigate("articles/${date.id}") }
                        .shadow(4.dp, RoundedCornerShape(12.dp))
                ) {
                    val imageUrl = "https://sententiapp.iatext.ulpgc.es/img/fechas/${date.image}"
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = imageUrl,
                            error = painterResource(R.drawable.mockup)
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.2f),
                                        Color.Black.copy(alpha = 0.7f)
                                    ),
                                    startY = 0f
                                )
                            )
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = imageUrl,
                                error = painterResource(R.drawable.mockup)
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth().padding(end = 4.dp)
                        ) {
                            Text(
                                text = date.tag,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                lineHeight = 18.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = date.day,
                                color = goldenColor,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
            // PAGINATION
            if (totalPages > 1) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { if (currentPage > 0) currentPage-- },
                        enabled = currentPage > 0,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = goldenColor,
                            contentColor = Color.White,
                            disabledContainerColor = goldenColor.copy(alpha = 0.4f),
                            disabledContentColor = Color.White.copy(alpha = 0.6f)
                        )
                    ) {
                        Text("Anterior")
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text("Página ${currentPage + 1} de $totalPages")

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = { if (currentPage < totalPages - 1) currentPage++ },
                        enabled = currentPage < totalPages - 1,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = goldenColor,
                            contentColor = Color.White,
                            disabledContainerColor = goldenColor.copy(alpha = 0.4f),
                            disabledContentColor = Color.White.copy(alpha = 0.6f)
                        )
                    ) {
                        Text("Siguiente")
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    //HomeScreen(rememberNavController(), HomeViewModel(SententiAppRepository(SoapService())))
}