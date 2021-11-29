package ca.sheridancollege;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.sheridancollege.beans.Book;
import ca.sheridancollege.beans.Review;
import ca.sheridancollege.database.DatabaseAccess;

@RunWith(SpringRunner.class)

@SpringBootTest
public class TestDatabase {
	@Autowired
	private DatabaseAccess database;
	
	
	@Test
	public void testDatabaseAddBook() {
		Book book = new Book();
		book.setTitle("The da Vinci Code");
		book.setAuthor("Dan Brown");
		int originalSize= database.getAllbooks().size();
		database.addBook(book);
		int sizeAfterAdding= database.getAllbooks().size();
		assertThat(sizeAfterAdding).isEqualTo(originalSize+1);
		
		
	}
	
	@Test
	public void testDtabaseAddReview() {
	
		long id =1;
		database.getBookById(id);
		Review review= new Review();
		long old=database.getReviewWithId(id).size();
		review.setText("testing");
		review.setBookId(id);
		database.addReview(review);
		long sizeAfterAdding = database.getReviewWithId(id).size();
		assertThat(sizeAfterAdding).isEqualTo(old+1);
	
	}
	
	
	
	
}
