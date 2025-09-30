package vn.iotstar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class ViewController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@GetMapping("/")
	public String home() {
		return "products";
	}

	@GetMapping("/users")
	public String users(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "users";
	}

	@GetMapping("/categories")
	public String categories(Model model) {
		model.addAttribute("categories", categoryRepository.findAll());
		return "categories";
	}

	@GetMapping("/products")
	public String products(Model model) {
		model.addAttribute("categories", categoryRepository.findAll());
		model.addAttribute("users", userRepository.findAll());
		return "products";
	}
}
