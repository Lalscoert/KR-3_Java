import java.util.List;
import java.util.Map;
import java.util.Set;

public class SelfCheck {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        Warehouse warehouse = new Warehouse();

        check("Пустой склад",
            warehouse.getAllItems().isEmpty());

        warehouse.addItem(
            new StockItem("PC01", "Laptop", Category.COMPUTER, 30)
        );

        check("Добавление оборудования",
            warehouse.findByCode("PC01").getQuantity() == 30);

        check("Начальный приход в истории",
            warehouse.getMovements().size() == 1
            && warehouse.getMovements().get(0).getType()
                == MovementType.RECEIVE);

        expectError("Дубликат инвентарного кода",
            () -> warehouse.addItem(
                new StockItem("PC01", "Other", Category.OTHER, 10)
            ));

        warehouse.receive("PC01", 10);

        check("Приход 10 единиц",
            warehouse.findByCode("PC01").getQuantity() == 40);

        check("История после прихода",
            warehouse.getMovements().size() == 2);

        warehouse.issue("PC01", 15);

        check("Выдача 15 единиц",
            warehouse.findByCode("PC01").getQuantity() == 25);

        check("Запись выдачи в истории",
            warehouse.getMovements().size() == 3
            && warehouse.getMovements().get(2).getType()
                == MovementType.ISSUE);

        // Индивидуальный вариант №4
        expectError("Нельзя нарушать резерв 25",
            () -> warehouse.issue("PC01", 1));

        check("Остаток после ошибки не изменился",
            warehouse.findByCode("PC01").getQuantity() == 25);

        check("История после ошибки не изменилась",
            warehouse.getMovements().size() == 3);

        expectError("Выдача больше остатка",
            () -> warehouse.issue("PC01", 26));

        expectError("Выдача нуля",
            () -> warehouse.issue("PC01", 0));

        expectError("Приход нуля",
            () -> warehouse.receive("PC01", 0));

        expectError("Несуществующий код",
            () -> warehouse.receive("UNKNOWN", 5));

        expectError("Пустой инвентарный код",
            () -> new StockItem(
                "", "Mouse", Category.OTHER, 10
            ));

        StockItem copy = warehouse.findByCode("PC01");
        copy.increase(50);

        check("Защита внутреннего объекта",
            warehouse.findByCode("PC01").getQuantity() == 25);

        List<StockItem> found =
            warehouse.findByCategory(Category.COMPUTER);

        found.get(0).increase(10);
        found.clear();

        check("Защита списка поиска",
            warehouse.findByCode("PC01").getQuantity() == 25);

        Map<Category, Integer> totals =
            warehouse.totalsByCategory();

        check("Подсчёт по категориям",
            totals.get(Category.COMPUTER) == 25);

        Set<Category> categories =
            warehouse.getUniqueCategories();

        check("Уникальные категории",
            categories.size() == 1
            && categories.contains(Category.COMPUTER));

        check("Поиск отсутствующей категории",
            warehouse.findByCategory(Category.PRINTER).isEmpty());

        expectError("Добавление null",
            () -> warehouse.addItem(null));

        expectError("Отрицательный начальный остаток",
            () -> new StockItem(
                "PC02", "Keyboard", Category.COMPUTER, -1
            ));

        System.out.println("\n========================");
        System.out.println("Успешных проверок: " + passed);
        System.out.println("Ошибок: " + failed);

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void check(String name, boolean success) {
        if (success) {
            passed++;
            System.out.println("[PASS] " + name);
        } else {
            failed++;
            System.out.println("[FAIL] " + name);
        }
    }

    private static void expectError(String name, Runnable action) {
        try {
            action.run();
            check(name, false);
        } catch (IllegalArgumentException e) {
            check(name, true);
        }
    }
}
