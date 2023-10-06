package ma.alae.eloula.helpers;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Helper {
    public static int getInputInt(Scanner scanner) {
        int userInput = 0; // Initialisation avec une valeur par défaut

        boolean inputValid = false;
        while (!inputValid) {
            System.out.print("Entrez un nombre entier : ");
            String input = scanner.nextLine();

            try {
                userInput = Integer.parseInt(input);
                if (userInput > 0)
                    inputValid = true; // La conversion en int a réussi, l'entrée est valide
            }
            catch (NumberFormatException e) {
                System.out.println("Entrée non valide. Veuillez entrer un nombre entier.");
            }
        }

        return userInput;
    }
    public static LocalDate getDate(String message) {
        Scanner scanner = new Scanner(System.in);
        Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        System.out.println(message);
        String inputDate = scanner.nextLine();
        if (datePattern.matcher(inputDate).matches()) {
            try {
                return LocalDate.parse(inputDate);
            } catch (Exception e) {
                System.out.println(e);
                return getDate(message);
            }
        } else {
            System.out.println("Format de date invalide. Veuillez entrer la date au format yyyy-mm-dd.");
            return getDate(message);
        }
    }
}
