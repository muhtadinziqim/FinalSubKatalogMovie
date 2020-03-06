package com.acer.example.katalogfilmsub2.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.acer.example.katalogfilmsub2.db.DatabaseContract.FavColumns.Companion.JUDUL
import com.acer.example.katalogfilmsub2.db.DatabaseContract.FavColumns.Companion.KATEGORI
import com.acer.example.katalogfilmsub2.db.DatabaseContract.FavColumns.Companion.TABLE_NAME
import com.acer.example.katalogfilmsub2.db.DatabaseContract.FavColumns.Companion._ID
import java.sql.SQLException

class FavHelper(context: Context) {

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: FavHelper? = null

        fun getInstance(context: Context): FavHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    /**
     * Ambil data dari semua note yang ada di dalam database
     *
     * @return cursor hasil queryAll
     */
    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    /**
     * Ambil data dari note berdasarakan parameter id
     *
     * @param id id note yang dicari
     * @return cursor hasil queryAll
     */
    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    fun queryByCat(cat: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$KATEGORI = ?",
            arrayOf(cat),
            null,
            null,
            null,
            null)
    }

    /**
     * Simpan data ke dalam database
     *
     * @param values nilai data yang akan di simpan
     * @return long id dari data yang baru saja di masukkan
     */
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    /**
     * Update data dalam database
     *
     * @param id     data dengan id berapa yang akan di update
     * @param values nilai data baru
     * @return int jumlah data yang ter-update
     */
    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    /**
     * Delete data dalam database
     *
     * @param id data dengan id berapa yang akan di delete
     * @return int jumlah data yang ter-delete
     */
    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$JUDUL = '$id'", null)
    }}