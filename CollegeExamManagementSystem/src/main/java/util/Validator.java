package util;

public class Validator {

    public static boolean isValidUsername(String username) {
        return username != null && !username.trim().isEmpty() && username.trim().length() >= 3;
    }

    public static boolean isValidPassword(String password) {
        return password != null && !password.trim().isEmpty() && password.trim().length() >= 6;
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
