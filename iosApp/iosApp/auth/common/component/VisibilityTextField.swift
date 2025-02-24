//
//  VisibilityTextField.swift
//  iosApp
//
//  Created by Wong Samuel on 24/2/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct VisibilityTextField: View {
    @Binding var text: String
    let errorText: String?
    let placeholder: String

    @State private var isVisible: Bool = false
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

    private var imageName: String {
        if isVisible {
            return "eye.fill"
        } else {
            return "eye.slash.fill"
        }
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack {
                Group {
                    if isVisible {
                        TextField(placeholder, text: $text)
                    } else {
                        SecureField(placeholder, text: $text)
                    }
                }

                Image(systemName: imageName).onTapGesture {
                    isVisible.toggle()
                }
            }.focused($isFocused)
                .padding(.horizontal, 16)
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
