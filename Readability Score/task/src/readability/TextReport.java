package readability;

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
