package search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchCardId implements SearchFunction<String, String, Map<Integer, List<Integer>>> {

    @Override
    public void search(Integer pageId, List<String> data, String key, Map<Integer, List<Integer>> items) {
        List<Integer> result = new ArrayList<>();
        if (data.size() > 0) {
            String[] cardIds = data.get(0).split(",");
            for (int i = 0; i < cardIds.length; i++) {
                if (cardIds[i].equals(key)) {
                    result.add(i);
                }
            }
        }
        items.put(pageId, result);
    }
}
