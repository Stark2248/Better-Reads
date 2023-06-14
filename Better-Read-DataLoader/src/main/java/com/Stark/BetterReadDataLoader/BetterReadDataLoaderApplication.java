package com.Stark.BetterReadDataLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.Stark.BetterReadDataLoader.Auther.AutherRepository;
import com.Stark.BetterReadDataLoader.Book.Book;
import com.Stark.BetterReadDataLoader.Book.BookRepository;
import com.Stark.BetterReadDataLoader.Connection.DataStaxAstraProperties;


@SpringBootApplication
@EnableConfigurationProperties(DataStaxAstraProperties.class)
public class BetterReadDataLoaderApplication {

	@Autowired
	AutherRepository autherRepository;
	@Autowired
	BookRepository bookRepository;
	@Value("${datadump.location.auther}")
	private String autherDumpLocation;
	@Value("${datadump.location.works}")
	private String worksDumpLocation;


	public static void main(String[] args) {

		SpringApplication.run(BetterReadDataLoaderApplication.class, args);
	}

	/*
	 * private void initAuthers() { Path path = Paths.get(autherDumpLocation); try
	 * (Stream<String> lines = Files.lines(path)) {
	 *
	 * lines.forEach(line -> { // Read and parse the line String jsonString =
	 * line.substring(line.indexOf("{")); JSONParser parser = new JSONParser(); try
	 * { JSONObject json = (JSONObject) parser.parse(jsonString);
	 *
	 * // System.out.println(((String) json.get("key")).replace("/authors/", ""));
	 *
	 * Auther auther = new Auther(); auther.setName((String) json.get("name"));
	 * auther.setPersonalName((String) json.get("personal_name"));
	 * auther.setId(((String) json.get("key")).replace("/authors/", ""));
	 * autherRepository.save(auther);
	 *
	 * // autherRepository.save(auther); } catch (ParseException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } // construct auther object
	 * // persists using repository });
	 *
	 * } catch (IOException e) { e.printStackTrace(); } }
	 */

	private void initWorks() {
		Path path = Paths.get(worksDumpLocation);
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
		try (Stream<String> lines = Files.lines(path)) {
			lines.forEach(line -> {
				String jsonString = line.substring(line.indexOf("{"));
				JSONParser parser = new JSONParser();
				try {
					JSONObject json = (JSONObject) parser.parse(jsonString);
					Book book = new Book();
					book.setId(((String) json.get("key")).replace("/works/", ""));
					book.setName((String) json.get("title"));
					if (json.get("description") != null) {
						JSONObject desc = (JSONObject) json.get("description");
						book.setDescription((String) desc.get("value"));
					} else {
						book.setDescription(null);
					}
					if (json.get("created") != null) {
						JSONObject published = (JSONObject) json.get("created");
						book.setPublishedDate(LocalDate.parse((String) published.get("value"), dateFormat));
					}

					JSONArray covers = (JSONArray) json.get("covers");
					if (covers != null) {
						List<String> coverIds = new ArrayList<>();
						for (int i = 0; i < covers.size(); i++) {
							coverIds.add(String.valueOf(covers.get(i)));
						}
						book.setCoversIds(coverIds);
					}
					JSONArray authers = (JSONArray) json.get("authors");
					if (authers != null) {
						List<String> authersId = new ArrayList<>();
						for (int i = 0; i < authers.size(); i++) {
							JSONObject temp = (JSONObject) authers.get(i);
							JSONObject author = (JSONObject) temp.get("author");
							authersId.add(((String) author.get("key")).replace("/authors/", ""));
						}
						book.setAutherId(authersId);
						List<String> authernames = authersId.stream().map(id -> autherRepository.findById(id))
								.map(optionalAuthor -> {
									if (!optionalAuthor.isPresent())
										return "Unknown Author";
									else
										return optionalAuthor.get().getName();
								}).collect(Collectors.toList());
						book.setAutherNames(authernames);

					}
					System.out.println("Saving " + book.getName());
					bookRepository.save(book);

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@PostConstruct
	public void start() {
		// System.out.println(autherDumpLocation);
		// initAuthers();
		initWorks();
	}

	@Bean
	public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
		Path bundle = astraProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

}
