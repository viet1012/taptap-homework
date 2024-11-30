package search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchDirection implements SearchFunction<String, Map<Integer, List<Integer>>, List<String>> {

    @Override
    public void search(Integer pageId, List<String> data, Map<Integer, List<Integer>> key, List<String> items) {
        List<String> result = new ArrayList<>();
        if (data.size() > 0) {
            String[] directions = data.get(0).split(",");
            for (Integer index : key.get(pageId)) {
                result.add(directions[index]);
            }
        }
        items.addAll(result);
        result.clear();
    }
}
