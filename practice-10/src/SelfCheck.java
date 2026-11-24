public class SelfCheck {

    private static int passed = 0;
    private static int failed = 0;

    private static final DeliveryService service =
        new DeliveryService();

    private static final DeliveryPolicyFactory factory =
        new DeliveryPolicyFactory();

    public static void main(String[] args) {

        // самовывоз

        checkCost("Самовывоз бесплатный",
            DeliveryType.PICKUP, 10, 2, 0);

        checkCost("Самовывоз: граница 50 кг",
            DeliveryType.PICKUP, 0, 50, 0);

        check("Самовывоз: 51 кг недоступен",
            !service.canDeliver(
                request(DeliveryType.PICKUP, 0, 51)
            ));

        expectError("Самовывоз: превышение веса",
            () -> service.calculateCost(
                request(DeliveryType.PICKUP, 0, 51)
            ));

        // стандартная доставка

        checkCost("Стандартная доставка: расчёт",
            DeliveryType.STANDARD, 10, 2, 260);

        checkCost("Стандартная: верхние границы",
            DeliveryType.STANDARD, 100, 30, 1750);

        expectError("Стандартная: расстояние 101 км",
            () -> service.calculateCost(
                request(DeliveryType.STANDARD, 101, 2)
            ));

        expectError("Стандартная: вес 31 кг",
            () -> service.calculateCost(
                request(DeliveryType.STANDARD, 10, 31)
            ));

        // экспресс доставка

        checkCost("Экспресс: расчёт",
            DeliveryType.EXPRESS, 10, 2, 570);

        checkCost("Экспресс: верхние границы",
            DeliveryType.EXPRESS, 30, 10, 1250);

        expectError("Экспресс: расстояние 31 км",
            () -> service.calculateCost(
                request(DeliveryType.EXPRESS, 31, 2)
            ));

        expectError("Экспресс: вес 11 кг",
            () -> service.calculateCost(
                request(DeliveryType.EXPRESS, 10, 11)
            ));

        // эконом доставка

        checkCost("Эконом: стоимость 306 рублей",
            DeliveryType.ECONOMY, 10, 2, 306);

        checkCost("Эконом: верхние границы",
            DeliveryType.ECONOMY, 50, 20, 1360);

        expectError("Эконом: расстояние 51 км",
            () -> service.calculateCost(
                request(DeliveryType.ECONOMY, 51, 2)
            ));

        expectError("Эконом: вес 21 кг",
            () -> service.calculateCost(
                request(DeliveryType.ECONOMY, 10, 21)
            ));

        // неправильные входные данные

        expectError("Отрицательное расстояние",
            () -> request(DeliveryType.STANDARD, -1, 2));

        expectError("Нулевой вес",
            () -> request(DeliveryType.STANDARD, 10, 0));

        expectError("Отсутствует способ доставки",
            () -> request(null, 10, 2));

        expectError("Некорректное расстояние NaN",
            () -> request(
                DeliveryType.STANDARD, Double.NaN, 2
            ));

        expectError("Бесконечный вес",
            () -> request(
                DeliveryType.STANDARD, 10,
                Double.POSITIVE_INFINITY
            ));

        expectError("Фабрика: null",
            () -> factory.create(null));

        expectError("Сервис: null",
            () -> service.calculateCost(null));

        expectError("Проверка доступности: null",
            () -> service.canDeliver(null));

        System.out.println("\n========================");
        System.out.println("Успешных проверок: " + passed);
        System.out.println("Ошибок: " + failed);

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static DeliveryRequest request(
            DeliveryType type,
            double distance,
            double weight) {

        return new DeliveryRequest(type, distance, weight);
    }

    private static void checkCost(
            String name,
            DeliveryType type,
            double distance,
            double weight,
            double expected) {

        double actual = service.calculateCost(
            request(type, distance, weight)
        );

        check(name, Math.abs(actual - expected) < 0.000001);
    }

    private static void expectError(
            String name,
            Runnable action) {

        try {
            action.run();
            check(name, false);
        } catch (IllegalArgumentException e) {
            check(name, true);
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
}
