package com.akka.fix1.DbPersistenceService;

import java.io.Serializable;

public class UserDetail implements Serializable{
	
	private String id;
	private String name;
	private int age;
	private String sex;
	private String company;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String toString(){
		return 	"UserDetails{" +
        "name='" + name + '\'' +
        ", Age='" + age + '\'' +
        ", sex='" + sex + '\'' +
        ", company='" + company + '\'' +
        '}';
		
	}

}
