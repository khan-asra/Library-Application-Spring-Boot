package ca.sheridancollege.database;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ca.sheridancollege.beans.Book;
import ca.sheridancollege.beans.Review;

/**
 * 
 * @author asra.k
 *
 */

// an instance will be injected to our controller with the @Repository annotation 
@Repository
public class DatabaseAccess {

	
/**
 * A  method that returns a NamedParameterJdbcTempelate Bean 
 * @param dataSource injected to spring 
 * @return the instance of NamedParameterJdbcTemplate
 */
	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}

	private NamedParameterJdbcTemplate jdbc;
/**
 * the framework will inject the instance from the Bean through our constructor 
 * @param dataSource
 */
	public DatabaseAccess(DataSource dataSource) {
		this.jdbc = new NamedParameterJdbcTemplate(dataSource);
	}
/**
 * this method is used to select all the books from the database.
 * @return list of books
 */
	public List<Book> getAllbooks() {
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		String query = "SELECT * from books";
		BeanPropertyRowMapper<Book> bookMapper = new BeanPropertyRowMapper<Book>(Book.class);
		List<Book> books = jdbc.query(query, namedParameter, bookMapper);
		return books;

	}
/**
 * this method take the id of th book and return a list of revieww 
 * for the book selected
 * @param id  of the book
 * @return review list of reviews
 */
	public List<Review> getReviewWithId(Long id) {
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		String query = "SELECT * from reviews where bookid= :id";
		namedParameter.addValue("id", id);
		BeanPropertyRowMapper<Review> bookMapper = new BeanPropertyRowMapper<>(Review.class);
		List<Review> reviews = jdbc.query(query, namedParameter, bookMapper);
		return reviews;
	}
/**
 * get the book by id.it takes the id and return the book
 * @param id book id 
 * @return
 */
	public Book getBookById(Long id) {
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		String query = "SELECT * from books where id= :id";
		namedParameter.addValue("id", id);
		BeanPropertyRowMapper<Book> bookMapper = new BeanPropertyRowMapper<>(Book.class);
		List<Book> books = jdbc.query(query, namedParameter, bookMapper);
		return books.get(0);
	}
	
	/**
	 * this method add book to the database
	 * @param book 
	 * @return
	 */

	public int addBook(Book book) {
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		String query = "INSERT into books (title,author)" + "VALUES(:title, :author)";
		namedParameter.
		addValue("title", book.getTitle())
		.addValue("author",book.getAuthor());
		int returnValue = jdbc.update(query, namedParameter);
		return returnValue;
	}
/**this method is used to add review to database.
 * 
 * @param review
 * @return
 */
	public int addReview(Review review) {
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		String query = "INSERT into reviews (text,bookId)" + "VALUES(:text, :bookId)";
		namedParameter.
		addValue("text" ,review.getText())
		.addValue("bookId", review.getBookId());
		int returnValue = jdbc.update(query, namedParameter);
		return returnValue;
		
	}
	

}
