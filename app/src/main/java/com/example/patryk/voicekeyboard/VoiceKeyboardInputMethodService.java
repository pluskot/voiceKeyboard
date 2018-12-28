package com.example.patryk.voicekeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class VoiceKeyboardInputMethodService extends InputMethodService {
    private FrameLayout keyboardView;
    private ImageButton addLetterButton;
    private ImageButton addNumericButton;
    private ImageButton addSpecialCharacterButton;
    private ImageButton fullSetupButton;
    private TextView captionText;
    private FrameLayout buttonPanel;
    private FrameLayout captionPanel;

    @Override
    public View onCreateInputView() {
        keyboardView = (FrameLayout) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        captionPanel = keyboardView.findViewById(R.id.captionPanel);
        buttonPanel = keyboardView.findViewById(R.id.buttonPanel);
        captionText = keyboardView.findViewById(R.id.caption_text);
        addLetterButton = keyboardView.findViewById(R.id.addLetter);
        addNumericButton = keyboardView.findViewById(R.id.addNumeric);
        addSpecialCharacterButton = keyboardView.findViewById(R.id.addSpecialCharacter);
        fullSetupButton = keyboardView.findViewById(R.id.fullSetup);
        addLetterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddLetterButtonClick();
            }
        });
        addNumericButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddNumericButtonClick();
            }
        });
        addSpecialCharacterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddSpecialCharacterButtonClick();
            }
        });
        fullSetupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFullSetupButtonClick();
            }
        });
        return keyboardView;
    }

    private void onAddLetterButtonClick() {

    }

    private void onAddNumericButtonClick() {

    }

    private void onAddSpecialCharacterButtonClick() {

    }

    private void onFullSetupButtonClick() {

    }
}
