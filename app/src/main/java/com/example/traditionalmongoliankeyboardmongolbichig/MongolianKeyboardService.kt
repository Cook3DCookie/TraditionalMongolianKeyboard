package com.example.traditionalmongoliankeyboardmongolbichig

import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.View
import android.view.inputmethod.InputConnection

class MongolianKeyboardService : InputMethodService(), KeyboardView.OnKeyboardActionListener {

    private lateinit var keyboardView: KeyboardView

    companion object {
        const val LAYOUT_MAIN = 0
        const val LAYOUT_SYMBOLS = 1
        // Add codes for custom keys if needed, e.g.:
        const val CODE_SWITCH_TO_MAIN = -100 // Matches android:codes in symbols layout
        const val CODE_SWITCH_TO_SYMBOLS = -101 // If you add a ?123 key to main layout
    }

    private var currentLayout: Int = LAYOUT_MAIN
    private lateinit var mainKeyboard: Keyboard
    private lateinit var symbolsKeyboard: Keyboard

    override fun onCreateInputView(): View? {
        keyboardView = layoutInflater.inflate(R.layout.keyboard_view, null) as KeyboardView

        // Load the layouts
        mainKeyboard = Keyboard(this, R.xml.qwerty_mongolian_latin)
        symbolsKeyboard = Keyboard(this, R.xml.symbols_mongolian)

        // Set initial keyboard
        keyboardView.keyboard = mainKeyboard
        currentLayout = LAYOUT_MAIN

        keyboardView.setOnKeyboardActionListener(this)

        return keyboardView
    }
    // --- Keyboard Action Listener Methods ---

    override fun onPress(primaryCode: Int) {
        // Optional: Handle key press visual feedback (e.g., key preview)
    }

    override fun onRelease(primaryCode: Int) {
        // Optional: Handle key release
    }

    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        val inputConnection: InputConnection? = currentInputConnection

        when (primaryCode) {
            Keyboard.KEYCODE_DELETE -> {
                // Handle backspace
                inputConnection?.deleteSurroundingText(1, 0)
            }
            Keyboard.KEYCODE_DONE -> {
                // Handle Enter/Return key
                // inputConnection?.performEditorAction(android.view.inputmethod.EditorInfo.IME_ACTION_DONE)
                inputConnection?.commitText("\n", 1)
            }
            CODE_SWITCH_TO_MAIN -> { // Switch back to main letters
                keyboardView?.keyboard = mainKeyboard
                currentLayout = LAYOUT_MAIN
            }
            CODE_SWITCH_TO_SYMBOLS -> { // Switch to symbols
                keyboardView?.keyboard = symbolsKeyboard
                currentLayout = LAYOUT_SYMBOLS
            }
            // Add more special keys if needed (e.g., shift, symbols)
            else -> {
                // Handle regular character keys
                // The 'primaryCode' is the Unicode value you defined in the XML
                if (primaryCode > 0) {
                    val char = primaryCode.toChar()
                    inputConnection?.commitText(char.toString(), 1)
                }
            }
        }
    }

    override fun onText(text: CharSequence?) {
        // Optional: Handle text input from popups or other sources
        currentInputConnection?.commitText(text, 1)
    }

    override fun swipeLeft() {
        // Optional: Handle swipe gestures
    }

    override fun swipeRight() {
        // Optional: Handle swipe gestures
    }

    override fun swipeDown() {
        // Optional: Handle swipe gestures (maybe close keyboard?)
        requestHideSelf(0)
    }

    override fun swipeUp() {
        // Optional: Handle swipe gestures
    }
}
