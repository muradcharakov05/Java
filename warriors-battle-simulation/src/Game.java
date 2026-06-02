import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;
import java.util.logging.Logger;

public class Game {

    private VictoryCondition standardVictory = fighter ->
            fighter.head.hhealth <= 0 || fighter.body.bhealth <= 0 || fighter.legs.lhealth <= 0;


    private static final Logger logger = Logger.getLogger(Game.class.getName());
    Fighter f1 = new Fighter();

    Fighter f2 = new Fighter();


    Scanner in = new Scanner(System.in);
    Random rand = new Random();

    String choice = "Выберите 1-атака головы, 2-атака туловища, 3-атака ног, 4 - зелье";
    String choice2 = "Выберите защиту 1-голова, 2-туловище, 3-ноги, 4 - зелье";

    Inventory inventory = new Inventory();

    private static class BattleState {
        boolean playerTurn = true;
        boolean finished = false;
    }

    BattleState state = new BattleState();

    public void start() {
        logger.info("Игра запущена");

        Thread playerThread = new Thread(this::playerLoop);
        Thread enemyThread = new Thread(this::enemyLoop);

        playerThread.start();
        enemyThread.start();
        try {
            playerThread.join();
            enemyThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int sum = f1.damages.stream()
                .mapToInt(s -> s)
                .sum();

        long countatt = f1.damages.stream()
                .filter(d -> d > 0)
                .count();

        System.out.println("Общее количество урона: " + sum);
        System.out.println("Количество успешных атак: " + countatt);
        in.close();
    }

    private void playerLoop() {

        while (true) {

            synchronized (state) {
                while (!state.playerTurn && !state.finished) {
                    try {
                        state.wait();
                    } catch (InterruptedException ignored) {
                    }
                }
                if (state.finished) return;
            }

            int critAtt = rand.nextInt(10);
            try {


                int num = rand.nextInt(3) + 1;
                System.out.println(choice);
                if (!in.hasNextInt()) {
                    finishGame();
                    return;
                }
                int x = in.nextInt();
                while (x == 4) {
                    for (String key : inventory.attzel.keySet()) {
                        System.out.println(key + " " + inventory.attzel.get(key));
                    }

                    System.out.println("Какое зелье хотите выпить?\n1 - зелье ярости, 2 - зелье заморозки, 0 - ничего");
                    int c = in.nextInt();

                    switch (c) {
                        case 0:
                            System.out.println("Выберите 1-атака головы, 2-атака туловища, 3-атака ног, 4-выпить зелье");
                            x = in.nextInt();
                            break;
                        case 1:
                            if (inventory.attzel.get("Зелье Ярости") == 0) {
                                System.out.println("Зелье закончилось");
                                break;
                            }
                            inventory.attzel.compute("Зелье Ярости", (k, v) -> v - 1);
                            f1.zelat = 1;
                            System.out.println(choice);
                            x = in.nextInt();
                            break;
                        case 2:
                            if (inventory.attzel.get("Зелье Заморозки") == 0) {
                                System.out.println("Зелье закончилось");
                                break;
                            }
                            inventory.attzel.compute("Зелье Заморозки", (k, v) -> v - 1);
                            f1.zelzam = 3;
                            System.out.println(choice);
                            x = in.nextInt();
                            break;


                    }


                }
                if (x < 1 || x > 5) throw new InvalidMoveException("Неверный выбор атаки!");

                if (x == num) {
                    System.out.println("Противник защитился");
                    logger.info("Ход игрока: атака заблокирована");
                    f1.damages.add(0);
                    resultHealth();
                } else {
                    logger.info("Ход игрока: атака на " + x);

                    System.out.println("Противник выбрал " + num);
                    attackByPlayer(x, critAtt);
                    resultHealth();


                }
                if (checkVictory()){
                    resultHealth();
                    return;
                }

                synchronized (state) {
                    if (f1.zelzam != 3) {
                        state.playerTurn = false;
                    }
                    f1.zelzam = 0;
                    state.notifyAll();
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }

        }
    }

    private void enemyLoop() {
        synchronized (state) {
            while (state.playerTurn && !state.finished) {
                try {
                    state.wait();
                } catch (InterruptedException ignored) {
                }
            }
            if (state.finished) return;
        }

        int critAtt = rand.nextInt(10);

        try {
            System.out.println(choice2);
            int x = in.nextInt();

            while (x == 4) {

                for (String key : inventory.defzel.keySet()) {
                    System.out.println(key + " " + inventory.defzel.get(key));
                }

                System.out.println("Какое зелье хотите выпить?\n1 - зелье защиты, 0 - ничего");
                int c = in.nextInt();

                switch (c) {
                    case 0:
                        System.out.println(choice2);
                        x = in.nextInt();
                        break;
                    case 1:
                        if (inventory.defzel.get("Зелье Защиты") == 0) {
                            System.out.println("Зелье закончилось");
                            break;
                        }
                        inventory.defzel.compute("Зелье Защиты", (k, v) -> v - 1);
                        f1.zeldef = 2;
                        System.out.println(choice2);
                        x = in.nextInt();
                        break;


                }


            }

            int num = rand.nextInt(3) + 1;

            if (x == num) {
                System.out.println("Ты защитился");
                resultHealth();
            } else {
                System.out.println("Противник выбрал " + num);
                attackByEnemy(num, critAtt);
                resultHealth();
            }

            if (checkVictory()) {
                resultHealth();
                return;
            }

            synchronized (state) {
                state.playerTurn = true;
                state.notifyAll();
            }


        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }





    private void attackByPlayer(int x, int critAtt) {
        switch (x) {
            case 1 -> {
                f1.attackh(f2.head); f1.damages.add(20);
                if (critAtt == 5 || f1.zelat == 1) {
                    System.out.println("Крит!");
                    f1.attackh(f2.head); f1.damages.add(20);
                }
            }
            case 2 -> {
                f1.attackb(f2.body); f1.damages.add(20);
                if (critAtt == 5 || f1.zelat == 1) {
                    System.out.println("Крит!");
                    f1.attackb(f2.body); f1.damages.add(20);
                }
            }
            case 3 -> {
                f1.attackl(f2.legs); f1.damages.add(20);
                if (critAtt == 5 || f1.zelat == 1) {
                    System.out.println("Крит!");
                    f1.attackl(f2.legs); f1.damages.add(20);
                }
            }
        }
        f1.zelat = 0;

    }

    private void attackByEnemy(int num, int critAtt) {
        switch (num) {
            case 1 -> {
                if (f1.zeldef != 2) f2.attackh(f1.head);
                if (critAtt == 5) f2.attackh(f1.head);
            }
            case 2 -> {
                if (f1.zeldef != 2) f2.attackb(f1.body);
                if (critAtt == 5) f2.attackb(f1.body);
            }
            case 3 -> {
                if (f1.zeldef != 2) f2.attackl(f1.legs);
                if (critAtt == 5) f2.attackl(f1.legs);
            }
        }
    }

    private boolean checkVictory() {
        if (standardVictory.check(f1)) {
            System.out.println("Ты проиграл!");
            finishGame();
            return true;
        }
        if (standardVictory.check(f2)) {
            System.out.println("Ты выиграл!");
            finishGame();
            return true;
        }
        return false;
    }

    private void finishGame() {
        synchronized (state) {
            state.finished = true;
            state.notifyAll();
        }
    }
    private void resultHealth(){
        System.out.println("нГолова:" + f1.head.hhealth +
                " нТуловище:" + f1.body.bhealth +
                " нНоги:" + f1.legs.lhealth +
                " пГолова:" + f2.head.hhealth +
                " пТуловище:" + f2.body.bhealth +
                " пНоги:" + f2.legs.lhealth);
    }
    }


