package search;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SearchFile<P, K, R> implements Runnable {
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
