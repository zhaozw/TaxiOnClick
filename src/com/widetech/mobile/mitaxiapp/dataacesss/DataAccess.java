package com.widetech.mobile.mitaxiapp.dataacesss;

public interface DataAccess {

	<T> long create(T obj);

	<T> T read(int id);

	<T> T read();

	void delete();

	void delete(int id);

}
