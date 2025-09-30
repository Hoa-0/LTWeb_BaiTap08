package vn.iotstar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import vn.iotstar.entity.User;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.repository.CategoryRepository;
import vn.iotstar.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Controller
public class GraphQLController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	// ----- Queries -----
	@QueryMapping
	public List<User> allUsers() {
		return userRepository.findAll();
	}

	@QueryMapping
	public User userById(@Argument Long id) {
		return userRepository.findById(id).orElse(null);
	}

	@QueryMapping
	public List<Category> allCategories() {
		return categoryRepository.findAll();
	}

	@QueryMapping
	public Category categoryById(@Argument Long id) {
		return categoryRepository.findById(id).orElse(null);
	}

	@QueryMapping
	public List<Product> allProducts() {
		return productRepository.findAll();
	}

	@QueryMapping
	public List<Product> productsByCategory(@Argument Long categoryId) {
		return productRepository.findByCategoryId(categoryId);
	}

	@QueryMapping
	public List<Product> productsSortedByPriceAsc() {
		return productRepository.findAllByOrderByPriceAsc();
	}

	// ----- Mutations -----
	@MutationMapping
	public User createUser(@Argument String fullname, @Argument String email, @Argument String password,
			@Argument String phone) {
		User u = new User();
		u.setFullname(fullname);
		u.setEmail(email);
		u.setPassword(password);
		u.setPhone(phone);
		return userRepository.save(u);
	}

	@MutationMapping
	public User updateUser(@Argument Long id, @Argument String fullname, @Argument String email,
			@Argument String password, @Argument String phone) {
		Optional<User> ou = userRepository.findById(id);
		if (ou.isEmpty())
			return null;
		User u = ou.get();
		if (fullname != null)
			u.setFullname(fullname);
		if (email != null)
			u.setEmail(email);
		if (password != null)
			u.setPassword(password);
		if (phone != null)
			u.setPhone(phone);
		return userRepository.save(u);
	}

	@MutationMapping
	public Boolean deleteUser(@Argument Long id) {
		try {
			userRepository.deleteById(id);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	@MutationMapping
	public Category createCategory(@Argument String name, @Argument String images) {
		Category c = new Category();
		c.setName(name);
		c.setImages(images);
		return categoryRepository.save(c);
	}

	@MutationMapping
	public Category updateCategory(@Argument Long id, @Argument String name, @Argument String images) {
		Optional<Category> oc = categoryRepository.findById(id);
		if (oc.isEmpty())
			return null;
		Category c = oc.get();
		if (name != null)
			c.setName(name);
		if (images != null)
			c.setImages(images);
		return categoryRepository.save(c);
	}

	@MutationMapping
	public Boolean deleteCategory(@Argument Long id) {
		try {
			categoryRepository.deleteById(id);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	@MutationMapping
	public Product createProduct(@Argument String title, @Argument Integer quantity, @Argument String description,
			@Argument Double price, @Argument Long userId, @Argument Long categoryId) {
		Product p = new Product();
		p.setTitle(title);
		p.setQuantity(quantity);
		p.setDescription(description);
		p.setPrice(price);
		if (userId != null) {
			userRepository.findById(userId).ifPresent(p::setUser);
		}
		if (categoryId != null) {
			categoryRepository.findById(categoryId).ifPresent(p::setCategory);
		}
		return productRepository.save(p);
	}

	@MutationMapping
	public Product updateProduct(@Argument Long id, @Argument String title, @Argument Integer quantity,
			@Argument String description, @Argument Double price, @Argument Long userId, @Argument Long categoryId) {
		Optional<Product> op = productRepository.findById(id);
		if (op.isEmpty())
			return null;
		Product p = op.get();
		if (title != null)
			p.setTitle(title);
		if (quantity != null)
			p.setQuantity(quantity);
		if (description != null)
			p.setDescription(description);
		if (price != null)
			p.setPrice(price);
		if (userId != null) {
			userRepository.findById(userId).ifPresent(p::setUser);
		}
		if (categoryId != null) {
			categoryRepository.findById(categoryId).ifPresent(p::setCategory);
		}
		return productRepository.save(p);
	}

	@MutationMapping
	public Boolean deleteProduct(@Argument Long id) {
		try {
			productRepository.deleteById(id);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}
