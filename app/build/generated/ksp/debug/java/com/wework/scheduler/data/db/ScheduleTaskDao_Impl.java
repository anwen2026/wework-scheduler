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
import com.wework.scheduler.data.db.entities.ScheduleTask;
import com.wework.scheduler.data.db.entities.TaskStatus;
import java.lang.Class;
import java.lang.Exception;
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
public final class ScheduleTaskDao_Impl implements ScheduleTaskDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ScheduleTask> __insertionAdapterOfScheduleTask;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<ScheduleTask> __deletionAdapterOfScheduleTask;

  private final EntityDeletionOrUpdateAdapter<ScheduleTask> __updateAdapterOfScheduleTask;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStatus;

  private final SharedSQLiteStatement __preparedStmtOfUpdatePausedAt;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCompletedBefore;

  public ScheduleTaskDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfScheduleTask = new EntityInsertionAdapter<ScheduleTask>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `schedule_tasks` (`id`,`groupId`,`groupName`,`sendTime`,`intervalSeconds`,`status`,`sendPlanId`,`createdAt`,`updatedAt`,`pausedAt`,`commandMessageId`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduleTask entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getGroupId());
        statement.bindString(3, entity.getGroupName());
        statement.bindLong(4, entity.getSendTime());
        statement.bindLong(5, entity.getIntervalSeconds());
        final String _tmp = __converters.fromTaskStatus(entity.getStatus());
        statement.bindString(6, _tmp);
        if (entity.getSendPlanId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getSendPlanId());
        }
        statement.bindLong(8, entity.getCreatedAt());
        statement.bindLong(9, entity.getUpdatedAt());
        if (entity.getPausedAt() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getPausedAt());
        }
        if (entity.getCommandMessageId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getCommandMessageId());
        }
      }
    };
    this.__deletionAdapterOfScheduleTask = new EntityDeletionOrUpdateAdapter<ScheduleTask>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `schedule_tasks` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduleTask entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfScheduleTask = new EntityDeletionOrUpdateAdapter<ScheduleTask>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `schedule_tasks` SET `id` = ?,`groupId` = ?,`groupName` = ?,`sendTime` = ?,`intervalSeconds` = ?,`status` = ?,`sendPlanId` = ?,`createdAt` = ?,`updatedAt` = ?,`pausedAt` = ?,`commandMessageId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScheduleTask entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getGroupId());
        statement.bindString(3, entity.getGroupName());
        statement.bindLong(4, entity.getSendTime());
        statement.bindLong(5, entity.getIntervalSeconds());
        final String _tmp = __converters.fromTaskStatus(entity.getStatus());
        statement.bindString(6, _tmp);
        if (entity.getSendPlanId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getSendPlanId());
        }
        statement.bindLong(8, entity.getCreatedAt());
        statement.bindLong(9, entity.getUpdatedAt());
        if (entity.getPausedAt() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getPausedAt());
        }
        if (entity.getCommandMessageId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getCommandMessageId());
        }
        statement.bindLong(12, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE schedule_tasks SET status = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdatePausedAt = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE schedule_tasks SET pausedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteCompletedBefore = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM schedule_tasks WHERE status = 'COMPLETED' AND updatedAt < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final ScheduleTask task, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfScheduleTask.insertAndReturnId(task);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final ScheduleTask task, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfScheduleTask.handle(task);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final ScheduleTask task, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfScheduleTask.handle(task);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStatus(final long id, final TaskStatus status, final long updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStatus.acquire();
        int _argIndex = 1;
        final String _tmp = __converters.fromTaskStatus(status);
        _stmt.bindString(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, id);
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
          __preparedStmtOfUpdateStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePausedAt(final long id, final Long pausedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdatePausedAt.acquire();
        int _argIndex = 1;
        if (pausedAt == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, pausedAt);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
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
          __preparedStmtOfUpdatePausedAt.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCompletedBefore(final long beforeTime,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCompletedBefore.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, beforeTime);
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
          __preparedStmtOfDeleteCompletedBefore.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final long id, final Continuation<? super ScheduleTask> $completion) {
    final String _sql = "SELECT * FROM schedule_tasks WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ScheduleTask>() {
      @Override
      @Nullable
      public ScheduleTask call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfGroupName = CursorUtil.getColumnIndexOrThrow(_cursor, "groupName");
          final int _cursorIndexOfSendTime = CursorUtil.getColumnIndexOrThrow(_cursor, "sendTime");
          final int _cursorIndexOfIntervalSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalSeconds");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfSendPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "sendPlanId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfPausedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "pausedAt");
          final int _cursorIndexOfCommandMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "commandMessageId");
          final ScheduleTask _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpGroupName;
            _tmpGroupName = _cursor.getString(_cursorIndexOfGroupName);
            final long _tmpSendTime;
            _tmpSendTime = _cursor.getLong(_cursorIndexOfSendTime);
            final int _tmpIntervalSeconds;
            _tmpIntervalSeconds = _cursor.getInt(_cursorIndexOfIntervalSeconds);
            final TaskStatus _tmpStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toTaskStatus(_tmp);
            final Long _tmpSendPlanId;
            if (_cursor.isNull(_cursorIndexOfSendPlanId)) {
              _tmpSendPlanId = null;
            } else {
              _tmpSendPlanId = _cursor.getLong(_cursorIndexOfSendPlanId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Long _tmpPausedAt;
            if (_cursor.isNull(_cursorIndexOfPausedAt)) {
              _tmpPausedAt = null;
            } else {
              _tmpPausedAt = _cursor.getLong(_cursorIndexOfPausedAt);
            }
            final String _tmpCommandMessageId;
            if (_cursor.isNull(_cursorIndexOfCommandMessageId)) {
              _tmpCommandMessageId = null;
            } else {
              _tmpCommandMessageId = _cursor.getString(_cursorIndexOfCommandMessageId);
            }
            _result = new ScheduleTask(_tmpId,_tmpGroupId,_tmpGroupName,_tmpSendTime,_tmpIntervalSeconds,_tmpStatus,_tmpSendPlanId,_tmpCreatedAt,_tmpUpdatedAt,_tmpPausedAt,_tmpCommandMessageId);
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
  public Object getByGroupId(final String groupId,
      final Continuation<? super List<ScheduleTask>> $completion) {
    final String _sql = "SELECT * FROM schedule_tasks WHERE groupId = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, groupId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ScheduleTask>>() {
      @Override
      @NonNull
      public List<ScheduleTask> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfGroupName = CursorUtil.getColumnIndexOrThrow(_cursor, "groupName");
          final int _cursorIndexOfSendTime = CursorUtil.getColumnIndexOrThrow(_cursor, "sendTime");
          final int _cursorIndexOfIntervalSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalSeconds");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfSendPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "sendPlanId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfPausedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "pausedAt");
          final int _cursorIndexOfCommandMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "commandMessageId");
          final List<ScheduleTask> _result = new ArrayList<ScheduleTask>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScheduleTask _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpGroupName;
            _tmpGroupName = _cursor.getString(_cursorIndexOfGroupName);
            final long _tmpSendTime;
            _tmpSendTime = _cursor.getLong(_cursorIndexOfSendTime);
            final int _tmpIntervalSeconds;
            _tmpIntervalSeconds = _cursor.getInt(_cursorIndexOfIntervalSeconds);
            final TaskStatus _tmpStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toTaskStatus(_tmp);
            final Long _tmpSendPlanId;
            if (_cursor.isNull(_cursorIndexOfSendPlanId)) {
              _tmpSendPlanId = null;
            } else {
              _tmpSendPlanId = _cursor.getLong(_cursorIndexOfSendPlanId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Long _tmpPausedAt;
            if (_cursor.isNull(_cursorIndexOfPausedAt)) {
              _tmpPausedAt = null;
            } else {
              _tmpPausedAt = _cursor.getLong(_cursorIndexOfPausedAt);
            }
            final String _tmpCommandMessageId;
            if (_cursor.isNull(_cursorIndexOfCommandMessageId)) {
              _tmpCommandMessageId = null;
            } else {
              _tmpCommandMessageId = _cursor.getString(_cursorIndexOfCommandMessageId);
            }
            _item = new ScheduleTask(_tmpId,_tmpGroupId,_tmpGroupName,_tmpSendTime,_tmpIntervalSeconds,_tmpStatus,_tmpSendPlanId,_tmpCreatedAt,_tmpUpdatedAt,_tmpPausedAt,_tmpCommandMessageId);
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
  public Object getByStatus(final TaskStatus status,
      final Continuation<? super List<ScheduleTask>> $completion) {
    final String _sql = "SELECT * FROM schedule_tasks WHERE status = ? ORDER BY sendTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromTaskStatus(status);
    _statement.bindString(_argIndex, _tmp);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ScheduleTask>>() {
      @Override
      @NonNull
      public List<ScheduleTask> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfGroupName = CursorUtil.getColumnIndexOrThrow(_cursor, "groupName");
          final int _cursorIndexOfSendTime = CursorUtil.getColumnIndexOrThrow(_cursor, "sendTime");
          final int _cursorIndexOfIntervalSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalSeconds");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfSendPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "sendPlanId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfPausedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "pausedAt");
          final int _cursorIndexOfCommandMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "commandMessageId");
          final List<ScheduleTask> _result = new ArrayList<ScheduleTask>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScheduleTask _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpGroupName;
            _tmpGroupName = _cursor.getString(_cursorIndexOfGroupName);
            final long _tmpSendTime;
            _tmpSendTime = _cursor.getLong(_cursorIndexOfSendTime);
            final int _tmpIntervalSeconds;
            _tmpIntervalSeconds = _cursor.getInt(_cursorIndexOfIntervalSeconds);
            final TaskStatus _tmpStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toTaskStatus(_tmp_1);
            final Long _tmpSendPlanId;
            if (_cursor.isNull(_cursorIndexOfSendPlanId)) {
              _tmpSendPlanId = null;
            } else {
              _tmpSendPlanId = _cursor.getLong(_cursorIndexOfSendPlanId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Long _tmpPausedAt;
            if (_cursor.isNull(_cursorIndexOfPausedAt)) {
              _tmpPausedAt = null;
            } else {
              _tmpPausedAt = _cursor.getLong(_cursorIndexOfPausedAt);
            }
            final String _tmpCommandMessageId;
            if (_cursor.isNull(_cursorIndexOfCommandMessageId)) {
              _tmpCommandMessageId = null;
            } else {
              _tmpCommandMessageId = _cursor.getString(_cursorIndexOfCommandMessageId);
            }
            _item = new ScheduleTask(_tmpId,_tmpGroupId,_tmpGroupName,_tmpSendTime,_tmpIntervalSeconds,_tmpStatus,_tmpSendPlanId,_tmpCreatedAt,_tmpUpdatedAt,_tmpPausedAt,_tmpCommandMessageId);
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
  public Flow<List<ScheduleTask>> getAllFlow() {
    final String _sql = "SELECT * FROM schedule_tasks ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"schedule_tasks"}, new Callable<List<ScheduleTask>>() {
      @Override
      @NonNull
      public List<ScheduleTask> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfGroupName = CursorUtil.getColumnIndexOrThrow(_cursor, "groupName");
          final int _cursorIndexOfSendTime = CursorUtil.getColumnIndexOrThrow(_cursor, "sendTime");
          final int _cursorIndexOfIntervalSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalSeconds");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfSendPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "sendPlanId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfPausedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "pausedAt");
          final int _cursorIndexOfCommandMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "commandMessageId");
          final List<ScheduleTask> _result = new ArrayList<ScheduleTask>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScheduleTask _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpGroupName;
            _tmpGroupName = _cursor.getString(_cursorIndexOfGroupName);
            final long _tmpSendTime;
            _tmpSendTime = _cursor.getLong(_cursorIndexOfSendTime);
            final int _tmpIntervalSeconds;
            _tmpIntervalSeconds = _cursor.getInt(_cursorIndexOfIntervalSeconds);
            final TaskStatus _tmpStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toTaskStatus(_tmp);
            final Long _tmpSendPlanId;
            if (_cursor.isNull(_cursorIndexOfSendPlanId)) {
              _tmpSendPlanId = null;
            } else {
              _tmpSendPlanId = _cursor.getLong(_cursorIndexOfSendPlanId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Long _tmpPausedAt;
            if (_cursor.isNull(_cursorIndexOfPausedAt)) {
              _tmpPausedAt = null;
            } else {
              _tmpPausedAt = _cursor.getLong(_cursorIndexOfPausedAt);
            }
            final String _tmpCommandMessageId;
            if (_cursor.isNull(_cursorIndexOfCommandMessageId)) {
              _tmpCommandMessageId = null;
            } else {
              _tmpCommandMessageId = _cursor.getString(_cursorIndexOfCommandMessageId);
            }
            _item = new ScheduleTask(_tmpId,_tmpGroupId,_tmpGroupName,_tmpSendTime,_tmpIntervalSeconds,_tmpStatus,_tmpSendPlanId,_tmpCreatedAt,_tmpUpdatedAt,_tmpPausedAt,_tmpCommandMessageId);
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
  public Object getAll(final Continuation<? super List<ScheduleTask>> $completion) {
    final String _sql = "SELECT * FROM schedule_tasks ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ScheduleTask>>() {
      @Override
      @NonNull
      public List<ScheduleTask> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfGroupName = CursorUtil.getColumnIndexOrThrow(_cursor, "groupName");
          final int _cursorIndexOfSendTime = CursorUtil.getColumnIndexOrThrow(_cursor, "sendTime");
          final int _cursorIndexOfIntervalSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalSeconds");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfSendPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "sendPlanId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfPausedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "pausedAt");
          final int _cursorIndexOfCommandMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "commandMessageId");
          final List<ScheduleTask> _result = new ArrayList<ScheduleTask>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScheduleTask _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpGroupName;
            _tmpGroupName = _cursor.getString(_cursorIndexOfGroupName);
            final long _tmpSendTime;
            _tmpSendTime = _cursor.getLong(_cursorIndexOfSendTime);
            final int _tmpIntervalSeconds;
            _tmpIntervalSeconds = _cursor.getInt(_cursorIndexOfIntervalSeconds);
            final TaskStatus _tmpStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toTaskStatus(_tmp);
            final Long _tmpSendPlanId;
            if (_cursor.isNull(_cursorIndexOfSendPlanId)) {
              _tmpSendPlanId = null;
            } else {
              _tmpSendPlanId = _cursor.getLong(_cursorIndexOfSendPlanId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Long _tmpPausedAt;
            if (_cursor.isNull(_cursorIndexOfPausedAt)) {
              _tmpPausedAt = null;
            } else {
              _tmpPausedAt = _cursor.getLong(_cursorIndexOfPausedAt);
            }
            final String _tmpCommandMessageId;
            if (_cursor.isNull(_cursorIndexOfCommandMessageId)) {
              _tmpCommandMessageId = null;
            } else {
              _tmpCommandMessageId = _cursor.getString(_cursorIndexOfCommandMessageId);
            }
            _item = new ScheduleTask(_tmpId,_tmpGroupId,_tmpGroupName,_tmpSendTime,_tmpIntervalSeconds,_tmpStatus,_tmpSendPlanId,_tmpCreatedAt,_tmpUpdatedAt,_tmpPausedAt,_tmpCommandMessageId);
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
  public Object getActiveTasksBeforeTime(final long time,
      final Continuation<? super List<ScheduleTask>> $completion) {
    final String _sql = "SELECT * FROM schedule_tasks WHERE status IN ('PENDING', 'RUNNING') AND sendTime <= ? ORDER BY sendTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, time);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ScheduleTask>>() {
      @Override
      @NonNull
      public List<ScheduleTask> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfGroupName = CursorUtil.getColumnIndexOrThrow(_cursor, "groupName");
          final int _cursorIndexOfSendTime = CursorUtil.getColumnIndexOrThrow(_cursor, "sendTime");
          final int _cursorIndexOfIntervalSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalSeconds");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfSendPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "sendPlanId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfPausedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "pausedAt");
          final int _cursorIndexOfCommandMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "commandMessageId");
          final List<ScheduleTask> _result = new ArrayList<ScheduleTask>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScheduleTask _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpGroupName;
            _tmpGroupName = _cursor.getString(_cursorIndexOfGroupName);
            final long _tmpSendTime;
            _tmpSendTime = _cursor.getLong(_cursorIndexOfSendTime);
            final int _tmpIntervalSeconds;
            _tmpIntervalSeconds = _cursor.getInt(_cursorIndexOfIntervalSeconds);
            final TaskStatus _tmpStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toTaskStatus(_tmp);
            final Long _tmpSendPlanId;
            if (_cursor.isNull(_cursorIndexOfSendPlanId)) {
              _tmpSendPlanId = null;
            } else {
              _tmpSendPlanId = _cursor.getLong(_cursorIndexOfSendPlanId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Long _tmpPausedAt;
            if (_cursor.isNull(_cursorIndexOfPausedAt)) {
              _tmpPausedAt = null;
            } else {
              _tmpPausedAt = _cursor.getLong(_cursorIndexOfPausedAt);
            }
            final String _tmpCommandMessageId;
            if (_cursor.isNull(_cursorIndexOfCommandMessageId)) {
              _tmpCommandMessageId = null;
            } else {
              _tmpCommandMessageId = _cursor.getString(_cursorIndexOfCommandMessageId);
            }
            _item = new ScheduleTask(_tmpId,_tmpGroupId,_tmpGroupName,_tmpSendTime,_tmpIntervalSeconds,_tmpStatus,_tmpSendPlanId,_tmpCreatedAt,_tmpUpdatedAt,_tmpPausedAt,_tmpCommandMessageId);
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
  public Object countByStatus(final TaskStatus status,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM schedule_tasks WHERE status = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromTaskStatus(status);
    _statement.bindString(_argIndex, _tmp);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(0);
            _result = _tmp_1;
          } else {
            _result = 0;
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
  public Object getLatestActiveTask(final String groupId,
      final Continuation<? super ScheduleTask> $completion) {
    final String _sql = "SELECT * FROM schedule_tasks WHERE groupId = ? AND status NOT IN ('COMPLETED', 'CANCELLED') ORDER BY createdAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, groupId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ScheduleTask>() {
      @Override
      @Nullable
      public ScheduleTask call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfGroupId = CursorUtil.getColumnIndexOrThrow(_cursor, "groupId");
          final int _cursorIndexOfGroupName = CursorUtil.getColumnIndexOrThrow(_cursor, "groupName");
          final int _cursorIndexOfSendTime = CursorUtil.getColumnIndexOrThrow(_cursor, "sendTime");
          final int _cursorIndexOfIntervalSeconds = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalSeconds");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfSendPlanId = CursorUtil.getColumnIndexOrThrow(_cursor, "sendPlanId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfPausedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "pausedAt");
          final int _cursorIndexOfCommandMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "commandMessageId");
          final ScheduleTask _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpGroupId;
            _tmpGroupId = _cursor.getString(_cursorIndexOfGroupId);
            final String _tmpGroupName;
            _tmpGroupName = _cursor.getString(_cursorIndexOfGroupName);
            final long _tmpSendTime;
            _tmpSendTime = _cursor.getLong(_cursorIndexOfSendTime);
            final int _tmpIntervalSeconds;
            _tmpIntervalSeconds = _cursor.getInt(_cursorIndexOfIntervalSeconds);
            final TaskStatus _tmpStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toTaskStatus(_tmp);
            final Long _tmpSendPlanId;
            if (_cursor.isNull(_cursorIndexOfSendPlanId)) {
              _tmpSendPlanId = null;
            } else {
              _tmpSendPlanId = _cursor.getLong(_cursorIndexOfSendPlanId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Long _tmpPausedAt;
            if (_cursor.isNull(_cursorIndexOfPausedAt)) {
              _tmpPausedAt = null;
            } else {
              _tmpPausedAt = _cursor.getLong(_cursorIndexOfPausedAt);
            }
            final String _tmpCommandMessageId;
            if (_cursor.isNull(_cursorIndexOfCommandMessageId)) {
              _tmpCommandMessageId = null;
            } else {
              _tmpCommandMessageId = _cursor.getString(_cursorIndexOfCommandMessageId);
            }
            _result = new ScheduleTask(_tmpId,_tmpGroupId,_tmpGroupName,_tmpSendTime,_tmpIntervalSeconds,_tmpStatus,_tmpSendPlanId,_tmpCreatedAt,_tmpUpdatedAt,_tmpPausedAt,_tmpCommandMessageId);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
