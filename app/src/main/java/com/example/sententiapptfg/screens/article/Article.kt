package com.example.sententiapptfg.screens.article

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.sententiapptfg.R
import com.example.sententiapptfg.data.SententiAppRepository
import com.example.sententiapptfg.data.SoapService
import com.example.sententiapptfg.data.models.Date
import com.example.sententiapptfg.data.models.Quote
import com.example.sententiapptfg.navigation.Footer
import com.example.sententiapptfg.data.UserInteractions
import kotlinx.coroutines.delay


val LocalArticleViewModel = compositionLocalOf<ArticleViewModel> {
    error("No ArticleViewModel provided")
}

@Composable
fun ArticleScreen(navController: NavHostController, viewModel: ArticleViewModel, dateId:Int){
    val scrollState = rememberScrollState()
    /*val viewModel = remember {
        ArticleViewModel(SententiAppRepository(SoapService()))
    }*/

    //println(">>> DATEID: $dateId")

    val date by viewModel.dateDetails.collectAsState()
    val quotes by viewModel.quotes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(dateId) {
        viewModel.loadDateDetails(dateId)
        viewModel.loadQuotes(dateId)

        kotlinx.coroutines.delay(500)
    }
    CompositionLocalProvider(LocalArticleViewModel provides viewModel){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (date != null && !isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize().background(Color.White).verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    ArticleBody(navController, date!!, quotes)
                    Spacer(modifier = Modifier.weight(1f))
                    Footer()
                }
            } else {
                CircularProgressIndicator(modifier = Modifier.testTag("loading"))
            }
        }
    }


}

@Composable
fun ArticleBody(navController: NavHostController, date: Date, quotes: List<Quote>){
    val goldenColor = Color(red = 225, green = 165, blue = 75)
    val context = LocalContext.current
    Box(
    ){
        Column (
            modifier = Modifier.fillMaxWidth()
        ){
            /*ARTICLE IMAGE*/
            ArticleImage(date)
            /*ARTICLE TITLE, DESCRIPTION AND QUOTES*/
            ArticleInfo(date, quotes)
            OtherDates(navController, date.id)
        }
        /*ARROW BACK HOME ICON*/
        IconButton(
            onClick = { navController.navigate("home") },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "ArrowBack",
                tint = Color.White            )
        }
    }
}

@Composable
private fun ArticleInfo(
    date: Date,
    quotes: List<Quote>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = date.tag, fontWeight = FontWeight.Bold,
            fontSize = 30.sp, lineHeight = 36.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = date.details, fontSize = 15.sp, fontWeight = FontWeight.Light)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Sentencias", fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Para ver las traducciones, deslice la sentencia a la izquierda o derecha con el dedo o haga clic en las flechas de los extremos.",
            fontSize = 15.sp,
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.height(20.dp))
        /* QUOTES */
        if (quotes.isEmpty()) {
            Text(
                text = "No hay sentencias disponibles",
                modifier = Modifier.testTag("empty_quotes"),
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quote_list")
            ) {
                for (quote in quotes) {
                    AddQuote(quote)
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }

    }
}

