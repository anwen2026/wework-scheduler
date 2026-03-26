package com.wework.scheduler.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.wework.scheduler.data.db.entities.AppConfig;
import com.wework.scheduler.data.db.entities.ConfigType;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppConfigDao_Impl implements AppConfigDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AppConfig> __insertionAdapterOfAppConfig;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<AppConfig> __deletionAdapterOfAppConfig;

  private final EntityDeletionOrUpdateAdapter<AppConfig> __updateAdapterOfAppConfig;

  private final SharedSQLiteStatement __preparedStmtOfUpdateValue;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByKey;

  public AppConfigDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAppConfig = new EntityInsertionAdapter<AppConfig>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `app_config` (`key`,`value`,`type`,`description`,`updatedAt`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppConfig entity) {
        statement.bindString(1, entity.getKey());
        statement.bindString(2, entity.getValue());
        final String _tmp = __converters.fromConfigType(entity.getType());
        statement.bindString(3, _tmp);
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        statement.bindLong(5, entity.getUpdatedAt());
      }
    };
    this.__deletionAdapterOfAppConfig = new EntityDeletionOrUpdateAdapter<AppConfig>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `app_config` WHERE `key` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppConfig entity) {
        statement.bindString(1, entity.getKey());
      }
    };
    this.__updateAdapterOfAppConfig = new EntityDeletionOrUpdateAdapter<AppConfig>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `app_config` SET `key` = ?,`value` = ?,`type` = ?,`description` = ?,`updatedAt` = ? WHERE `key` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppConfig entity) {
        statement.bindString(1, entity.getKey());
        statement.bindString(2, entity.getValue());
        final String _tmp = __converters.fromConfigType(entity.getType());
        statement.bindString(3, _tmp);
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        statement.bindLong(5, entity.getUpdatedAt());
        statement.bindString(6, entity.getKey());
      }
    };
    this.__preparedStmtOfUpdateValue = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE app_config SET value = ?, updatedAt = ? WHERE key = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByKey = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM app_config WHERE key = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final AppConfig config, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAppConfig.insert(config);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<AppConfig> configs,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAppConfig.insert(configs);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final AppConfig config, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfAppConfig.handle(config);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final AppConfig config, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfAppConfig.handle(config);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateValue(final String key, final String value, final long updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateValue.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, value);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 3;
        _stmt.bindString(_argIndex, key);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateValue.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByKey(final String key, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByKey.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, key);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteByKey.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getByKey(final String key, final Continuation<? super AppConfig> $completion) {
    final String _sql = "SELECT * FROM app_config WHERE key = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, key);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AppConfig>() {
      @Override
      @Nullable
      public AppConfig call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfKey = CursorUtil.getColumnIndexOrThrow(_cursor, "key");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final AppConfig _result;
          if (_cursor.moveToFirst()) {
            final String _tmpKey;
            _tmpKey = _cursor.getString(_cursorIndexOfKey);
            final String _tmpValue;
            _tmpValue = _cursor.getString(_cursorIndexOfValue);
            final ConfigType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toConfigType(_tmp);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new AppConfig(_tmpKey,_tmpValue,_tmpType,_tmpDescription,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAll(final Continuation<? super List<AppConfig>> $completion) {
    final String _sql = "SELECT * FROM app_config";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AppConfig>>() {
      @Override
      @NonNull
      public List<AppConfig> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfKey = CursorUtil.getColumnIndexOrThrow(_cursor, "key");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<AppConfig> _result = new ArrayList<AppConfig>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AppConfig _item;
            final String _tmpKey;
            _tmpKey = _cursor.getString(_cursorIndexOfKey);
            final String _tmpValue;
            _tmpValue = _cursor.getString(_cursorIndexOfValue);
            final ConfigType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toConfigType(_tmp);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new AppConfig(_tmpKey,_tmpValue,_tmpType,_tmpDescription,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AppConfig>> getAllFlow() {
    final String _sql = "SELECT * FROM app_config";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"app_config"}, new Callable<List<AppConfig>>() {
      @Override
      @NonNull
      public List<AppConfig> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfKey = CursorUtil.getColumnIndexOrThrow(_cursor, "key");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<AppConfig> _result = new ArrayList<AppConfig>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AppConfig _item;
            final String _tmpKey;
            _tmpKey = _cursor.getString(_cursorIndexOfKey);
            final String _tmpValue;
            _tmpValue = _cursor.getString(_cursorIndexOfValue);
            final ConfigType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toConfigType(_tmp);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new AppConfig(_tmpKey,_tmpValue,_tmpType,_tmpDescription,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getValue(final String key, final Continuation<? super String> $completion) {
    final String _sql = "SELECT value FROM app_config WHERE key = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, key);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<String>() {
      @Override
      @Nullable
      public String call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final String _result;
          if (_cursor.moveToFirst()) {
            if (_cursor.isNull(0)) {
              _result = null;
            } else {
              _result = _cursor.getString(0);
            }
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getString(final String key, final String defaultValue,
      final Continuation<? super String> $completion) {
    return AppConfigDao.DefaultImpls.getString(AppConfigDao_Impl.this, key, defaultValue, $completion);
  }

  @Override
  public Object getInt(final String key, final int defaultValue,
      final Continuation<? super Integer> $completion) {
    return AppConfigDao.DefaultImpls.getInt(AppConfigDao_Impl.this, key, defaultValue, $completion);
  }

  @Override
  public Object getLong(final String key, final long defaultValue,
      final Continuation<? super Long> $completion) {
    return AppConfigDao.DefaultImpls.getLong(AppConfigDao_Impl.this, key, defaultValue, $completion);
  }

  @Override
  public Object getBoolean(final String key, final boolean defaultValue,
      final Continuation<? super Boolean> $completion) {
    return AppConfigDao.DefaultImpls.getBoolean(AppConfigDao_Impl.this, key, defaultValue, $completion);
  }

  @Override
  public Object getFloat(final String key, final float defaultValue,
      final Continuation<? super Float> $completion) {
    return AppConfigDao.DefaultImpls.getFloat(AppConfigDao_Impl.this, key, defaultValue, $completion);
  }

  @Override
  public Object setString(final String key, final String value, final String description,
      final Continuation<? super Unit> $completion) {
    return AppConfigDao.DefaultImpls.setString(AppConfigDao_Impl.this, key, value, description, $completion);
  }

  @Override
  public Object setInt(final String key, final int value, final String description,
      final Continuation<? super Unit> $completion) {
    return AppConfigDao.DefaultImpls.setInt(AppConfigDao_Impl.this, key, value, description, $completion);
  }

  @Override
  public Object setLong(final String key, final long value, final String description,
      final Continuation<? super Unit> $completion) {
    return AppConfigDao.DefaultImpls.setLong(AppConfigDao_Impl.this, key, value, description, $completion);
  }

  @Override
  public Object setBoolean(final String key, final boolean value, final String description,
      final Continuation<? super Unit> $completion) {
    return AppConfigDao.DefaultImpls.setBoolean(AppConfigDao_Impl.this, key, value, description, $completion);
  }

  @Override
  public Object setFloat(final String key, final float value, final String description,
      final Continuation<? super Unit> $completion) {
    return AppConfigDao.DefaultImpls.setFloat(AppConfigDao_Impl.this, key, value, description, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
