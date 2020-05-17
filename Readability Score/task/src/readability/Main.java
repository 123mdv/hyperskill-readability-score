package readability;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import static java.util.Map.entry;

public class Main {
    public static void main(String[] args) {
        TextAnalyser textAnalyser = new TextAnalyser(args[0]);
        TextReport textReport = new TextReport(textAnalyser);
        textReport.printDetails();
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        Scanner sc = new Scanner(System.in);
        String score = sc.nextLine();
        sc.close();
        System.out.println();
        textReport.printScore(score);
    }
}

class TextReport {
    TextAnalyser textAnalyser;

    public TextReport(TextAnalyser textAnalyser) {
    this.textAnalyser = textAnalyser;
    }

    public void printDetails() {
        System.out.println("The text is:");
        System.out.println(textAnalyser.text);
        System.out.println();
        System.out.println("Words: " + textAnalyser.wordCount);
        System.out.println("Sentences: " + textAnalyser.sentCount);
        System.out.println("Characters: " + textAnalyser.charCount);
        System.out.println("Syllables: " + textAnalyser.syllCount);
        System.out.println("Polysyllables: " + textAnalyser.polyCount);
    }

    public void printScore(String score) {
        switch (score) {
            case "ARI":
                printARI();
                break;
            case "FK":
                printFK();
                break;
            case "SMOG":
                printSMOG();
                break;
            case "CL":
                printCL();
                break;
            case "all":
                printARI();
                printFK();
                printSMOG();
                printCL();
                System.out.println();
                printAverageAge();
        }
    }

    private void printARI() {
        System.out.println("Automated Readability Index: " + textAnalyser.ariScore
                + " (about " + textAnalyser.ariAge + " year olds).");
    }

    private void printFK() {
        System.out.println("Flesch–Kincaid readability tests: " + textAnalyser.fkScore
                + " (about " + textAnalyser.fkAge + " year olds).");
    }

    private void printSMOG() {
        System.out.println("Simple Measure of Gobbledygook: " + textAnalyser.smogScore
                + " (about " + textAnalyser.smogAge + " year olds).");
    }

    private void printCL() {
        System.out.println("Coleman–Liau index: " + textAnalyser.clScore
                + " (about " + textAnalyser.clAge + " year olds).");
    }

    private void printAverageAge() {
        System.out.println("This text should be understood in average by "
                + textAnalyser.averageAge + " year olds.");
    }

}

class TextAnalyser {
    String text = "";
    String[] words;
    int wordCount, sentCount, charCount, syllCount, polyCount = 0;
    double ariScore, fkScore, smogScore, clScore, averageAge = 0;
    String ariAge, fkAge, smogAge, clAge = "";

    public TextAnalyser(String fileName) {
        try (FileReader reader = new FileReader(fileName)) {
            Scanner scanner = new Scanner(reader);
            text = scanner.nextLine();
            words = text.split("\\s+");
            wordCount = countWords();
            sentCount = countSentences();
            charCount = countChars();
            syllCount = countSyllables();
            polyCount = countPolysyllables();
            ariScore = calculateARI();
            ariAge = getAgeFromScore(ariScore);
            fkScore = calculateFK();
            fkAge = getAgeFromScore(fkScore);
            smogScore = calculateSMOG();
            smogAge = getAgeFromScore(smogScore);
            clScore = calculateCL();
            clAge = getAgeFromScore(clScore);
            averageAge = calculateAverageAge();
            scanner.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.getStackTrace());
        }
    }

    private double calculateAverageAge() {
        return (Integer.parseInt(ariAge.substring(0, 2)) +
                Integer.parseInt(fkAge.substring(0, 2)) +
                Integer.parseInt(smogAge.substring(0, 2)) +
                Integer.parseInt((clAge).substring(0, 2))
                ) / 4.0;
    }

    private String getAgeFromScore(double score) {
        Map<Integer, String> ages = Map.ofEntries(
                entry(1, "6"),
                entry(2, "7"),
                entry(3, "9"),
                entry(4, "10"),
                entry(5, "11"),
                entry(6, "12"),
                entry(7, "13"),
                entry(8, "14"),
                entry(9, "15"),
                entry(10, "16"),
                entry(11, "17"),
                entry(12, "18"),
                entry(13, "24"),
                entry(14, "24+")
        );
        if (score < 1) score = 1;
        if (score > 14) score = 14;
        return ages.get((int) Math.round(score));
    }

    private double calculateCL() {
        double l = charCount / (wordCount / 100.0);
        double s = sentCount / (wordCount / 100.0);
        double score = 0.0588 * l - 0.296 * s - 15.8;
        return roundScore(score);
    }

    private double calculateSMOG() {
        double score = 1.043 * Math.sqrt(polyCount * (30.0 / sentCount)) + 3.1291;
        return roundScore(score);
    }

    private double calculateFK() {
        double score = (0.39 * ((double) wordCount / sentCount)) + (11.8 * ((double) syllCount / wordCount)) - 15.59;
        return roundScore(score);
    }

    private double roundScore(double score) {
        return Math.round(score * 100.0) / 100.0;
    }

    private double calculateARI() {
        double score = 4.71 * charCount / wordCount
                + 0.5 * wordCount / sentCount - 21.43;
        return roundScore(score);
    }

    public int countWords() {
        return words.length;
    }

    public int countSentences() {
        return text.split("[!.?]").length;
    }

    public int countChars() {
        int result = 0;
        for (String word : words) {
            result += word.length();
        }
        return result;
    }

    public int countSyllables() {
        int syllables = 0;
        for (String word : words) {
            syllables += countSyllablesInWord(word);
        }
        return syllables;
    }

    public int countPolysyllables() {
        int polysyllables = 0;
        for (String word: words) {
            if (countSyllablesInWord(word) > 2)
                polysyllables++;
        }
        return polysyllables;
    }

    private int countSyllablesInWord(String word) {
        int syllables = 0;
        char[] chars = word.toCharArray();
        for (char ch : chars) {
            if ((ch + "").matches("([aeiou])"))
                syllables++;
        }
        for (int i = 0; i < word.length() - 1; i++) {
            if (word.substring(i, i + 2).matches("[aeiou][aeiou]"))
                syllables--;
        }
        if (word.matches(".*e$")) syllables--;
        if (word.matches(".*e[!.?]$")) syllables--;
        if (syllables == 0) syllables++;
        return syllables;
    }

}