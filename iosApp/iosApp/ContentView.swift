import SwiftUI
import shared
import Combine


let weatherApi = WeatherApi()

var jobs:[Kotlinx_coroutines_coreJob?] = []

struct ContentView: View {
    
    @State private var requestStatus: String = ""
    @State private var countyResult:String = ""
    @State private var weatherResult:String = ""
    
	var body: some View {
        
        VStack {
            Button("Cancel requests", action:{
                jobs.forEach { cancellableJob in
                    cancellableJob?.cancel(cause: nil)
                }
                requestStatus = "cancelled"
            })
            Text(requestStatus)
            
            Button("get country", action:{
                requestStatus = "get country..."
                weatherApi.getCountryByCity(
                    cityName: "Moscow",
                    success: { result in
                        requestStatus = "Complete"
                        countyResult = result
                    },
                    completionHandler: {job, error in
                        jobs.append(job)
                    }
                )
            })
            
            Text(countyResult)
            
            Button("get weather", action:{
                weatherApi.getWeatherByCity(
                    cityName: "Moscow",
                    success: { result in
                        requestStatus = "Complete"
                        weatherResult = result
                    },
                    completionHandler: {job, error in jobs.append(job)}
                )
            })
            Text(weatherResult)
        }
		
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}

