package tools.preferences.api

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

//region Property delegate definition
private class PrefsDelegate<in T, R>(val default: R?, prefs: () -> SharedPreferences) : ReadWriteProperty<T, R> {
  val preferences by lazy(prefs)
  @Suppress("UNCHECKED_CAST")
  override fun getValue(thisRef: T, property: KProperty<*>): R {
    val propertyType = property.returnType.toString()
    return when (propertyType) {
      "kotlin.String?",
      "kotlin.String" -> preferences.getString(property.name, default as? String?) as R
      "kotlin.Int" -> preferences.getInt(property.name, default as? Int ?: 0) as R
      "kotlin.Long" -> preferences.getLong(property.name, default as? Long ?: 0) as R
      "kotlin.Float" -> preferences.getFloat(property.name, default as? Float ?: 0f) as R
      "kotlin.Boolean" -> preferences.getBoolean(property.name, default as? Boolean ?: false) as R
      else -> throw IllegalStateException("Not supported type: $propertyType")
    }
  }

  override fun setValue(thisRef: T, property: KProperty<*>, value: R) {
    val propertyType = property.returnType.toString()
    when (propertyType) {
      "kotlin.String?",
      "kotlin.String" -> preferences.edit().putString(property.name, value.toString()).apply()
      "kotlin.Int" -> preferences.edit().putInt(property.name, value as Int).apply()
      "kotlin.Long" -> preferences.edit().putLong(property.name, value as Long).apply()
      "kotlin.Float" -> preferences.edit().putFloat(property.name, value as Float).apply()
      "kotlin.Boolean" -> preferences.edit().putBoolean(property.name, value as Boolean).apply()
      else -> throw IllegalStateException("Not supported type: $propertyType")
    }
  }
}
//endregion

//region Helpers
@Suppress("NOTHING_TO_INLINE")
private inline fun Context.defaultPreferences() = applicationContext.getSharedPreferences("prefs.default", Context.MODE_PRIVATE)
//endregion

//region Default SharedPreferences
fun <T> Context.defaultPrefs(default: T? = null): ReadWriteProperty<Any?, T> = PrefsDelegate(default) { defaultPreferences() }

fun <T> Fragment.defaultPrefs(default: T? = null): ReadWriteProperty<Any?, T> = PrefsDelegate(default) { activity.defaultPreferences() }
//endregion

//region Generic SharedPreferences
fun <T> sharedPrefs(default: T? = null, prefs: () -> SharedPreferences): ReadWriteProperty<Any?, T> = PrefsDelegate(default, prefs)
//endregion

