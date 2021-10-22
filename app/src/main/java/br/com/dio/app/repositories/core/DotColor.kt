package br.com.dio.app.repositories.core

import android.graphics.Color

enum class DotColor(val colorDot:Int, val nameLanguage:String) {
    JAVASCRIPT(Color.parseColor("#FFD700"),"JavaScript"),
    KOTLIN(Color.parseColor("#1E90FF"),"Kotlin"),
    TYPESCRIPT(Color.parseColor("#2B7489"),"TypeScript"),
    HTML(Color.parseColor("#E34C26"),"HTML"),
    JAVA(Color.parseColor("#FF0000"),"Java"),
    OBJECTIVE_C(Color.parseColor("#228B22"),"Objective-C"),
    PHP(Color.parseColor("#F4A460"),"Php")
}