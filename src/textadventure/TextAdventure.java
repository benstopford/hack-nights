package textadventure;

import java.util.*;

public class TextAdventure {

    public static void main(String[] args) {
        new TextAdventure().run();
    }

    class Question {
        String text;
        private List<Answer> answers = new ArrayList();

        public Question(String q) {
            this.text = q;
        }

        public void addAnswer(Answer a) {
            answers.add(a);
        }
    }

    class Answer {
        String text;
        Question next;

        Answer(String a) {
            text = a;
        }

        Answer setNext(Question q) {
            this.next = q;
            return this;
        }
    }

    private void run() {
        Question root = setUp();

        while (true) {
            System.out.println(root.text);
            Scanner scanner = new Scanner(System.in);
            String guess = scanner.nextLine();
            if (matches(guess, root)) {
                root = get(guess, root).next;
            } else {
                System.out.println("I didn't get that, try again");
            }
        }
    }

    private boolean matches(String guess, Question root) {
        return root.answers.stream()
                .anyMatch(
                        s -> guess.toLowerCase().contains(s.text)
                );
    }

    private Answer get(String guess, Question root) {
        return root.answers.stream()
                .filter(
                        s -> guess.toLowerCase().contains(s.text)
                ).iterator().next();
    }

    private Question setUp() {
        Question bar = new Question("You walk into a bar and the barman says, Hey?");
        Question pint = new Question("The barman gives you a pint. He stares knowingly. Anything else I can do for you?");
        Question upstairs = new Question("You walk up stairs and Kris tells you a really bad joke. Do you laugh?");
        Question laughAlone = new Question("You laugh alone. Alas Kris tells another one. Do you laugh?");
        Question sensible = new Question("Sensible! You are charged with making a text adventure game. What do you do next?");
        Question end = new Question("You start to build an adventure game... knock knock");

        bar.addAnswer(new Answer("pint").setNext(pint));
        bar.addAnswer(new Answer("beer").setNext(pint));
        bar.addAnswer(new Answer("hack").setNext(upstairs));
        bar.addAnswer(new Answer("kris").setNext(upstairs));

        pint.addAnswer(new Answer("pint").setNext(pint));
        pint.addAnswer(new Answer("beer").setNext(pint));
        pint.addAnswer(new Answer("hack").setNext(upstairs));
        pint.addAnswer(new Answer("kris").setNext(upstairs));

        upstairs.addAnswer(new Answer("yes").setNext(laughAlone));
        upstairs.addAnswer(new Answer("no").setNext(sensible));

        laughAlone.addAnswer(new Answer("yes").setNext(laughAlone));
        laughAlone.addAnswer(new Answer("no").setNext(sensible));

        sensible.addAnswer(new Answer("code").setNext(end));
        sensible.addAnswer(new Answer("hack").setNext(end));

        end.addAnswer(new Answer("").setNext(bar));

        return bar;
    }

}
