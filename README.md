# svc-ems
展場投稿報名系統後端
本專案是一個基於 Spring Boot 的後端應用程式，用於管理展場投稿和報名系統。

功能簡介
投稿管理：新增、刪除、修改投稿內容。
報名管理：參加者可進行報名、取消報名。
文件生成：自動生成 API 文件，便於測試和開發。
啟動項目
系統需求
Java 版本：JDK 17
Maven：3.6 或以上
Spring Boot：3.4.1
啟動步驟
下載或克隆此專案：
git clone https://github.com/your-repo-name.git
cd your-repo-name
啟動一般spring boot start 或 docker ，到專案資料夾後
1. 打包
mvn clean package
2. 建立鏡像
docker build -t sweetolive/exhibition-backend .
3. 執行Docker 容器並映射到9090
docker run -d -p 9090:8080 --name exhibition-backend sweetolive/exhibition-backend
啟動後SwaggerUI
http://localhost:9090/swagger-ui/index.html#/
先獲取Token在打其他api
dd
