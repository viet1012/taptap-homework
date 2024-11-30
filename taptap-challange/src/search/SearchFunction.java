package search;

import java.util.List;

@FunctionalInterface
public interface SearchFunction<I, K, R> {
    void search(Integer pageId, List<I> data, K key, R result);
}