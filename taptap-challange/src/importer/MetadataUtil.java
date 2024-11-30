package importer;

import mapping.ColumnMapping;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetadataUtil {
    private static Map<Integer, Integer> columnPage;
    private static Map<String, Integer> fileInfo;

    private MetadataUtil() {
        if (null == columnPage) {
            loadMetaData();
            loadFileInfo();
        }
    }

    public static MetadataUtil getInstance() {
        return MetadataUtilHelper.INSTANCE;
    }

    private static class MetadataUtilHelper {
        private static final MetadataUtil INSTANCE = new MetadataUtil();
    }

    public void writeMetaData() {
        List<String> data = new ArrayList<>();
        for (Integer key : columnPage.keySet()) {
            data.add(key + "," + columnPage.get(key));
        }
        String fileName = "data/store/metadata.txt";
        try {
            FileUtils.writeLines(new File(fileName), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadMetaData() {
        try {
            columnPage = new HashMap<>();
            fileInfo = new HashMap<>();

            String fileName = "data/store/metadata.txt";
            File file = new File(fileName);
            if (!file.exists()) {
                return;
            }
            List<String> lines = FileUtils.readLines(new File(fileName), Charset.defaultCharset());
            for (String line : lines) {
                String[] arr = line.split(",");
                columnPage.put(Integer.valueOf(arr[0]), Integer.valueOf(arr[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFileInfo() {
        List<String> data = new ArrayList<>();
        for (String key : fileInfo.keySet()) {
            data.add(key + "," + fileInfo.get(key));
        }
        String fileName = "data/store/fileInfo.txt";
        try {
            FileUtils.writeLines(new File(fileName), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFileInfo() {
        try {
            fileInfo = new HashMap<>();

            String fileName = "data/store/fileInfo.txt";
            File file = new File(fileName);
            if (!file.exists()) {
                return;
            }
            List<String> lines = FileUtils.readLines(new File(fileName), Charset.defaultCharset());
            for (String line : lines) {
                String[] arr = line.split(",");
                fileInfo.put(arr[0], Integer.valueOf(arr[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkPage(int colId, int pageId) {
        int rowIndex = 0;
        if (!columnPage.containsKey(colId)) {
            columnPage.put(colId, pageId);
            fileInfo.put(colId + "." + pageId, rowIndex);
        } else {
            pageId = columnPage.get(colId);
            rowIndex = fileInfo.get(colId + "." + (pageId - 1)) + rowIndex;
        }
        fileInfo.put(colId + "." + pageId, rowIndex);
    }

    public void addPage(int colId, int pageId) {
        columnPage.put(colId, pageId);
    }

    public int getCurrentPage(ColumnMapping column) {
        return columnPage.get(column.getPrefix());
    }
    public int getCurrentPage(int colId) {
        return columnPage.getOrDefault(colId, 0); // Nếu không có pageId cho colId thì trả về 0
    }

    public void updateFileInfo(int colId, int pageId, int rowIndex) {
        // Thêm hàm updateFileInfo để cập nhật thông tin vị trí dòng trong file
        String key = colId + "." + pageId;
        System.out.println("key " + key);
        fileInfo.put(key, rowIndex);
    }
}
