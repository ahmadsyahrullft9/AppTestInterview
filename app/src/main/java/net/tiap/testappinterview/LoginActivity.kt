package net.tiap.testappinterview

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import net.tiap.testappinterview.apis.ApiClient
import net.tiap.testappinterview.apis.Users
import net.tiap.testappinterview.binding.BaseActivityBinding
import net.tiap.testappinterview.databinding.ActivityLoginBinding
import net.tiap.testappinterview.models.LoginModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivityBinding<ActivityLoginBinding>() {

    val retrofit = ApiClient.getClien()
    lateinit var loginApi: Call<ResponseBody>

    override val bindingInflater: (LayoutInflater) -> ActivityLoginBinding
        get() = ActivityLoginBinding::inflate

    override fun setupView(binding: ActivityLoginBinding) {

        binding.apply {

            btnLogin.setOnClickListener {
                edUsername.addTextChangedListener(getTextChange(edUsername))
                edPassword.addTextChangedListener(getTextChange(edPassword))
                if (TextUtils.isEmpty(edUsername.text)) {
                    edUsername.setError("username masih kosong")
                    edUsername.requestFocus()
                    return@setOnClickListener
                }
                if (TextUtils.isEmpty(edPassword.text)) {
                    edPassword.setError("password masih kosong")
                    edPassword.requestFocus()
                    return@setOnClickListener
                }

                login(edUsername.text.toString(), edPassword.text.toString())
            }
        }
    }

    private fun getTextChange(editText: EditText): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                if (editText.error != null)
                    editText.error = null
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (editText.error != null)
                    editText.error = null
            }

            override fun afterTextChanged(s: Editable?) {
                if (editText.error != null)
                    editText.error = null
            }

        }
    }

    private fun login(username: String, password: String) {
        loginApi = retrofit.create(Users::class.java).login(LoginModel(username, password))
        loginApi.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!call.isCanceled) {
                    if (response.isSuccessful) {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        showDialog("Login gagal, username/password tidak sesuai")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
                showDialog("Login gagal, terjadi kesalahan")
            }

        })
    }

    private fun showDialog(message: String) {
        val builder = AlertDialog.Builder(this).setMessage(message)
            .setPositiveButton("OK", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                }
            })
        val alertDialog = builder.create()
        alertDialog.show()
    }
}