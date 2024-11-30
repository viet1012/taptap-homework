package search;

import importer.MetadataUtil;
import mapping.ColumnMapping;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class SearchUtil {
    private static int MAX_THREAD = 5;
    private static final String STORE_PATH = "data/store/col";

    private static <C, T, R> R search(ColumnMapping column, SearchFunction searchFunction, T key, R lines) throws InterruptedException {
        int currentPage = MetadataUtil.getInstance().getCurrentPage(column);
        int pageId = 0;

        List<Thread> workers = null;
        CountDownLatch countDownLatch = null;
        for (int i = 0; i < currentPage / MAX_THREAD; i++) {
            workers = new ArrayList<>();
            countDownLatch = new CountDownLatch(MAX_THREAD);
            for (int j = 0; j < MAX_THREAD; j++) {
                String fileName = STORE_PATH + column.getPrefix() + "." + pageId + ".txt";
                workers.add(new Thread(new SearchFile(fileName, pageId, lines, countDownLatch, searchFunction, key)));
                pageId++;
            }
            workers.forEach(Thread::start);
            countDownLatch.await();
        }

        int remainPage = currentPage % MAX_THREAD;
        if (remainPage > 0) {
            workers = new ArrayList<>();
            countDownLatch = new CountDownLatch(remainPage);
            for (int j = 0; j < remainPage; j++) {
                String fileName = STORE_PATH + column.getPrefix() + "." + pageId + ".txt";
                workers.add(new Thread(new SearchFile(fileName, pageId, lines, countDownLatch, searchFunction, key)));
                pageId++;
            }
            workers.forEach(Thread::start);
            countDownLatch.await();
        }
        return lines;
    }
    public static List<String> searchDirection(Map<String, List<Integer>> indexes) {
        MAX_THREAD = 1;

        List<String> matchedDirections = Collections.synchronizedList(new ArrayList<>());
        try {
            search(ColumnMapping.DIRECTION, new SearchDirection(), indexes, matchedDirections);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return matchedDirections;
    }

    public static Map<String, List<Integer>> searchCard(String cardId) {
        MAX_THREAD = 5;

        Map<String, List<Integer>> lines = Collections.synchronizedMap(new HashMap<>());
        try {
            search(ColumnMapping.CARD_ID, new SearchCardId(), cardId, lines);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return lines;
    }
    public static List<String> searchTimestamp(Map<String, List<Integer>> indexes) {
        MAX_THREAD = 1;

        List<String> matchedDirections = Collections.synchronizedList(new ArrayList<>());
        try {
            search(ColumnMapping.TIMESTAMP, new SearchTimestamp(), indexes, matchedDirections);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return matchedDirections;
    }

    private static class SearchFile<P, K, R> implements Runnable {
        private String fileName;
        private int pageId;
        private R lines;
        private CountDownLatch countDownLatch;
        private SearchFunction searchFunction;
        private K key;

        public SearchFile(String fileName, int pageId, R lines, CountDownLatch countDownLatch, SearchFunction searchFunction, K key) {
            this.fileName = fileName;
            this.pageId = pageId;
            this.lines = lines;
            this.countDownLatch = countDownLatch;
            this.searchFunction = searchFunction;
            this.key = key;
        }

        @Override
        public void run() {
            try {
                List<String> data = FileUtils.readLines(new File(fileName));
                searchFunction.search(pageId, data, key, lines);
                countDownLatch.countDown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

