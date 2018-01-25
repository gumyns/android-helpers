##Usage

When preferences object is available, when context is super class is not Context, or with dependency injection, e.g. Dagger
```kotlin
  @Inject lateinit var preferences: SharedPreferences
  
  // that's same as prefences.getString("someString", null), but supports both reading and writing
  private var someString: String? by sharedPrefs { preferences } 
  private var someOtherString: String? by sharedPrefs("Default!") { preferences } 
```
Or, use default shared preferences in activity, fragment, service, etc. All below are valid
```kotlin
private var webserviceAddress: String? by defaultPrefs()
private var webserviceAddress: String? by applicationContext.defaultPrefs()
private var webserviceAddress: String? by defaultPrefs("Hello")
private var webserviceAddress: String? by applicationContext.defaultPrefs("Hello")
```

##Dependecies

```groovy
"org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
"org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"
```

##Proguard

```proguard
-keep class kotlin.reflect.** { *; }
-keep class kotlin.Metadata { *; }
```
