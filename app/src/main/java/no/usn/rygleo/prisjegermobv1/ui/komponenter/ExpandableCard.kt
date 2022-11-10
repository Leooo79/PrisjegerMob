package no.usn.rygleo.prisjegermobv1.ui.komponenter

import android.graphics.drawable.shapes.OvalShape
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.*
import androidx.compose.material.ContentAlpha.medium
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import no.usn.rygleo.prisjegermobv1.ui.MainChart

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpandableCard(
    title : String,
    open : Boolean = false,
    titleFontSize: TextUnit = MaterialTheme.typography.h6.fontSize,
    fontWeight : FontWeight = FontWeight.Bold,
    description : String,
    descriptionFontSize : TextUnit = MaterialTheme.typography.subtitle1.fontSize,
    descriptionFontWeight : FontWeight = FontWeight.Normal,
    descriptionMaxLines : Int = 4,
    metode2: @Composable () -> Unit,
    shape : CornerBasedShape = no.usn.rygleo.prisjegermobv1.ui.theme.Shapes.large,
    padding : Dp = 12.dp
) {
    var expandedState by remember { mutableStateOf(open) }
    val rotationState by animateFloatAsState(targetValue =
    if (expandedState) 180f else 0f)

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 6.dp)
        .animateContentSize(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing
            )
        ),
        //backgroundColor = MaterialTheme.colors.secondaryVariant,
        backgroundColor = Color.White,
        shape = shape,
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(padding)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Hvis at tittel er tom, så blir pilen plassert i midten
                if (title != "") {
                    if (!expandedState) {
                        Text(modifier = Modifier
                            .weight(5f),
                            text = title,
                            fontSize = titleFontSize,
                            fontWeight = fontWeight,
                            color = Color.Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                IconButton(modifier = Modifier
                    .alpha(ContentAlpha.medium)
                    .weight(1f)
                    .rotate(rotationState),
                    onClick = { expandedState = !expandedState }) {
                    if (expandedState) {
                        OutlinedButton(modifier= Modifier.size(40.dp),
                            onClick = { expandedState = !expandedState },
                            border= BorderStroke(1.dp, Color.DarkGray),
                            contentPadding = PaddingValues(0.dp),
                            shape = CircleShape) {
                            Icon(modifier = Modifier,
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Drop-Down Arrow",
                            )
                        }
                    } else {
                        IconButton(modifier = Modifier
                            .alpha(ContentAlpha.medium)
                            .weight(1f)
                            .rotate(rotationState),
                            onClick = {
                                expandedState = !expandedState
                            }) {
                            Icon(modifier = Modifier,
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Drop-Down Arrow",
                            )
                        }
                    }
                }
                /*
                IconButton(modifier = Modifier
                    .alpha(ContentAlpha.medium)
                    .weight(1f)
                    .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }) {
                    Icon(modifier = Modifier,
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow",
                    )
                }

                 */
            } //End of column
            if (expandedState) {
                Row(modifier = Modifier
                    .padding(top = 2.dp)
                    .background(Color.White)
                    .fillMaxWidth(),
                )
                {
                    metode2()
                    /*
                    Text(modifier = Modifier
                        .weight(2f),
                        text = description,
                        fontSize = descriptionFontSize,
                        color = Color.Black,
                        fontWeight = descriptionFontWeight,
                        maxLines = descriptionMaxLines,
                        overflow = TextOverflow.Ellipsis
                    )

                     */
                }
            }
            else {
                Divider(color = MaterialTheme.colors.primary, thickness = 2.dp)
            }
        }
    }
}
