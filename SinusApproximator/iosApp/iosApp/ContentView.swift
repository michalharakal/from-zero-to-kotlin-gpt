import SwiftUI
import SinusApproximatorKit

struct ContentView: View {
    @State var radians = 0.0
    @State private var showContent = false
    var body: some View {
        VStack {
            VStack {
                Slider(value: $radians, in: 0.0...Double.pi/2) {
                    
                }
                Text("Winkel: \(radians, format: .number.precision(.fractionLength(2))) rad")
            }
            .padding()
            Text("Sinus: \(sin(radians), format: .number.precision(.fractionLength(4)))")
                .font(.title)
            Text("Model Sinus: \(0.0, format: .number.precision(.fractionLength(4)))")
            Button("Modell Laden") {
                
            }
            .padding()
          }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
