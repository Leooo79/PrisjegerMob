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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpandableCard(
    title : String,
    open : Boolean = false,
    titleFontSize: TextUnit = 15.sp,
    fontWeight : FontWeight = FontWeight.Bold,
    metode2: @Composable () -> Unit,
    shape : CornerBasedShape = no.usn.rygleo.prisjegermobv1.ui.theme.Shapes.large,
    padding : Dp = 1.dp
) {
    var expandedState by remember { mutableStateOf(open) }
    val rotationState by animateFloatAsState(targetValue =
    if (expandedState) 180f else 0f)

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 10.dp)
        .animateContentSize(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing
            )
        ),
        //backgroundColor = MaterialTheme.colors.secondaryVariant,
        backgroundColor = Color(0xFFd9eeee),
        shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp),
        onClick = {
            expandedState = !expandedState
        },

    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(padding)) {
            Row(modifier = Modifier
                .background(MaterialTheme.colors.primary),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(modifier = Modifier
                    .fillMaxWidth(),
                    text = title,
                    fontSize = titleFontSize,
                    fontWeight = fontWeight,
                    color = MaterialTheme.colors.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
                IconButton(modifier = Modifier
                    .alpha(ContentAlpha.medium)
                    .rotate(rotationState),
                    onClick = { expandedState = !expandedState }) {
                        IconButton(modifier = Modifier
                            .alpha(ContentAlpha.medium)
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
            Divider(color = MaterialTheme.colors.onPrimary, thickness = 2.dp)
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
        }
    }
}
