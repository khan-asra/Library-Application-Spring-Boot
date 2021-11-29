package ca.sheridancollege.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.beans.Book;
import ca.sheridancollege.beans.Review;
import ca.sheridancollege.database.DatabaseAccess;

/**
 * This is a Spring Controller. The controller annotation ensures that the
 * spring framework recognize this class as "Something it must manage".
 * 
 * @author asra.k
 *
 */
@Controller
public class HomeController {

	// a thread-safe arraylist. for the reviews
	public CopyOnWriteArrayList<Review> reviewList = new CopyOnWriteArrayList<>();

	// a thread-safe arraylist.
	public CopyOnWriteArrayList<Book> booksList = new CopyOnWriteArrayList<>();
   // to access the class database.
	private DatabaseAccess database;

	@Autowired
	@Lazy
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	JdbcUserDetailsManager jdbcUserDetailsManager;

	/**
	 * for dependency injection one arg constructor.
	 * 
	 * @param database will be injected at runtime by Spring
	 */
	public HomeController(DatabaseAccess database) {
		this.database = database;
	}

	/**
	 * This method will be used by the framework whenever there is a request for the
	 * root of the web site.
	 * @param model supplied by MVC
	 * @param book an instance of model Book that can be used to send data back and forward .html
	 * @return index.html
	 */
	@GetMapping("/")
	public String goHome(Model model, @ModelAttribute Book book) {
		model.addAttribute("booksList", database.getAllbooks());
		return "index";
	}


	/**
	 * This method allow the user to enter the review of the book.
	 * @param model supplied by MVC
	 * @param review an instance of model Review
	 * @param text user review about the book
	 * @param bookId the bookid of the book that is review by the user
	 * @return view-book.
	 */
	@PostMapping("/user/addReview/{bookId}")
	public String addReview(Model model, @ModelAttribute Review review, @RequestParam String text,
			@RequestParam long bookId) {
		reviewList.add(review);
		database.addReview(review);
		model.addAttribute("review", review);
		addToModelAttribute(bookId, model);
		return "view-book";

	}

	/**
	 * admin can add the book 
	 * @return admin/addbook
	 */
	@GetMapping("/admin/addBook")
	public String addBook() {
		return "/admin/add-book";
	}

	/**
	 * this is for the admin to addAnewBook
	 * @param model book supplied by MVC
	 * @param book book admin wants to enter into database
	 * @return index
	 */
	@PostMapping("/admin/addANewBook")
	public String addBooktoBooks(Model model, @ModelAttribute Book book) {
		booksList.add(book);
		database.addBook(book);
		model.addAttribute("booksList", database.getAllbooks());
		return "index";

	}

	
	/**
	 * A method to avoid duplication of code,it is used in
	 * ("/user/addReview/{bookId}")
	 * ("user/add-review/{id}")
	 * ("/viewBook/{id}")
	 *
	 * @param id bookid
	 * @param model model supplied by MVC
	 */
	public void addToModelAttribute(long id, Model model) {
		model.addAttribute("title", database.getBookById(id).getTitle());
		model.addAttribute("author", database.getBookById(id).getAuthor());
		model.addAttribute("reviewList", database.getReviewWithId(id));
		model.addAttribute("bookId", id);

	}

	@GetMapping("user/add-review/{id}")
	public String newReview(Model model, @PathVariable Long id) {
		Review review = new Review();
		review.setBookId(id);
		addToModelAttribute(id, model);
		model.addAttribute("review", review);
		return "/user/add-review";

	}

	/**
	 * mapping used to access the login page. user can use it to log in the system
	 * @return login.html
	 */
	@GetMapping("/login")
	public String goLogin() {
		return "/login";

	}
/**
 * mapping for the register form 
 * @param model 
 * @return register page 
 */
	@GetMapping("/register")
	public String goRegister(Model model) {
		return "/register";

	}

	/**
	 * method used for user to add a new user with a Distinct username.
	 * @param username the name of the user 
	 * @param password password 
	 * @param model 
	 * @return if new user is created it take to the index. else 
	 * on  the same page 
	 * @throws Exception if user name exist 
	 */
	@PostMapping("/add-user")
	public String addUser(@RequestParam String username, @RequestParam String password, Model model) throws Exception {

		List<GrantedAuthority> accessList = new ArrayList<>();
		accessList.add(new SimpleGrantedAuthority("ROLE_USER"));
		try {
			String encodedPass = passwordEncoder.encode(password);
			User user = new User(username, encodedPass, accessList);
			model.addAttribute("booksList", database.getAllbooks());
			jdbcUserDetailsManager.createUser(user);
			model.addAttribute("message", "Registration Successful");
		} catch (Exception e) {
			model.addAttribute("message", "User name taken. Please try another name");
			return "/register";
		}
		return "index";
	}
/**
 * can view the reviews of the book selected 
 * @param id book id
 * @param model model supplied by MVC
 * @return view-book.
 */
	@GetMapping("/viewBook/{id}")
	public String viewBook(@PathVariable Long id, Model model) {
		addToModelAttribute(id, model);
		return "view-book";
	}
	
	
/**
 * in case of unauthorized access 
 * @return to a error/permission-denied
 */
	@GetMapping("/permission-denied")
	public String permissionDenied() {
		return "/error/permission-denied";

	}

}
