import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {

        SalesAnalyticsService service =
            new SalesAnalyticsService();

        // создаём список продаж
        List<Sale> sales = List.of(
            new Sale("S001", "Anna", new BigDecimal("100")),
            new Sale("S002", "Boris", new BigDecimal("20")),
            new Sale("S003", "Boris", new BigDecimal("200")),
            new Sale("S004", "Anna", new BigDecimal("75")),
            new Sale("S005", "Olga", new BigDecimal("25")),
            new Sale("S006", "Olga", new BigDecimal("10"))
        );

        System.out.println("=== SALES ANALYTICS ===");

        // выводим все продажи
        System.out.println("\n1. All sales:");

        for (Sale sale : sales) {
            System.out.println(sale);
        }

        // получаем три крупнейшие продажи от 25
        System.out.println("\n2. Top 3 sales:");

        List<Sale> topSales = service.topSales(
            sales,
            new BigDecimal("25"),
            3
        );

        for (Sale sale : topSales) {
            System.out.println(sale);
        }

        // находим максимальную продажу
        System.out.println("\n3. Maximum sale:");

        Optional<Sale> maximum = service.maxSale(sales);

        if (maximum.isPresent()) {
            System.out.println(maximum.get());
        } else {
            System.out.println("No sales found");
        }

        // считаем выручку каждого менеджера
        System.out.println("\n4. Totals by manager:");

        Map<String, BigDecimal> totals =
            service.totalsByManager(sales);

        totals.forEach((manager, total) ->
            System.out.println(manager + ": " + total)
        );

        // рассчитываем общую выручку
        System.out.println("\n5. Total revenue:");

        BigDecimal total = service.totalAmount(sales);

        System.out.println(total);

        // подсчитываем крупные сделки
        System.out.println("\n6. Large sales count:");

        long largeSales = service.countLargeSales(sales);

        System.out.println(largeSales);

        System.out.println("\nAnalytics completed!");
    }
}
