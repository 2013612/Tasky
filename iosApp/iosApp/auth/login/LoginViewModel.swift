//
//  LoginViewModel.swift
//  iosApp
//
//  Created by Wong Samuel on 25/2/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Observation
import shared_
import SwiftUICore

@Observable
class LoginViewModel {
    var email: String = ""
    var password: String = ""
    var emailError: LocalizedStringKey?
    var passwordError: LocalizedStringKey?

    var isCheckVisible: Bool {
        return Validator.shared.validateEmail(email: email)
    }

    private let authRepository: IAuthRepository = KoinHelper.shared.getIAuthRepository()

    private func validateInput() -> Bool {
        return Validator.shared.validateEmail(email: email) && Validator.shared.validatePassword(password: password)
    }

    func login() {
        guard validateInput() else { return }

        _Concurrency.Task {
            let result = try await authRepository.login(email: email, password: password)
            ResultWrapperKt.onError(result, action: { _ in
                print("error")
                self.emailError = ""
                self.passwordError = ""
            })
        }
    }
}
