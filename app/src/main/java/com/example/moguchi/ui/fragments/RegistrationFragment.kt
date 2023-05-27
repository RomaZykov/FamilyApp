package com.example.moguchi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.moguchi.R
import com.example.moguchi.api.ApiService
import com.example.moguchi.data.ApiClient
import com.example.moguchi.databinding.FragmentRegistrationBinding
import com.example.moguchi.domain.models.RegisterResponse
import com.example.moguchi.domain.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var api: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registrationButton.setOnClickListener { registerUser() }
    }

    private fun registerUser() {
        val firstName: String = binding.firstName.editText?.text.toString().trim { it <= ' ' }
        val lastName: String = binding.lastName.editText?.text.toString().trim { it <= ' ' }
        val email: String = binding.email.editText?.text.toString().trim { it <= ' ' }
        val password: String = binding.password.editText?.text.toString().trim { it <= ' ' }
        val role: String = binding.status.editText?.text.toString().trim { it <= ' ' }

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty()) {
            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        api = ApiClient.getApiClient().create(ApiService::class.java)
        val registeredUser = User(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
            role = role
        )

        api.registerUser(registeredUser).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Пользователь сохранен", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_registrationFragment_to_homeBottomFragment)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Возникла непредвиденная ошибка, попробуйте позже",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}