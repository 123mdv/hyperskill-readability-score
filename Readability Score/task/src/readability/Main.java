import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var filePath = args[0];
        var textAnalyser = new TextAnalyser(filePath);
        var textReport = new TextReport(textAnalyser);
        textReport.printDetails();
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        Scanner sc = new Scanner(System.in);
        String score = sc.nextLine();
        sc.close();
        System.out.println();
        textReport.printScore(score);
    }
}