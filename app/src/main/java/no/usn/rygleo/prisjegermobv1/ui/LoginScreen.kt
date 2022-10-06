package no.usn.rygleo.prisjegermobv1.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun LoginScreen( ) {
    var brukerNavn by remember{ mutableStateOf("")}
    var passord by remember{ mutableStateOf("")}
    Column() {

        Text(text = "Login her",
            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.ExtraBold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            textAlign = TextAlign.Left,
            color = Color.Red
        )
        OutlinedTextField(value =brukerNavn ,
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
                .padding(bottom = 10.dp, top = 10.dp) ){
            Text(
                text = "login",
                textAlign = TextAlign.Center)

        }

    }

}