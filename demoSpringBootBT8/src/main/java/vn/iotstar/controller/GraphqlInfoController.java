package vn.iotstar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;

@Controller
public class GraphqlInfoController {

	@GetMapping(path = "/graphql", produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> graphqlInfo() {
		String html = "<!doctype html>"
				+ "<html><head><meta charset='utf-8'><title>GraphQL Endpoint</title></head><body>"
				+ "<h2>GraphQL endpoint</h2>"
				+ "<p>Endpoint này nhận các yêu cầu <strong>POST</strong> JSON. Không mở trực tiếp bằng GET.</p>"
				+ "<p>Ví dụ dùng <code>curl</code>:</p>"
				+ "<pre>curl -X POST http://localhost:8088/graphql -H \"Content-Type: application/json\" -d '{\"query\":\"{ productsSortedByPriceAsc { id title price } }\"}'</pre>"
				+ "<p>Hoặc mở giao diện Thymeleaf:</p>" + "<ul>" + "<li><a href=\"/products\">/products</a></li>"
				+ "<li><a href=\"/users\">/users</a></li>" + "<li><a href=\"/categories\">/categories</a></li>"
				+ "</ul>"
				+ "<p>Nếu bạn muốn UI tương tác cho GraphQL (GraphiQL/Playground), hãy cài thêm dependency tương ứng.</p>"
				+ "</body></html>";
		return ResponseEntity.ok(html);
	}
}
