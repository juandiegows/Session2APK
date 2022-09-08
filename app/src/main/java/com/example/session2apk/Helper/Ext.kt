package com.example.session2apk.Helper

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.service.quicksettings.Tile
import android.text.Editable
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

@OptIn(ExperimentalStdlibApi::class)
fun JSONObject.toClass(nameClass: String): Any {
    var onClass = Class.forName(nameClass)
    var any = onClass.newInstance()

    onClass.declaredFields.forEach {
        it.isAccessible = true
        it.set(

            any, when (it.type) {
                typeOf<Int>().javaType -> this.getInt(it.name)
                typeOf<Log>().javaType -> this.get(it.name)
                typeOf<String>().javaType -> this.getString(it.name)
                typeOf<Double>().javaType -> this.getDouble(it.name)
                typeOf<Boolean>().javaType -> this.getBoolean(it.name)
                else -> {

                    try {
                        this.getJSONObject(it.name).toClass(it.type.name)
                    } catch (e: Exception) {

                    }

                }
            }
        )
    }

    return any
}

fun JSONArray.ToList(nameClass: String): Any {
    var list = ArrayList<Any>()
    for (i in 0 until this.length()) {
        list.add(this.getJSONObject(i).toClass(nameClass))
    }

    return list
}

fun Any.toJson(): String {
    var json = JSONObject();
    var onClass = Class.forName(this::class.java.name)
    onClass.declaredFields.forEach {
        it.isAccessible = true
        json.put(it.name, it.get(this))
    }
    return json.toString()
}

inline fun <reified T> Any.Cast(): T {
    return this as T
}

var TextInputEditText.TextJD: String
    get() {
        return this.text.toString()
    }
    set(value) {

        this.setText(value)
    }

fun Context.AlertOK(tile: String, Message: String) {
    AlertDialog.Builder(this)
        .setTitle(tile)
        .setMessage(Message)
        .setPositiveButton("OK", { _, _ -> })
        .create()
        .show()
}

fun TextInputEditText.Requerido(layout: TextInputLayout) {
    this.addTextChangedListener {
        Validar(layout, it)
    }
    this.setOnFocusChangeListener { v, b ->
        if (!b)
            Validar(layout, this.text)
    }

}

fun TextInputEditText.Requerido(layout: TextInputLayout, cant: Int) {
    this.addTextChangedListener {
        Validar(layout, it)
        if ((it!!.length > 0) && (it.length < 3)) {
            layout.error = "minimum required is 3"
        }
    }
    this.setOnFocusChangeListener { v, b ->
        if (!b) {
            Validar(layout, this.text)
            if (this.text!!.length > 0 && this.text!!.length < 3)
                layout.error = "minimum required is 3"
        }


    }

}

fun TextInputEditText.IsEmail(layout: TextInputLayout) {
    this.addTextChangedListener {
        ValidarCorreo(layout)
    }
    this.setOnFocusChangeListener { v, b ->
        if (!b)
            ValidarCorreo(layout)
    }

}

fun List<View>.ISCorrecto(): Boolean {
    var valido = true
    this.forEach {
        it.requestFocus()
        it.clearFocus()
        if (it is TextInputEditText) {
            if (!(it as TextInputEditText).error.isNullOrEmpty()) {
                valido = false
            }
        } else if (it is EditText) {
            if (!(it as EditText).error.isNullOrEmpty()) {
                valido = false
            }
        } else if (it is TextInputLayout) {
            if (!(it as TextInputLayout).error.isNullOrEmpty()) {

                valido = false
            }
        }
    }
    return valido
}

private fun TextInputEditText.ValidarCorreo(layout: TextInputLayout) {
    layout.error = ""

    if (!Patterns.EMAIL_ADDRESS.matcher(this.TextJD).matches()) {
        layout.error = "it isn't email valid"
    } else
        if (Regex(
                "^[a-zA-Z]{1,1}+[a-zA-Z0-9]{3,20}+[-._]{1}+[A-Za-z0-9]{0,20}+@[a-zA-Z]{3,20}.[a-zA-Z]{2,10}"
            ).matches(
                this.TextJD
            )
        ) {
            Log.e("TAG", "ValidarCorreo:")
            if (Pattern.matches(
                    "^[a-zA-Z]{1,1}+[a-zA-Z0-9]{3,20}+[-._]{1,1}+[A-Za-z0-9]{4,20}+@[A-Za-z]{3,15}+.[A-Za-z]{2,15}",
                    this.TextJD
                )
            ) {
                layout.error = ""
            } else {
                layout.error = "this email isn't valid, please to ckeck"
            }
        } else {
            if (Pattern.matches(
                    "^[a-zA-Z]{1,1}+[a-zA-Z0-9]{7,20}+@[A-Za-z]{3,15}+.[A-Za-z]{2,15}",
                    this.TextJD
                )
            )
                layout.error = ""
            else
                layout.error = "Required to start by letter A-Za-z"
        }
}

fun TextInputEditText.EqualsTo(layout: TextInputLayout, text: TextInputEditText) {
    this.addTextChangedListener {
        layout.error = ""
        if (this.TextJD != text.TextJD) {
            layout.error = "it is not equals to password"
        }
    }
    this.setOnFocusChangeListener { v, b ->
        if (!b) {
            layout.error = ""
            if (this.TextJD != text.TextJD) {
                layout.error = "it is not equals to password"
            }
        }


    }

}

private fun Validar(
    layout: TextInputLayout,
    it: Editable?
) {
    layout.error = ""
    if (it.toString().isEmpty()) {
        layout.error = "this field is required"
    }
}

fun String.toImage(): Bitmap? {
    var byte = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(byte, 0,byte.size )
}
fun String.toDate(format: String = "yyyy-MM-dd"): Calendar {
    try {
        return Calendar.getInstance().apply {
            time = SimpleDateFormat(format).parse(this@toDate).apply {
                month +=1
            }

        }
    } catch (e: Exception) {
        return  Calendar.getInstance()
    }
}