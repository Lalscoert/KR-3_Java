import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        DeliveryService service = new DeliveryService();

        System.out.println("=== СИСТЕМА ДОСТАВКИ ===");
        System.out.println("Индивидуальный вариант №4");

        while (true) {

            System.out.println("\n=== МЕНЮ ===");
            System.out.println("1. Самовывоз");
            System.out.println("2. Стандартная доставка");
            System.out.println("3. Экспресс-доставка");
            System.out.println("4. Эконом-доставка");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("0")) {
                System.out.println("Программа завершена");
                break;
            }

            DeliveryType type;

            switch (choice) {
                case "1":
                    type = DeliveryType.PICKUP;
                    break;
                case "2":
                    type = DeliveryType.STANDARD;
                    break;
                case "3":
                    type = DeliveryType.EXPRESS;
                    break;
                case "4":
                    type = DeliveryType.ECONOMY;
                    break;
                default:
                    System.out.println("Неизвестная команда");
                    continue;
            }

            try {
                System.out.print("Расстояние (км): ");
                double distance = Double.parseDouble(
                    scanner.nextLine().trim()
                );

                System.out.print("Вес заказа (кг): ");
                double weight = Double.parseDouble(
                    scanner.nextLine().trim()
                );

                DeliveryRequest request = new DeliveryRequest(
                    type, distance, weight
                );

                double cost = service.calculateCost(request);

                System.out.println("\n=== РЕЗУЛЬТАТ ===");
                System.out.println("Способ доставки: " + type);
                System.out.printf(
                    "Стоимость доставки: %.2f руб.%n", cost
                );

            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
