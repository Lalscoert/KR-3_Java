public final class DeliveryService {

    private final DeliveryPolicyFactory factory =
        new DeliveryPolicyFactory();

    public double calculateCost(DeliveryRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                "Заявка на доставку обязательна"
            );
        }

        DeliveryPolicy policy = factory.create(
            request.getType()
        );

        if (!policy.supports(request)) {
            throw new IllegalArgumentException(
                "Доставка недоступна для указанных параметров"
            );
        }

        return policy.calculateCost(request);
    }

    public boolean canDeliver(DeliveryRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                "Заявка на доставку обязательна"
            );
        }

        DeliveryPolicy policy = factory.create(
            request.getType()
        );

        return policy.supports(request);
    }
}
