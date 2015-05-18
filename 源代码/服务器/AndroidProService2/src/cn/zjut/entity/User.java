package cn.zjut.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class User implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String password;
	private String info;
	private Set locations = new HashSet(0);
	private Set relationships = new HashSet(0);

	// Constructors

	/** default constructor */
	public User() {
	}

	/** minimal constructor */
	public User(String name, String password) {
		this.name = name;
		this.password = password;
	}

	/** full constructor */
	public User(String name, String password, String info, Set locations,
			Set relationships) {
		this.name = name;
		this.password = password;
		this.info = info;
		this.locations = locations;
		this.relationships = relationships;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getInfo() {
		return this.info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Set getLocations() {
		return this.locations;
	}

	public void setLocations(Set locations) {
		this.locations = locations;
	}

	public Set getRelationships() {
		return this.relationships;
	}

	public void setRelationships(Set relationships) {
		this.relationships = relationships;
	}

}