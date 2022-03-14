package com.listapp.ListAppUtil;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface OrderDao {

    @Insert
    void addOrder(Order order);

    @Insert
    void addProduct(Product product);


    @Insert
    void addSupplier(Supplier supplier);

    @Query("SELECT * FROM orders")
    List<Order> getOrderlist();

    @Query("SELECT product FROM products")
    List<String> getProductlist();



    @Query("SELECT * FROM suppliers")
    List<Supplier> getCacheSupplierlist();


    @Query("SELECT * FROM orders GROUP BY supplier")
    List<Order> getSupplierlist();

    @Query("SELECT * FROM orders WHERE supplier = :supplier")
    List<Order> getOrderlist(String supplier);

    @Query("SELECT * FROM orders WHERE product = :product")
    Order getOrderbyproduct(String product);

    @Query("SELECT * FROM orders WHERE newproduct = 'true'")
    List<Order> getNewproducts();




    @Update
    void update(Order order);

    @Delete
    void deleteOrder(Order order);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Update
    void update(Supplier supplier);

    @Delete
    void delete(Supplier supplier);

}
