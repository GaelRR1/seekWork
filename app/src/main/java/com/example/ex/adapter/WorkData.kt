package com.example.ex.adapter

class WorkData {
    var name :String? = null
    var subject :String? = null
    var text : String? = null
    var image :String? = null
    constructor(){}

    constructor(
        name:String?,
        subject:String?,
        text:String?,
        image:String
    ){
        this.name = name
        this.subject = subject
        this.text = text
        this.image = image
    }
}