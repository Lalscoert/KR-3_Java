public final class PickupPolicy implements DeliveryPolicy {

    private static final double MAX_WEIGHT = 50.0;

    @Override
    public boolean supports(DeliveryRequest request) {
        return request != null
            && request.getWeightKg() <= MAX_WEIGHT;
    }

    @Override
    public double calculateCost(DeliveryRequest request) {

        if (!supports(request)) {
            throw new IllegalArgumentException(
                "Самовывоз недоступен для этого заказа"
            );
        }

        return 0.0;
    }
}
