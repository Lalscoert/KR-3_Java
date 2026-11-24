import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Warehouse warehouse = new Warehouse();

        System.out.println("=== СИСТЕМА УЧЁТА ОБОРУДОВАНИЯ ===");
        System.out.println("Индивидуальный вариант №4");

        while (true) {

            System.out.println("\n=== МЕНЮ ===");
            System.out.println("1. Добавить оборудование");
            System.out.println("2. Приход оборудования");
            System.out.println("3. Выдача оборудования");
            System.out.println("4. Показать весь склад");
            System.out.println("5. Поиск по категории");
            System.out.println("6. Остатки по категориям");
            System.out.println("7. История операций");
            System.out.println("8. Уникальные категории");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("0")) {
                System.out.println("Программа завершена");
                break;
            }

            try {
                switch (choice) {

                    case "1":
                        System.out.print("Инвентарный код: ");
                        String code = scanner.nextLine();

                        System.out.print("Название: ");
                        String name = scanner.nextLine();

                        System.out.print(
                            "Категория (COMPUTER/MONITOR/PRINTER/OTHER): "
                        );

                        Category category = Category.valueOf(
                            scanner.nextLine().trim().toUpperCase()
                        );

                        System.out.print("Количество: ");
                        int quantity = Integer.parseInt(
                            scanner.nextLine().trim()
                        );

                        warehouse.addItem(
                            new StockItem(code, name, category, quantity)
                        );

                        System.out.println("Оборудование добавлено!");
                        break;

                    case "2":
                        System.out.print("Код оборудования: ");
                        String receiveCode = scanner.nextLine();

                        System.out.print("Количество прихода: ");
                        int receiveQuantity = Integer.parseInt(
                            scanner.nextLine().trim()
                        );

                        warehouse.receive(receiveCode, receiveQuantity);

                        System.out.println("Приход выполнен!");
                        break;

                    case "3":
                        System.out.print("Код оборудования: ");
                        String issueCode = scanner.nextLine();

                        System.out.print("Количество выдачи: ");
                        int issueQuantity = Integer.parseInt(
                            scanner.nextLine().trim()
                        );

                        warehouse.issue(issueCode, issueQuantity);

                        System.out.println("Выдача выполнена!");
                        break;

                    case "4":
                        System.out.println("\n=== СКЛАД ===");

                        for (StockItem item : warehouse.getAllItems()) {
                            System.out.println(item);
                        }
                        break;

                    case "5":
                        System.out.print("Введите категорию: ");

                        Category searchCategory = Category.valueOf(
                            scanner.nextLine().trim().toUpperCase()
                        );

                        for (StockItem item :
                                warehouse.findByCategory(searchCategory)) {
                            System.out.println(item);
                        }
                        break;

                    case "6":
                        System.out.println("=== ОСТАТКИ ===");

                        warehouse.totalsByCategory().forEach(
                            (cat, total) ->
                                System.out.println(cat + ": " + total)
                        );
                        break;

                    case "7":
                        System.out.println("=== ИСТОРИЯ ===");

                        for (StockMovement movement :
                                warehouse.getMovements()) {
                            System.out.println(movement);
                        }
                        break;

                    case "8":
                        System.out.println("Уникальные категории:");

                        for (Category cat :
                                warehouse.getUniqueCategories()) {
                            System.out.println(cat);
                        }
                        break;

                    default:
                        System.out.println("Неизвестная команда");
                }

            } catch (IllegalArgumentException | ArithmeticException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
