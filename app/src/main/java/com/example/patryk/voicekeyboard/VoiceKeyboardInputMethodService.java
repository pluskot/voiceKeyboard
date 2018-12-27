package com.example.patryk.voicekeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.view.View;
import android.widget.FrameLayout;

public class VoiceKeyboardInputMethodService extends InputMethodService{
    private FrameLayout keyboardView;

    @Override
    public View onCreateInputView() {
        keyboardView = (FrameLayout) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        return keyboardView;
    }
}
