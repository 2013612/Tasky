//
//  NavigationManager.swift
//  iosApp
//
//  Created by Wong Samuel on 4/3/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Observation
import shared_
import SwiftUI

@MainActor
@Observable
class NavigationManager {
    private let sessionManager: ISessionManager = KoinHelper.shared.getISessionManager()
    var path: [Route] = [.login]

    init() {
        subscribeIsLogin()
    }

    private func subscribeIsLogin() {
        _Concurrency.Task {
            for await isLogin in sessionManager.isLoginFlow {
                if isLogin.boolValue {
                    path = [.agenda]
                } else {
                    path = [.login]
                }
            }
        }
    }

    enum Route {
        case login
        case register
        case agenda
    }
}
