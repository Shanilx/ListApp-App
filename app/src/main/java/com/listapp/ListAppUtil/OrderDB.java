package com.listapp.ListAppUtil;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Order.class,Product.class,Supplier.class}, version = 3, exportSchema = false)
public abstract class OrderDB extends RoomDatabase {
    private static OrderDB INSTANCE;

    public abstract OrderDao orderDao();

    public static final Migration MIGRATION_1_2 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE orders "
//                    + " ADD COLUMN company TEXT");
//            database.execSQL("ALTER TABLE orders "
//                    + " ADD COLUMN whatsappnumber TEXT");
//            database.execSQL("ALTER TABLE orders "
//                    + " ADD COLUMN othernumber TEXT");
//            database.execSQL("ALTER TABLE orders "
//                    + " ADD COLUMN city TEXT");

        }
    };
}
