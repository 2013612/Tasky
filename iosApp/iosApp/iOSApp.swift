import shared_
import SwiftUI

@main
struct iOSApp: App {
    init() {
        KoinHelper.shared.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
