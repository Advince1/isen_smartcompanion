package fr.isen.vincenti.isensmartcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.*
import androidx.room.Room
import fr.isen.vincenti.isensmartcompanion.ui.theme.IsenSmartCompanionTheme
import fr.isen.vincenti.isensmartcompanion.composable.*
import fr.isen.vincenti.isensmartcompanion.db.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val chatDao = DBInstance.getChatDao(applicationContext)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            IsenSmartCompanionTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavigationBar(navController) }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavigationGraph(navController)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    IsenSmartCompanionTheme {
        MainScreen()
    }
}
