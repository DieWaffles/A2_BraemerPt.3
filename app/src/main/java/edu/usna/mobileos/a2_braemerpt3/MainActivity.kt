package edu.usna.mobileos.a2_braemerpt3

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat.startActivity

class MainActivity : AppCompatActivity() {
    lateinit var editText: EditText
    lateinit var webView: WebView
    val bookmarkList = ArrayList<String?>()
    var requestCode = 5
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText = findViewById(R.id.editURL)
        webView = findViewById(R.id.webView)
        webView.webViewClient = WebViewClient()
        initializeWebView()
        val contextButton: WebView = findViewById(R.id.webView)
        registerForContextMenu(contextButton)

        findViewById<Button>(R.id.goBut).setOnClickListener{
            var url: String = editText.getText().toString()
            webView.loadUrl(url)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        // check to see which activity is returning the result
        if (requestCode == this.requestCode) {
            // check result code
            if (resultCode == RESULT_OK) {
                val response = data?.getStringExtra("returned_url")
                //Log.println(Log.INFO, "response", "made it here")
                if (response != null && response != "") {
                    webView.loadUrl(response)
                    editText.setText(response)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.backBut -> {
                if(webView.canGoBack())
                    webView.goBack()
                editText.setText(webView.originalUrl)
                return true
            }
            R.id.forBut -> {
                if(webView.canGoForward())
                    webView.goForward()
                editText.setText(webView.originalUrl)
                return true
            }
            R.id.viewBookBut ->{
                val intent = Intent(baseContext, Bookmark::class.java)
                intent.putStringArrayListExtra("list", bookmarkList)
                startActivityForResult(intent,requestCode)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu,menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.bookBut ->{
                if(webView.originalUrl == ""){
                    //add other url
                    bookmarkList.add(webView.url)
                }
                else{
                    //add original url
                    bookmarkList.add(webView.originalUrl)
                }
            }
            else -> super.onContextItemSelected(item)
        }
    }
    private fun initializeWebView(){
        webView.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                editText.setText(webView.url)
            }
        }
    }
}