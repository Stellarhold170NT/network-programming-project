# Đồ án SoundQuiz - BTL Lập Trình Mạng

Đây là dự án game đố vui âm thanh (SoundQuiz) được xây dựng theo mô hình Client-Server. Người chơi sẽ nghe một đoạn âm thanh và đoán tên bài hát hoặc nguồn gốc của âm thanh đó.

## Tính năng chính

-   **Đăng ký tài khoản:** Người dùng mới có thể tạo tài khoản.
-   **Xác thực OTP:** Sử dụng OTP gửi qua email để xác thực khi đăng ký.
-   **Đăng nhập:** Người dùng có thể đăng nhập vào hệ thống để bắt đầu chơi.
-   **Game chính:** Tham gia vào phòng chơi, nghe âm thanh và trả lời câu hỏi trắc nghiệm.
-   **Bảng xếp hạng:** (Dự kiến) Hiển thị bảng xếp hạng người chơi.

## Công nghệ sử dụng

-   **Ngôn ngữ:** Java
-   **Giao diện (Client):** Java Swing
-   **Build tool:** Apache Maven
-   **Mạng:** Java Socket cho giao tiếp Client-Server.
-   **Cơ sở dữ liệu:** MySQL (quản lý thông tin người dùng).
-   **Gửi Email:** JavaMail API.

## Cấu trúc dự án

Dự án được chia thành hai module chính: `SoundQuiz-Server` và `SoundQuiz-Client`.

---

### 1. SoundQuiz-Server

Module này chịu trách nhiệm xử lý toàn bộ logic nghiệp vụ, quản lý kết nối từ các client, tương tác với cơ sở dữ liệu và gửi email.

-   `pom.xml`: File cấu hình Maven, quản lý các thư viện phụ thuộc cho Server.
-   `config/config.properties`: File cấu hình thông tin nhạy cảm như tài khoản database, tài khoản email để gửi OTP.
-   `src/main/java/com/mycompany/soundquiz/server`: Thư mục chứa mã nguồn chính của Server.
    -   `connection`: Lớp quản lý kết nối và truy vấn đến cơ sở dữ liệu MySQL (`DatabaseConnection.java`).
    -   `dto`: Các đối tượng truyền dữ liệu (Data Transfer Object) để giao tiếp với Client (`MessageRequest.java`, `MessageResponse.java`).
    -   `model`: Định nghĩa các đối tượng dữ liệu của ứng dụng, ví dụ: `User.java`.
    -   `service`: Nơi chứa các logic nghiệp vụ cốt lõi.
        -   `Server.java`: Lớp chính, khởi tạo server socket và lắng nghe kết nối từ client.
        -   `ClientThread.java`: Tạo một luồng riêng để xử lý cho mỗi client kết nối tới.
        -   `UserService.java`: Xử lý các nghiệp vụ liên quan đến người dùng (đăng ký, đăng nhập).
        -   `MailService.java`: Xử lý việc gửi email chứa mã OTP.
    -   `view`: Giao diện đồ họa đơn giản cho Server để theo dõi trạng thái (`MainFrm.java`).

---

### 2. SoundQuiz-Client

Module này là ứng dụng desktop cho người dùng cuối, cung cấp giao diện đồ họa để tương tác với server.

-   `pom.xml`: File cấu hình Maven cho Client.
-   `src/main/java/com/mycompany/soundquiz/client`: Thư mục chứa mã nguồn chính của Client.
    -   `connection`: Quản lý kết nối socket đến Server (`ClientConnection.java`, `ClientNetwork.java`).
    -   `dto`: Các đối tượng DTO tương ứng với Server để gửi và nhận dữ liệu.
    -   `view`: Chứa các lớp giao diện (Forms) được xây dựng bằng Java Swing.
        -   `LoginFrm.java`: Giao diện đăng nhập.
        -   `RegisterFrm.java`: Giao diện đăng ký.
        -   `OTPFrm.java`: Giao diện nhập mã OTP.
        -   `MainFrm.java`: Giao diện chính của trò chơi sau khi đăng nhập thành công.

## Hướng dẫn cài đặt và chạy

1.  **Cơ sở dữ liệu:**
    -   Tạo một database trong MySQL.
    -   Import file `sound_quiz.sql` để tạo bảng `user` và các dữ liệu cần thiết.

2.  **Cấu hình Server:**
    -   Mở file `SoundQuiz-Server/config/config.properties`.
    -   Cập nhật lại các thông tin `db.url`, `db.user`, `db.password` cho đúng với cấu hình MySQL của bạn.
    -   Cập nhật `mail.user` và `mail.password` bằng tài khoản email của bạn (Lưu ý: bạn cần bật "Quyền truy cập của ứng dụng kém an toàn" cho tài khoản Google hoặc tạo mật khẩu ứng dụng).

3.  **Chạy Server:**
    -   Mở dự án `SoundQuiz-Server` bằng NetBeans (hoặc IDE khác).
    -   Chạy file `com.mycompany.soundquiz.server.service.Server.java`.

4.  **Chạy Client:**
    -   Mở dự án `SoundQuiz-Client`.
    -   Chạy file `com.mycompany.soundquiz.client.view.LoginFrm.java`.
    -   Bạn có thể chạy nhiều client cùng lúc để kiểm tra.

---

## Tác giả

Thực hiện bởi **Nhóm 8**.
