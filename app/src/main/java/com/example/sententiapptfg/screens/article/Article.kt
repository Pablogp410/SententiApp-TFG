package com.example.sententiapptfg.screens.article

import android.content.Intent
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sententiapptfg.R
import com.example.sententiapptfg.data.Quote
import com.example.sententiapptfg.navigation.Footer
import com.example.sententiapptfg.screens.UserInteractions
import com.example.sententiapptfg.screens.home.testArticles
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material3.Divider
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ArticleScreen(navController: NavHostController, id:String?){
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(Color.White).verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            ArticleBody(navController, id)
            Spacer(modifier = Modifier.weight(1f))
            Footer()
        }
    }

}

@Composable
fun ArticleBody(navController: NavHostController, id:String?){
    val articles = testArticles()
    val context = LocalContext.current
    Box(
    ){
        Column (
            modifier = Modifier.fillMaxWidth()
        ){
            for (article in articles){
                if (article.id==id){
                    /*ARTICLE IMAGE*/
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp)
                    ) {
                        val imageResId = context.resources.getIdentifier(article.imagePath, "drawable", context.packageName)
                        val mainImage = if (imageResId != 0) imageResId else R.drawable.mockup
                        Image(
                            painter = painterResource(mainImage),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Image(
                            painter = painterResource(id = R.drawable.pagina_rota),
                            contentDescription = "logo",
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                        )
                    }
                    /*ARTICLE TITLE, DESCRIPTION AND QUOTES*/
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(20.dp)
                    ) {
                        Text(text = article.title, fontWeight = FontWeight.Bold,
                            fontSize = 30.sp)
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text=article.description, fontSize = 15.sp, fontWeight = FontWeight.Light)
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text= "Sentencias", fontWeight = FontWeight.Bold,
                            fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text= "Para ver las traducciones, deslice la sentencia a la izquierda o derecha con el dedo o haga clic en las flechas de los extremos.",  fontSize = 15.sp, fontWeight = FontWeight.Light)
                        Spacer(modifier = Modifier.height(20.dp))
                        /*QUOTES*/
                        for (quote in article.quotes){
                            AddQuote(quote)
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                    OtherDates(navController, id)
                }
            }
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
                tint = Color.White
            )
        }
    }

}

@Composable
fun AddQuote(quote: Quote) {
    val context = LocalContext.current
    var likes by remember { mutableStateOf(quote.likes) }
    var favorites by remember { mutableStateOf(quote.favorites) }
    var funny by remember { mutableStateOf(quote.funny) }
    var dislikes by remember { mutableStateOf(quote.dislikes) }

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
    ) {
        /*LEFT ARROW*/
        IconButton(
            onClick = {
                currentLanguageIndex = (currentLanguageIndex - 1 + languages.size) % languages.size
            },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Previous Language")
        }

        /*QUOTE CONTENT*/
        Column(
            modifier = Modifier
                .width(280.dp)
                .align(Alignment.Center)
                .padding(12.dp)
        ) {
            Text(
                text = currentQuoteText,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

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
            Icon(Icons.Default.ArrowForward, contentDescription = "Next Language")
        }
    }
}


@Composable
fun OtherDates(navController: NavHostController,id:String?){
    val articles = testArticles()
    val context = LocalContext.current
    /*OTRAS FECHAS*/
    Column (
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(articles.filter { it.id != id }) { otherArticle ->
                Box(
                    modifier = Modifier
                        .width(110.dp)
                        .aspectRatio(1f)
                        .clickable { navController.navigate("articles/${otherArticle.id}") }
                ) {
                    val imageResId = context.resources.getIdentifier(otherArticle.imagePath, "drawable", context.packageName)
                    val articleImage = if (imageResId != 0) imageResId else R.drawable.mockup

                    Image(
                        painter = painterResource(id = articleImage),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Text(
                            text = otherArticle.title,
                            fontSize = 15.sp,
                            color = Color.White,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
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
    ArticleScreen(rememberNavController(), id="0")
}