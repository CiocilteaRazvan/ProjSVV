package lab1;

public class StringConv {
    public int strToInt(String str) {
        int nr = 0;
        for (int i =0; i < str.length(); i++) {
            int digit = (int) str.charAt(str.length() -i -1) - (int) '0';
            nr = nr + digit * (int)Math.pow(10, i);
        }
        return nr;
    }
}
