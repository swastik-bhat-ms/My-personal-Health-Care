package com.example.personalhealthcare;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Docdatabase.class, exportSchema = false, version = 5)
public abstract class Docdatahelper extends RoomDatabase {
	private static final String DOC_DATA = "Docdatabase";
	private static Docdatahelper instance;

	public static synchronized Docdatahelper getdoc(Context context) {
		if (instance == null) {
			instance = Room.databaseBuilder(context, Docdatahelper.class, DOC_DATA)
					 .fallbackToDestructiveMigration()
					 .allowMainThreadQueries()
					 .build();
		}
		return instance;
	}

	public abstract DocdataDao DocdataDao();
}
