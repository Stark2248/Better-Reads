package io.javabrains.Book;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.javabrains.userbooks.UserBooks;
import io.javabrains.userbooks.UserBooksPrimaryKey;
import io.javabrains.userbooks.UserBooksRepository;

@Controller
public class BookController {
	private final String COVER_IMAGE_ROOT = "http://covers.openlibrary.org/b/id/";

	@Autowired
	BookRepository bookRepository;

	@Autowired
	UserBooksRepository userBookRepository;

	@GetMapping("/books/{bookId}")
	public String getBook(@PathVariable String bookId, Model model, @AuthenticationPrincipal OAuth2User principal) {
		Optional<Book> optionalBook = bookRepository.findById(bookId);
		String coverImageUrl = "/images/no-image.png";
		if (optionalBook.isPresent()) {
			Book book = optionalBook.get();
			if (book.getCoversIds() != null && book.getCoversIds().size() > 0) {
				coverImageUrl = COVER_IMAGE_ROOT + book.getCoversIds().get(0) + "-L.jpg";
			}
			model.addAttribute("CoverImage", coverImageUrl);
			model.addAttribute("book", book);
			if (principal != null && principal.getAttribute("login") != null) {
				model.addAttribute("loginId", principal.getAttribute("login"));
				UserBooksPrimaryKey key = new UserBooksPrimaryKey();
				key.setBookId(book.getId());
				key.setUserId(principal.getAttribute("login"));
				Optional<UserBooks> optionalUserBook = userBookRepository.findById(key);
				if (optionalUserBook.isPresent()) {
					model.addAttribute("userBooks", optionalUserBook.get());
				} else {
					model.addAttribute("userBooks", new UserBooks());
				}
			}
			return "book";

		}
		return "book-not-found";
	}
}
