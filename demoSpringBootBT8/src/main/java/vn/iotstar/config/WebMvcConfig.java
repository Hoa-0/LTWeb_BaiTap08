package vn.iotstar.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path uploadDir = Paths.get("").toAbsolutePath().resolve("uploads");
		String uploadPath = uploadDir.toUri().toString();
		if (!uploadPath.endsWith("/"))
			uploadPath = uploadPath + "/";
		registry.addResourceHandler("/uploads/**").addResourceLocations(uploadPath);
	}
}
