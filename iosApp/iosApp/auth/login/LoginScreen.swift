//
//  LoginScreen.swift
//  iosApp
//
//  Created by Wong Samuel on 21/2/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct LoginScreen: View {
    @State private var text = ""

    var body: some View {
        VStack(spacing: 0) {
            Spacer().frame(height: 47)
            Text("Welcome Back!")
                .font(.displayMedium)
                .foregroundColor(.white)
            Spacer().frame(height: 40)
            VStack(spacing: 0) {
                Spacer().frame(height: 50)
                TextField(text: $text) {
                    Text("ABC")
                }.foregroundColor(.black)
                Spacer()
            }.padding(.horizontal, 16).background(Color.white).clipShape(UnevenRoundedRectangle(cornerRadii: RectangleCornerRadii(topLeading: 30, bottomLeading: 0, bottomTrailing: 0, topTrailing: 30)))
        }.background(Color.black)
    }
}

// #Preview {
//    LoginScreen()
// }
