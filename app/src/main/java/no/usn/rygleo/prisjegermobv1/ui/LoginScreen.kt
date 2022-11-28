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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
  //  var isLoggedIn by remember { mutableStateOf(false) } TODO: trenger ikke?
    var brukerNavn by remember { mutableStateOf("") }
    var passord by remember { mutableStateOf("") }
  //  val openDialog = remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

    val feilBrukerNavn = stringResource(id = R.string.wrongUserNameOrPassword)
    val loggetInn = stringResource(id = R.string.loggedIn)
    val userAlreadyExists = stringResource(id = R.string.userAlreadyExists)
    val userRegistered = stringResource(id = R.string.userRegistered)
    val userMustHaveValue = stringResource(id = R.string.userMustHaveValue)
    val register = stringResource(id = R.string.register)
    val logout = stringResource(id = R.string.logout)

    // TODO: lagt til suspendert endring av tekst i alertDialog
    // TODO: endrer basert på om innlogget
    if (prisjegerViewModel.isLoggedIn.value) {
        LaunchedEffect(Unit) {
            text = loggetInn + " " + brukerNavn
        }
    } else {
        LaunchedEffect(Unit) {
            text = feilBrukerNavn
        }
    }

    if (prisjegerViewModel.registert.value) {
        LaunchedEffect(Unit) {
            text = userRegistered
        }
    } else {
        LaunchedEffect(Unit) {
            text = userAlreadyExists
        }
    }

    if (prisjegerViewModel.openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                prisjegerViewModel.openDialog.value = false
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
                        onClick = { prisjegerViewModel.openDialog.value = false }
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
            Text(
                text = "Prisjeger",
                color = MaterialTheme.colors.onPrimary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                style = TextStyle(
                    fontSize = 24.sp,
                    shadow = Shadow(
                        color = Color.Black,
                        blurRadius = 5f
                    )
                ),
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            )
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
                label = { Text(modifier = Modifier.background(Color.White), text = stringResource(id = R.string.userName), color = MaterialTheme.colors.onSecondary) },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "bruker", tint = MaterialTheme.colors.onSecondary)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                keyboardOptions= KeyboardOptions(keyboardType= KeyboardType.Text, imeAction = ImeAction.Next),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    textColor = MaterialTheme.colors.onSecondary
                )
            )

            OutlinedTextField(
                value = passord,
                onValueChange = { passord = it },
                label = { Text(modifier = Modifier.background(Color.White),text = stringResource(id = R.string.password), color = MaterialTheme.colors.onSecondary) },
                leadingIcon = {
                    Icon(Icons.Default.Info, contentDescription = "password", tint = MaterialTheme.colors.onSecondary)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Go),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    textColor = MaterialTheme.colors.onSecondary
                )
            )

            if (!regisrerView) {
                OutlinedButton(
                    onClick = {
                        if (brukerNavn.isNotEmpty() && passord.isNotEmpty()) {
                            prisjegerViewModel.postAPILogin(brukerNavn, passord)
                   //         prisjegerViewModel.openDialog.value = true
                            /*
                            // TODO: denne if-en skjer aldri:
                      //      if (prisjegerViewModel.brukerAPI.value?.get("melding")
                      //              .equals("innlogget")
                      // TODO: denne if-en skjer aldri:
                            if (prisjegerViewModel.isLoggedIn.value) {
                        //        isLoggedIn = true
                                println("LLLLLLLLLLLLLLLLLOGOOOOOOOOOOOG")
                                openDialog.value = true
                         //       text = loggetInn + " " + brukerNavn
                                // TODO: denne else-en skjer alltid
                            } else {
                      //          isLoggedIn = false
                                openDialog.value = true
                       //         text = feilBrukerNavn
                            }
                            //HVIS VELLYKKET = RES.JSON("MEDLING": 'INNLOGGET')
                        } else {
                            text = "HVa her"
                            */

                        }

              //          openDialog.value = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 80.dp),
                    elevation = ButtonDefaults.elevation(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.Login),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onPrimary
                    )

                }
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally


                ) {
                    /*
                    ClickableText(
                        text = AnnotatedString(stringResource(id = R.string.registerUser)),

                        onClick = { regisrerView = true
                            passord =""
                            brukerNavn ="" },
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Default,
                            textDecoration = TextDecoration.Underline,
                            color = MaterialTheme.colors.onPrimary
                        )
                    )
                     */
                    OutlinedButton(onClick = { regisrerView = true
                        passord =""
                        brukerNavn ="" },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 80.dp),
                        elevation = ButtonDefaults.elevation(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primary,
                            contentColor = MaterialTheme.colors.onPrimary
                        ))
                    {
                        Text(
                            text = AnnotatedString(stringResource(id = R.string.registerUser)),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
                }
            } else {
                OutlinedButton(
                    onClick = {
                        if (brukerNavn.isNotEmpty() && passord.isNotEmpty() ) {
                            prisjegerViewModel.postAPIRegistrer(brukerNavn, passord)
                  //          prisjegerViewModel.openDialog.value = true
                            /*
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

                             */
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 80.dp),
                    elevation = ButtonDefaults.elevation(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary
                    )
                ) {
                    Text(
                        text = register,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
                OutlinedButton(
                    onClick = { regisrerView = false },
                    contentPadding = PaddingValues(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 170.dp, vertical = 0.dp)
                        .fillMaxWidth()
                        .defaultMinSize(minWidth = 0.dp, minHeight = 0.dp),
                    elevation = ButtonDefaults.elevation(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary
                    )
                ) {
                    Image(modifier = Modifier
                        .width(40.dp)
                        .height(40.dp),
                        painter = painterResource(R.drawable.leftarrow),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
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
            val t = prisjegerViewModel.brukernavn
            Text(
                text = stringResource(id = R.string.loginYourName) + t.value,
                color = MaterialTheme.colors.onPrimary,
                style = TextStyle(
                    fontSize = 20.sp,
                    shadow = Shadow(
                        color = Color.Black,
                        blurRadius = 5f
                    )
                ),
            )
            OutlinedButton(
                onClick = {
                    prisjegerViewModel.postAPILoggout()},
                elevation = ButtonDefaults.elevation(100.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                )
            ){
                Text(modifier = Modifier
                    .padding(horizontal = 80.dp),
                text = logout,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onPrimary
            )}
        }


    }
}

