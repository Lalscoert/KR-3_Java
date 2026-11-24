public interface DeliveryPolicy {

    // проверка возможности доставки
    boolean supports(DeliveryRequest request);

    // расчёт стоимости доставки
    double calculateCost(DeliveryRequest request);
}
