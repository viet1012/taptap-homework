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

// Class ImportData implement interface Function<String, String>
public class ImportData implements Function<String, String> {
    private static Map<Integer, List<String>> columnData; // Lưu dữ liệu được xử lý theo cột
//    private static int fileCutoff = 200000;
    private static int fileCutoff = 2;

    // Phương thức chính xử lý logic khi gọi Function<String, String>
    @Override
    public String apply(String path) {
        File file = new File(path); // Tạo đối tượng File từ đường dẫn được truyền vào
        if (!file.exists()) {
            return null;
        }
        try {
            // Đọc toàn bộ nội dung file thành danh sách các dòng
            List<String> lines = FileUtils.readLines(file, Charset.defaultCharset());
            columnData = new HashMap<>(); // Khởi tạo lại dữ liệu lưu theo cột

            // Duyệt qua từng dòng và chuyển đổi dữ liệu theo dạng cột
            String[] fields;
            for (String line : lines) {
                fields = line.split(","); // Tách dòng thành các trường dựa trên dấu phẩy
                convertToColumnData(fields); // Gọi hàm xử lý chuyển đổi dữ liệu
                fields = null; // Xóa tham chiếu để tối ưu bộ nhớ
            }

            // Lưu dữ liệu đã chuyển đổi sang tệp theo định dạng cột
            saveToDataCenter();
        } catch (IOException e) {
            e.printStackTrace(); // In lỗi nếu có ngoại lệ xảy ra
        }
        return null;
    }

    // Chuyển đổi dữ liệu từ danh sách dòng sang lưu trữ theo cột
    private void convertToColumnData(String[] fields) {
        for (int i = 0; i < fields.length; i++) {
            if (columnData.containsKey(i)) { // Nếu đã tồn tại cột với index `i`
                columnData.get(i).add(fields[i]); // Thêm giá trị vào danh sách cột
            } else {
                // Tạo danh sách mới và thêm giá trị nếu cột chưa tồn tại
                List<String> values = new ArrayList<>();
                values.add(fields[i]);
                columnData.put(i, values);
            }
        }
    }

    // Lưu dữ liệu đã được chuyển đổi vào các tệp theo từng cột
    private void saveToDataCenter() {
        for (Integer index : columnData.keySet()) { // Duyệt qua từng cột
            try {
                writeFile(index, columnData.get(index), 0, columnData.get(index).size());

                MetadataUtil.getInstance().writeMetaData();

                MetadataUtil.getInstance().writeFileInfo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Ghi dữ liệu một cột vào file, hỗ trợ chia nhỏ nếu vượt quá `fileCutoff`
    private void writeFile(int colIndex, List<String> data, int lowerIndex, int upperIndex) throws IOException {
        int range = upperIndex - lowerIndex; // Số lượng dòng cần ghi
        System.out.println("Range: " + range + ", lowerIndex: " + lowerIndex + ", upperIndex: " + upperIndex);

        if (range <= fileCutoff) {
            int pageId = 0;

            MetadataUtil.getInstance().checkPage(colIndex, pageId); // Kiểm tra thông tin trang hiện tại
            // MetadataUtil.getInstance().getCurrentPage(colIndex); // Có thể sử dụng nếu cần lấy Page ID hiện tại

            // Tạo tên file theo định dạng `colX.Y.txt` (X: cột, Y: pageId)
            String fileName = String.format("data/store/col%d.%d.txt", colIndex, pageId);
            FileUtils.writeByteArrayToFile(new File(fileName), String.join(",", data.subList(lowerIndex, upperIndex)).getBytes(Charset.defaultCharset()));

            MetadataUtil.getInstance().addPage(colIndex, pageId + 1);
            MetadataUtil.getInstance().updateFileInfo(colIndex, pageId, lowerIndex);
        } else {
            // Nếu số dòng vượt giới hạn, chia đôi khoảng dữ liệu và ghi riêng lẻ
            int middleIndex = lowerIndex + range / 2; // Tìm điểm giữa
            writeFile(colIndex, data, lowerIndex, middleIndex); // Ghi phần đầu
            writeFile(colIndex, data, middleIndex + 1, upperIndex); // Ghi phần sau
        }
    }
}
