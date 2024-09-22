package com.example.unitconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unitconverter.ui.theme.UnitConverterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnitConverterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UnitConverterScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitConverterScreen(modifier: Modifier = Modifier) {
    var inputValue by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var selectedInputUnit by remember { mutableStateOf("Centimeters") }
    var selectedOutputUnit by remember { mutableStateOf("Meters") }
    var expandedInput by remember { mutableStateOf(false) }
    var expandedOutput by remember { mutableStateOf(false) }
    val unitOptions = listOf("Centimeters", "Meters", "Kilometers")

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Unit Converter",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            label = { Text("Enter value") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Input Unit Dropdown
        ExposedDropdownMenuBox(
            expanded = expandedInput,
            onExpandedChange = { expandedInput = !expandedInput }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedInputUnit,
                onValueChange = { },
                label = { Text("Input Unit") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedInput) },
                modifier = Modifier
                    .menuAnchor()
                    .padding(bottom = 16.dp)
            )
            DropdownMenu(
                expanded = expandedInput,
                onDismissRequest = { expandedInput = false }
            ) {
                unitOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedInputUnit = selectionOption
                            expandedInput = false
                        }
                    )
                }
            }
        }

        // Output Unit Dropdown
        ExposedDropdownMenuBox(
            expanded = expandedOutput,
            onExpandedChange = { expandedOutput = !expandedOutput }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedOutputUnit,
                onValueChange = { },
                label = { Text("Output Unit") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedOutput) },
                modifier = Modifier
                    .menuAnchor()
                    .padding(bottom = 16.dp)
            )
            DropdownMenu(
                expanded = expandedOutput,
                onDismissRequest = { expandedOutput = false }
            ) {
                unitOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedOutputUnit = selectionOption
                            expandedOutput = false
                        }
                    )
                }
            }
        }

        Button(onClick = {
            result = convert(inputValue, selectedInputUnit, selectedOutputUnit)
        }) {
            Text("Convert")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Result: $result")
    }
}

fun convert(inputValue: String, inputUnit: String, outputUnit: String): String {
    val value = inputValue.toDoubleOrNull() ?: return "Invalid input"

    if (inputUnit == outputUnit) return value.toString()

    // Conversion logic
    when (inputUnit) {
        "Centimeters" -> {
            when (outputUnit) {
                "Meters" -> return (value / 100).toString()
                "Kilometers" -> return (value / 100000).toString()
            }
        }
        "Meters" -> {
            when (outputUnit) {
                "Centimeters" -> return (value * 100).toString()
                "Kilometers" -> return (value / 1000).toString()
            }
        }
        "Kilometers" -> {
            when (outputUnit) {
                "Centimeters" -> return (value * 100000).toString()
                "Meters" -> return (value * 1000).toString()
            }
        }
    }
    return "Invalid unit"
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    UnitConverterTheme {
        UnitConverterScreen()
    }
}