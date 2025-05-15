package cmpe272.tamalesHr;

import java.util.regex.Pattern;

public class UUIDValidator {
    private static final Pattern pattern = Pattern.compile(
            "^f:[0-9a-fA-F]{8}-" +
                    "[0-9a-fA-F]{4}-" +
                    "[0-9a-fA-F]{4}-" +
                    "[0-9a-fA-F]{4}-" +
                    "[0-9a-fA-F]{12}:[0-9]+$"
    );

    public static boolean isValid(String input) {
        return pattern.matcher(input).matches();
    }

    public static String extractEmpNo(String input) {
        if (!isValid(input)) {
            return null;
        }
        int lastColonIndex = input.lastIndexOf(':');
        if (lastColonIndex == -1 || lastColonIndex == input.length() - 1) {
            return null;
        }
        return input.substring(lastColonIndex + 1);
    }
}
