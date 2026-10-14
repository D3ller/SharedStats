package fr.korastrip.paperplugin.enums;

import fr.korastrip.paperplugin.Config;

public enum ErrorMessage {
    NO_GROUP("There is no group. Create one with <gold>/" + Config.PREFIX + " create</gold>."),
    NOT_IN_GROUP("You are not in the group."),
    NOT_OWNER("Only the group owner can use this command."),
    NO_GAME_RUNNING("No game is running. Start one with <gold>/" + Config.PREFIX + " start</gold>."),
    GAME_RUNNING("A game is running. Stop it with <gold>/" + Config.PREFIX + " stop</gold>."),
    PLAYER_NOT_FOUND("Player not found."),
    GROUP_ALREADY_EXISTS("A group already exists. You cannot create another one."),
    PLAYER_ALREADY_IN_GROUP("This player is already in the group."),
    PLAYER_NOT_IN_GROUP("This player is not in the group."),
    CANNOT_REMOVE_YOURSELF("You cannot remove yourself. Use <gold>/" + Config.PREFIX + " revoke</gold> instead."),
    NOT_ENOUGH_PLAYERS("You cannot start a game alone. Invite at least one player.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
