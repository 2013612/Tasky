//
//  RegisterViewModel.swift
//  iosApp
//
//  Created by Wong Samuel on 26/2/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Observation
import shared_
import SwiftUICore

@Observable
class RegisterViewModel {
    var name: String = ""
    var email: String = ""
    var password: String = ""
    var nameError: LocalizedStringKey?
    var emailError: LocalizedStringKey?
    var passwordError: LocalizedStringKey?

    var isNameCheckVisible: Bool {
        return Validator.shared.validateName(name: name)
    }

    var isEmailCheckVisible: Bool {
        return Validator.shared.validateEmail(email: email)
    }

    var isRegisterSuccess: Bool = false

    private let authRepository: IAuthRepository = KoinHelper.shared.getIAuthRepository()

    private func validateInput() -> Bool {
        return Validator.shared.validateName(name: name) && Validator.shared.validateEmail(email: email) && Validator.shared.validatePassword(password: password)
    }

    func register() {
        guard validateInput() else { return }

        _Concurrency.Task {
            let result = try await authRepository.register(fullName: name, email: email, password: password)

            switch onEnum(of: result) {
            case .success:
                isRegisterSuccess = true
            case let .error(error):
                print(error)
                self.nameError = ""
                self.emailError = ""
                self.passwordError = ""
            }
        }
    }
}
