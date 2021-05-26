package com.example.zombiestepv01.Model

import java.io.File
import java.io.Serializable

class BEItem (
        var itemId: Int,
        var userId: Int,
        var itemName: String,
        var itemPrice: Int,
        var itemDescription: String,
        var itemLevel: Int,
        var itemType: Int
        ) : Serializable
