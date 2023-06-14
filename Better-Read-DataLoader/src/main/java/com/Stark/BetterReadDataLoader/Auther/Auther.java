package com.Stark.BetterReadDataLoader.Auther;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "auther_by_id")
public class Auther {

	@Id
	@PrimaryKeyColumn(name = "auther_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String Id;
	@Column("auther_name")
	@CassandraType(type = Name.TEXT)
	private String name;
	@Column("personal_name")
	@CassandraType(type = Name.TEXT)
	private String personalName;

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

	public String getPersonalName() {
		return personalName;
	}

	public void setPersonalName(String personalName) {
		this.personalName = personalName;
	}

	public void printAuther() {
		System.out.println(Id + " " + name + " " + personalName);
	}

}
