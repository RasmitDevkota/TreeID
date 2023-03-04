package com.alientech.treeid

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alientech.treeid.ui.theme.*
import com.alientech.treeid.Index.images
import com.alientech.treeid.Index.names

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TreeIDTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Row (
                        Modifier.padding(15.dp, 5.dp, 0.dp, 5.dp)
                    ) {
                        var mode by remember {
                            mutableStateOf("mcq")
                        }

                        var specimen by remember {
                            mutableStateOf(getRandomSpecimen())
                        }

                        var image by remember {
                            mutableStateOf(getRandomImage(specimen))
                        }

                        var options by remember {
                            mutableStateOf(generateOptions(specimen))
                        }

                        var selection by remember {
                            mutableStateOf("")
                        }

                        var response by remember {
                            mutableStateOf(TextFieldValue(""))
                        }

                        var reveal by remember {
                            mutableStateOf(false)
                        }

                        fun reset() {
                            specimen = getRandomSpecimen(specimen)
                            image = getRandomImage(specimen, image)
                            options = generateOptions(specimen)
                            selection = ""
                            response = TextFieldValue("")
                            reveal = false
                        }

                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row (
                                Modifier
                                    .padding(top = 10.dp)
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                Text(
                                    text = "Identify the specimen below!",
                                    textDecoration = TextDecoration.Underline
                                )
                            }

                            Row(
                                Modifier
                                    .padding(top = 20.dp, bottom = 15.dp)
                                    .background(Color.Black)
                            ) {
                                var identifier = resources.getIdentifier(image, "drawable", packageName)

                                while (identifier == 0) {
                                    image = getRandomImage(specimen, image)
                                    identifier = resources.getIdentifier(image, "drawable", packageName)
                                }

                                Image(
                                    painter = painterResource(id = identifier),
                                    contentDescription = "",
                                    Modifier
                                        .fillMaxWidth(0.95f)
                                        .fillMaxHeight(0.45f)
                                )
                            }

                            Column (
                                Modifier
                                    .fillMaxWidth(0.95f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row (
                                    Modifier
                                        .align(Alignment.CenterHorizontally),
                                ) {
                                    Button(
                                        onClick = {
                                            mode = if (mode == "frq") "mcq" else "frq"
                                        },
                                        Modifier.padding(horizontal = 5.dp)
                                    ) {
                                        Text(text = mode.uppercase())
                                    }
                                    Button(
                                        onClick = {
                                            image = getRandomImage(specimen)
                                        },
                                        Modifier.padding(horizontal = 5.dp)
                                    ) {
                                        Text(text = "New Image")
                                    }
                                }

                                Column (
                                    Modifier.padding(vertical = 10.dp)
                                ) {
                                    if (mode == "mcq") {
                                        options.forEach {
                                            Row {
                                                Button(
                                                    onClick = {
                                                        selection = it
                                                    },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor =
                                                            if (reveal) {
                                                                if (specimen == it) {
                                                                    Correct
                                                                } else {
                                                                    if (selection == it) {
                                                                        Incorrect
                                                                    } else {
                                                                        BeigeUnselected
                                                                    }
                                                                }
                                                            } else {
                                                                if (selection == it) {
                                                                    BeigeSelected
                                                                } else {
                                                                    BeigeUnselected
                                                                }
                                                            }
                                                    )
                                                ) {
                                                    Text(
                                                        text = getCommonName(it) + " — ",
                                                        style = TextStyle(
                                                            fontFamily = FontFamily.Default,
                                                            fontWeight = FontWeight.Normal,
                                                            fontSize = 13.sp,
                                                            lineHeight = 18.sp,
                                                            letterSpacing = 0.2.sp
                                                        ),
                                                        modifier = Modifier.padding(start = 1.dp)
                                                    )
                                                    Text(
                                                        it.replace("_", " ").replaceFirstChar { c -> c.uppercase() },
                                                        style = TextStyle(
                                                            fontFamily = FontFamily.Default,
                                                            fontWeight = FontWeight.Normal,
                                                            fontStyle = FontStyle.Italic,
                                                            fontSize = 13.sp,
                                                            lineHeight = 18.sp,
                                                            letterSpacing = 0.2.sp
                                                        ),
                                                        modifier = Modifier.padding(start = 1.dp)
                                                    )
                                                }
                                            }
                                        }
                                    } else if (mode == "frq") {
                                        Row {
                                            BasicTextField(
                                                value = response,
                                                onValueChange = { response = it },
                                                modifier = Modifier
                                                    .fillMaxWidth(0.9f)
                                                    .fillMaxHeight(0.1f)
                                                    .background(Color.LightGray, AbsoluteRoundedCornerShape(15.dp))
                                                    .border(
                                                        1.5.dp,
                                                        if (reveal) {
                                                            with (response.text.lowercase()) {
                                                                if (this == specimen.replace("_", " ") || this == getCommonName(specimen).lowercase()) {
                                                                    Correct
                                                                } else {
                                                                    Incorrect
                                                                }
                                                            }
                                                        } else {
                                                            Color.DarkGray
                                                        },
                                                        AbsoluteRoundedCornerShape(15.dp)),
                                                textStyle = TextStyle(
                                                    fontFamily = FontFamily.Default,
                                                    fontWeight = FontWeight.Normal,
                                                    fontSize = 22.sp,
                                                    lineHeight = 30.sp,
                                                    letterSpacing = 0.2.sp,
                                                    textIndent = TextIndent(10.sp),
                                                    color =
                                                        if (reveal) {
                                                            with (response.text.lowercase()) {
                                                                if (this == specimen.replace("_", " ") || this == getCommonName(specimen).lowercase()) {
                                                                    Correct
                                                                } else {
                                                                    Incorrect
                                                                }
                                                            }
                                                        } else {
                                                            Color.DarkGray
                                                        }
                                                ),
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }

                            Row {
                                Button(
                                    onClick = {
                                        if (selection == "" && response.text == "") {
                                            // Answer the question first!
                                        } else {
                                            reveal = true

//                                            if (selection == specimen || response.text == specimen) {
//
//                                            } else {
//
//                                            }
                                        }
                                    },
                                    Modifier.padding(horizontal = 5.dp)
                                ) {
                                    Text(
                                        text = "Check Answer"
                                    )
                                }

                                Button(
                                    onClick = {
                                        reset()
                                    },
                                    Modifier.padding(horizontal = 5.dp)
                                ) {
                                    Text(
                                        text = "Next Question"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getRandomSpecimen(prev: String = "") : String {
        var specimen: String

        do {
            specimen = images.keys.random()
        } while (specimen == prev)

        return specimen
    }

    private fun getCommonName(specimen: String) : String {
        return names[specimen]!!
    }

    private fun getRandomImage(specimen: String, prevString: String = "-1") : String {
        val prev = if (prevString == "-1") -1 else prevString.split("_").last().toString()

        var image: Int

        do {
            image = (0 until images[specimen]!!).random()
        } while (image == prev && images[specimen]!! > 1)

        return "${specimen}_$image"
    }

    private fun generateOptions(specimen: String): ArrayList<String> {
        val options = ArrayList<String>()

        options.add(specimen)

        for (i in 0..3) {
            var incorrectSpecimen: String

            do {
                incorrectSpecimen = images.entries.random().key
            } while (incorrectSpecimen == specimen || incorrectSpecimen in options)

            options.add(incorrectSpecimen)
        }

        return options.apply { shuffle() }
    }
}
