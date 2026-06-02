package com.sfedu;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Класс для представления банковской карты
class BankCard {
    private String cardNumber;
    private String pinCode;
    private double balance;

    public BankCard(String cardNumber, String pinCode, double initialBalance) {
        this.cardNumber = cardNumber;
        this.pinCode = pinCode;
        this.balance = initialBalance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public boolean checkPin(String pin) {
        return this.pinCode.equals(pin);
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
}

// Класс банкомата
class BankATM {
    private Map<String, BankCard> cards = new HashMap<>();
    private double cashInATM;

    public BankATM(double initialCash) {
        this.cashInATM = initialCash;
    }

    // Добавление карты
    public void addCard(BankCard card) {
        cards.put(card.getCardNumber(), card);
    }

    // Ввод PIN и проверка
    private boolean authenticate(BankCard card, String pin) {
        return card.checkPin(pin);
    }

    // Перевод между картами
    public boolean transfer(String fromCardNum, String toCardNum, String pin, double amount) {
        BankCard fromCard = cards.get(fromCardNum);
        BankCard toCard = cards.get(toCardNum);
        if (fromCard == null || toCard == null) {
            System.out.println("Одна из карт не найдена");
            return false;
        }
        if (!authenticate(fromCard, pin)) {
            System.out.println("Неверный PIN");
            return false;
        }
        if (fromCard.withdraw(amount)) {
            toCard.deposit(amount);
            System.out.println("Перевод выполнен успешно");
            return true;
        } else {
            System.out.println("Недостаточно средств");
            return false;
        }
    }

    // Оплата услуги мобильной связи
    public boolean payMobileService(String cardNumber, String pin, double amount) {
        BankCard card = cards.get(cardNumber);
        if (card == null) {
            System.out.println("Карта не найдена");
            return false;
        }
        if (!authenticate(card, pin)) {
            System.out.println("Неверный PIN");
            return false;
        }
        if (card.withdraw(amount)) {
            cashInATM += amount; // деньги уходят в кассу
            System.out.println("Оплата прошла успешно");
            return true;
        } else {
            System.out.println("Недостаточно средств");
            return false;
        }
    }

    // Вывод баланса карты
    public void showBalance(String cardNumber) {
        BankCard card = cards.get(cardNumber);
        if (card != null) {
            System.out.println("Баланс карты " + cardNumber + ": " + card.getBalance() + " рублей");
        } else {
            System.out.println("Карта не найдена");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BankATM atm = new BankATM(10000);

        // Создаем несколько карт
        atm.addCard(new BankCard("1111 2222 3333 4444", "1234", 5000));
        atm.addCard(new BankCard("5555 6666 7777 8888", "5678", 3000));

        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1 - Проверить баланс");
            System.out.println("2 - Перевести деньги");
            System.out.println("3 - Оплатить мобильную связь");
            System.out.println("4 - Выйти");
            int choice = scanner.nextInt();
            scanner.nextLine(); // очистка буфера

            switch (choice) {
                case 1:
                    System.out.print("Введите номер карты: ");
                    String cardNumBalance = scanner.nextLine();
                    atm.showBalance(cardNumBalance);
                    break;
                case 2:
                    System.out.print("Введите номер вашей карты: ");
                    String fromCard = scanner.nextLine();
                    System.out.print("Введите PIN: ");
                    String pinFrom = scanner.nextLine();
                    System.out.print("Введите номер карты получателя: ");
                    String toCard = scanner.nextLine();
                    System.out.print("Введите сумму перевода: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    atm.transfer(fromCard, toCard, pinFrom, amount);
                    break;
                case 3:
                    System.out.print("Введите номер карты: ");
                    String cardNumber = scanner.nextLine();
                    System.out.print("Введите PIN: ");
                    String pin = scanner.nextLine();
                    System.out.print("Введите сумму оплаты: ");
                    double payAmount = scanner.nextDouble();
                    scanner.nextLine();
                    atm.payMobileService(cardNumber, pin, payAmount);
                    break;
                case 4:
                    System.out.println("Выход из программы");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Некорректный выбор");
            }
        }
    }
}
