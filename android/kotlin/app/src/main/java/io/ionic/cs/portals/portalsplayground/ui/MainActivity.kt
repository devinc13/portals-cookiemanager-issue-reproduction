package io.ionic.cs.portals.portalsplayground.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.ionic.cs.portals.portalsplayground.R
import java.net.CookieHandler
import java.net.CookieManager

class MainActivity : AppCompatActivity() {
    var button: Button? = null
    var cookieButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle(R.string.title_main)
        button = findViewById(R.id.button_main)
        button?.setOnClickListener {
            startActivity(Intent(this,PortalsActivity::class.java))
        }

        // Devin's reproduction of the issue:
        // Set a basic cookie manager, we set a custom one in our app but that shouldn't matter for reproduction
        CookieHandler.setDefault(CookieManager())

        cookieButton = findViewById(R.id.button_cookies)
        cookieButton?.setOnClickListener {
            // Try and use the default cookie manager to set a cookie
            (CookieHandler.getDefault() as? CookieManager)?.cookieStore?.let {
                it.add(null, java.net.HttpCookie("crash", "1"))
                Toast.makeText(this, "Cookie set successfully - now try this after coming back from the portal", Toast.LENGTH_SHORT).show()

                // If you hit this button after coming back from the portal, the app will crash with
                // java.lang.UnsupportedOperationException
                // at com.getcapacitor.plugin.CapacitorCookieManager.getCookieStore(CapacitorCookieManager.java:203)
            }
        }
    }
}
