package com.example.dadostabuleiros

import android.app.Activity
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dadostabuleiros.R.drawable.driver
import com.example.dadostabuleiros.R.drawable.player
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
    val activity = (LocalContext.current as? Activity)

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
            Text(
                text = "BEM VINDOS AO DADOS&TABULEIROS",
                textAlign = TextAlign.Center,
                color = Color.Red,
                fontSize = 30.sp,
                fontFamily = FontFamily.Default, // Adicionado tipo de letra
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .background(Color.White) // Adicionado espaçamento vertical
            )
            Spacer(modifier = Modifier.height(40.dp))
            Button(onClick = { navController.navigate("game_screen") }) {
                Text(text = "Iniciar Jogo")
            }
            Spacer(modifier = Modifier.height(16.dp)) // Espaçamento entre os botões
            Button(onClick = {
                activity?.finish() // Finaliza a atividade para sair do jogo
            }) {
                Text(text = "Sair do Jogo")
            }
        }
    }
}



@Composable
fun GameScreen(navController: NavHostController) {
    var diceValue by remember { mutableIntStateOf(1) }
    var diceValue2 by remember { mutableIntStateOf(1)    }
    var playerPosition by remember { mutableIntStateOf(1) }
    var secondPlayerPosition by remember { mutableIntStateOf(1) }
    var currentPlayer by remember { mutableIntStateOf(1) }
    var gameOver by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.view_3d_dice_with_abstract_scenery),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Board(playerPosition, secondPlayerPosition)
            Spacer(modifier = Modifier.height(16.dp))
            Dice(diceValue)
            Spacer(modifier = Modifier.height(16.dp))
            // Alerta de vez do jogador
            Text(
                text = "Vez do Jogador $currentPlayer",
                color = Color.White,
                fontSize = 15.sp,
                modifier = Modifier.padding(10.dp)
            )
            if (!gameOver) {
                Button(onClick = {
                    diceValue = Random.nextInt(1, 7)
                    if (currentPlayer == 1) {
                        val newPosition = playerPosition + diceValue
                        if (newPosition <= 79 && newPosition != secondPlayerPosition) {
                            playerPosition = newPosition
                            if (playerPosition == 79) {
                                gameOver = true
                            } else {
                                currentPlayer = 2
                            }
                        }
                    } else {
                        diceValue2 = Random.nextInt(1, 7)
                        val newPosition = secondPlayerPosition + diceValue
                        if (newPosition <= 79 && newPosition != playerPosition) {
                            secondPlayerPosition = newPosition
                            if (secondPlayerPosition == 79) {
                                gameOver = true
                            } else {
                                currentPlayer = 1
                            }
                        }
                    }
                }) {
                    Text(text = "Lançar Dados")
                }
            } else {
                Text(text = "Fim de Jogo! Jogador ${if (playerPosition == 79) 1 else 2} venceu!")
                Button(onClick = {
                    // Reiniciar o jogo
                    playerPosition = 1
                    secondPlayerPosition = 1
                    currentPlayer = 1
                    gameOver = false
                }) {
                    Text(text = "Reiniciar")
                }
                Button(onClick = { navController.popBackStack() }) {
                    Text(text = "Voltar")
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun Board(playerPosition: Int, secondPlayerPosition: Int) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(8),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        items(80) { index ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(4.dp)
                    .background(Color.White),
            ) {
                when (index) {
                    playerPosition -> {
                        Image(
                            painter = painterResource(id = player),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    secondPlayerPosition -> {
                        Image(
                            painter = painterResource(id = driver),
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    0 -> {
                        Text(text = "Início", color = Color.Green,
                            fontSize = 11.5.sp)
                    }
                    79 -> {
                        Text(text = "Fim", color = Color.Red)
                    }
                    else -> {
                        Box(
                            modifier = Modifier
                                .background(Color.LightGray)
                                .fillMaxSize()

                        )
                    }
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
        MainScreen(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    DadosTabuleirosTheme {
        GameScreen(rememberNavController()) // Adicionado
    }
}
