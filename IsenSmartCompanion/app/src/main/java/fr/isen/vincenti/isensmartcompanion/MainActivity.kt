package fr.isen.vincenti.isensmartcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.vincenti.isensmartcompanion.ui.theme.IsenSmartCompanionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IsenSmartCompanionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        Main()
                    }
                }
            }
        }
    }
}

@Composable
fun Main() {
    var saveinput by remember { mutableStateOf("") }
    var input by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.logo_desc),
        )
        Text(
            text = saveinput
        )
        Spacer(modifier = Modifier.weight(1f))
        Row (verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 10.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(50))
        ){
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f),
                value = input,
                singleLine = true,
                onValueChange = { input = it },
                placeholder = { Text(text = stringResource(id = R.string.main_placeholder)) },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )

            )
            IconButton(
                onClick = { saveinput = input; input = "" },
                modifier = Modifier
                    .background(Color.Red, shape = RoundedCornerShape(50))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    IsenSmartCompanionTheme {
        Main()
    }
}
