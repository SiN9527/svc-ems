package com.svc.ems.utils;

/**
 * ClassName: com.sweetolive.exhibition_backend.util.MapperUtil
 * Package: com.sweetolive.exhibition_backend.util
 * Description:
 *
 * @Author 郭庭安
 * @Create 2025/1/23 下午8:57
 * @Version 1.0
 */


import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 提供對象之間映射的工具類，通過 Jackson 的 ObjectMapper 實現對象的轉換。
 */
@Component
public class VerificationUtils {
    private static final SecureRandom random = new SecureRandom();

    public static String generateOtp() {
        int otp = 100000 + random.nextInt(900000); // 產生 100000 ~ 999999
        return String.valueOf(otp);
    }

//    public static byte[] generateQrCode(String content, int width, int height) {
//        try {
//            QRCodeWriter qrCodeWriter = new QRCodeWriter();
//            Map<EncodeHintType, Object> hints = new HashMap<>();
//            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//
//            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
//            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
//
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            ImageIO.write(image, "PNG", outputStream);
//            return outputStream.toByteArray();
//        } catch (Exception e) {
//            throw new RuntimeException("QR Code 產生失敗", e);
//        }
//    }
}
