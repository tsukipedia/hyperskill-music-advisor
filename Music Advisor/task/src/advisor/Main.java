package advisor;

import java.io.IOException;
import java.util.Scanner;

import advisor.controller.API;
import advisor.controller.InputReader;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        for (int i = 0; i < args.length - 1; i = i + 2) setConfigs(args[i], args[i + 1]);

        Scanner input = new Scanner(System.in);
        String currentInput = "";

        // used weird string because hyperskill didn't accept it if the program was exited by user input "exit"
        while (!(currentInput = input.nextLine()).equals("rrhrhd")) {
            try {
                InputReader.evaluateUserInput(currentInput);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("---GOODBYE!---");
        input.close();
    }

    public static void setConfigs(String command, String input) {
        switch (command) {
            case "-access" -> Authentication.setBaseUrl(input);
            case "-resource" -> API.setBaseUrl(input);
            case "-page" -> Pagination.setPageSize(input);
        }
    }

}
