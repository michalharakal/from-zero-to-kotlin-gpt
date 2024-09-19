import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kkon.kmp.ai.sinus.approximator.ASinusCalculator
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun SinusSliderScreen() {
    // State für den Sliderwert (0 bis PI/2)
    var sliderValue by remember { mutableStateOf(0f) }

    var calculator by remember { mutableStateOf(ASinusCalculator()) }

    // State für die Modellberechnung und ob das Modell geladen wurde
    var isModelLoaded by remember { mutableStateOf(false) }
    //var modelSinusValue by remember { mutableStateOf(0.0) }

    // Sinus des aktuellen Wertes berechnen
    val sinusValue = sin(sliderValue.toDouble())
    val modelSinusValue = calculator.calculate(sliderValue.toDouble())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        // Slider für Werte von 0 bis PI/2
        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            valueRange = 0f..(PI.toFloat() / 2),
            modifier = Modifier.fillMaxWidth()
        )

        // Anzeigen des aktuellen Sliderwertes und des berechneten Sinuswertes
        Text(
            text = "Winkel: $sliderValue",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Sinus: $sinusValue",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Model Sinus: $modelSinusValue",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(top = 16.dp)
        )


        // Nur anzeigen, wenn das Modell geladen wurde
        if (!isModelLoaded) {
            // Button zum Laden des Modells
            Button(
                onClick = {
                    // Modell laden (hier einfach eine Berechnung, z.B. Sinus multipliziert mit einer Konstante)
                    isModelLoaded = true
                    calculator.loadModel()
                },
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("Load Model")
            }

        }
    }
}
