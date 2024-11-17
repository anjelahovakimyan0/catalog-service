package am.itspace.catalogservice.web;

import am.itspace.catalogservice.domain.Book;
import am.itspace.catalogservice.domain.BookNotFoundException;
import am.itspace.catalogservice.domain.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerMvcTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BookService bookService;

    @Test
    void whenGetBookExistingThenShouldReturn200() throws Exception {
        var isbn = "7373731394";
        var expectedBook = Book.of(isbn, "Title", "Author", 9.90, null);
        given(bookService.viewBookDetails(isbn)).willReturn(expectedBook);
        mockMvc
                .perform(get("/books/" + isbn))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetBookNotExistingThenShouldReturn404() throws Exception {
        var isbn = "7373731394";
        given(bookService.viewBookDetails(isbn)).willThrow(BookNotFoundException.class);
        mockMvc
                .perform(get("/books/" + isbn))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDeleteBookThenShouldReturn204() throws Exception {
        var isbn = "7373731394";
        mockMvc
                .perform(delete("/books/" + isbn))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenPostBookThenShouldReturn201() throws Exception {
        var isbn = "7373731394";
        var bookToCreate = Book.of(isbn, "Title", "Author", 9.90, null);
        given(bookService.addBookToCatalog(bookToCreate)).willReturn(bookToCreate);
        mockMvc
                .perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookToCreate)))
                .andExpect(status().isCreated());
    }

    @Test
    void whenPutBookThenShouldReturn200() throws Exception {
        var isbn = "7373731394";
        var bookToCreate = Book.of(isbn, "Title", "Author", 9.90, null);
        given(bookService.addBookToCatalog(bookToCreate)).willReturn(bookToCreate);
        mockMvc
                .perform(put("/books/" + isbn)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookToCreate)))
                .andExpect(status().isOk());
    }
}
