package io.javabrains.Book;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "book_by_id")
public class Book {

	@Id
	@PrimaryKeyColumn(name = "book_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String Id;

	@Column("book_name")
	@CassandraType(type = Name.TEXT)
	private String name;
	@Column("book_description")
	@CassandraType(type = Name.TEXT)
	private String description;

	@Column("published_date")
	@CassandraType(type = Name.DATE)
	private LocalDate publishedDate;

	@Column("covers_id")
	@CassandraType(type = Name.LIST, typeArguments = Name.TEXT)
	private List<String> coversIds;

	@Column("auther_names")
	@CassandraType(type = Name.LIST, typeArguments = Name.TEXT)
	private List<String> autherNames;

	@Column("auther_id")
	@CassandraType(type = Name.LIST, typeArguments = Name.TEXT)
	private List<String> autherId;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(LocalDate publishedDate) {
		this.publishedDate = publishedDate;
	}

	public List<String> getCoversIds() {
		return coversIds;
	}

	public void setCoversIds(List<String> coversIds) {
		this.coversIds = coversIds;
	}

	public List<String> getAutherNames() {
		return autherNames;
	}

	public void setAutherNames(List<String> autherNames) {
		this.autherNames = autherNames;
	}

	public List<String> getAutherId() {
		return autherId;
	}

	public void setAutherId(List<String> autherId) {
		this.autherId = autherId;
	}

}
