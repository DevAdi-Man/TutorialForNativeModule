package com.contactsmodule

import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap

class ContactModule(reactContext: ReactApplicationContext) : NativeContactsSpec(reactContext) {
    override fun getName() = NAME

    /*
        * Retrive a list of all constacts ( name and phone number) asynchronously
        * The result is returned via a React Native Promise
     */

    override fun getContacts(promise: Promise) {
        // 1. Check if the Read_Contacts permission is granted
        if (ContextCompat.checkSelfPermission(
             reactApplicationContext,
                android.Manifest.permission.READ_CONTACTS
        ) != PackageManager.PERMISSION_GRANTED
        ){
            // Permission not granted. The JS side Should handle the request flow,
            // but we fail the promise just in case
            promise.reject("E_PERMISSION", "Read Contacts permission not granted")
            return
        }

        // 2. Defined Column we want to retrieve
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        // 3. Prepare the ContactResolver  to quarey the database
        val contentResolver = reactApplicationContext.contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null, // Selection (Where clause)
            null, // Selection Arguments
            ContactsContract.CommonDataKinds.Phone.NUMBER + " ASC" // sort order
        )

        val contactsArray = WritableNativeArray()

        // 4. Iterate through the result and build the JS-Compatible array
        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()){
                val name = if (nameIndex >= 0) it.getString(nameIndex) else "Unknown"
                val number = if (numberIndex >= 0)  it.getString(numberIndex)  else ""

                // Create a contacts object (WritableNativeMap)
                val contactMap = WritableNativeMap()
                contactMap.putString("name", name)
                contactMap.putString("phone", number.replace("[^0-9+]".toRegex(),"")) // clean the phone number
                contactsArray.pushMap(contactMap)
            }
        }
        promise.resolve(contactsArray)
    }
    companion object {
        const val NAME = "NativeContacts"
    }
}