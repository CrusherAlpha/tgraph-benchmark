package common;


// We avoid introduce third party like apache common for just Pair.
public class Pair<K, V> {
    private final K first;
    private final V second;

    Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K first() {
        return first;
    }

    public V second() {
        return second;
    }

    public static <K, V> Pair<K, V> of(K first, V second) {
        return new Pair<>(first, second);
    }

}
