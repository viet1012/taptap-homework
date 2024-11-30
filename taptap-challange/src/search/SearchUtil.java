package search;

import importer.MetadataUtil;
import mapping.ColumnMapping;

import java.util.ArrayList;
import java.util.List;
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
}
