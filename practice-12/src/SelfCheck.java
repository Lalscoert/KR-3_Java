import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelfCheck {

    private static int passed = 0;
    private static int failed = 0;

    private static final SalesAnalyticsService service =
        new SalesAnalyticsService();

    public static void main(String[] args) {

        // создаём тестовые продажи
        List<Sale> sales = new ArrayList<>(List.of(
            sale("S001", "Anna", "100"),
            sale("S002", "Boris", "20"),
            sale("S003", "Boris", "200"),
            sale("S004", "Anna", "75"),
            sale("S005", "Olga", "25"),
            sale("S006", "Olga", "10")
        ));

        List<Sale> original = List.copyOf(sales);

        // проверяем общую выручку
        check("Общая выручка 430",
            equal(service.totalAmount(sales), "430"));

        // проверяем крупные сделки
        check("Четыре крупные сделки",
            service.countLargeSales(sales) == 4);

        // проверяем максимальную продажу
        check("Максимальная продажа S003",
            service.maxSale(sales).orElseThrow()
                .getId().equals("S003"));

        // проверяем фильтрацию и сортировку
        List<Sale> top = service.topSales(
            sales, new BigDecimal("25"), 3
        );

        check("Три крупнейшие продажи",
            top.size() == 3);

        check("Правильная сортировка",
            top.get(0).getId().equals("S003")
            && top.get(1).getId().equals("S001")
            && top.get(2).getId().equals("S004"));

        List<Sale> filtered = service.topSales(
            sales, new BigDecimal("25"), 10
        );

        check("Нижняя граница включена",
            filtered.stream().anyMatch(
                s -> s.getId().equals("S005")));

        check("Продажи ниже порога исключены",
            filtered.stream().noneMatch(
                s -> s.getId().equals("S002")));

        check("Лимит 0",
            service.topSales(
                sales, BigDecimal.ZERO, 0).isEmpty());

        check("Лимит 1",
            service.topSales(
                sales, BigDecimal.ZERO, 1).size() == 1);

        check("Слишком высокий порог",
            service.topSales(
                sales, new BigDecimal("1000"), 10).isEmpty());

        // проверяем вторичную сортировку по id
        List<Sale> equalAmounts = List.of(
            sale("S002", "Anna", "50"),
            sale("S001", "Boris", "50")
        );

        check("Сортировка одинаковых сумм по ID",
            service.topSales(
                equalAmounts, BigDecimal.ZERO, 2)
                .get(0).getId().equals("S001"));

        // проверяем агрегаты менеджеров
        Map<String, BigDecimal> totals =
            service.totalsByManager(sales);

        check("Выручка Anna",
            equal(totals.get("Anna"), "175"));

        check("Выручка Boris",
            equal(totals.get("Boris"), "220"));

        check("Выручка Olga",
            equal(totals.get("Olga"), "35"));

        check("Количество менеджеров",
            totals.size() == 3);

        // проверяем пустые коллекции
        check("Максимум пустого списка",
            service.maxSale(List.of()).isEmpty());

        check("Сумма пустого списка",
            equal(service.totalAmount(List.of()), "0"));

        check("Группировка пустого списка",
            service.totalsByManager(List.of()).isEmpty());

        check("Топ пустого списка",
            service.topSales(
                List.of(), BigDecimal.ZERO, 3).isEmpty());

        // проверяем отсутствие побочных эффектов
        check("Исходный список не изменился",
            sales.equals(original));

        totals.put("Anna", BigDecimal.ZERO);

        check("Агрегаты не изменяют исходные данные",
            equal(service.totalsByManager(sales)
                .get("Anna"), "175"));

        // проверяем некорректные параметры
        expectError("Отсутствует список",
            () -> service.maxSale(null));

        List<Sale> withNull = new ArrayList<>(sales);
        withNull.add(null);

        expectError("Пустой элемент в списке",
            () -> service.totalAmount(withNull));

        expectError("Отсутствует минимальная сумма",
            () -> service.topSales(sales, null, 3));

        expectError("Отрицательный порог",
            () -> service.topSales(
                sales, new BigDecimal("-1"), 3));

        expectError("Отрицательный лимит",
            () -> service.topSales(
                sales, BigDecimal.ZERO, -1));

        // проверяем конструктор продажи
        expectError("Нулевая сумма",
            () -> sale("S007", "Anna", "0"));

        expectError("Пустое имя менеджера",
            () -> sale("S007", "", "100"));

        expectError("Пустой ID",
            () -> sale("", "Anna", "100"));

        expectError("Отсутствует сумма",
            () -> new Sale("S007", "Anna", null));

        System.out.println("\n========================");
        System.out.println("Успешных проверок: " + passed);
        System.out.println("Ошибок: " + failed);

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static Sale sale(
            String id, String manager, String amount) {
        return new Sale(
            id, manager, new BigDecimal(amount));
    }

    private static boolean equal(
            BigDecimal actual, String expected) {
        return actual != null
            && actual.compareTo(
                new BigDecimal(expected)) == 0;
    }

    private static void expectError(
            String name, Runnable action) {
        try {
            action.run();
            check(name, false);
        } catch (IllegalArgumentException e) {
            check(name, true);
        }
    }

    private static void check(
            String name, boolean success) {
        if (success) {
            passed++;
            System.out.println("[PASS] " + name);
        } else {
            failed++;
            System.out.println("[FAIL] " + name);
        }
    }
}
