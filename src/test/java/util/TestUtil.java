package util;

public class TestUtil {
    public static String getRandomNumber(int n)
    {

        String AlphaNumericString = "123456789";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }

    public static String getRandomNumber(int n, String start)
    {
        String AlphaNumericString = "123456789";

        StringBuilder sb = new StringBuilder(n);
        sb.append(start);
        for (int i = 0; i < n-start.length(); i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }
}
