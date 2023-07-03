
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        char[] inputNumbers = input.toCharArray();
        Integer leftSum = 0;
        Integer rightSum = 0;

        for (int i = 0; i < 3; i++) {
            leftSum += Character.getNumericValue(inputNumbers[i]);
        }

        for (int i = 3; i < 6; i++) {
            rightSum += Character.getNumericValue(inputNumbers[i]);
        }

        String output = leftSum.equals(rightSum) ? "Lucky" : "Regular";

        System.out.println(output);
    }
}
