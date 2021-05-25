package com.example.zombiestepv01.Model

import java.io.File
import java.io.Serializable

class BEUser (
    var id: Int,
    var name: String,
    var email: String,
    var password: String,
    var stepCoins: Int,
    var totalSteps: Int,
    var multiplier: Double,
    var fortressLvl: Int,
    var wallLvl: Int,
    var weaponsLvl: Int,
    var Picture: File?) : Serializable
