import shared_
import SwiftUI

struct ContentView: View {
    @State private var navigationManager: NavigationManager = .init()

    var body: some View {
        NavigationStack(path: $navigationManager.path) {
            Rectangle().navigationDestination(for: NavigationManager.Route.self, destination: { destination in
                switch destination {
                case .login:
                    LoginScreen()
                case .register:
                    RegisterScreen()
                case .agenda:
                    Rectangle()
                }
            })
        }.environment(navigationManager)
    }
}

// struct ContentView_Previews: PreviewProvider {
//	static var previews: some View {
//		ContentView()
//	}
// }
