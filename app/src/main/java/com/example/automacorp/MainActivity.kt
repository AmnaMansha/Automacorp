package com.example.automacorp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.automacorp.ui.theme.AutomacorpTheme

class MainActivity : ComponentActivity() {
    companion object {
        const val ROOM_PARAM = "com.automacorp.room.attribute"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val onSayHelloButtonClick: (name: String) -> Unit = {name->

            val intent = if (name!==" ") {
                Intent(this, RoomActivity::class.java).apply {
                    putExtra(ROOM_PARAM, name)
                }
            } else {
                Toast.makeText(
                    baseContext,
                    "Please enter a valid room name ",
                    Toast.LENGTH_SHORT
                ).show()
                null
            }
            intent?.let {
                startActivity(it)
            }
        }

        setContent {
            AutomacorpTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(

                        onClick = onSayHelloButtonClick,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
@Composable
fun AppLogo(modifier: Modifier) {
    Image(
        painter = painterResource(R.drawable.ic_logo),
        contentDescription = stringResource(R.string.app_logo_description),
        modifier = modifier.paddingFromBaseline(top = 100.dp).height(80.dp),
    )
}
@Composable
fun Greeting(onClick: (name: String) -> Unit, modifier: Modifier = Modifier) {
    Column {
        // ...
        Column {
           AppLogo(Modifier.padding(top = 32.dp).fillMaxWidth())
            Text(
                stringResource(R.string.act_main_welcome),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(24.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )
        }
        var name by remember { mutableStateOf("") }
        OutlinedTextField(
            name,
            onValueChange = { name = it },
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            placeholder = {
                Text(stringResource(R.string.act_main_fill_name))
            })

        Button(
            onClick = { onClick(name) },
            modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(R.string.act_main_open))
        }
    }
}