@Composable
private fun ArticleImage(date: Date) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        val imageUrl = "https://sententiapp.iatext.ulpgc.es/img/fechas/${date.image}"
        Image(
            painter = rememberAsyncImagePainter(
                model = imageUrl,
                error = painterResource(R.drawable.mockup)
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.1f),
                            Color.Black.copy(alpha = 0.6f)
                        ),
                        startY = 0f
                    )
                )
        )
        Image(
            painter = painterResource(id = R.drawable.pagina_rota),
            contentDescription = "logo",
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddQuote(quote: Quote) {
    //val viewModel = LocalArticleViewModel.current
    val goldenColor = Color(red = 225, green = 165, blue = 75)
    val context = LocalContext.current
    /*val viewModel = remember {
        ArticleViewModel(SententiAppRepository(SoapService()))
    }*/
    /*var loadedQuote by remember { mutableStateOf<Quote?>(null) }
    LaunchedEffect(quoteId) {
        viewModel.loadQuoteDetails(quoteId) { quoteDetails ->
            loadedQuote = quoteDetails
        }
    }
    if (loadedQuote == null) {
        /*WHILE LOADING*/
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    val quote = loadedQuote!!*/

    /*var likes by remember { mutableStateOf(quote.likes) }
    var favorites by remember { mutableStateOf(quote.favorites) }
    var funny by remember { mutableStateOf(quote.funny) }
    var dislikes by remember { mutableStateOf(quote.dislikes) }*/

    /* TEST INTERACTIONS*/
    var likes by remember { mutableStateOf(1) }
    var favorites by remember { mutableStateOf(1) }
    var funny by remember { mutableStateOf(1) }
    var dislikes by remember { mutableStateOf(1) }

    var currentLanguageIndex by remember { mutableStateOf(0) }
    val languages = listOf("Latin", "Spanish", "English")
    val currentQuoteText = when (currentLanguageIndex) {
        0 -> quote.latinQuote
        1 -> quote.spanishQuote
        2 -> quote.englishQuote
        else -> quote.latinQuote
    }

    val isLiked = UserInteractions.likedQuotes.contains(quote)
    val isFavourite = UserInteractions.favoriteQuotes.contains(quote)
    val isFunny = UserInteractions.funnyQuotes.contains(quote)
    val isDisliked = UserInteractions.dislikedQuotes.contains(quote)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(2.dp, Color(0xFFE1E1E1))
            /* SLIDER */
            /*.pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount > 50) {
                        // SWIPE RIGHT
                        currentLanguageIndex = (currentLanguageIndex - 1 + languages.size) % languages.size
                    } else if (dragAmount < -50) {
                        // SWIPE LEFT
                        currentLanguageIndex = (currentLanguageIndex + 1) % languages.size
                    }
                }
            }*/
    ) {
        /*LEFT ARROW*/
        IconButton(
            onClick = {
                currentLanguageIndex = (currentLanguageIndex - 1 + languages.size) % languages.size
            },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Previous Language", tint = goldenColor)
        }

        /*QUOTE CONTENT*/
        Column(
            modifier = Modifier
                .width(280.dp)
                .align(Alignment.Center)
                .padding(12.dp)
        ) {
            /* LANGUAGE SWITCH TEXT ANIMATION*/
            AnimatedContent(
                targetState = currentQuoteText,
                transitionSpec = {
                    fadeIn(tween(300)) with fadeOut(tween(300))
                },
                modifier = Modifier.fillMaxWidth()
            ) { targetText ->
                Text(
                    text = targetText,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }

            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = quote.author,
                    fontSize = 14.sp,
                    color = Color(0xFFE1A54B)
                )

                /*LIKES*/
                Icon(
                    imageVector = if (isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                    contentDescription = "QuoteLikes",
                    tint = Color(0xFFE1A54B),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            if (!isLiked) {
                                UserInteractions.likedQuotes.add(quote)
                                likes++
                            } else {
                                UserInteractions.likedQuotes.remove(quote)
                                likes--
                            }
                        }
                )
                Text(likes.toString(), fontSize = 12.sp)

                /*FAVORITES*/
                Icon(
                    imageVector = if (isFavourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "QuoteFavorites",
                    tint = Color(0xFFE1A54B),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            if (!isFavourite) {
                                UserInteractions.favoriteQuotes.add(quote)
                                favorites++
                            } else {
                                UserInteractions.favoriteQuotes.remove(quote)
                                favorites--
                            }
                        }
                )
                Text(favorites.toString(), fontSize = 12.sp)

                /*FUNNY*/
                Icon(
                    imageVector = if (isFunny) Icons.Filled.Face else Icons.Outlined.Face,
                    contentDescription = "QuoteFunny",
                    tint = Color(0xFFE1A54B),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            if (!isFunny) {
                                UserInteractions.funnyQuotes.add(quote)
                                funny++
                            } else {
                                UserInteractions.funnyQuotes.remove(quote)
                                funny--
                            }
                        }
                )
                Text(funny.toString(), fontSize = 12.sp)

                /*DISLIKES*/
                Icon(
                    imageVector = if (isDisliked) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown,
                    contentDescription = "QuoteDislikes",
                    tint = Color(0xFFE1A54B),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            if (!isDisliked) {
                                UserInteractions.dislikedQuotes.add(quote)
                                dislikes++
                            } else {
                                UserInteractions.dislikedQuotes.remove(quote)
                                dislikes--
                            }
                        }
                )
                Text(dislikes.toString(), fontSize = 12.sp)

                /*SHARE*/
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "ShareQuote",
                    tint = Color(0xFFE1A54B),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "\"$currentQuoteText\" - ${quote.author}")
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Compartir frase")
                            context.startActivity(shareIntent)
                        }
                )
            }

        }

        /*RIGHT ARROW*/
        IconButton(
            onClick = {
                currentLanguageIndex = (currentLanguageIndex + 1) % languages.size
            },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Next Language",  tint = goldenColor)
        }
    }
}

@Composable
fun OtherDates(navController: NavHostController, id: Int) {
    val viewModel = remember {
        ArticleViewModel(SententiAppRepository(SoapService()))
    }
    val dates by viewModel.otherDates.collectAsState()
    val listState = rememberLazyListState()
    LaunchedEffect(id) {
        viewModel.loadOtherDates(id)
    }

    // AUTO-SCROLL
    LaunchedEffect(dates) {
        if (dates.isNotEmpty()) {
            while (true) {
                delay(3000)
                val visibleItem = listState.firstVisibleItemIndex
                val nextIndex = if (visibleItem + 1 >= dates.size) 0 else visibleItem + 1
                listState.animateScrollToItem(nextIndex)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = "Otras fechas",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(20.dp))

        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            items(dates) { otherDate ->
                Box(
                    modifier = Modifier
                        .width(116.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { navController.navigate("articles/${otherDate.id}") }
                ) {
                    val imageUrl = "https://sententiapp.iatext.ulpgc.es/img/fechas/${otherDate.image}"
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
                                        Color.Black.copy(alpha = 0.1f),
                                        Color.Black.copy(alpha = 0.6f)
                                    ),
                                    startY = 0f
                                )
                            )
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Text(
                            text = otherDate.tag,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ArticlePreview(){
    //ArticleScreen(rememberNavController(), dateId = 0)
}