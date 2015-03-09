package com.pushtorefresh.android.bamboostorage;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.android.bamboostorage.operation.delete.PreparedDelete;
import com.pushtorefresh.android.bamboostorage.operation.exec_sql.PreparedExecSql;
import com.pushtorefresh.android.bamboostorage.operation.get.PreparedGet;
import com.pushtorefresh.android.bamboostorage.operation.put.PreparedPut;
import com.pushtorefresh.android.bamboostorage.query.DeleteQuery;
import com.pushtorefresh.android.bamboostorage.query.InsertQuery;
import com.pushtorefresh.android.bamboostorage.query.Query;
import com.pushtorefresh.android.bamboostorage.query.RawQuery;
import com.pushtorefresh.android.bamboostorage.query.UpdateQuery;

import java.util.Set;

import rx.Observable;

public interface BambooStorage {

    /**
     * Prepares "execute sql" operation for BambooStorage
     * Allows to execute a single SQL statement that is NOT a SELECT/INSERT/UPDATE/DELETE.
     *
     * @return builder for PreparedExecSql
     */
    @NonNull PreparedExecSql.Builder execSql();

    /**
     * Prepares "get" operation for BambooStorage
     * Allows to get information from BambooStorage
     *
     * @return builder for PreparedGet
     */
    @NonNull PreparedGet.Builder get();

    /**
     * Prepares "put" operation for BambooStorage
     * Allows to insert/update information in BambooStorage
     *
     * @return builder for PreparedPut
     */
    @NonNull PreparedPut.Builder put();

    /**
     * Prepares "delete" operation for BambooStorage
     * Allows to delete information from BambooStorage
     *
     * @return builder for PreparedDelete
     */
    @NonNull PreparedDelete.Builder delete();

    /**
     * Subscribes on changes in required tables
     *
     * @param tables set of tables that should be monitored
     * @return {@link rx.Observable} subscribed on changes in required tables
     */
    @NonNull Observable<Set<String>> subscribeOnChanges(@NonNull Set<String> tables);

    /**
     * Hides some internal operations for BambooStorage to make API of BambooStorage clean and easy to understand
     *
     * @return implementation of Internal operations for BambooStorage
     */
    @NonNull Internal internal();

    /**
     * Hides some internal operations for BambooStorage to make API of BambooStorage clean and easy to understand
     */
    interface Internal {

        /**
         * Execute a single SQL statement that is NOT a SELECT/INSERT/UPDATE/DELETE on the database
         *
         * @param rawQuery sql query
         */
        void execSql(@NonNull RawQuery rawQuery);

        /**
         * Executes raw query on the database and returns {@link android.database.Cursor} over the result set
         *
         * @param rawQuery sql query
         * @return A Cursor object, which is positioned before the first entry. Note that Cursors are not synchronized, see the documentation for more details.
         */
        @NonNull Cursor rawQuery(@NonNull RawQuery rawQuery);

        /**
         * Executes query on the database and returns {@link android.database.Cursor} over the result set
         *
         * @param query sql query
         * @return A Cursor object, which is positioned before the first entry. Note that Cursors are not synchronized, see the documentation for more details.
         */
        @NonNull Cursor query(@NonNull Query query);

        /**
         * Inserts a row into the database
         *
         * @param insertQuery   query
         * @param contentValues map that contains the initial column values for the row. The keys should be the column names and the values the column values
         * @return id of inserted row
         */
        long insert(@NonNull InsertQuery insertQuery, @NonNull ContentValues contentValues);

        /**
         * Updates one or multiple rows in the database
         *
         * @param updateQuery   query
         * @param contentValues a map from column names to new column values. null is a valid value that will be translated to NULL.
         * @return the number of rows affected
         */
        int update(@NonNull UpdateQuery updateQuery, @NonNull ContentValues contentValues);

        /**
         * Deletes one or multiple rows in the database
         *
         * @param deleteQuery query
         * @return the number of rows deleted
         */
        int delete(@NonNull DeleteQuery deleteQuery);

        /**
         * Notifies subscribers about change in set of tables
         * One operation can affect multiple tables, to reduce number of notifications
         * you can call this method once and provide set of tables that were changed
         *
         * @param affectedTables set of affected tables
         */
        void notifyAboutChanges(@NonNull Set<String> affectedTables);

        /**
         * BambooStorage implementation could not provide support for transactions
         *
         * @return true if transactions are supported, false otherwise
         */
        boolean areTransactionsSupported();

        /**
         * Begins a transaction in EXCLUSIVE mode
         */
        void beginTransaction();

        /**
         * Marks the current transaction as successful
         */
        void setTransactionSuccessful();

        /**
         * End a transaction
         */
        void endTransaction();
    }
}