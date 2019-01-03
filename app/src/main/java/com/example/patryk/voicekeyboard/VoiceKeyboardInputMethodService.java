package com.example.patryk.voicekeyboard;

import android.app.Activity;
import android.inputmethodservice.InputMethodService;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;

public class VoiceKeyboardInputMethodService extends InputMethodService implements RecognitionListener {
    private FrameLayout keyboardView;
    private ImageButton addLetterButton;
    private ImageButton addNumericButton;
    private ImageButton addSpecialCharacterButton;
    private ImageButton addCommandButton;
    private ImageButton fullSetupButton;
    private ImageButton shiftIcon;
    private ImageButton altIcon;
    private TextView captionText;
    private FrameLayout buttonPanel;
    private FrameLayout captionPanel;
    private FrameLayout modifiersPanel;
    private VoiceRecognizer voiceRecognizer;

    @Override
    public View onCreateInputView() {
        keyboardView = (FrameLayout) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        captionPanel = keyboardView.findViewById(R.id.captionPanel);
        buttonPanel = keyboardView.findViewById(R.id.buttonPanel);
        modifiersPanel = keyboardView.findViewById(R.id.modifiersPanel);
        captionText = keyboardView.findViewById(R.id.caption_text);
        addLetterButton = keyboardView.findViewById(R.id.addLetter);
        addNumericButton = keyboardView.findViewById(R.id.addNumeric);
        addSpecialCharacterButton = keyboardView.findViewById(R.id.addSpecialCharacter);
        addCommandButton = keyboardView.findViewById(R.id.addCommand);
        fullSetupButton = keyboardView.findViewById(R.id.fullSetup);
        shiftIcon = keyboardView.findViewById(R.id.shiftIcon);
        altIcon = keyboardView.findViewById(R.id.altIcon);
        voiceRecognizer = new VoiceRecognizer();
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
        addCommandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddCommandClick();
            }
        });
        fullSetupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFullSetupButtonClick();
            }
        });
        changePanelsVisibility(false);
        changeModifiersVisibility(false);
        new SetupTask(this).execute();
        return keyboardView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        voiceRecognizer.destroy();
    }

    private void onAddLetterButtonClick() {
        changePanelsVisibility(true);
        changeModifiersVisibility(true);
        voiceRecognizer.setMode(VoiceRecognizer.RecognitionMode.LETTER);
        voiceRecognizer.switchSearch(VoiceRecognizer.ALPHABET_SEARCH);
        captionText.setText(R.string.alphabet_caption);
    }

    private void onAddNumericButtonClick() {
        changePanelsVisibility(true);
        changeModifiersVisibility(false);
        voiceRecognizer.setMode(VoiceRecognizer.RecognitionMode.DIGIT);
        voiceRecognizer.switchSearch(VoiceRecognizer.DIGITS_SEARCH);
        captionText.setText(R.string.digits_caption);
    }

    private void onAddSpecialCharacterButtonClick() {
        changePanelsVisibility(true);
        changeModifiersVisibility(false);
        voiceRecognizer.setMode(VoiceRecognizer.RecognitionMode.CHARACTER);
        voiceRecognizer.switchSearch(VoiceRecognizer.SPECIAL_CHARACTERS_SEARCH);
        captionText.setText(R.string.special_characters_caption);
    }

    private void onAddCommandClick() {
        changePanelsVisibility(true);
        changeModifiersVisibility(false);
        voiceRecognizer.setMode(VoiceRecognizer.RecognitionMode.COMMANDS);
        voiceRecognizer.switchSearch(VoiceRecognizer.COMMANDS_SEARCH);
        captionText.setText(R.string.command_caption);
    }

    private void onFullSetupButtonClick() {
        changePanelsVisibility(true);
        changeModifiersVisibility(false);
        voiceRecognizer.setMode(VoiceRecognizer.RecognitionMode.FULL);
        voiceRecognizer.switchSearch(VoiceRecognizer.KWS_SEARCH);
        captionText.setText(R.string.menu_caption);
    }

    private void changePanelsVisibility(boolean hideButtons) {
        if (hideButtons) {
            buttonPanel.setVisibility(View.GONE);
            captionPanel.setVisibility(View.VISIBLE);
        } else {
            buttonPanel.setVisibility(View.VISIBLE);
            captionPanel.setVisibility(View.GONE);
        }
    }

    private void changeModifiersVisibility(boolean showModifiers) {
        if (showModifiers) {
            modifiersPanel.setVisibility(View.VISIBLE);
            if (voiceRecognizer.isShift()) {
                shiftIcon.setVisibility(View.VISIBLE);
            } else {
                shiftIcon.setVisibility(View.GONE);
            }
            if (voiceRecognizer.isAlt()) {
                altIcon.setVisibility(View.VISIBLE);
            } else {
                altIcon.setVisibility(View.GONE);
            }
        } else {
            modifiersPanel.setVisibility(View.GONE);
            altIcon.setVisibility(View.GONE);
            shiftIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBeginningOfSpeech() {
        voiceRecognizer.onBeginningOfSpeech();
    }

    @Override
    public void onEndOfSpeech() {
        voiceRecognizer.onEndOfSpeech();
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        voiceRecognizer.onPartialResult(hypothesis);
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        voiceRecognizer.onResult(hypothesis);
        InputConnection inputConnection = getCurrentInputConnection();
        if (voiceRecognizer.getMode() == VoiceRecognizer.RecognitionMode.COMMANDS) {
            if (Command.fromString(voiceRecognizer.getResult()) == Command.DELETE) {
                inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            } else if (Command.fromString(voiceRecognizer.getResult()) == Command.RESET) {
                inputConnection.performContextMenuAction(android.R.id.selectAll);
                inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            } else {
                inputConnection.commitText(voiceRecognizer.getResult(), 0);
            }
        } else {
            inputConnection.commitText(voiceRecognizer.getResult(), 0);
        }
        changePanelsVisibility(false);
        changeModifiersVisibility(false);
    }

    @Override
    public void onError(Exception e) {
        voiceRecognizer.onError(e);
        Log.i("VK", e.toString());
        changePanelsVisibility(false);
        changeModifiersVisibility(false);
    }

    @Override
    public void onTimeout() {
        voiceRecognizer.onTimeout();
        Log.i("VK", "timeout");
        changePanelsVisibility(false);
        changeModifiersVisibility(false);
    }

    private static class SetupTask extends AsyncTask<Void, Void, Exception> {
        WeakReference<VoiceKeyboardInputMethodService> activityReference;

        SetupTask(VoiceKeyboardInputMethodService activity) {
            this.activityReference = new WeakReference<>(activity);
        }

        @Override
        protected Exception doInBackground(Void... params) {
            try {
                Assets assets = new Assets(activityReference.get());
                File assetDir = assets.syncAssets();
                activityReference.get().voiceRecognizer.initVoiceRecognizer(assetDir, activityReference.get());
            } catch (IOException e) {
                return e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception result) {
            if (result != null) {
                activityReference.get().captionText.setText("Failed to init recognizer " + result);
            }
        }
    }
}
