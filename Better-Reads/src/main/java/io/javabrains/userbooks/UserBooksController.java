package io.javabrains.userbooks;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

public class UserBooksController {

	@Autowired
	UserBooksRepository userBookRepository;

	@PostMapping("/addUserBook")
	public ModelAndView addBookForUser(@RequestBody MultiValueMap<String, String> formData,
			@AuthenticationPrincipal OAuth2User principal) {
		if (principal == null || principal.getAttribute("login") == null) {
			return null;
		}

		UserBooks userBook = new UserBooks();
		UserBooksPrimaryKey key = new UserBooksPrimaryKey();
		key.setUserId(principal.getAttribute("login"));
		String bookId = formData.getFirst("bookId");
		key.setBookId(bookId);
		userBook.setKey(key);
		userBook.setStatedDate(LocalDate.parse(formData.getFirst("startDate")));
		userBook.setCompletedDate(LocalDate.parse(formData.getFirst("completedDate")));
		userBook.setReadingStatus(formData.getFirst("readingStatus"));
		userBook.setRating(Integer.parseInt(formData.getFirst("rating")));
		userBookRepository.save(userBook);
		return new ModelAndView("redirect:/books/" + bookId);
	}
}
