package es.angelillo15.antiabusers.utils

import com.google.inject.Inject
import com.google.inject.Injector
import java.lang.reflect.Field

object StaticMembersInjector {
  fun <T> injectStatics(injector: Injector, clazz: Class<T>) {
    val fields: Array<Field> = clazz.getDeclaredFields()
    for (field in fields) {
      if (field.isAnnotationPresent(Inject::class.java)) {
        try {
          field.setAccessible(true)
          field.set(null, injector.getInstance(field.getType()))
        } catch (e: IllegalAccessException) {
          e.printStackTrace()
        }
      }
    }
  }
}