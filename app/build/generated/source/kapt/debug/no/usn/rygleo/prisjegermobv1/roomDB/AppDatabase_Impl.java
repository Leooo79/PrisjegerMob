package no.usn.rygleo.prisjegermobv1.roomDB;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile BrukerDAO _brukerDAO;

  private volatile VarerDAO _varerDAO;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(6) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Bruker` (`brukerId` INTEGER NOT NULL, `brukerNavn` TEXT, `passord` TEXT, PRIMARY KEY(`brukerId`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Varer` (`listenavn` TEXT NOT NULL, `varenavn` TEXT NOT NULL, `enhetspris` REAL, `antall` INTEGER, PRIMARY KEY(`listenavn`, `varenavn`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '94800c6884205f80cdfd8d180f508629')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Bruker`");
        _db.execSQL("DROP TABLE IF EXISTS `Varer`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsBruker = new HashMap<String, TableInfo.Column>(3);
        _columnsBruker.put("brukerId", new TableInfo.Column("brukerId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBruker.put("brukerNavn", new TableInfo.Column("brukerNavn", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBruker.put("passord", new TableInfo.Column("passord", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBruker = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBruker = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBruker = new TableInfo("Bruker", _columnsBruker, _foreignKeysBruker, _indicesBruker);
        final TableInfo _existingBruker = TableInfo.read(_db, "Bruker");
        if (! _infoBruker.equals(_existingBruker)) {
          return new RoomOpenHelper.ValidationResult(false, "Bruker(no.usn.rygleo.prisjegermobv1.roomDB.Bruker).\n"
                  + " Expected:\n" + _infoBruker + "\n"
                  + " Found:\n" + _existingBruker);
        }
        final HashMap<String, TableInfo.Column> _columnsVarer = new HashMap<String, TableInfo.Column>(4);
        _columnsVarer.put("listenavn", new TableInfo.Column("listenavn", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVarer.put("varenavn", new TableInfo.Column("varenavn", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVarer.put("enhetspris", new TableInfo.Column("enhetspris", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVarer.put("antall", new TableInfo.Column("antall", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVarer = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVarer = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoVarer = new TableInfo("Varer", _columnsVarer, _foreignKeysVarer, _indicesVarer);
        final TableInfo _existingVarer = TableInfo.read(_db, "Varer");
        if (! _infoVarer.equals(_existingVarer)) {
          return new RoomOpenHelper.ValidationResult(false, "Varer(no.usn.rygleo.prisjegermobv1.roomDB.Varer).\n"
                  + " Expected:\n" + _infoVarer + "\n"
                  + " Found:\n" + _existingVarer);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "94800c6884205f80cdfd8d180f508629", "db7dc277f07c184e0e3f12f1c3362004");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "Bruker","Varer");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `Bruker`");
      _db.execSQL("DELETE FROM `Varer`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(BrukerDAO.class, BrukerDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(VarerDAO.class, VarerDAO_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public BrukerDAO brukerDAO() {
    if (_brukerDAO != null) {
      return _brukerDAO;
    } else {
      synchronized(this) {
        if(_brukerDAO == null) {
          _brukerDAO = new BrukerDAO_Impl(this);
        }
        return _brukerDAO;
      }
    }
  }

  @Override
  public VarerDAO varerDAO() {
    if (_varerDAO != null) {
      return _varerDAO;
    } else {
      synchronized(this) {
        if(_varerDAO == null) {
          _varerDAO = new VarerDAO_Impl(this);
        }
        return _varerDAO;
      }
    }
  }
}
