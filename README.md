# Đồ án SoundQuiz - BTL Lập Trình Mạng

Dự án game đố vui âm thanh "SoundQuiz" được xây dựng theo mô hình Client-Server, nơi người chơi sẽ nghe một đoạn nhạc và đoán tên bài hát.

## Tính năng chính

-   **Đăng ký & Đăng nhập:** Người dùng có thể tạo tài khoản mới và đăng nhập vào hệ thống.
-   **Xác thực OTP:** Sử dụng mã OTP gửi qua email để xác thực tài khoản khi đăng ký, tăng cường bảo mật.
-   **Giao diện trò chơi:** Giao diện đồ họa thân thiện được xây dựng bằng Java Swing.
-   **Gameplay:** Tham gia phòng chơi, nghe các đoạn nhạc và trả lời câu hỏi trắc nghiệm để ghi điểm.
-   **Tích hợp AI:** Server có khả năng sử dụng Google Gemini để thực hiện các tác vụ nâng cao (tính năng đang phát triển).

## Công nghệ sử dụng

| Thành phần      | Công nghệ                                                                                                 |
| --------------- | --------------------------------------------------------------------------------------------------------- |
| **Ngôn ngữ**    | Java (JDK 17 cho Client, JDK 23 cho Server)                                                               |
| **Build Tool**  | Apache Maven                                                                                              |
| **Giao diện**   | Java Swing                                                                                                |
| **Mạng**        | Java Socket                                                                                               |
| **Cơ sở dữ liệu** | MySQL                                                                                                     |
| **Thư viện (Server)** | - `mysql-connector-java`: Kết nối MySQL.<br>- `javax.mail`: Gửi email OTP.<br>- `gson`: Xử lý dữ liệu JSON.<br>- `okhttp`: Thực hiện các yêu cầu HTTP.<br>- `google-genai`: Tích hợp Google Gemini API. |
| **Thư viện (Client)** | - `gson`: Xử lý dữ liệu JSON.<br>- `okhttp`: Thực hiện các yêu cầu HTTP.                                   |

## Cấu trúc dự án

Dự án bao gồm 2 module chính: `SoundQuiz-Server` và `SoundQuiz-Client`.

### 1. SoundQuiz-Server

Module backend chịu trách nhiệm xử lý logic nghiệp vụ, quản lý kết nối, tương tác với database và các dịch vụ bên ngoài.

-   `pom.xml`: Quản lý các thư viện phụ thuộc cho Server.
-   `config/config_example.properties`: File mẫu cho cấu hình.
-   `config/config.properties`: **(Đã được ignore bởi .gitignore)** File chứa thông tin nhạy cảm như tài khoản database, email.
-   `src/main/java/com/mycompany/soundquiz/server`: Mã nguồn của Server.
    -   `connection`: Quản lý kết nối database.
    -   `service`: Chứa các logic nghiệp vụ cốt lõi (`Server.java`, `ClientThread.java`, `UserService.java`, `MailService.java`, `GeminiService.java`).
    -   `view`: Giao diện đồ họa đơn giản để theo dõi trạng thái Server.

### 2. SoundQuiz-Client

Module frontend cung cấp giao diện đồ họa cho người dùng cuối.

-   `pom.xml`: Quản lý các thư viện phụ thuộc cho Client.
-   `src/main/java/com/mycompany/soundquiz/client`: Mã nguồn của Client.
    -   `connection`: Quản lý kết nối socket đến Server.
    -   `view`: Chứa các lớp giao diện (Forms) được xây dựng bằng Java Swing (`LoginFrm.java`, `RegisterFrm.java`, `PlayGameFrm.java`, v.v.).

## Hướng dẫn cài đặt và chạy

1.  **Cơ sở dữ liệu:**
    -   Cài đặt một MySQL server.
    -   Tạo một database mới (ví dụ: `sound_quiz`).
    -   Import file `sound_quiz.sql` vào database vừa tạo để khởi tạo bảng `users`.

2.  **Cấu hình Server:**
    -   Tạo một bản sao của file `SoundQuiz-Server/config/config_example.properties` và đổi tên thành `config.properties`.
    -   Mở file `config.properties` và cập nhật các thông tin `db.url`, `db.user`, `db.password` cho đúng với cấu hình MySQL của bạn.
    -   Cập nhật `mail.user` và `mail.password` bằng tài khoản email của bạn. **Lưu ý:** Bạn cần bật "Quyền truy cập của ứng dụng kém an toàn" cho tài khoản Google hoặc tạo "Mật khẩu ứng dụng".

3.  **Chạy Server:**
    -   Mở dự án `SoundQuiz-Server` bằng một IDE (ví dụ: NetBeans, IntelliJ).
    -   Chạy file `com.mycompany.soundquiz.server.service.Server.java`.

4.  **Chạy Client:**
    -   Mở dự án `SoundQuiz-Client` trong IDE.
    -   Chạy file `com.mycompany.soundquiz.client.view.MainFrm.java`.
    -   Bạn có thể khởi chạy nhiều Client để kiểm tra tương tác.

## Quản lý mã nguồn

-   File `.gitignore` đã được cấu hình để bỏ qua các file không cần thiết như file build (`target/`), log, các file đóng gói (`.jar`, `.exe`) và đặc biệt là file `config.properties` để tránh đưa thông tin nhạy cảm vào repository.

---

Thực hiện bởi **Nhóm 8**.