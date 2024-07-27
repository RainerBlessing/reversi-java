package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MoveParser {
    Move parseMove(String inputString) {
        List<String> coordinateToken = parseMoveStringIntoToken(inputString);

        if(coordinateToken.size()!=2){
            return new Move();
        }

        String startToken = coordinateToken.getFirst();
        char colCharStart = startToken.charAt(0);
        char rowCharStart = startToken.charAt(1);

        String endToken = coordinateToken.get(1);
        char colCharEnd = endToken.charAt(0);
        char rowCharEnd = endToken.charAt(1);

        return new Move(colCharStart,rowCharStart,colCharEnd,rowCharEnd);
    }

    ArrayList<String> parseMoveStringIntoToken(String inputString) {
        char separator = determineSeparator(inputString);
        String[] tokens = inputString.split(String.valueOf(separator));

        return Arrays.stream(tokens).map(String::trim).collect(Collectors.toCollection(ArrayList::new));
    }

    char determineSeparator(String inputString) {
        boolean separator = inputString.indexOf(',') > -1;
        char separatorChar = 0;
        if (!separator) {
            separator = inputString.indexOf('-') > -1;
            if (!separator) {
                separator = inputString.indexOf(' ') > -1;
                if (separator) {
                    separatorChar = ' ';
                }
            } else {
                separatorChar = '-';
            }
        } else {
            separatorChar = ',';
        }

        return separatorChar;
    }
}
