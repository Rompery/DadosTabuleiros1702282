package com.example.dadostabuleiros

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dadostabuleiros.ui.theme.DadosTabuleirosTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DadosTabuleirosTheme {
                val navController = rememberNavController()
                Surface(color = MaterialTheme.colorScheme.background) {
                    NavHost(navController, startDestination = "main_screen") {
                        composable("main_screen") { MainScreen(navController) }
                        composable("game_screen") { GameScreen(navController) }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
       ) {
        Image(
            painter = painterResource(id = R.drawable.d_dice_outdoors),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Button(onClick = { navController.navigate("game_screen") }) {
                Text(text = "Iniciar Jogo")
            }
        }

    }

}
@Composable
fun GameScreen(navController: NavHostController) { // Adicionado navController
    var diceValue by remember { mutableIntStateOf(1) }
    var playerPosition by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.view_3d_dice_with_abstract_scenery),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Conteúdo da tela do jogo...
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Board(playerPosition)
            Spacer(modifier = Modifier.height(16.dp))
            Dice(diceValue)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                diceValue = Random.nextInt(1, 7)
                playerPosition = (playerPosition + diceValue) % 64
            }) {
                Text(text = "Lançar Dados")
            }
            Spacer(modifier = Modifier.height(16.dp)) // Adicionado
            Button(onClick = { navController.popBackStack() }) { // Adicionado
                Text(text = "Voltar")
            }
        }
    }

}
@Composable
fun Board(playerPosition: Int) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(8),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(64) { index ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(4.dp)
            ) {
                if (index == playerPosition) {
                    Image(
                        painter = painterResource(id = R.drawable.player),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .background(Color.Yellow)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun Dice(value: Int) {
    val diceImage = when (value) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        6 -> R.drawable.dice_6
        else -> R.drawable.dice_1
    }
    Image(
        painter = painterResource(id = diceImage),
        contentDescription = null,
        modifier = Modifier.size(100.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DadosTabuleirosTheme {
        MainScreen(rememberNavController()) // Adicionado
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    DadosTabuleirosTheme {
        GameScreen(rememberNavController()) // Adicionado
    }
}