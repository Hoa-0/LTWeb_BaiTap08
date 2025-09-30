package vn.iotstar.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/upload")
public class UploadController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

	private final Path uploadDir;

	public UploadController() throws IOException {
		// đặt folder uploads tại project root
		this.uploadDir = Paths.get("").toAbsolutePath().resolve("uploads");
		if (!Files.exists(uploadDir)) {
			Files.createDirectories(uploadDir);
			logger.info("Created uploads directory at: {}", uploadDir.toString());
		} else {
			logger.info("Uploads directory exists at: {}", uploadDir.toString());
		}
	}

	/**
	 * Upload a single file. Returns JSON: { "url": "/uploads/filename.ext",
	 * "filename": "...", "size": ... }
	 */
	@PostMapping(consumes = { "multipart/form-data" })
	public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {
		Map<String, Object> resp = new HashMap<>();
		if (file == null || file.isEmpty()) {
			resp.put("error", "No file provided or file is empty");
			logger.warn("Upload attempt with empty file");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
		}

		logger.info("Received upload: originalName='{}', size={} bytes, contentType={}", file.getOriginalFilename(),
				file.getSize(), file.getContentType());

		// Basic validation: only images allowed (optional)
		String contentType = file.getContentType();
		if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
			resp.put("error", "Only image files are allowed (contentType=" + contentType + ")");
			logger.warn("Rejected non-image upload: contentType={}", contentType);
			return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(resp);
		}

		// limit size check (redundant with Spring's multipart limits)
		long maxSize = 20L * 1024L * 1024L; // 20MB
		if (file.getSize() > maxSize) {
			resp.put("error", "File too large. Max allowed is 20MB");
			logger.warn("Rejected upload: file too large ({} bytes)", file.getSize());
			return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(resp);
		}

		try (InputStream in = file.getInputStream()) {
			String original = file.getOriginalFilename();
			String ext = "";
			if (original != null && original.contains(".")) {
				ext = original.substring(original.lastIndexOf("."));
				// sanitize extension simple
				if (ext.length() > 10)
					ext = "";
			}
			String filename = UUID.randomUUID().toString() + ext;
			Path target = uploadDir.resolve(filename);

			// Copy stream to target (atomic replace if exists)
			Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);

			String url = "/uploads/" + filename;
			resp.put("url", url);
			resp.put("filename", filename);
			resp.put("size", Files.size(target));
			logger.info("Saved upload to: {}", target.toString());
			return ResponseEntity.ok(resp);
		} catch (IOException ex) {
			logger.error("Failed to save uploaded file", ex);
			resp.put("error", "Upload failed: " + ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
		} catch (Exception ex) {
			logger.error("Unexpected error during upload", ex);
			resp.put("error", "Unexpected server error: " + ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
		}
	}
}
