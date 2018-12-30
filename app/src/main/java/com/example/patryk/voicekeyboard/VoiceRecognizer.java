package com.example.patryk.voicekeyboard;

import com.example.patryk.voicekeyboard.SpecialCharacter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

public class VoiceRecognizer {
    private SpeechRecognizer recognizer;
    private RecognitionMode mode;
    private String result;


    public enum RecognitionMode{
        LETTER, DIGIT, CHARACTER, FULL, OTHER;
    }

    /* Named searches allow to quickly reconfigure the decoder */
    public static final String KWS_SEARCH = "wakeup";
    public static final String ALPHABET_SEARCH = "alphabet";
    public static final String DIGITS_SEARCH = "digits";
    public static final String MENU_SEARCH = "menu";
    public static final String SPECIAL_CHARACTERS_SEARCH = "characters";

    /* Keyword we are looking for to activate menu */
    public static final String START_KEYPHRASE = "start";
    public static final String ALPHABET_KEYPHRASE = "litery";
    public static final String DIGITS_KEYPHRASE = "cyfry";
    public static final String SPECIAL_CHARACTERS_KEYPHRASE = "znaki";

    public void onBeginningOfSpeech() {

    }

    public void onEndOfSpeech() {
        recognizer.stop();
    }

    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;
        if(mode == RecognitionMode.FULL){
            String text = hypothesis.getHypstr();
            if (text.equals(START_KEYPHRASE)) {
                switchSearch(MENU_SEARCH);
                mode = RecognitionMode.OTHER;
            }
            else if (text.equals(ALPHABET_KEYPHRASE)) {
                switchSearch(ALPHABET_SEARCH);
                mode = RecognitionMode.LETTER;
            }
            else if (text.equals(DIGITS_KEYPHRASE)){
                switchSearch(DIGITS_SEARCH);
                mode = RecognitionMode.DIGIT;
            }
            else if (text.equals(SPECIAL_CHARACTERS_KEYPHRASE)) {
                mode = RecognitionMode.CHARACTER;
                switchSearch(SPECIAL_CHARACTERS_SEARCH);
            }
        }
    }

    public void onResult(Hypothesis hypothesis) {
        result = "";
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            if (shouldDisplayResult()) {
                if(mode == RecognitionMode.CHARACTER){
                    SpecialCharacter character = SpecialCharacter.fromString(text);
                    if(character!=null){
                        result = character.getCharacter();
                    }
                }else {
                    result = text;
                }
            }
        }
    }

    public void onError(Exception e) {
        result = "error";
    }

    public void onTimeout() {
        result = "timeout";
    }

    public void destroy(){
        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
        }
    }

    public boolean shouldDisplayResult() {
        return Arrays.asList(RecognitionMode.CHARACTER, RecognitionMode.LETTER, RecognitionMode.DIGIT).contains(mode);
    }

    public void switchSearch(String searchName) {
        recognizer.stop();

        if (searchName.equals(KWS_SEARCH)) {
            recognizer.startListening(searchName);
        } else if (searchName.equals(ALPHABET_SEARCH)) {
            recognizer.startListening(searchName, 500);
        } else {
            recognizer.startListening(searchName, 10000);
        }
    }

    public void initVoiceRecognizer(File assetsDir, RecognitionListener listener) throws IOException {
        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "de-de-ptm"))
                .setDictionary(new File(assetsDir, "pl-pl.dict"))

                .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)

                .getRecognizer();
        recognizer.addListener(listener);

/* In your application you might not need to add all those searches.
They are added here for demonstration. You can leave just one.
*/

// Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, START_KEYPHRASE);

        File menuGrammar = new File(assetsDir, "menu.gram");
        recognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);
// Phonetic search
        File alphabetGrammar = new File(assetsDir, "alphabet.gram");
        recognizer.addGrammarSearch(ALPHABET_SEARCH, alphabetGrammar);

        File digitsGrammar = new File(assetsDir, "digits.gram");
        recognizer.addGrammarSearch(DIGITS_SEARCH, digitsGrammar);

        File specialCharactersGrammar = new File(assetsDir, "characters.gram");
        recognizer.addGrammarSearch(SPECIAL_CHARACTERS_SEARCH, specialCharactersGrammar);
    }

    public SpeechRecognizer getRecognizer() {
        return recognizer;
    }

    public void setRecognizer(SpeechRecognizer recognizer) {
        this.recognizer = recognizer;
    }

    public RecognitionMode getMode() {
        return mode;
    }

    public void setMode(RecognitionMode mode) {
        this.mode = mode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
