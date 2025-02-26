//
//  PrimaryButtonModifier.swift
//  iosApp
//
//  Created by Wong Samuel on 25/2/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct PrimaryButtonModifier: ViewModifier {
    func body(content: Content) -> some View {
        content.frame(height: 55).frame(maxWidth: .infinity).foregroundStyle(Color.white).background(Color.black).clipShape(Capsule())
    }
}

extension View {
    func makePrimaryButton() -> some View {
        ModifiedContent(content: self, modifier: PrimaryButtonModifier())
    }
}
