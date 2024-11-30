## Cấu trúc dữ liệu:

Để xử lý và tìm kiếm hiệu quả trên dữ liệu lớn, hệ thống thực hiện **ÁNH XẠ DỮ LIỆU TỪ DẠNG HÀNG (ROW-ORIENTED) SANG DẠNG CỘT (COLUMN-ORIENTED)**.  
- Phương pháp này được thiết kế để tăng cường hiệu suất truy vấn và phân tích dữ liệu, đặc biệt trong các hệ thống xử lý dữ liệu lớn hoặc các ứng dụng đòi hỏi hiệu suất cao như hệ thống báo cáo, thống kê, hoặc tìm kiếm dữ liệu.
### Chi tiết cách tổ chức dữ liệu
#### Phân tách theo cột:
- Dữ liệu từ mỗi hàng được chia thành các tệp riêng biệt, mỗi tệp đại diện cho một cột dữ liệu (column file).
#### Phân trang (pagination):
- Mỗi tệp cột được chia thành nhiều trang (page) dựa trên kích thước của hàng dữ liệu.
Điều này giúp mỗi file nhỏ gọn, dễ dàng xử lý hơn.
Dữ liệu được tổ chức theo cách này giảm thiểu thời gian đọc file và cho phép sử dụng đa luồng (multithreading) để tăng hiệu suất.
####  Lợi ích
  - Hiệu quả đọc/ghi: Các file nhỏ gọn giúp giảm thời gian tải dữ liệu.
  - Khả năng mở rộng: Dễ dàng phân chia và xử lý đồng thời nhiều tệp dữ liệu.
  - Tăng tốc độ xử lý: Tối ưu hóa bằng cách chia nhỏ dữ liệu và tận dụng sức mạnh của nhiều luồng (threads).
## Thuật toán xử lý:
Hệ thống sử dụng các bước thuật toán sau để tìm thông tin như direction (IN/OUT) và timestamp, dựa trên các chỉ số (index) liên quan đến card ID:
- Các bước thực hiện:
``` js
Bước 1: Tìm chỉ số (indexes) trên cột CARD_ID (col1):
- Duyệt qua cột card_id để tìm các chỉ số phù hợp (matches) với card_id đầu vào.
Bước 2: Truy xuất dữ liệu IN/OUT (direction):
- Dựa vào các chỉ số tìm được ở bước 1, duyệt trên cột direction để lấy dữ liệu tương ứng (IN/OUT).
Bước 3: Truy xuất dữ liệu thời gian (timestamp):
- Tương tự, dựa vào các chỉ số tìm được ở bước 1, duyệt trên cột timestamp để lấy thời gian tương ứng.
Bước 4:Tạo danh sách hành trình (trip):
- Sử dụng danh sách hướng đi và thời gian để ghép nối các sự kiện thành các hành trình hoàn chỉnh (từ IN đến OUT).
```
## Hướng dẫn sử dụng
1. Simulate và Import Data
   Để simulate và import dữ liệu vào hệ thống, cần chạy ứng dụng sau:
```
Run ImportDataApplication.java
```

- Lưu ý: Có thể thay đổi tổng số dữ liệu cần import bằng cách cấu hình trong file hoặc qua tham số đầu vào, tùy vào cách ứng dụng được thiết kế.
2. Tìm kiếm hành trình
``` 
Run Main.java
``` 

- Ứng dụng này sẽ sử dụng dữ liệu đã được nhập từ bước trước để thực hiện việc tìm kiếm và trả về các hành trình phù hợp với thông tin tìm kiếm (như card ID, direction, timestamp).


