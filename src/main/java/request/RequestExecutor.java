package request;

public interface RequestExecutor {
    // We do not need response, we just return the Metrics.
    // We write the result into the specified files and
    // rely on script like python to check the correctness.
    // Also, we calculate the cost
    Metrics execute(AbstractRequest request);
}
