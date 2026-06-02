//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;

public class textRpgArena {
    public textRpgArena() {
    }

    public static void main(String[] args) {
        OrderService service = new OrderService();
        service.addOrder(new Order(1, "Alex", (double)1200.0F));
        service.addOrder(new Order(2, "Bob", (double)300.0F));
        service.addOrder(new Order(3, "Alex", (double)900.0F));
        service.addOrder(new Order(4, "Dima", (double)1500.0F));
        System.out.println("\nФильтр (сумма > 800):");
        List var10000 = service.filterOrders((amount) -> amount > (double)800.0F);
        PrintStream var10001 = System.out;
        Objects.requireNonNull(var10001);
        var10000.forEach(var10001::println);
        System.out.println("\nСортировка по сумме:");
        var10000 = service.sortByAmount();
        var10001 = System.out;
        Objects.requireNonNull(var10001);
        var10000.forEach(var10001::println);
        System.out.println("\nГруппировка по клиентам:");
        service.groupByCustomer().forEach((customer, orders) -> System.out.println(customer + " -> " + String.valueOf(orders)));
        System.out.println("\nОбработка заказов в потоках:");

        for(Order order : service.getOrders()) {
            Thread thread = new Thread(new OrderProcessor(order));
            thread.start();
        }

    }
}
