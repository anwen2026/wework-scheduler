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
import com.wework.scheduler.data.db.entities.MessageQueue;
import com.wework.scheduler.data.db.entities.MessageStatus;
import com.wework.scheduler.data.db.entities.MessageType;
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
public final class MessageQueueDao_Impl implements MessageQueueDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MessageQueue> __insertionAdapterOfMessageQueue;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<MessageQueue> __deletionAdapterOfMessageQueue;

  private final EntityDeletionOrUpdateAdapter<MessageQueue> __updateAdapterOfMessageQueue;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStatus;

  private final SharedSQLiteStatement __preparedStmtOfMarkSkippedBetween;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByTaskId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfAdjustScheduledTime;

  public MessageQueueDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMessageQueue = new EntityInsertionAdapter<MessageQueue>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `message_queue` (`id`,`taskId`,`messageType`,`content`,`extraData`,`order`,`status`,`scheduledTime`,`sentAt`,`originalMessageId`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MessageQueue entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTaskId());
        final String _tmp = __converters.fromMessageType(entity.getMessageType());
        statement.bindString(3, _tmp);
        statement.bindString(4, entity.getContent());
        if (entity.getExtraData() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getExtraData());
        }
        statement.bindLong(6, entity.getOrder());
        final String _tmp_1 = __converters.fromMessageStatus(entity.getStatus());
        statement.bindString(7, _tmp_1);
        statement.bindLong(8, entity.getScheduledTime());
        if (entity.getSentAt() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getSentAt());
        }
        if (entity.getOriginalMessageId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getOriginalMessageId());
        }
        statement.bindLong(11, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfMessageQueue = new EntityDeletionOrUpdateAdapter<MessageQueue>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `message_queue` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MessageQueue entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfMessageQueue = new EntityDeletionOrUpdateAdapter<MessageQueue>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `message_queue` SET `id` = ?,`taskId` = ?,`messageType` = ?,`content` = ?,`extraData` = ?,`order` = ?,`status` = ?,`scheduledTime` = ?,`sentAt` = ?,`originalMessageId` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MessageQueue entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTaskId());
        final String _tmp = __converters.fromMessageType(entity.getMessageType());
        statement.bindString(3, _tmp);
        statement.bindString(4, entity.getContent());
        if (entity.getExtraData() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getExtraData());
        }
        statement.bindLong(6, entity.getOrder());
        final String _tmp_1 = __converters.fromMessageStatus(entity.getStatus());
        statement.bindString(7, _tmp_1);
        statement.bindLong(8, entity.getScheduledTime());
        if (entity.getSentAt() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getSentAt());
        }
        if (entity.getOriginalMessageId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getOriginalMessageId());
        }
        statement.bindLong(11, entity.getCreatedAt());
        statement.bindLong(12, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE message_queue SET status = ?, sentAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkSkippedBetween = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE message_queue SET status = ? WHERE taskId = ? AND scheduledTime BETWEEN ? AND ? AND status = 'PENDING'";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByTaskId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM message_queue WHERE taskId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM message_queue WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfAdjustScheduledTime = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE message_queue SET scheduledTime = scheduledTime + ? WHERE taskId = ? AND status = 'PENDING'";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final MessageQueue message, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMessageQueue.insertAndReturnId(message);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<MessageQueue> messages,
      final Continuation<? super List<Long>> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<List<Long>>() {
      @Override
      @NonNull
      public List<Long> call() throws Exception {
        __db.beginTransaction();
        try {
          final List<Long> _result = __insertionAdapterOfMessageQueue.insertAndReturnIdsList(messages);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final MessageQueue message, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMessageQueue.handle(message);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final MessageQueue message, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMessageQueue.handle(message);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStatus(final long id, final MessageStatus status, final Long sentAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStatus.acquire();
        int _argIndex = 1;
        final String _tmp = __converters.fromMessageStatus(status);
        _stmt.bindString(_argIndex, _tmp);
        _argIndex = 2;
        if (sentAt == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, sentAt);
        }
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
  public Object markSkippedBetween(final long taskId, final long startTime, final long endTime,
      final MessageStatus status, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkSkippedBetween.acquire();
        int _argIndex = 1;
        final String _tmp = __converters.fromMessageStatus(status);
        _stmt.bindString(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, taskId);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, startTime);
        _argIndex = 4;
        _stmt.bindLong(_argIndex, endTime);
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
          __preparedStmtOfMarkSkippedBetween.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByTaskId(final long taskId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByTaskId.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, taskId);
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
          __preparedStmtOfDeleteByTaskId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
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
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object adjustScheduledTime(final long taskId, final long offsetMillis,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfAdjustScheduledTime.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, offsetMillis);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, taskId);
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
          __preparedStmtOfAdjustScheduledTime.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final long id, final Continuation<? super MessageQueue> $completion) {
    final String _sql = "SELECT * FROM message_queue WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MessageQueue>() {
      @Override
      @Nullable
      public MessageQueue call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "taskId");
          final int _cursorIndexOfMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "messageType");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfExtraData = CursorUtil.getColumnIndexOrThrow(_cursor, "extraData");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfSentAt = CursorUtil.getColumnIndexOrThrow(_cursor, "sentAt");
          final int _cursorIndexOfOriginalMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "originalMessageId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final MessageQueue _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTaskId;
            _tmpTaskId = _cursor.getLong(_cursorIndexOfTaskId);
            final MessageType _tmpMessageType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfMessageType);
            _tmpMessageType = __converters.toMessageType(_tmp);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpExtraData;
            if (_cursor.isNull(_cursorIndexOfExtraData)) {
              _tmpExtraData = null;
            } else {
              _tmpExtraData = _cursor.getString(_cursorIndexOfExtraData);
            }
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final MessageStatus _tmpStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toMessageStatus(_tmp_1);
            final long _tmpScheduledTime;
            _tmpScheduledTime = _cursor.getLong(_cursorIndexOfScheduledTime);
            final Long _tmpSentAt;
            if (_cursor.isNull(_cursorIndexOfSentAt)) {
              _tmpSentAt = null;
            } else {
              _tmpSentAt = _cursor.getLong(_cursorIndexOfSentAt);
            }
            final String _tmpOriginalMessageId;
            if (_cursor.isNull(_cursorIndexOfOriginalMessageId)) {
              _tmpOriginalMessageId = null;
            } else {
              _tmpOriginalMessageId = _cursor.getString(_cursorIndexOfOriginalMessageId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new MessageQueue(_tmpId,_tmpTaskId,_tmpMessageType,_tmpContent,_tmpExtraData,_tmpOrder,_tmpStatus,_tmpScheduledTime,_tmpSentAt,_tmpOriginalMessageId,_tmpCreatedAt);
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
  public Object getByTaskId(final long taskId,
      final Continuation<? super List<MessageQueue>> $completion) {
    final String _sql = "SELECT * FROM message_queue WHERE taskId = ? ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, taskId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MessageQueue>>() {
      @Override
      @NonNull
      public List<MessageQueue> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "taskId");
          final int _cursorIndexOfMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "messageType");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfExtraData = CursorUtil.getColumnIndexOrThrow(_cursor, "extraData");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfSentAt = CursorUtil.getColumnIndexOrThrow(_cursor, "sentAt");
          final int _cursorIndexOfOriginalMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "originalMessageId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<MessageQueue> _result = new ArrayList<MessageQueue>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageQueue _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTaskId;
            _tmpTaskId = _cursor.getLong(_cursorIndexOfTaskId);
            final MessageType _tmpMessageType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfMessageType);
            _tmpMessageType = __converters.toMessageType(_tmp);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpExtraData;
            if (_cursor.isNull(_cursorIndexOfExtraData)) {
              _tmpExtraData = null;
            } else {
              _tmpExtraData = _cursor.getString(_cursorIndexOfExtraData);
            }
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final MessageStatus _tmpStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toMessageStatus(_tmp_1);
            final long _tmpScheduledTime;
            _tmpScheduledTime = _cursor.getLong(_cursorIndexOfScheduledTime);
            final Long _tmpSentAt;
            if (_cursor.isNull(_cursorIndexOfSentAt)) {
              _tmpSentAt = null;
            } else {
              _tmpSentAt = _cursor.getLong(_cursorIndexOfSentAt);
            }
            final String _tmpOriginalMessageId;
            if (_cursor.isNull(_cursorIndexOfOriginalMessageId)) {
              _tmpOriginalMessageId = null;
            } else {
              _tmpOriginalMessageId = _cursor.getString(_cursorIndexOfOriginalMessageId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new MessageQueue(_tmpId,_tmpTaskId,_tmpMessageType,_tmpContent,_tmpExtraData,_tmpOrder,_tmpStatus,_tmpScheduledTime,_tmpSentAt,_tmpOriginalMessageId,_tmpCreatedAt);
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
  public Flow<List<MessageQueue>> getByTaskIdFlow(final long taskId) {
    final String _sql = "SELECT * FROM message_queue WHERE taskId = ? ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, taskId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"message_queue"}, new Callable<List<MessageQueue>>() {
      @Override
      @NonNull
      public List<MessageQueue> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "taskId");
          final int _cursorIndexOfMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "messageType");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfExtraData = CursorUtil.getColumnIndexOrThrow(_cursor, "extraData");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfSentAt = CursorUtil.getColumnIndexOrThrow(_cursor, "sentAt");
          final int _cursorIndexOfOriginalMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "originalMessageId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<MessageQueue> _result = new ArrayList<MessageQueue>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageQueue _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTaskId;
            _tmpTaskId = _cursor.getLong(_cursorIndexOfTaskId);
            final MessageType _tmpMessageType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfMessageType);
            _tmpMessageType = __converters.toMessageType(_tmp);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpExtraData;
            if (_cursor.isNull(_cursorIndexOfExtraData)) {
              _tmpExtraData = null;
            } else {
              _tmpExtraData = _cursor.getString(_cursorIndexOfExtraData);
            }
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final MessageStatus _tmpStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toMessageStatus(_tmp_1);
            final long _tmpScheduledTime;
            _tmpScheduledTime = _cursor.getLong(_cursorIndexOfScheduledTime);
            final Long _tmpSentAt;
            if (_cursor.isNull(_cursorIndexOfSentAt)) {
              _tmpSentAt = null;
            } else {
              _tmpSentAt = _cursor.getLong(_cursorIndexOfSentAt);
            }
            final String _tmpOriginalMessageId;
            if (_cursor.isNull(_cursorIndexOfOriginalMessageId)) {
              _tmpOriginalMessageId = null;
            } else {
              _tmpOriginalMessageId = _cursor.getString(_cursorIndexOfOriginalMessageId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new MessageQueue(_tmpId,_tmpTaskId,_tmpMessageType,_tmpContent,_tmpExtraData,_tmpOrder,_tmpStatus,_tmpScheduledTime,_tmpSentAt,_tmpOriginalMessageId,_tmpCreatedAt);
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
  public Object getByTaskIdAndStatus(final long taskId, final MessageStatus status,
      final Continuation<? super List<MessageQueue>> $completion) {
    final String _sql = "SELECT * FROM message_queue WHERE taskId = ? AND status = ? ORDER BY `order` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, taskId);
    _argIndex = 2;
    final String _tmp = __converters.fromMessageStatus(status);
    _statement.bindString(_argIndex, _tmp);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MessageQueue>>() {
      @Override
      @NonNull
      public List<MessageQueue> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "taskId");
          final int _cursorIndexOfMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "messageType");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfExtraData = CursorUtil.getColumnIndexOrThrow(_cursor, "extraData");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfSentAt = CursorUtil.getColumnIndexOrThrow(_cursor, "sentAt");
          final int _cursorIndexOfOriginalMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "originalMessageId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<MessageQueue> _result = new ArrayList<MessageQueue>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageQueue _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTaskId;
            _tmpTaskId = _cursor.getLong(_cursorIndexOfTaskId);
            final MessageType _tmpMessageType;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfMessageType);
            _tmpMessageType = __converters.toMessageType(_tmp_1);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpExtraData;
            if (_cursor.isNull(_cursorIndexOfExtraData)) {
              _tmpExtraData = null;
            } else {
              _tmpExtraData = _cursor.getString(_cursorIndexOfExtraData);
            }
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final MessageStatus _tmpStatus;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toMessageStatus(_tmp_2);
            final long _tmpScheduledTime;
            _tmpScheduledTime = _cursor.getLong(_cursorIndexOfScheduledTime);
            final Long _tmpSentAt;
            if (_cursor.isNull(_cursorIndexOfSentAt)) {
              _tmpSentAt = null;
            } else {
              _tmpSentAt = _cursor.getLong(_cursorIndexOfSentAt);
            }
            final String _tmpOriginalMessageId;
            if (_cursor.isNull(_cursorIndexOfOriginalMessageId)) {
              _tmpOriginalMessageId = null;
            } else {
              _tmpOriginalMessageId = _cursor.getString(_cursorIndexOfOriginalMessageId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new MessageQueue(_tmpId,_tmpTaskId,_tmpMessageType,_tmpContent,_tmpExtraData,_tmpOrder,_tmpStatus,_tmpScheduledTime,_tmpSentAt,_tmpOriginalMessageId,_tmpCreatedAt);
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
  public Object getPendingMessagesBeforeTime(final long time,
      final Continuation<? super List<MessageQueue>> $completion) {
    final String _sql = "SELECT * FROM message_queue WHERE status = 'PENDING' AND scheduledTime <= ? ORDER BY scheduledTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, time);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MessageQueue>>() {
      @Override
      @NonNull
      public List<MessageQueue> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "taskId");
          final int _cursorIndexOfMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "messageType");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfExtraData = CursorUtil.getColumnIndexOrThrow(_cursor, "extraData");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfSentAt = CursorUtil.getColumnIndexOrThrow(_cursor, "sentAt");
          final int _cursorIndexOfOriginalMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "originalMessageId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<MessageQueue> _result = new ArrayList<MessageQueue>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageQueue _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTaskId;
            _tmpTaskId = _cursor.getLong(_cursorIndexOfTaskId);
            final MessageType _tmpMessageType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfMessageType);
            _tmpMessageType = __converters.toMessageType(_tmp);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpExtraData;
            if (_cursor.isNull(_cursorIndexOfExtraData)) {
              _tmpExtraData = null;
            } else {
              _tmpExtraData = _cursor.getString(_cursorIndexOfExtraData);
            }
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final MessageStatus _tmpStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toMessageStatus(_tmp_1);
            final long _tmpScheduledTime;
            _tmpScheduledTime = _cursor.getLong(_cursorIndexOfScheduledTime);
            final Long _tmpSentAt;
            if (_cursor.isNull(_cursorIndexOfSentAt)) {
              _tmpSentAt = null;
            } else {
              _tmpSentAt = _cursor.getLong(_cursorIndexOfSentAt);
            }
            final String _tmpOriginalMessageId;
            if (_cursor.isNull(_cursorIndexOfOriginalMessageId)) {
              _tmpOriginalMessageId = null;
            } else {
              _tmpOriginalMessageId = _cursor.getString(_cursorIndexOfOriginalMessageId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new MessageQueue(_tmpId,_tmpTaskId,_tmpMessageType,_tmpContent,_tmpExtraData,_tmpOrder,_tmpStatus,_tmpScheduledTime,_tmpSentAt,_tmpOriginalMessageId,_tmpCreatedAt);
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
  public Object getPendingMessagesBeforeTimeByTask(final long taskId, final long time,
      final Continuation<? super List<MessageQueue>> $completion) {
    final String _sql = "SELECT * FROM message_queue WHERE taskId = ? AND status = 'PENDING' AND scheduledTime <= ? ORDER BY scheduledTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, taskId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, time);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MessageQueue>>() {
      @Override
      @NonNull
      public List<MessageQueue> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "taskId");
          final int _cursorIndexOfMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "messageType");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfExtraData = CursorUtil.getColumnIndexOrThrow(_cursor, "extraData");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfSentAt = CursorUtil.getColumnIndexOrThrow(_cursor, "sentAt");
          final int _cursorIndexOfOriginalMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "originalMessageId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<MessageQueue> _result = new ArrayList<MessageQueue>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageQueue _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTaskId;
            _tmpTaskId = _cursor.getLong(_cursorIndexOfTaskId);
            final MessageType _tmpMessageType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfMessageType);
            _tmpMessageType = __converters.toMessageType(_tmp);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpExtraData;
            if (_cursor.isNull(_cursorIndexOfExtraData)) {
              _tmpExtraData = null;
            } else {
              _tmpExtraData = _cursor.getString(_cursorIndexOfExtraData);
            }
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final MessageStatus _tmpStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toMessageStatus(_tmp_1);
            final long _tmpScheduledTime;
            _tmpScheduledTime = _cursor.getLong(_cursorIndexOfScheduledTime);
            final Long _tmpSentAt;
            if (_cursor.isNull(_cursorIndexOfSentAt)) {
              _tmpSentAt = null;
            } else {
              _tmpSentAt = _cursor.getLong(_cursorIndexOfSentAt);
            }
            final String _tmpOriginalMessageId;
            if (_cursor.isNull(_cursorIndexOfOriginalMessageId)) {
              _tmpOriginalMessageId = null;
            } else {
              _tmpOriginalMessageId = _cursor.getString(_cursorIndexOfOriginalMessageId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new MessageQueue(_tmpId,_tmpTaskId,_tmpMessageType,_tmpContent,_tmpExtraData,_tmpOrder,_tmpStatus,_tmpScheduledTime,_tmpSentAt,_tmpOriginalMessageId,_tmpCreatedAt);
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
  public Object getLastPendingMessage(final long taskId,
      final Continuation<? super MessageQueue> $completion) {
    final String _sql = "SELECT * FROM message_queue WHERE taskId = ? AND status = 'PENDING' ORDER BY `order` DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, taskId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MessageQueue>() {
      @Override
      @Nullable
      public MessageQueue call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "taskId");
          final int _cursorIndexOfMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "messageType");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfExtraData = CursorUtil.getColumnIndexOrThrow(_cursor, "extraData");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfSentAt = CursorUtil.getColumnIndexOrThrow(_cursor, "sentAt");
          final int _cursorIndexOfOriginalMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "originalMessageId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final MessageQueue _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTaskId;
            _tmpTaskId = _cursor.getLong(_cursorIndexOfTaskId);
            final MessageType _tmpMessageType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfMessageType);
            _tmpMessageType = __converters.toMessageType(_tmp);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpExtraData;
            if (_cursor.isNull(_cursorIndexOfExtraData)) {
              _tmpExtraData = null;
            } else {
              _tmpExtraData = _cursor.getString(_cursorIndexOfExtraData);
            }
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final MessageStatus _tmpStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toMessageStatus(_tmp_1);
            final long _tmpScheduledTime;
            _tmpScheduledTime = _cursor.getLong(_cursorIndexOfScheduledTime);
            final Long _tmpSentAt;
            if (_cursor.isNull(_cursorIndexOfSentAt)) {
              _tmpSentAt = null;
            } else {
              _tmpSentAt = _cursor.getLong(_cursorIndexOfSentAt);
            }
            final String _tmpOriginalMessageId;
            if (_cursor.isNull(_cursorIndexOfOriginalMessageId)) {
              _tmpOriginalMessageId = null;
            } else {
              _tmpOriginalMessageId = _cursor.getString(_cursorIndexOfOriginalMessageId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new MessageQueue(_tmpId,_tmpTaskId,_tmpMessageType,_tmpContent,_tmpExtraData,_tmpOrder,_tmpStatus,_tmpScheduledTime,_tmpSentAt,_tmpOriginalMessageId,_tmpCreatedAt);
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
  public Object countByStatus(final long taskId, final MessageStatus status,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM message_queue WHERE taskId = ? AND status = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, taskId);
    _argIndex = 2;
    final String _tmp = __converters.fromMessageStatus(status);
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
  public Object countByTaskId(final long taskId, final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM message_queue WHERE taskId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, taskId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
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
  public Object getMaxOrder(final long taskId, final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT MAX(`order`) FROM message_queue WHERE taskId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, taskId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
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
  public Object getByOriginalMessageId(final String messageId,
      final Continuation<? super MessageQueue> $completion) {
    final String _sql = "SELECT * FROM message_queue WHERE originalMessageId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, messageId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MessageQueue>() {
      @Override
      @Nullable
      public MessageQueue call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "taskId");
          final int _cursorIndexOfMessageType = CursorUtil.getColumnIndexOrThrow(_cursor, "messageType");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfExtraData = CursorUtil.getColumnIndexOrThrow(_cursor, "extraData");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfScheduledTime = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledTime");
          final int _cursorIndexOfSentAt = CursorUtil.getColumnIndexOrThrow(_cursor, "sentAt");
          final int _cursorIndexOfOriginalMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "originalMessageId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final MessageQueue _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTaskId;
            _tmpTaskId = _cursor.getLong(_cursorIndexOfTaskId);
            final MessageType _tmpMessageType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfMessageType);
            _tmpMessageType = __converters.toMessageType(_tmp);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpExtraData;
            if (_cursor.isNull(_cursorIndexOfExtraData)) {
              _tmpExtraData = null;
            } else {
              _tmpExtraData = _cursor.getString(_cursorIndexOfExtraData);
            }
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final MessageStatus _tmpStatus;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfStatus);
            _tmpStatus = __converters.toMessageStatus(_tmp_1);
            final long _tmpScheduledTime;
            _tmpScheduledTime = _cursor.getLong(_cursorIndexOfScheduledTime);
            final Long _tmpSentAt;
            if (_cursor.isNull(_cursorIndexOfSentAt)) {
              _tmpSentAt = null;
            } else {
              _tmpSentAt = _cursor.getLong(_cursorIndexOfSentAt);
            }
            final String _tmpOriginalMessageId;
            if (_cursor.isNull(_cursorIndexOfOriginalMessageId)) {
              _tmpOriginalMessageId = null;
            } else {
              _tmpOriginalMessageId = _cursor.getString(_cursorIndexOfOriginalMessageId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new MessageQueue(_tmpId,_tmpTaskId,_tmpMessageType,_tmpContent,_tmpExtraData,_tmpOrder,_tmpStatus,_tmpScheduledTime,_tmpSentAt,_tmpOriginalMessageId,_tmpCreatedAt);
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
