//
//  LoginScreen.swift
//  iosApp
//
//  Created by Wong Samuel on 21/2/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct LoginScreen: View {
    @State private var viewModel = LoginViewModel()
    @Environment(NavigationManager.self) private var navigationManager

    var body: some View {
        VStack(spacing: 0) {
            Spacer().frame(height: 47)
            Text("welcome_back")
                .font(.displayMedium)
                .foregroundColor(.white)
            Spacer().frame(height: 40)
            VStack(spacing: 0) {
                Spacer().frame(height: 50)
                CheckTextField(text: $viewModel.email, isCheckVisible: viewModel.isCheckVisible, errorText: viewModel.emailError, placeholder: "email_address")
                Spacer().frame(height: 15)
                VisibilityTextField(text: $viewModel.password, errorText: viewModel.passwordError, placeholder: "password")
                Spacer().frame(height: 25)
                Button(action: { viewModel.login() }) {
                    Text("log_in").makePrimaryButton()
                }
                Spacer()

                (Text("don_t_have_an_account") + Text("sign_up")).environment(\.openURL, OpenURLAction { _ in
                    navigationManager.path.append(.register)
                    return .handled
                }).font(.labelLarge).foregroundStyle(.gray).tint(.lightBlue)

                Spacer().frame(height: 20)
            }.padding(.horizontal, 16).background(Color.white).clipShape(UnevenRoundedRectangle(cornerRadii: RectangleCornerRadii(topLeading: 30, bottomLeading: 0, bottomTrailing: 0, topTrailing: 30)))
        }.background(Color.black).toolbar(.hidden, for: .navigationBar)
    }
}

// #Preview {
//    LoginScreen()
// }
