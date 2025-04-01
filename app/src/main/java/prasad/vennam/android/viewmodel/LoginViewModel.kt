package prasad.vennam.android.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

) : ViewModel() {
    fun saveUserData(email: String, password: String) {
        val userData = hashMapOf(
            "email" to email,
            "password" to password
        )
        println(
            "User data saved: $userData"
        )
    }

}