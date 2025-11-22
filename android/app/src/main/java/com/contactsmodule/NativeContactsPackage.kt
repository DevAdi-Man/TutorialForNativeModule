package com.contactsmodule

import com.facebook.react.BaseReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider

/**
 * Registers the ContactModule as a TurboModule with React Native.
 * Assumes a class named 'ContactModule' exists in the same package.
 */
class NativeContactsPackage : BaseReactPackage() {

    // --- 1. Module Instantiation (Fixed: Ensures the result is returned) ---
    override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
        // Use a Kotlin 'if' expression or 'when' expression to directly return the result.
        return if (name == ContactModule.NAME) {
            ContactModule(reactContext) // Assumes ContactModule is defined and implements NativeModule
        } else {
            null
        }
    }


    // --- 2. Module Metadata (Fixed: Correctly defined as a top-level override) ---
    override fun getReactModuleInfoProvider() = ReactModuleInfoProvider {
        // This map contains metadata for all modules registered in this package.
        mapOf(
            ContactModule.NAME to ReactModuleInfo(
                name = ContactModule.NAME,
                // The className should be the fully qualified name of the module class,
                // but using the NAME constant is often acceptable for simpler setups.
                className = ContactModule.NAME,
                canOverrideExistingModule = false,
                needsEagerInit = false,
                isCxxModule = false,
                isTurboModule = true // Correctly identifies it as a modern TurboModule
            )
        )
    }
}