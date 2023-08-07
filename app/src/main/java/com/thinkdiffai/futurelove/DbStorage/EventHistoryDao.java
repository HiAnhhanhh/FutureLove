package com.thinkdiffai.futurelove.DbStorage;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.thinkdiffai.futurelove.model.EventHomeDto;

import java.util.List;


@Dao
public interface EventHistoryDao {
    @Insert
    void insert(EventHomeDto event);

    @Query("SELECT * FROM event_history")
    List<EventHomeDto> getAllEvent();

//
//    @Query("SELECT * FROM chapter_download WHERE name_chapter = :name")
//    ChapterDownload getChapterByName(String name);
//
    @Query("SELECT * FROM event_history WHERE numberOrder = :number_id")
    EventHomeDto getEventByOrderNumber_id(int number_id);
//
//    @Query("SELECT * FROM chapter_download WHERE name_chapter = :name AND manga_id = :id")
//    ChapterDownload getChaptersByNameAndMangaId(String name, int id);


    @Delete
    void delete(EventHomeDto event);

    @Update
    void update(EventHomeDto event);


    @Query("Delete from event_history WHERE id = :id")
    void delete(int id);



}
