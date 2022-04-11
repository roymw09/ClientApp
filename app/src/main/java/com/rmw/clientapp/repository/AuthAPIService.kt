package com.rmw.clientapp.repository

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
import org.json.JSONObject
import org.json.JSONTokener
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.net.ssl.HttpsURLConnection

// Data retrieved from Github Auth
data class AuthUser(var token: String, var id: Int, var username: String, var email: String, var url: String)

@DelicateCoroutinesApi
class AuthAPIService(context: Context) {
    private var _user = mutableStateOf(User(0, "", ""))
    val user: User
        get() = _user.value
    var errorMessage: String by mutableStateOf("")

    private lateinit var githubDialog: Dialog
    private var githubAuthURLFull: String
    private var context: Context

    object GithubConstants {

        val CLIENT_ID = "11b6d54ddbb4ad8a4c52"
        val CLIENT_SECRET = "cd0051d83b1594a3040ddbfe01602074cf4977e2"
        val REDIRECT_URI = "https://hidden-tundra-10439.herokuapp.com/login/oauth2/code/github"
        val SCOPE = "read:user,user:email"
        val AUTHURL = "https://github.com/login/oauth/authorize"
        val TOKENURL = "https://github.com/login/oauth/access_token"

    }

    init {
        val state = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())

        githubAuthURLFull = GithubConstants.AUTHURL + "?client_id=" + GithubConstants.CLIENT_ID +
                "&scope=" + GithubConstants.SCOPE + "&redirect_uri=" + GithubConstants.REDIRECT_URI +
                "&state=" + state

        this.context = context
    }

    private fun createUser(user: User) {
        GlobalScope.launch(Dispatchers.Default) {
            val apiService = UserAPIService.getInstance()
            try {
                _user = mutableStateOf(apiService.createUser(user))
            } catch (e: Exception) {
                errorMessage = e.message.toString()
                println(errorMessage)
            }
        }
    }

    fun checkIfUserExists(username: String) {
        GlobalScope.launch(Dispatchers.Default) {
            val apiService = UserAPIService.getInstance()
            try {
                _user = mutableStateOf(apiService.checkIfUserExists(username))
            } catch (e: Exception) {
                errorMessage = e.message.toString()
                println(errorMessage)
                if (user.username.equals("")) {
                    createUser(User(null, username, null))
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun setupGithubWebViewDialog(url: String) {
        githubDialog = Dialog(context)
        val webClient = GithubWebViewClient()
        val webView = WebView(context)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.webViewClient = webClient
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
        githubDialog.setContentView(webView)
        githubDialog.show()
    }

    @Suppress("OverridingDeprecatedMember")
    inner class GithubWebViewClient : WebViewClient() {
        private var _authUserMutable = mutableStateOf(AuthUser("", 0, "", "", ""))
        val authUser: AuthUser
            get() = _authUserMutable.value

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request!!.url.toString().startsWith(GithubConstants.REDIRECT_URI)) {
                handleUrl(request.url.toString())

                // Close the dialog after getting the authorization code
                if (request.url.toString().contains("code=")) {
                    githubDialog.dismiss()
                }
                return true
            }
            return false
        }

        // Check webview url for access token code or error
        private fun handleUrl(url: String) {
            val uri = Uri.parse(url)
            if (url.contains("code")) {
                val githubCode = uri.getQueryParameter("code") ?: ""
                requestForAccessToken(githubCode)
            }
        }

        fun requestForAccessToken(code: String) {
            val grantType = "authorization_code"

            val postParams =
                "grant_type=" + grantType + "&code=" + code + "&redirect_uri=" + GithubConstants.REDIRECT_URI + "&client_id=" + GithubConstants.CLIENT_ID + "&client_secret=" + GithubConstants.CLIENT_SECRET
            GlobalScope.launch(Dispatchers.Default) {
                val url = URL(GithubConstants.TOKENURL)
                val httpsURLConnection =
                    withContext(Dispatchers.IO) { url.openConnection() as HttpsURLConnection }
                httpsURLConnection.requestMethod = "POST"
                httpsURLConnection.setRequestProperty(
                    "Accept",
                    "application/json"
                );
                httpsURLConnection.doInput = true
                httpsURLConnection.doOutput = true
                val outputStreamWriter = OutputStreamWriter(httpsURLConnection.outputStream)
                withContext(Dispatchers.IO) {
                    outputStreamWriter.write(postParams)
                    outputStreamWriter.flush()
                }
                val response = httpsURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8

                withContext(Dispatchers.Main) {
                    val jsonObject = JSONTokener(response).nextValue() as JSONObject
                    val accessToken = jsonObject.getString("access_token") //The access token
                    // Get user's id, first name, last name, profile pic url
                    fetchGithubUserProfile(accessToken)
                }
            }
        }

        fun fetchGithubUserProfile(token: String) {
            GlobalScope.launch(Dispatchers.Default) {
                val tokenURLFull =
                    "https://api.github.com/user"

                val url = URL(tokenURLFull)
                val httpsURLConnection =
                    withContext(Dispatchers.IO) { url.openConnection() as HttpsURLConnection }
                httpsURLConnection.requestMethod = "GET"
                httpsURLConnection.setRequestProperty("Authorization", "Bearer $token")
                httpsURLConnection.doInput = true
                httpsURLConnection.doOutput = false
                val response = httpsURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                val jsonObject = JSONTokener(response).nextValue() as JSONObject
                Log.i("GitHub Access Token: ", token)

                // GitHub Id
                val githubId = jsonObject.getInt("id")
                Log.i("GitHub Id: ", githubId.toString())

                // GitHub Display Name
                val githubDisplayName = jsonObject.getString("login")
                Log.i("GitHub Display Name: ", githubDisplayName)

                // GitHub Email
                val githubEmail = jsonObject.getString("email")
                Log.i("GitHub Email: ", githubEmail)

                // GitHub Profile Avatar URL
                val githubAvatarURL = jsonObject.getString("avatar_url")
                Log.i("Github Avatar URL: ", githubAvatarURL)

                _authUserMutable = mutableStateOf(
                    AuthUser(
                        token,
                        githubId,
                        githubDisplayName,
                        githubEmail,
                        githubAvatarURL
                    )
                )
                checkIfUserExists(authUser.username)
            }
        }
    }
}