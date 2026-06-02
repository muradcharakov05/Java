package academ_java;

import java.util.Random;
import java.util.Scanner;

public class consoleGameLizardSpock {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int playerScore = 0;
        int computerScore = 0;

        while (true) {
            System.out.println("Выберите: 1 - Камень, 2 - Ножницы, 3 - Бумага, 4 - Ящерица, 5 - Спок");
            int playerChoice = 0;
            try {
                playerChoice = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Некорректный ввод. Пожалуйста, введите число от 1 до 5.");
                scanner.next(); // очистка некорректного ввода из сканера
                continue;
            }

            if (playerChoice < 1 || playerChoice > 5) {
                System.out.println("Некорректный выбор. Пожалуйста, выберите число от 1 до 5.");
                continue;
            }


            int computerChoice = random.nextInt(5) + 1;

            // Получение строкового представления выбора
            String playerMove = getMoveName(playerChoice);
            String computerMove = getMoveName(computerChoice);

            System.out.println("Вы выбрали: " + playerMove);
            System.out.println("Компьютер выбрал: " + computerMove);


            int result = determineWinner(playerChoice, computerChoice);

            if (result == 0) {
                System.out.println("Ничья!");
            } else if (result == 1) {
                System.out.println("Вы выиграли!");
                playerScore++;
            } else {
                System.out.println("Компьютер выиграл!");
                computerScore++;
            }


            System.out.println("Счет: Вы - " + playerScore + ", Компьютер - " + computerScore);


            System.out.println("Хотите сыграть еще раз? (да/нет)");
            String playAgain = scanner.next().toLowerCase();

            if (!playAgain.equals("да")) {
                break;
            }
        }

        System.out.println("Спасибо за игру!");
        scanner.close();
    }


    public static String getMoveName(int choice) {
        switch (choice) {
            case 1:
                return "Камень";
            case 2:
                return "Ножницы";
            case 3:
                return "Бумага";
            case 4:
                return "Ящерица";
            case 5:
                return "Спок";
            default:
                return "Неизвестно";
        }
    }


    public static int determineWinner(int playerChoice, int computerChoice) {
        if (playerChoice == computerChoice) {
            return 0;
        }


        if ((playerChoice == 1 && (computerChoice == 2 || computerChoice == 4)) || // Камень > Ножницы, Камень > Ящерица
                (playerChoice == 2 && (computerChoice == 3 || computerChoice == 4)) || // Ножницы > Бумага, Ножницы > Ящерица
                (playerChoice == 3 && (computerChoice == 1 || computerChoice == 5)) || // Бумага > Камень, Бумага > Спок
                (playerChoice == 4 && (computerChoice == 3 || computerChoice == 5)) || // Ящерица > Бумага, Ящерица > Спок
                (playerChoice == 5 && (computerChoice == 1 || computerChoice == 2))) { // Спок > Камень, Спок > Ножницы
            return 1;
        } else {
            return -1;
        }
    }
}
