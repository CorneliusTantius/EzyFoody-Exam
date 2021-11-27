package com.corneliustantius.ezyfoody.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.corneliustantius.ezyfoody.models.CartItemModel;
import com.corneliustantius.ezyfoody.models.CartSelectionModel;
import com.corneliustantius.ezyfoody.models.ProductModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.ConsoleHandler;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static SQLiteDatabase DATABASE;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "EzyFoody";

    private static final String PRODUCTS_TABLE = "Products";
    private static final String KEY_P_ID = "pId";
    private static final String KEY_NAME = "name";
    private static final String KEY_PRICE = "price";
    private static final String KEY_CATEGORY = "category";


    private static final String CART_TABLE = "CartItem";
    private static final String KEY_C_ID = "cId";
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_QUANTITY = "quantity";


    private static final String USER_TABLE = "User";
    private static final String KEY_U_ID = "uId";
    private static final String KEY_U_BALANCE = "balance";

    boolean isCreating = false;

    private static DatabaseHelper sInstance;
    public static synchronized DatabaseHelper getInstance(Context context) {
        Log.d("DATABASE", "Database Sync");
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public SQLiteDatabase getReadableDatabase() {
        // TODO Auto-generated method stub
        if(isCreating && DATABASE != null){
            return DATABASE;
        }
        return super.getReadableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        isCreating = true;
        Log.d("DATABASE", "onCreate: database created");
        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + PRODUCTS_TABLE + "(" +
                KEY_P_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," +
                KEY_PRICE + " INTEGER," + KEY_CATEGORY + " TEXT" + ")";

        String CREATE_CART_TABLE = "CREATE TABLE " + CART_TABLE + "(" +
                KEY_C_ID + " INTEGER PRIMARY KEY, " + KEY_PRODUCT_ID + " INTEGER," +
                KEY_QUANTITY + " INTEGER" + ")";

        String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE + "(" +
                KEY_U_ID + " INTEGER PRIMARY KEY, " + KEY_U_BALANCE + " INTEGER)";

        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_USER_TABLE);

        ContentValues values = new ContentValues();
        values.put(KEY_U_BALANCE, 69420);
        db.insert(USER_TABLE, null, values);
        Integer bal = getBalance(db);
        Log.d("DATABASE", "onCreate: "+ bal.toString());
        this.populateProducts(db);
        isCreating = false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CART_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
    public void addProduct(SQLiteDatabase db, ProductModel productModel){

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, productModel.getName());
        values.put(KEY_PRICE, productModel.getPrice());
        values.put(KEY_CATEGORY, productModel.getCategory());

        db.insert(PRODUCTS_TABLE, null, values);
    }
    public ProductModel getProduct(SQLiteDatabase db, Integer id) {

        Cursor cursor = db.query(PRODUCTS_TABLE, new String[] { KEY_P_ID, KEY_NAME, KEY_PRICE, KEY_CATEGORY },
                KEY_P_ID + "=?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null);
        if (cursor != null)
            cursor.moveToFirst();

        ProductModel product = new ProductModel(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                Integer.parseInt(cursor.getString(2)),
                cursor.getString(3));
        return product;
    }
    public List<ProductModel> getAllProduct(SQLiteDatabase db,String category) {
        List<ProductModel> productList = new ArrayList<ProductModel>();
        Cursor cursor = db.query(PRODUCTS_TABLE, new String[] { KEY_P_ID, KEY_NAME, KEY_PRICE, KEY_CATEGORY },
                KEY_CATEGORY + "=?",
                new String[] { category },
                null,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                ProductModel productModel = new ProductModel();
                productModel.setId(Integer.parseInt(cursor.getString(0)));
                productModel.setName(cursor.getString(1));
                productModel.setPrice(Integer.parseInt(cursor.getString(2)));
                productModel.setCategory(cursor.getString(3));

                productList.add(productModel);
            } while (cursor.moveToNext());
        }

        return productList;
    }
    public int updateProduct(SQLiteDatabase db, ProductModel productModel) {

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, productModel.getName());
        values.put(KEY_PRICE, productModel.getPrice());

        return db.update(PRODUCTS_TABLE, values, KEY_P_ID + " = ?",
                new String[] { String.valueOf(productModel.getId()) });
    }
    public void deleteProduct(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PRODUCTS_TABLE, KEY_P_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public void addCartItem(SQLiteDatabase db, CartItemModel cartItemModel){

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_ID, cartItemModel.getProductId());
        values.put(KEY_QUANTITY, cartItemModel.getQuantity());

        db.insert(CART_TABLE, null, values);
    }
    public Integer[] checkCartItem(SQLiteDatabase db, Integer id){
        Cursor cursor = db.query(CART_TABLE, new String[] { KEY_QUANTITY, KEY_C_ID },
                KEY_PRODUCT_ID + "=?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            return new Integer[]{Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1))};
        }
        else
            return new Integer[]{0,0};
    }
    public List<CartSelectionModel> getAllCartItems(SQLiteDatabase db) {
        List<CartSelectionModel> cartItemList = new ArrayList<CartSelectionModel>();
        String selectQuery = "SELECT " +
                KEY_C_ID + "," + KEY_QUANTITY + "," + KEY_NAME + "," + KEY_PRICE +
                " FROM " + CART_TABLE +
                    " INNER JOIN " + PRODUCTS_TABLE +
                    " ON " +
                CART_TABLE + "." + KEY_PRODUCT_ID + " = " + PRODUCTS_TABLE + "." + KEY_P_ID;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CartSelectionModel cartSelectionModel = new CartSelectionModel();
                cartSelectionModel.setId(Integer.parseInt(cursor.getString(0)));
                cartSelectionModel.setQty(Integer.parseInt(cursor.getString(1)));
                cartSelectionModel.setName(cursor.getString(2));
                cartSelectionModel.setPrice(Integer.parseInt((cursor.getString(3))));

                cartItemList.add(cartSelectionModel);
            } while (cursor.moveToNext());
        }
        return cartItemList;
    }
    public int updateCartItem(SQLiteDatabase db, Integer id, Integer newQty) {

        ContentValues values = new ContentValues();
        values.put(KEY_QUANTITY, newQty);

        return db.update(CART_TABLE, values, KEY_C_ID + " = ?",
                new String[] { String.valueOf(id) });
    }
    public void deleteCartItem(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CART_TABLE, KEY_C_ID + " = ?",
                new String[] { String.valueOf(id) });
    }
    public void deleteAllCartItem() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CART_TABLE,null, null);
    }

    public Integer getBalance(SQLiteDatabase db){
        Cursor cursor = db.query(USER_TABLE, new String[] { KEY_U_BALANCE },
                KEY_U_ID + "=?",
                new String[] { String.valueOf(1) },
                null,
                null,
                null,
                null);
        Integer bal = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            bal = Integer.parseInt(cursor.getString(0));
        }
        return bal;
    }
    public int setBalance(SQLiteDatabase db, Integer val){
        ContentValues values = new ContentValues();
        values.put(KEY_U_BALANCE, val);

        return db.update(USER_TABLE, values, KEY_U_ID + " = ?",
                new String[] { String.valueOf(1) });
    }

    public void populateProducts(SQLiteDatabase db){
        Log.d("DATABASE", "populateProducts: populating");
        String[] foods = {"Kebab", "Burger", "Sphagetti", "Wheat Bread", "Grain Pasta", "Fried Rice", "Instant Noodle",
                "Pizza", "Sweet Potatoes", "Salmon Sushi Roll"};
        String[] drinks = {"Avocado Juice", "Apple Juice", "Orange Juice", "Watermelon Juice",
                "Pineaple Juice", "Fresh Milk", "Soy Milk", "Tea", "Milo", "Coffee", "Mineral Water"};
        String[] snacks = {"Chips", "Nachos", "Chicken Nuggets", "Ice Cream", "Sausages", "Chicken Wings",
                "Cereals", "Cookies", "Popcorn", "Cupcakes"};
        for (String food:foods){
            ProductModel pm = new ProductModel();
            pm.setName(food);
            pm.setCategory("Food");
            pm.setPrice(randomizer());
            this.addProduct(db, pm);
        }
        for (String food:drinks){
            ProductModel pm = new ProductModel();
            pm.setName(food);
            pm.setCategory("Drink");
            pm.setPrice(randomizer());
            this.addProduct(db, pm);
        }
        for (String food:snacks){
            ProductModel pm = new ProductModel();
            pm.setName(food);
            pm.setCategory("Snacks");
            pm.setPrice(randomizer());
            this.addProduct(db, pm);
        }
        Log.d("DATABASE", "populateProducts: populated");
    }
    private int randomizer(){
        Random rn = new Random();
        int max = 4, min = 1;
        int i = rn.nextInt(max - min) + min;
        i = (i)*10000;
        return i;
    }
}
