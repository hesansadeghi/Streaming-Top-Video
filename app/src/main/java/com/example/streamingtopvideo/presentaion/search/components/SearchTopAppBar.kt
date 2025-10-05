package com.example.streamingtopvideo.presentaion.search.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.streamingtopvideo.presentaion.ui.theme.EbonyClay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchTopAppBar(searchedText: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    var debouncedText by remember { mutableStateOf("") }
    var job: Job? = null

    LaunchedEffect(debouncedText) {
        if (debouncedText.isNotEmpty() && debouncedText.length >= 3) {
            searchedText(debouncedText)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 32.dp, bottom = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                text = newText

                // اگر کار قبلی هنوز در حال اجرا بود، آن را لغو کنید
                job?.cancel()

                // یک کار جدید برای debounce ایجاد کنید
                job = CoroutineScope(Dispatchers.Main).launch {
                    delay(300) // Delay for 300 milliseconds
                    debouncedText = newText
                    println("متن جدید با debounce: $debouncedText")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Search...") },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {

                    searchedText(text)
                }
            ),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = EbonyClay,
                unfocusedContainerColor = EbonyClay,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.DarkGray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                focusedLeadingIconColor = Color.White,
                unfocusedLeadingIconColor = Color.White,
            )
        )
    }
}



@Preview
@Composable
fun SearchTopAppBarPreview() {

    SearchTopAppBar {}

}

