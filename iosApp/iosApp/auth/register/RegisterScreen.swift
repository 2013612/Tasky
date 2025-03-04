//
//  RegisterScreen.swift
//  iosApp
//
//  Created by Wong Samuel on 26/2/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct RegisterScreen: View {
    @State private var viewModel = RegisterViewModel()
    @Environment(NavigationManager.self) private var navigationManager

    var body: some View {
        VStack(spacing: 0) {
            Spacer().frame(height: 47)
            Text("create_your_account")
                .font(.displayMedium)
                .foregroundColor(.white)
            Spacer().frame(height: 40)
            VStack(spacing: 0) {
                Spacer().frame(height: 50)
                CheckTextField(text: $viewModel.name, isCheckVisible: viewModel.isNameCheckVisible, errorText: viewModel.nameError, placeholder: "name")
                Spacer().frame(height: 15)
                CheckTextField(text: $viewModel.email, isCheckVisible: viewModel.isEmailCheckVisible, errorText: viewModel.emailError, placeholder: "email_address")
                Spacer().frame(height: 15)
                VisibilityTextField(text: $viewModel.password, errorText: viewModel.passwordError, placeholder: "password")
                Spacer().frame(height: 25)
                Button(action: { viewModel.register() }) {
                    Text("get_started").makePrimaryButton()
                }
                Spacer()
            }.padding(.horizontal, 16).background(Color.white).clipShape(UnevenRoundedRectangle(cornerRadii: RectangleCornerRadii(topLeading: 30, bottomLeading: 0, bottomTrailing: 0, topTrailing: 30)))
        }.background(Color.black).toolbar(.hidden, for: .navigationBar).onChange(of: viewModel.isRegisterSuccess) { _, value in
            if value {
                navigationManager.path.popLast()
            }
        }
    }
}
