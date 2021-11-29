package ca.sheridancollege.beans;

import lombok.Data;

/**
 * this is a review class. the reviews of the books are stored here.
 * this is also a model class.
 * @author asra.k
 *
 */

@Data
public class Review {
	
	
	private long id;
	private  long bookId;
	private String text;
	
	
}
