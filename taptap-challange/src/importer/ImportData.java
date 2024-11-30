package importer;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ImportData implements Function<String, String> {
    private static Map<Integer, List<String>> columnData;
    private static int fileCutoff = 200000;
//    private static int fileCutoff = 2;

    @Override
    public String apply(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        try {
            //Read import file to list
            List<String> lines = FileUtils.readLines(file, Charset.defaultCharset());
            columnData = new HashMap<>();

            //Convert list column oriented file
            String[] fields;
            for (String line : lines) {
                fields = line.split(",");
                convertToColumnData(fields);
                fields = null;
            }

            //Save column oriented data to disk, if row size > fileCutoff=200000 then split to many files
            saveToDataCenter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void convertToColumnData(String[] fields) {
        for (int i = 0; i < fields.length; i++) {
            if (columnData.containsKey(i)) {
                columnData.get(i).add(fields[i]);
            } else {
                List<String> values = new ArrayList<>();
                values.add(fields[i]);
                columnData.put(i, values);
            }
        }
    }

    private void saveToDataCenter() {
        for (Integer index : columnData.keySet()) {
            try {
                writeFile(index, columnData.get(index), 0, columnData.get(index).size());

                //save db metadata (column id with latest pageId)
                MetadataUtil.getInstance().writeMetaData();

                //save page metadata (column.page id with beginning row index)
                MetadataUtil.getInstance().writeFileInfo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    private void writeFile(int colIndex, List<String> data, int lowerIndex, int upperIndex) throws IOException {
//        int range = upperIndex - lowerIndex;
//        if (range <= fileCutoff) {
//            int pageId = 0;
//            MetadataUtil.getInstance().checkPage(colIndex, pageId);
//            String fileName = String.format("data/store/col%d.%d.txt", colIndex, pageId++);
//            FileUtils.writeByteArrayToFile(new File(fileName), String.join(",", data.subList(lowerIndex, upperIndex)).getBytes(Charset.defaultCharset()));
//            MetadataUtil.getInstance().addPage(colIndex, pageId);
//            MetadataUtil.getInstance().updateFileInfo(colIndex, pageId,lowerIndex);
//
//        } else {
//            int middleIndex = lowerIndex + range / 2;
//            writeFile(colIndex, data, lowerIndex, middleIndex);
//            writeFile(colIndex, data, middleIndex + 1, upperIndex);
//        }
//    }
private void writeFile(int colIndex, List<String> data, int lowerIndex, int upperIndex) throws IOException {
    int range = upperIndex - lowerIndex;
    System.out.println("Range: " + range + ", lowerIndex: " + lowerIndex + ", upperIndex: " + upperIndex);
    if (range <= fileCutoff) {
        int pageId = MetadataUtil.getInstance().getCurrentPage(colIndex);
        System.out.println("Page ID: " + pageId);

        String fileName = String.format("data/store/col%d.%d.txt", colIndex, pageId);
        FileUtils.writeByteArrayToFile(new File(fileName), String.join(",", data.subList(lowerIndex, upperIndex)).getBytes(Charset.defaultCharset()));

        // Cập nhật thông tin pageId
        MetadataUtil.getInstance().addPage(colIndex, pageId + 1);
        MetadataUtil.getInstance().updateFileInfo(colIndex, pageId, lowerIndex);
    } else {
        int middleIndex = lowerIndex + range / 2;
        writeFile(colIndex, data, lowerIndex, middleIndex);
        writeFile(colIndex, data, middleIndex + 1, upperIndex);
    }
}



}
