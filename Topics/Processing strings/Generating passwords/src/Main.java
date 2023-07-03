import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Integer A = sc.nextInt();
        Integer B = sc.nextInt();
        Integer C = sc.nextInt();
        Integer N = sc.nextInt();

        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < A; i++) {
            char randomUpperCase = (char) (random.nextInt(26) + 'A');
            if (!password.isEmpty()) {
                while (password.charAt(i-1) == randomUpperCase) {
                    randomUpperCase = (char) (random.nextInt(26) + 'A');
                }
            }
            password.append(randomUpperCase);
        }

        for (int i = A; i < (A+B); i++) {
            char randomLowerCase = (char) (random.nextInt(26) + 'a');
            if (!password.isEmpty()) {
                while (password.charAt(i-1) == randomLowerCase) {
                    randomLowerCase = (char) (random.nextInt(26) + 'a');
                }
            }
            password.append(randomLowerCase);
        }

        for (int i = (A+B); i < (A+B+C); i++) {
            char randomDigit = (char) (random.nextInt(10) + '0');
            if (!password.isEmpty()) {
                while (password.charAt(i - 1) == randomDigit) {
                    randomDigit = (char) (random.nextInt(10) + '0');
                }
            }
            password.append(randomDigit);
        }

        while (password.length() < N) {
            char randomUpperCase = (char) (random.nextInt(26) + 'A');
            if (!password.isEmpty()) {
                while (password.charAt(password.length() - 1) == randomUpperCase) {
                    randomUpperCase = (char) (random.nextInt(26) + 'A');
                }
            }
            password.append(randomUpperCase);
        }
        
        System.out.println(password.toString());
    }

}
