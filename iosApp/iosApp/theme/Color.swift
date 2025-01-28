//
//  Color.swift
//  iosApp
//
//  Created by Wong Samuel on 28/1/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

extension Color {
    static let black = Color(hex: 0xFF16161C)
    static let light = Color(hex: 0xFFEEF6FF)
    static let light2 = Color(hex: 0xFFF2F3F7)
    static let green = Color(hex: 0xFF279F70)
    static let lightGreen = Color(hex: 0xFFCAEF45)
    static let orange = Color(hex: 0xFFFDEFA8)
    static let darkGray = Color(hex: 0xFF5C5D5A)
    static let gray = Color(hex: 0xFFA9B4BE)
    static let lightBlue = Color(hex: 0xFFB7C6DE)
    static let red = Color(hex: 0xFFFF7272)
    static let brown = Color(hex: 0xFF40492D)
    
    init(hex: UInt64) {
        self.init(
            .sRGB,
            red: Double((hex & 0xFF0000) >> 16) / 255.0,
            green: Double((hex & 0x00FF00) >> 8) / 255.0,
            blue: Double(hex & 0x0000FF) / 255.0,
            opacity: hex > 0xFFFFFF ? Double((hex & 0xFF000000) >> 24) / 255.0 : 1.0
        )
    }
}
