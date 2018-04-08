package engine.quality.util;

import java.util.List;

/**
 * Parser for SQL queries with parameters
 */
public class SQLParser {

    private static final char START_PARAMETER = '{';
    private static final char END_PARAMETER = '}';

    private final char[] characters;
    private final int size;
    private int pointer;

    public SQLParser(char[] characters) {
        this.characters = characters;
        this.size = characters.length;
    }

    public SQLParser(String string) {
        this.characters = string.toCharArray();
        this.size = string.length();
    }

    /**
     * Replace parametres in {@link #characters} with value from arguments
     * @param arguments
     * @return String with replaced values
     */
    public String parametrize(List<String> arguments) {
        StringBuilder builder = new StringBuilder();
        int parameter;

        for (pointer = 0; pointer < size; pointer++) {
            if (characters[pointer] == START_PARAMETER) {
                parameter = readParameter();
                if (parameter >= 0 && parameter < arguments.size()) {
                    builder.append(arguments.get(parameter));
                } else {
                    throw new IllegalArgumentException("Wrong parameters in SQL query");
                }
            }
            else {
                builder.append(characters[pointer]);
            }
        }
        return builder.toString();
    }

    private int readParameter() {
        StringBuilder parameterNumber = new StringBuilder();
        for (pointer++; pointer < size; pointer++) {
            if (characters[pointer] == END_PARAMETER) {
                return Integer.parseInt(parameterNumber.toString());
            }
            parameterNumber.append(characters[pointer]);
        }
        return -1;
    }
}
