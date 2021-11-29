package ca.sheridancollege;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
// creating and using a MockMVC
@AutoConfigureMockMvc
public class TestController {
	
	
	
	
	//used to mimic client request
	@Autowired
	MockMvc mockMvc;
	
	/**
	 * testing the / mapping. mockMvc.perform(get("/"))mimic the client request.
	 * status().isOk()) expecting HTTP_Ok. test what view we go to.
	 * @throws Exception
	 */
	
	@Test
	public void testIndex()throws Exception {
		mockMvc.perform(get("/"))
		.andExpect(status().isOk())
		.andExpect(view().name("index"));
	}
	
	/**
	 * testing the mapping for  addBook.mockMvc.perform(get("/testAddBook"))mimic the client request.
	 * status().isOk()) expecting HTTP_Ok. test what view we go to.
	 * @throws Exception
	 */
	@Test
	public void tesAddBook()throws Exception {
		
		mockMvc.perform(get("/addBook"))
		.andExpect(status().isOk())
		.andExpect(view().name("/admin/add-book"));
	}
	
}
