package org.api.subsonic.utils;


import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public class CharUtil {
    private CharUtil() {}
    
    
    @NotNull
    public static Character getCharacterFirstLetter(String name) {
        return switch (Optional.ofNullable(name).orElse("").charAt(0)) {
            case 'A', 'a' -> 'A';
            case 'B', 'b' -> 'B';
            case 'C', 'c' -> 'C';
            case 'D', 'd' -> 'D';
            case 'E', 'e' -> 'E';
            case 'F', 'f' -> 'F';
            case 'G', 'g' -> 'G';
            case 'H', 'h' -> 'H';
            case 'I', 'i' -> 'I';
            case 'J', 'j' -> 'J';
            case 'K', 'k' -> 'K';
            case 'L', 'l' -> 'L';
            case 'M', 'm' -> 'M';
            case 'N', 'n' -> 'N';
            case 'O', 'o' -> 'O';
            case 'P', 'p' -> 'P';
            case 'Q', 'q' -> 'Q';
            case 'R', 'r' -> 'R';
            case 'S', 's' -> 'S';
            case 'T', 't' -> 'T';
            case 'U', 'u' -> 'U';
            case 'W', 'w' -> 'W';
            case 'X', 'x' -> 'X';
            case 'Y', 'y' -> 'Y';
            case 'Z', 'z' -> 'Z';
            default -> '#';
        };
    }
}
