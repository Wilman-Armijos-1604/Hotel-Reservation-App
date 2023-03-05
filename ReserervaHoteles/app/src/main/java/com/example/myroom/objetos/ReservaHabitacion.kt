package com.example.myroom.objetos

import com.example.myroom.TipoDeHabitacion

class ReservaHabitacion(
    var id:String?=null,
    var imagen:ByteArray?=null,
    var fechaEntrada:String?=null,
    var fechaSalida:String?=null,
    var idReservaCabecera:String?=null,
    var idTipoDeHabitacion:String?=null,
    var idUsuario:String?=null,
    var nombreHabitacion:String?=null,
    var numeroDeAdultos:Int?=null,
    val numeroDeNinos:Int?=null,
    val numeroDeCuartos:Int?=null,
    val numeroDeDias:Int?=null,
    val numeroDeCamas:Int?=null,
    var subtotal:Double?=null

) {


}