//
//  FlowPublisher.swift
//  iosApp
//
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import Combine
import shared

class NopCollector<T>: Kotlinx_coroutines_coreFlowCollector {
    
    let callback:(T) -> Void
    
    init(callback: @escaping (T) -> Void) {
        self.callback = callback
    }
    
    func emit(value: Any?) async throws {
        // does nothing
        callback(value as! T)
    }
    
    func stop() {
        
    }
}



//class Collector<T>: Kotlinx_coroutines_coreFlowCollector {
//
//    let callback:(T) -> Void
//
//    init(callback: @escaping (T) -> Void) {
//        self.callback = callback
//    }
//
//    func emit(value: Any?)  async throws {
//        // do whatever you what with the emitted value
//        callback(value as! T)
//
//        // after you finished your work you need to call completionHandler to
//        // tell that you consumed the value and the next value can be consumed,
//        // otherwise you will not receive the next value
//        //
//        // i think first parameter can be always nil or KotlinUnit()
//        // second parameter is for an error which occurred while consuming the value
//        // passing an error object will throw a NSGenericException in kotlin code, which can be handled or your app will crash
//        completionHandler(KotlinUnit(), nil)
//    }
//
//
//
//}
