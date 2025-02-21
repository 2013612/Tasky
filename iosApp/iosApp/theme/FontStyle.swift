//
//  FontStyle.swift
//  iosApp
//
//  Created by Wong Samuel on 18/2/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import SwiftUI

enum FontWeight: String {
    case bold
    case thin
    case black
    case light
    case regular
    case medium
    case semiBold
    case extraBold
    case extraLight

    var name: String {
        "inter_" + rawValue
    }
}

extension Font {
    /// Use this method for custom fonts with variable weight and style.
    /// Dynamically updates the font size with the system size.
    /// - Parameters:
    ///   - weight: Cases that describe the preferred weight for fonts.
    ///   - style: Constants that describe the preferred styles for fonts.
    /// - Returns: Custom font based on the parameters you specified.
    private static func font(weight: FontWeight, style: UIFont.TextStyle) -> Font {
        .custom(weight.name, size: UIFont.preferredFont(forTextStyle: style).pointSize)
    }

    /// Use this method for custom fonts with variable weight and size.
    /// Dynamically updates the font size with the system size.
    /// - Parameters:
    ///   - weight: Cases that describe the preferred weight for fonts.
    ///   - size: Constants that describe the preferred size for fonts.
    /// - Returns: Custom font based on the parameters you specified.
    static func font(weight: FontWeight, size: CGFloat) -> Font {
        .custom(weight.name, size: size)
    }
}

extension Font {
    static let displayMedium = font(weight: .bold, size: 28)
    static let displaySmall = font(weight: .bold, size: 26)
    static let headlineMedium = font(weight: .bold, size: 20)
    static let headlineSmall = font(weight: .semiBold, size: 18)
    static let titleMedium = font(weight: .bold, size: 17)
    static let bodyLarge = font(weight: .bold, size: 16)
    static let bodyMedium = font(weight: .semiBold, size: 16)
    static let bodySmall = font(weight: .regular, size: 16)
    static let labelLarge = font(weight: .medium, size: 14)
    static let labelMedium = font(weight: .light, size: 14)
    static let labelSmall = font(weight: .bold, size: 11)
}
