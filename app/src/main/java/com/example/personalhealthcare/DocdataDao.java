package com.example.personalhealthcare;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DocdataDao {
	@Query("Select * from Docdatabase")
	List<Docdatabase> getThese();


	@Insert
	void insertall(ArrayList<Docdatabase> docdatabase);

	@Update
	void updateall(ArrayList<Docdatabase> docdatabase);

	@Delete
	void deletall(ArrayList<Docdatabase> docdatabase);
}
