package com.example.patryk.voicekeyboard;

public enum Command {
    DELETE("kasuj", "kasuj"),
    RESET("reset", "reset"),
    SPACE("spacja", " "),
    NEW_LINE("nowa_linia", "\n");

    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    Command(String name, String command){
        this.name = name;
        this.command = command;
    }

    public static Command fromString(String text) {
        for (Command command : Command.values()) {
            if (command.name.equalsIgnoreCase(text)) {
                return command;
            }
        }
        return null;
    }
}
