import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

public final class SalesAnalyticsService {

    // вариант №4: порог крупной сделки
    private static final BigDecimal LARGE_SALE_THRESHOLD =
        new BigDecimal("25");

    // сортировка по сумме и затем по id
    private static final Comparator<Sale> SALE_COMPARATOR =
        Comparator.comparing(Sale::getAmount)
            .reversed()
            .thenComparing(Sale::getId);

    // получение самых крупных продаж
    public List<Sale> topSales(
            List<Sale> sales,
            BigDecimal minimumAmount,
            int limit) {

        validateSales(sales);

        if (minimumAmount == null
                || minimumAmount.signum() < 0) {
            throw new IllegalArgumentException(
                "Некорректная минимальная сумма"
            );
        }

        if (limit < 0) {
            throw new IllegalArgumentException(
                "Лимит не может быть отрицательным"
            );
        }

        return sales.stream()
            .filter(s -> s.getAmount()
                .compareTo(minimumAmount) >= 0)
            .sorted(SALE_COMPARATOR)
            .limit(limit)
            .toList();
    }

    // поиск максимальной продажи
    public Optional<Sale> maxSale(List<Sale> sales) {

        validateSales(sales);

        return sales.stream()
            .max(Comparator.comparing(Sale::getAmount));
    }

    // подсчёт выручки каждого менеджера
    public Map<String, BigDecimal> totalsByManager(
            List<Sale> sales) {

        validateSales(sales);

        return sales.stream()
            .collect(Collectors.groupingBy(
                Sale::getManager,
                TreeMap::new,
                Collectors.reducing(
                    BigDecimal.ZERO,
                    Sale::getAmount,
                    BigDecimal::add
                )
            ));
    }

    // общая сумма всех продаж
    public BigDecimal totalAmount(List<Sale> sales) {

        validateSales(sales);

        return sales.stream()
            .map(Sale::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // количество крупных сделок
    public long countLargeSales(List<Sale> sales) {

        validateSales(sales);

        return sales.stream()
            .filter(s -> s.getAmount()
                .compareTo(LARGE_SALE_THRESHOLD) >= 0)
            .count();
    }

    // проверка списка продаж
    private void validateSales(List<Sale> sales) {

        if (sales == null) {
            throw new IllegalArgumentException(
                "Список продаж отсутствует"
            );
        }

        if (sales.stream().anyMatch(s -> s == null)) {
            throw new IllegalArgumentException(
                "Список содержит пустую продажу"
            );
        }
    }
}
