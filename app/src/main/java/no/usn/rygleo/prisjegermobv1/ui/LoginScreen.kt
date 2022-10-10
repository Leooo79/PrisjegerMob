package no.usn.rygleo.prisjegermobv1.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.usn.rygleo.prisjegermobv1.R


@Preview(showBackground = true, widthDp = 320, heightDp = 600)
@Composable
fun LoginScreen( ) {
    var brukerNavn by remember{ mutableStateOf("")}
    var passord by remember{ mutableStateOf("")}
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier.size(90.dp),
            shape = CircleShape,
        )
        {
            Image(
                painterResource(R.drawable.prisjegerlogo),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally    ) {

        /*  Text(text = "Login her",
              style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.ExtraBold),
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(bottom = 20.dp),
              textAlign = TextAlign.Left,
              color = Color.Red
          )*/
        OutlinedTextField(value = brukerNavn,
            onValueChange = {brukerNavn=it},
            label = { Text("brukernavn")},
            leadingIcon =  {
                Icon( Icons.Default.Person, contentDescription = "bruker")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp)
        )
        OutlinedTextField(value =passord ,
            onValueChange = {passord=it},
            label = { Text("passord")},
            leadingIcon =  {
                Icon( Icons.Default.Info, contentDescription = "password")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        OutlinedButton(onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp),
            elevation = ButtonDefaults.elevation(100.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Blue,
                contentColor = Color.White
            )
        ){
            Text(
                text = "login",
                textAlign = TextAlign.Center)

        }

    }

}