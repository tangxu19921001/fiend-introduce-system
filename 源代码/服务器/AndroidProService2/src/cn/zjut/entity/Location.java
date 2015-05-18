package cn.zjut.entity;

import java.sql.Timestamp;
import java.util.Date;
/**
 * Location entity. @author MyEclipse Persistence Tools
 */

public class Location implements java.io.Serializable {

	// Fields

	private Integer id;
	private User user;
	private Integer turn;
	private Float latitude;
	private Float longitude;
	private Date time;

	// Constructors

	/** default constructor */
	public Location() {
	}

	/** full constructor */
	public Location(User user, Integer turn, Float latitude, Float longitude,
			Date time) {
		this.user = user;
		this.turn = turn;
		this.latitude = latitude;
		this.longitude = longitude;
		this.time = time;
	  
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

	public Integer getTurn() {
		return this.turn;
	}

	public void setTurn(Integer turn) {
		this.turn = turn;
	}

	public Float getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}