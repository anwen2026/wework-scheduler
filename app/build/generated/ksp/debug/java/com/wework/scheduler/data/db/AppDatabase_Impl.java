package com.wework.scheduler.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile ScheduleTaskDao _scheduleTaskDao;

  private volatile MessageQueueDao _messageQueueDao;

  private volatile SendPlanDao _sendPlanDao;

  private volatile AppConfigDao _appConfigDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `schedule_tasks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `groupId` TEXT NOT NULL, `groupName` TEXT NOT NULL, `sendTime` INTEGER NOT NULL, `intervalSeconds` INTEGER NOT NULL, `status` TEXT NOT NULL, `sendPlanId` INTEGER, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `pausedAt` INTEGER, `commandMessageId` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `message_queue` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `taskId` INTEGER NOT NULL, `messageType` TEXT NOT NULL, `content` TEXT NOT NULL, `extraData` TEXT, `order` INTEGER NOT NULL, `status` TEXT NOT NULL, `scheduledTime` INTEGER NOT NULL, `sentAt` INTEGER, `originalMessageId` TEXT, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`taskId`) REFERENCES `schedule_tasks`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_message_queue_taskId` ON `message_queue` (`taskId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_message_queue_scheduledTime` ON `message_queue` (`scheduledTime`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `send_plans` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `targetGroups` TEXT NOT NULL, `isDefault` INTEGER NOT NULL, `enabled` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `app_config` (`key` TEXT NOT NULL, `value` TEXT NOT NULL, `type` TEXT NOT NULL, `description` TEXT, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`key`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'f65a45f1b0a04a795046d9bfd01edcc2')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `schedule_tasks`");
        db.execSQL("DROP TABLE IF EXISTS `message_queue`");
        db.execSQL("DROP TABLE IF EXISTS `send_plans`");
        db.execSQL("DROP TABLE IF EXISTS `app_config`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsScheduleTasks = new HashMap<String, TableInfo.Column>(11);
        _columnsScheduleTasks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleTasks.put("groupId", new TableInfo.Column("groupId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleTasks.put("groupName", new TableInfo.Column("groupName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleTasks.put("sendTime", new TableInfo.Column("sendTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleTasks.put("intervalSeconds", new TableInfo.Column("intervalSeconds", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleTasks.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleTasks.put("sendPlanId", new TableInfo.Column("sendPlanId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleTasks.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleTasks.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleTasks.put("pausedAt", new TableInfo.Column("pausedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheduleTasks.put("commandMessageId", new TableInfo.Column("commandMessageId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysScheduleTasks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesScheduleTasks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoScheduleTasks = new TableInfo("schedule_tasks", _columnsScheduleTasks, _foreignKeysScheduleTasks, _indicesScheduleTasks);
        final TableInfo _existingScheduleTasks = TableInfo.read(db, "schedule_tasks");
        if (!_infoScheduleTasks.equals(_existingScheduleTasks)) {
          return new RoomOpenHelper.ValidationResult(false, "schedule_tasks(com.wework.scheduler.data.db.entities.ScheduleTask).\n"
                  + " Expected:\n" + _infoScheduleTasks + "\n"
                  + " Found:\n" + _existingScheduleTasks);
        }
        final HashMap<String, TableInfo.Column> _columnsMessageQueue = new HashMap<String, TableInfo.Column>(11);
        _columnsMessageQueue.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageQueue.put("taskId", new TableInfo.Column("taskId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageQueue.put("messageType", new TableInfo.Column("messageType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageQueue.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageQueue.put("extraData", new TableInfo.Column("extraData", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageQueue.put("order", new TableInfo.Column("order", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageQueue.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageQueue.put("scheduledTime", new TableInfo.Column("scheduledTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageQueue.put("sentAt", new TableInfo.Column("sentAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageQueue.put("originalMessageId", new TableInfo.Column("originalMessageId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMessageQueue.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMessageQueue = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysMessageQueue.add(new TableInfo.ForeignKey("schedule_tasks", "CASCADE", "NO ACTION", Arrays.asList("taskId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMessageQueue = new HashSet<TableInfo.Index>(2);
        _indicesMessageQueue.add(new TableInfo.Index("index_message_queue_taskId", false, Arrays.asList("taskId"), Arrays.asList("ASC")));
        _indicesMessageQueue.add(new TableInfo.Index("index_message_queue_scheduledTime", false, Arrays.asList("scheduledTime"), Arrays.asList("ASC")));
        final TableInfo _infoMessageQueue = new TableInfo("message_queue", _columnsMessageQueue, _foreignKeysMessageQueue, _indicesMessageQueue);
        final TableInfo _existingMessageQueue = TableInfo.read(db, "message_queue");
        if (!_infoMessageQueue.equals(_existingMessageQueue)) {
          return new RoomOpenHelper.ValidationResult(false, "message_queue(com.wework.scheduler.data.db.entities.MessageQueue).\n"
                  + " Expected:\n" + _infoMessageQueue + "\n"
                  + " Found:\n" + _existingMessageQueue);
        }
        final HashMap<String, TableInfo.Column> _columnsSendPlans = new HashMap<String, TableInfo.Column>(7);
        _columnsSendPlans.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSendPlans.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSendPlans.put("targetGroups", new TableInfo.Column("targetGroups", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSendPlans.put("isDefault", new TableInfo.Column("isDefault", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSendPlans.put("enabled", new TableInfo.Column("enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSendPlans.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSendPlans.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSendPlans = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSendPlans = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSendPlans = new TableInfo("send_plans", _columnsSendPlans, _foreignKeysSendPlans, _indicesSendPlans);
        final TableInfo _existingSendPlans = TableInfo.read(db, "send_plans");
        if (!_infoSendPlans.equals(_existingSendPlans)) {
          return new RoomOpenHelper.ValidationResult(false, "send_plans(com.wework.scheduler.data.db.entities.SendPlan).\n"
                  + " Expected:\n" + _infoSendPlans + "\n"
                  + " Found:\n" + _existingSendPlans);
        }
        final HashMap<String, TableInfo.Column> _columnsAppConfig = new HashMap<String, TableInfo.Column>(5);
        _columnsAppConfig.put("key", new TableInfo.Column("key", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppConfig.put("value", new TableInfo.Column("value", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppConfig.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppConfig.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppConfig.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAppConfig = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAppConfig = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAppConfig = new TableInfo("app_config", _columnsAppConfig, _foreignKeysAppConfig, _indicesAppConfig);
        final TableInfo _existingAppConfig = TableInfo.read(db, "app_config");
        if (!_infoAppConfig.equals(_existingAppConfig)) {
          return new RoomOpenHelper.ValidationResult(false, "app_config(com.wework.scheduler.data.db.entities.AppConfig).\n"
                  + " Expected:\n" + _infoAppConfig + "\n"
                  + " Found:\n" + _existingAppConfig);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "f65a45f1b0a04a795046d9bfd01edcc2", "ef030f974019b27117c7a9938ac7bd71");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "schedule_tasks","message_queue","send_plans","app_config");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `schedule_tasks`");
      _db.execSQL("DELETE FROM `message_queue`");
      _db.execSQL("DELETE FROM `send_plans`");
      _db.execSQL("DELETE FROM `app_config`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ScheduleTaskDao.class, ScheduleTaskDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MessageQueueDao.class, MessageQueueDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SendPlanDao.class, SendPlanDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AppConfigDao.class, AppConfigDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ScheduleTaskDao scheduleTaskDao() {
    if (_scheduleTaskDao != null) {
      return _scheduleTaskDao;
    } else {
      synchronized(this) {
        if(_scheduleTaskDao == null) {
          _scheduleTaskDao = new ScheduleTaskDao_Impl(this);
        }
        return _scheduleTaskDao;
      }
    }
  }

  @Override
  public MessageQueueDao messageQueueDao() {
    if (_messageQueueDao != null) {
      return _messageQueueDao;
    } else {
      synchronized(this) {
        if(_messageQueueDao == null) {
          _messageQueueDao = new MessageQueueDao_Impl(this);
        }
        return _messageQueueDao;
      }
    }
  }

  @Override
  public SendPlanDao sendPlanDao() {
    if (_sendPlanDao != null) {
      return _sendPlanDao;
    } else {
      synchronized(this) {
        if(_sendPlanDao == null) {
          _sendPlanDao = new SendPlanDao_Impl(this);
        }
        return _sendPlanDao;
      }
    }
  }

  @Override
  public AppConfigDao appConfigDao() {
    if (_appConfigDao != null) {
      return _appConfigDao;
    } else {
      synchronized(this) {
        if(_appConfigDao == null) {
          _appConfigDao = new AppConfigDao_Impl(this);
        }
        return _appConfigDao;
      }
    }
  }
}
