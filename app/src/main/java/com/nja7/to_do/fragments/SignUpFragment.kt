package com.nja7.to_do.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.nja7.to_do.R
import com.nja7.to_do.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding : FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentSignUpBinding.inflate(inflater, container , false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()

    }

    private fun registerEvents() {

        binding.textViewSignIn.setOnClickListener {
            navControl.navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        try {
            binding.nextBtn.setOnClickListener {
                val email: String = binding.emailEt.text.toString()
                val password: String = binding.passEt.text.toString()
                val verifyPassword: String = binding.verifyPassEt.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty() && verifyPassword.isNotEmpty()) {
                    if (password == verifyPassword) {
                        if (password.length > 6) {
                            try {
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener {


                                            if (it.isSuccessful) {
                                                Toast.makeText(
                                                    context,
                                                    "$email  added successfully",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                try {
                                                    navControl.navigate(R.id.action_signUpFragment_to_homeFragment)

                                                } catch (n: Exception) {
                                                    Toast.makeText(
                                                        context,
                                                        "error : ${n.message.toString()}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    val x = n.message
                                                    if (x != null) {
                                                        Log.e("TAG", x)
                                                    }
                                                }
                                            }

                                    }
                            } catch (e: FirebaseAuthException) {
                                Toast.makeText(
                                    context,
                                    "something happend ${e.message.toString()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        } else {
                            Toast.makeText(
                                context,
                                "password should be more than 6 chars",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(context, "password are not the same", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(context, "fill all fields ,please", Toast.LENGTH_SHORT).show()
                }

            }

        }catch (e: Exception){
            Toast.makeText(context, "somethingWrong", Toast.LENGTH_SHORT).show()
        }
        }

    private fun init(view: View) {
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }




}