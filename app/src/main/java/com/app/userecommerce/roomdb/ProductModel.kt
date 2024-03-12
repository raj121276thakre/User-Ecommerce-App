package com.app.userecommerce.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.Nonnull


@Entity(tableName = "products")
data class ProductModel(
    @PrimaryKey
    @Nonnull
    val productId : String,
    @ColumnInfo(name = "productName") val productName: String?,
    @ColumnInfo(name = "productImages") val productImages: String?,
    @ColumnInfo(name = "productSp") val productSp: String?,
)
