//
//  CheckTextField.swift
//  iosApp
//
//  Created by Wong Samuel on 24/2/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct CheckTextField: View {
    @Binding var text: String
    let isCheckVisible: Bool
    let errorText: String?
    let placeholder: String

    @FocusState private var isFocused: Bool

    private var borderColor: Color {
        if errorText != nil {
            return Color.red
        } else if isFocused {
            return Color.lightBlue
        } else {
            return Color.clear
        }
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack {
                TextField(placeholder, text: $text).focused($isFocused).autocorrectionDisabled(true).textInputAutocapitalization(.never)
                if isCheckVisible {
                    Image(systemName: "checkmark")
                        .foregroundColor(.green)
                }
            }.padding(.horizontal, 16)
                .padding(.vertical, 12)
                .background(ZStack {
                    RoundedRectangle(cornerRadius: 10).fill(Color.light2)

                    RoundedRectangle(cornerRadius: 10).stroke(borderColor, lineWidth: 1)
                })
            if let errorText = errorText {
                Text(errorText)
                    .font(.labelSmall)
                    .foregroundColor(Color.red)
                    .padding(.horizontal, 16)
                    .padding(.top, 4)
            }
        }
    }
}
