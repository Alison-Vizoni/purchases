package br.com.alison.purchases.utils;

import javax.swing.text.MaskFormatter;
import java.text.ParseException;


public class GeneratorUtil {

    public static String generateName(){
        String[] nomes = {"Alexandre", "Eduardo", "Henrique", "Murilo", "Theo"};
        int indice = (int) (Math.random() * nomes.length);
        return nomes[indice].toString();
    }

    public static String generateNumber(int quantity){

        String randomNumber = "";
        for (int i = 0; i < quantity; i++) {
            randomNumber += (int) (Math.random() * 10);
        }
        return randomNumber;
    }

    private static String formatField(String value, String inputMask) {
        try {
            MaskFormatter mask = new MaskFormatter(inputMask);
            mask.setValueContainsLiteralCharacters(false);
            value = mask.valueToString(value);
        } catch (ParseException e) {
            System.out.println("Error formatting mask " + e.getMessage());
        }
        return value;
    }

    public static String generateCnpj(){
        return formatField(generateNumber(14), "##.###.###/####-##");
    }

    public static String generateCard() {
        return formatField(generateNumber(12), "####-####-####-####");
    }

    public static String generateCity() {
        String city;
        int index;
        String[] cities = {"Florianópolis", "São José", "Palhoça", "São Bento", "Biguaçu", "Curitiba", "Urupema",
                "Tijucas", "Balneário", "Camboriú", "Santo Antônio", "Lapa", "Portal", "Matinhos"};

        index = (int) (Math.random() * cities.length);
        city = cities[index];
        return city;
    }

    public static String generateCategory() {
        String category;
        int index;
        String[] categories = {"Construction material", "Clothes", "Instruments"};
        index = (int) (Math.random() * categories.length);
        category = categories[index];
        return category;
    }

    public static String generateUF() {
        String state;
        int indice;
        String[] states = {"RS", "SC", "PR", "SP", "BA", "AC", "ES", "AM", "CE", "GO", "SE", "MT", "TO", "RJ"};
        indice = (int) (Math.random() * states.length);
        state = states[indice];
        return state;
    }

    public static String generateCharacter(int qtd) {
        String word = "";
        int index;
        String[] character = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q",
                "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
                "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", " "};

        for (int i = 0; i < qtd; i++) {
            index = (int) (Math.random() * character.length);
            word += character[index];
        }
        return word;
    }

    public static String generateTeleCeluar() {
        return formatField("4899" + generateNumber(7), "(##)#####-####");
    }
}
