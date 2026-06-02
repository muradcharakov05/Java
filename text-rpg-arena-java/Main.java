import java.time.ZonedDateTime;
import java.util.Random;
import java.lang.Thread;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
     War legioner = new War();
     War mushketer = new War();
    Choice war1 = new Choice(legioner,mushketer);
    war1.run();


 }
}


class War{
    int hp;
    int energy;
    int magicEnergy;
    int power;
    int levelDefence;
    Random random = new Random();

    War(){
        hp=100;
        energy=100;
        magicEnergy=100;
        power=25;
        levelDefence=1;
    }

    void Attack(War m2){
    System.out.println("Атака");
    m2.hp-=power;
    energy-=30;
        System.out.println("Нанесен урон на "+power);
    System.out.println("Энергия уменьшилась на 30");
    }
    void Defence(War m2){
        System.out.println("Защита");
        hp+= (levelDefence*10);
        energy-=levelDefence*20;
        System.out.println("Появился щит на 10hp");
        System.out.println("Энергия уменьшилась на "+ (levelDefence*20));
    }


    void Rest(){
        System.out.println("Отдых");
        for (int i=0;i<6;i++) {
        energy += random.nextInt(13);
        hp += random.nextInt(5);
        if(hp>=100 & energy>=100) {
            hp = 100;
            energy = 100;
            break;
        }
        else if (hp>100) {
            hp=100;}
        else if (energy>100) {
            energy=100;}
            System.out.println("Здоровье " + hp +"\nЭнергия "+energy );
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    }


    void Meditation(){
        System.out.println("Медитация");
        for (int i=0;i<8;i++) {
            magicEnergy += random.nextInt(9);
            if(magicEnergy>=100){
                magicEnergy=100;
                break;
            }
            System.out.println("Магическая энергия "+magicEnergy);
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    void spellHealing(){
        System.out.println("Заклинание лечения");
        for (int i=0;i<5;i++) {
            int diff = random.nextInt(15);
            hp+=diff;
            magicEnergy-=diff;
            energy-=diff/3;
            if(hp>=100){
                hp=100;
                break;
            }
            else if (magicEnergy<=0){
                magicEnergy=0;
                break;
            }
            else if (energy<=0){
                energy=0;
                break;
            }
            System.out.println("Здоровье " + hp);
            System.out.println("Магическая энергия "+ magicEnergy + "\nЭнергия " + energy);
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void Stats(){
        System.out.println("Уровень жизни: " + hp +"\nУровень энергии: " +energy + "\nУровень магической энергии: " + magicEnergy);
        System.out.println("Уровень силы: " +power + "\nУровень защиты: " + levelDefence );
    }
}


class Choice {
    Random random;
    Scanner scanner;
    War zd;
    War pr;
    War leviy;
    int count;
    String[] protocol;
    String zdname, prname, levname;

    public Choice(War object, War object2) {
         random = new Random();
        scanner = new Scanner(System.in);
        object = new War();
        zd=object;
        object2=new War();
        pr=object2;
        count = 0;
        protocol = new String[100];
        zdname="Легионер";
        prname="Мушкетер";
    }

    public void run() {
        int num = 0;


        while (true) {
            boolean flag=true;
            if (count % 2 == 0) {
                try {
                    System.out.println("Введите команду (1-Атака 2-Защита 3-Отдых 4-Медитация 5-Заклинание исцеления  6, -1 для выхода):");
                    num = scanner.nextInt();
                    scanner.nextLine(); // Consume newline left-over


                } catch (java.util.InputMismatchException e) {
                    System.out.println("Некорректный ввод. Пожалуйста, введите целое число.");
                    scanner.next(); // discard the invalid input
                    continue; //restart the loop
                }
            } else if (count % 2 != 0) {
                System.out.println("Компьютер выбирает команду (1-6, -1 для выхода):");
                num = random.nextInt(4) + 1;
                System.out.println(num);
                leviy = zd;
                zd = pr;
                pr = leviy;

                levname = zdname;
                zdname = prname;
                prname = levname;

            }

            if (num == -1) {
                System.out.println("Выход из программы.");
                break;
            }

            switch (num) {
                case 1: {
                    if (zd.energy>30) {
                        zd.Attack(pr);
                        protocol[count] = zdname + " атакует " + prname; // Запись в протокол
                        count++;

                    }
                    else {
                        System.out.println("Не хватает энергии. Выберите другой ход.");
                        flag=false;
                    }
                    break;
                }
                case 2: {
                    if(zd.energy>zd.levelDefence*20) {
                        zd.Defence(pr);
                        protocol[count] = zdname + " защищается от " + prname;
                        count++;

                    }
                    else {
                        System.out.println("Не хватает энергии. Выберите другой ход.");
                        flag=false;
                    }
                    break;
                }
                case 3: {
                    zd.Rest();
                    protocol[count] = zdname + " отдыхает";
                    count++;
                    break;
                }
                case 4: {
                    zd.Meditation();
                    protocol[count] = zdname + " медитирует";
                    count++;
                    break;
                }
                case 5: {
                    if (zd.magicEnergy>0 & zd.energy>0){
                    zd.spellHealing();
                    protocol[count] = zdname + " использует лечение";
                    count++;}
                    else {
                        System.out.println("Не хватает энергии или магической энергии. Выберите другой ход.");
                        flag=false;
                    }
                    break;
                }
                case 6: {
                    System.out.println("Статы:");
                    System.out.println(zdname+ "---------------");
                    zd.Stats();
                    System.out.println(prname+"-----------------");
                    pr.Stats();
                    flag=false;
                    break;

                }
                default: {
                    System.out.println("Некорректная команда. Пожалуйста, введите число от 0 до 6.");
                    flag=false;
                    break;
                }
            }

            if (zd.hp<=0) {
                System.out.println("Легионер выиграл. Конец");
                break;
            }
            if (pr.hp<=0) {
                System.out.println("\n\nМушкетер(компьютер) выиграл. Конец\n\n");
                break;
            }

            if (count % 2 == 0 & flag) {
                leviy = zd;
                zd = pr;
                pr = leviy;

                levname = zdname;
                zdname = prname;
                prname = levname;
            }
        }
        System.out.println("Протокол:\n");
        for (int i = 0; i < count; i++) {
            System.out.println(protocol[i]);
        }

    }
}
