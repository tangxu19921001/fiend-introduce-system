package cn.zjut.entity;

/**
 * Relationship entity. @author MyEclipse Persistence Tools
 */

public class Relationship implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Integer user2;

	// Constructors

	/** default constructor */
	public Relationship() {
	}

	/** full constructor */
	public Relationship(User user, Integer user2) {
		this.user = user;
		this.user2 = user2;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getUser2() {
		return this.user2;
	}

	public void setUser2(Integer user2) {
		this.user2 = user2;
	}

}