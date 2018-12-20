package com.fanneng.filemanager.bean;

public class FileBean {
	
	private int id;
	private String name_collect;
	private String time_duration;
	private String time_create;
	private String file_path;
	private String unique_key;
	private String other;

	public FileBean() {
	}

	public FileBean(int id, String name_collect, String time_duration, String time_create, String file_path, String unique_key, String other) {
		this.id = id;
		this.name_collect = name_collect;
		this.time_duration = time_duration;
		this.time_create = time_create;
		this.file_path = file_path;
		this.unique_key = unique_key;
		this.other = other;
	}

	public String getUnique_key() {
		return unique_key;
	}

	public void setUnique_key(String unique_key) {
		this.unique_key = unique_key;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}





	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName_collect() {
		return name_collect;
	}

	public void setName_collect(String name_collect) {
		this.name_collect = name_collect;
	}

	public String getTime_duration() {
		return time_duration;
	}

	public void setTime_duration(String time_duration) {
		this.time_duration = time_duration;
	}

	public String getTime_create() {
		return time_create;
	}

	public void setTime_create(String time_create) {
		this.time_create = time_create;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	@Override
	public String toString() {
		return "FileBean{" +
				"id=" + id +
				", name_collect='" + name_collect + '\'' +
				", time_duration='" + time_duration + '\'' +
				", time_create='" + time_create + '\'' +
				", file_path='" + file_path + '\'' +
				", unique_key='" + unique_key + '\'' +
				", other='" + other + '\'' +
				'}';
	}
}
