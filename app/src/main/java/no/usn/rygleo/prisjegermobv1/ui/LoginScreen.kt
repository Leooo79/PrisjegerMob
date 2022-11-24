package no.usn.rygleo.prisjegermobv1.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.materialIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.usn.rygleo.prisjegermobv1.R
import no.usn.rygleo.prisjegermobv1.ui.theme.Purple700


@Composable
fun LoginScreen( prisjegerViewModel: PrisjegerViewModel) {
    var regisrerView by remember { mutableStateOf(false) }
    var isLoggedIn by remember { mutableStateOf(false) }
    var brukerNavn by remember { mutableStateOf("") }
    var passord by remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

    val feilBrukerNavn = stringResource(id = R.string.wrongUserNameOrPassword)
    val loggetInn = stringResource(id = R.string.loggedIn)
    val userAlreadyExists = stringResource(id = R.string.userAlreadyExists)
    val userRegistered = stringResource(id = R.string.userRegistered)
    val userMustHaveValue = stringResource(id = R.string.userMustHaveValue)
    val register = stringResource(id = R.string.register)
    val logout = stringResource(id = R.string.logout)


    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = text)
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { openDialog.value = false }
                    ) {
                        Text(stringResource(id = R.string.goBack))
                    }
                }
            }
        )
    }
    if (!prisjegerViewModel.isLoggedIn.value) {
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /*  Text(text = "Login her",
              style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.ExtraBold),
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(bottom = 20.dp),
              textAlign = TextAlign.Left,
              color = Color.Red
          )*/
            OutlinedTextField(
                value = brukerNavn,
                onValueChange = { brukerNavn = it },
                label = { Text(stringResource(id = R.string.userName, Modifier.background(Color.Red)), color = MaterialTheme.colors.onSecondary) },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "bruker", tint = MaterialTheme.colors.onSecondary)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                keyboardOptions= KeyboardOptions(keyboardType= KeyboardType.Text, imeAction = ImeAction.Next),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White
                )
            )

            OutlinedTextField(
                value = passord,
                onValueChange = { passord = it },
                label = { Text(stringResource(id = R.string.password)) },
                leadingIcon = {
                    Icon(Icons.Default.Info, contentDescription = "password")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Go),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White
                )
            )

            if (regisrerView == false) {
                OutlinedButton(
                    onClick = {
                        if (brukerNavn.isNotEmpty() && passord.isNotEmpty()) {
                            prisjegerViewModel.postAPILogin(brukerNavn, passord)
                            // TODO: Kun alertDialog som viser feil
                            if (prisjegerViewModel.brukerAPI.value?.get("melding")
                                    .equals("innlogget")
                            ) {
                                // TODO: trenger ny verdi brukernavn
                                isLoggedIn = true
                                text = feilBrukerNavn
                            } else {
                                isLoggedIn = false
                                text = loggetInn

                            }
                            //HVIS VELLYKKET = RES.JSON("MEDLING": 'INNLOGGET')
                        } else {
                            text = feilBrukerNavn
                        }
                        openDialog.value = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    elevation = ButtonDefaults.elevation(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.Login),
                        textAlign = TextAlign.Center
                    )

                }
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally


                ) {

                    ClickableText(
                        text = AnnotatedString(stringResource(id = R.string.registerUser)),

                        onClick = { regisrerView = true
                                   passord =""
                                  brukerNavn ="" },
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Default,
                            textDecoration = TextDecoration.Underline,
                            color = Purple700
                        )
                    )
                }
            } else {
                OutlinedButton(
                    onClick = {
                        if (brukerNavn.isNotEmpty() && passord.isNotEmpty()) {
                            prisjegerViewModel.postAPIRegistrer(brukerNavn, passord)
                            if (prisjegerViewModel.registert.value
                            ) {
                                //"bruker eksisterer allerede"
                                text = userRegistered

                            } else {
                                //Bruker registrert
                                text = userAlreadyExists
                            }
                        } else text = userMustHaveValue
                        openDialog.value = true
                        regisrerView = false

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, top = 10.dp),
                    elevation = ButtonDefaults.elevation(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = register,
                        textAlign = TextAlign.Center
                    )

                }

            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedButton(
                onClick = {prisjegerViewModel.postAPILoggout()},

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                elevation = ButtonDefaults.elevation(100.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Blue,
                    contentColor = Color.White
                )
            ){Text(
                text = logout,
                textAlign = TextAlign.Center
            ) }
        }


    }
}

