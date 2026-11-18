package com.example.demo.model.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component // [11주차 제공] 업로드 파일 저장소 : 프로젝트 폴더 아래 uploads/ 에 저장한다
public class FileStore {
    private static final Set<String> ALLOWED = Set.of("jpg", "jpeg", "png", "gif", "webp"); // ① 허용할 확장자만 (허용 목록)
    private final Path root; // 저장 폴더의 절대 경로

    public FileStore(@Value("${app.upload-dir:uploads}") String uploadDir) throws IOException {
        this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(root); // 폴더가 없으면 만든다
    }

    // 이미지 1개 저장 → 저장된 파일 이름을 돌려준다 (파일이 없으면 null)
    public String save(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null; // 포스터를 고르지 않은 글
        }
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (ext == null || !ALLOWED.contains(ext.toLowerCase())) {
            throw new IllegalArgumentException("이미지 파일(jpg, png, gif, webp)만 올릴 수 있습니다.");
        }
        try (InputStream in = file.getInputStream()) {
            if (!isImage(in.readNBytes(12))) { // ② 확장자·Content-Type 은 바꿀 수 있다 → 파일 앞부분(시그니처)을 직접 확인
                throw new IllegalArgumentException("이미지 파일이 아닙니다.");
            }
        } catch (IOException e) {
            throw new IllegalStateException("파일을 읽을 수 없습니다.", e);
        }
        String storedName = UUID.randomUUID() + "." + ext.toLowerCase(); // ③ 이름은 서버가 새로 만든다 (덮어쓰기·../ 방지)
        Path target = root.resolve(storedName).normalize();
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING); // ④ static 폴더가 아닌 uploads/ 에 저장
        } catch (IOException e) {
            throw new IllegalStateException("파일 저장에 실패했습니다.", e);
        }
        return storedName;
    }

    // 저장된 파일 삭제 (글을 지울 때)
    public void delete(String storedName) {
        if (storedName == null) return;
        try {
            Path target = root.resolve(storedName).normalize();
            if (target.startsWith(root)) {
                Files.deleteIfExists(target);
            }
        } catch (IOException e) {
            // 파일 삭제 실패는 글 삭제를 막지 않는다
        }
    }

    // 파일 시그니처(매직 넘버) : JPG FF D8 FF, PNG 89 50 4E 47, GIF "GIF8", WEBP "RIFF....WEBP"
    private boolean isImage(byte[] h) {
        if (h.length >= 3 && (h[0] & 0xFF) == 0xFF && (h[1] & 0xFF) == 0xD8 && (h[2] & 0xFF) == 0xFF) return true;
        if (h.length >= 4 && (h[0] & 0xFF) == 0x89 && h[1] == 'P' && h[2] == 'N' && h[3] == 'G') return true;
        if (h.length >= 4 && h[0] == 'G' && h[1] == 'I' && h[2] == 'F' && h[3] == '8') return true;
        return h.length >= 12 && h[0] == 'R' && h[1] == 'I' && h[2] == 'F' && h[3] == 'F'
                && h[8] == 'W' && h[9] == 'E' && h[10] == 'B' && h[11] == 'P';
    }
}
