
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `sound_quiz` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `sound_quiz`;
DROP TABLE IF EXISTS `game_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `game_room` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` text,
  `listSong` text,
  `question` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `game_room` WRITE;
/*!40000 ALTER TABLE `game_room` DISABLE KEYS */;
INSERT INTO `game_room` VALUES (1,'','Có những khoảnh khắc, ta lạc vào một miền đất lạ, nơi ánh sáng của ngày mai vẽ nên những bức tranh tuyệt đẹp. Nơi tâm hồn được vỗ về bởi những giai điệu dịu dàng, hứa hẹn một sự bừng sáng, một khởi đầu mới, như hạt sương ban mai lung linh trong nắng.','[{\"image\":\"https://i.postimg.cc/7ZJsFXCz/unnamed-2.jpg\",\"file_name\":\"em-se-rang-ngoi.wav\",\"id\":2,\"title\":\"Em sẽ rạng ngời nhé - Khải\",\"mood_id\":4}]','{\"title\": \"Tên bài hát là gì?\", \"A\": \"Em sẽ rạng ngời nhé - Khải\", \"B\": \"Em mãi rạng ngời\", \"C\": \"Nụ cười rạng ngời\", \"D\": \"Khải sẽ rạng ngời\", \"answer\": \"A\"}'),(2,'','Có những giai điệu như dòng sông thời gian, len lỏi qua tầng tầng ký ức. Chúng mang theo hương vị của những ngày đã xa, và phảng phất hình bóng của mai sau chưa tới. Một lời thì thầm từ khoảng không vô định, dệt nên giấc mộng chênh vênh giữa bình minh và hoàng hôn, nơi mọi giới hạn đều tan biến.','[{\"image\":\"https://i.postimg.cc/zB1TRsDS/Screenshot-2025-09-28-163504.png\",\"file_name\":\"tinh-ve.wav\",\"id\":1,\"title\":\"Tinh Vệ – 30 năm trước, 50 năm sau\",\"mood_id\":12}]','{\"title\": \"Tên bài hát là gì?\", \"A\": \"Tinh Vệ – 30 năm trước, 50 năm sau\", \"B\": \"30 năm trước, 50 năm sau\", \"C\": \"Tinh Vệ và những năm tháng\", \"D\": \"Thời gian và Tinh Vệ\", \"answer\": \"A\"}'),(3,'','Trong dòng chảy miên viễn của thời gian, có những âm thanh vượt qua ngàn năm vọng lại. Tựa như giấc mơ chưa tròn, hay lời thì thầm từ một chiều không gian khác, nơi quá khứ và tương lai giao thoa. Một giai điệu nhẹ nhàng khẽ chạm, mang theo những ký ức xa xăm, còn vương vấn mãi.','[{\"image\":\"https://i.postimg.cc/zB1TRsDS/Screenshot-2025-09-28-163504.png\",\"file_name\":\"tinh-ve.wav\",\"id\":1,\"title\":\"Tinh Vệ – 30 năm trước, 50 năm sau\",\"mood_id\":12}]','{\"title\": \"Tên bài hát là gì?\", \"A\": \"Tinh Vệ – 30 năm trước, 50 năm sau\", \"B\": \"Hồi Ức Về Tương Lai\", \"C\": \"Dòng Thời Gian Vô Tận\", \"D\": \"Chuyện Tình Ngàn Năm\", \"answer\": \"A\"}'),(4,'','Có những giai điệu như làn sương mỏng, lướt qua ranh giới của thời gian. Chúng mang theo hơi thở của những ký ức chưa từng tồn tại, và lời thì thầm của một tương lai chưa chạm tới. Một tiếng vọng khẽ khàng, dẫn lối ta vào miền không gian vô định, nơi tâm hồn tìm thấy chốn riêng mình giữa dòng chảy vĩnh cửu.','[{\"image\":\"https://i.postimg.cc/zB1TRsDS/Screenshot-2025-09-28-163504.png\",\"file_name\":\"tinh-ve.wav\",\"id\":1,\"title\":\"Tinh Vệ – 30 năm trước, 50 năm sau\",\"mood_id\":12}]','{\"title\": \"Tên bài hát là gì?\", \"A\": \"Tinh Vệ – 30 năm trước, 50 năm sau\", \"B\": \"Huyền Thoại Tinh Vệ\", \"C\": \"Dòng Thời Gian Vô Tận\", \"D\": \"Tinh Vệ – Ngàn năm trước, vạn năm sau\", \"answer\": \"A\"}');
/*!40000 ALTER TABLE `game_room` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `music`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `music` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `image` text,
  `mood_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `mood_id` (`mood_id`),
  CONSTRAINT `music_ibfk_1` FOREIGN KEY (`mood_id`) REFERENCES `music_mood` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `music` WRITE;
/*!40000 ALTER TABLE `music` DISABLE KEYS */;
INSERT INTO `music` VALUES (1,'Tinh Vệ – 30 năm trước, 50 năm sau','tinh-ve.wav','https://i.postimg.cc/zB1TRsDS/Screenshot-2025-09-28-163504.png',12),(2,'Em sẽ rạng ngời nhé - Khải','em-se-rang-ngoi.wav','https://i.postimg.cc/7ZJsFXCz/unnamed-2.jpg',4),(3,'Giữ mãi nụ cười','giu-mai-nu-cuoi.wav','https://i.postimg.cc/YSVJyHc2/z7133190928823-74a1a09ec8b893bc4719defcda13f519.jpg',4);
/*!40000 ALTER TABLE `music` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `music_mood`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `music_mood` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `music_mood` WRITE;
/*!40000 ALTER TABLE `music_mood` DISABLE KEYS */;
INSERT INTO `music_mood` VALUES (1,'Happy','Vui vẻ, tích cực, tràn đầy năng lượng'),(2,'Sad','Buồn bã, trầm lắng, cô đơn'),(3,'Calm','Bình yên, thư giãn, nhẹ nhàng'),(4,'Romantic','Lãng mạn, tình cảm, ấm áp'),(5,'Energetic','Sôi động, hưng phấn, kích thích'),(6,'Chill','Thư giãn, thoải mái, dễ chịu'),(7,'Epic','Hoành tráng, hùng tráng, cảm hứng'),(8,'Dark','Tối tăm, bí ẩn, căng thẳng'),(9,'Playful','Vui nhộn, tinh nghịch, dí dỏm'),(10,'Dreamy','Mơ màng, huyền ảo, nhẹ nhàng'),(11,'Excited','Phấn khích, hồi hộp, năng lượng cao'),(12,'Nostalgic','Hoài niệm, hồi tưởng, cảm xúc sâu'),(13,'Suspense','Kịch tính, hồi hộp, căng thẳng'),(14,'Magical','Ma mị, kỳ ảo, thần bí'),(15,'Funky','Vui nhộn, groovy, có nhịp điệu đặc trưng');
/*!40000 ALTER TABLE `music_mood` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `player` (
  `id` int NOT NULL AUTO_INCREMENT,
  `room_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `point` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `room_id` (`room_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `player_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `game_room` (`id`),
  CONSTRAINT `player_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `player` WRITE;
/*!40000 ALTER TABLE `player` DISABLE KEYS */;
INSERT INTO `player` VALUES (1,1,10,1),(2,1,17,0),(3,2,10,NULL),(4,3,10,NULL),(5,4,10,1),(6,4,17,0);
/*!40000 ALTER TABLE `player` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `total_points` int DEFAULT '0',
  `total_wins` int DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (10,'nt170','stellarhold170nt@gmail.com','c58f77c7a6920b55522ad5af379265f39903c4babef99ccc686dc3d9f63509c0',0,0,'2025-09-26 09:30:44'),(11,'BruceLee','lehuyitnt03@gmail.com','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3',0,0,'2025-09-27 00:49:55'),(17,'huyhuyhuy','nguyenviethuy2004ll@gmail.com','7bf526eeccd3818c0ca998813017eb45e7f437d4a1ee6b11fbb542ec33fe96b7',0,0,'2025-09-28 09:09:33'),(18,'vibecoder','neptune170nt@gmail.com ','ed83fdda9d202a3d6ef5ae9b901d266b883aa68aca687ab14fedc54544bc73ff',0,0,'2025-10-20 03:42:03');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

