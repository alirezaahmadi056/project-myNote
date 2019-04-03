package application.ahmadi.mynote

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DBManager {
    val dbName = "NotesDB"
    val dbTable = "tblNotes"
    private val colID = "ID"
    private val colTitle = "Title"
    private val colDes = "Description"
    val dbVersion = 1

    val sqlCreateTable = "CREATE TABLE IF NOT EXISTS $dbTable ($colID INTEGER PRIMARY KEY, $colTitle VARCHAR,$colDes TEXT);"

    private var sqlDB: SQLiteDatabase? = null

    constructor(context: Context) {
        val db = DatabaseHelper(context)
        sqlDB = db.writableDatabase
    }

    inner class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, dbName, null, dbVersion) {

        private var context: Context? = context

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(sqlCreateTable)
            Toast.makeText(context, "Create DataBase", Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL("DROP TABLE IF EXISTS $dbTable")
        }

    }

    fun insert(values: ContentValues): Long {
        return sqlDB!!.insert(dbTable, "", values)
    }

    fun runQuery(columns: Array<String>, selection: String, selectionArgs: Array<String>, sortOrder: String): Cursor {
        val qb = SQLiteQueryBuilder()
        qb.tables = dbTable
        val cursor = qb.query(sqlDB, columns, selection, selectionArgs, null, null, sortOrder)
        return cursor
    }

    fun delete(selection: String, selectionArgs: Array<String>): Int {
        val count = sqlDB?.delete(dbTable, selection, selectionArgs)
        return count!!
    }

    fun update(values: ContentValues, selection: String, selectionArgs: Array<String>): Int {
        val count = sqlDB?.update(dbTable, values, selection, selectionArgs)
        return count!!
    }

}