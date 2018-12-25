package com.example.patryk.voicekeyboard;

public enum SpecialCharacter {
    PLUS("plus", "+"),
    MINUS("minus", "-"),
    QUESTION_MARK("znak_zapytania", "?"),
    EXCLAMATION_MARK("wykrzyknik", "!"),
    COMMA("przecinek", ","),
    FULL_STOP("kropka", "."),
    SPACE("spacja", " "),
    ELLIPSIS("wielokropek", "..."),
    SEMICOLON("średnik", ";"),
    COLON("dwukropek", ":");

    private String character;

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    SpecialCharacter(String name, String character){
        this.name = name;
        this.character = character;
    }

    public static SpecialCharacter fromString(String text) {
        for (SpecialCharacter character : SpecialCharacter.values()) {
            if (character.name.equalsIgnoreCase(text)) {
                return character;
            }
        }
        return null;
    }
}
