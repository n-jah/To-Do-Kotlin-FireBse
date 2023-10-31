package com.nja7.to_do.fragments

import android.os.Bundle
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
import com.nja7.to_do.R
import com.nja7.to_do.databinding.FragmentSignInBinding
import com.nja7.to_do.databinding.FragmentSignUpBinding

class SignInFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentSignInBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()

    }

    private fun registerEvents() {

        binding.textViewSignUp.setOnClickListener {
            navControl.navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        binding.nextBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passEt.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
               
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {

                        if (it.isSuccessful) {
                            Toast.makeText(
                                context,
                                "login successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            navControl.navigate(R.id.action_signInFragment_to_homeFragment)
                        }else{
                            Toast.makeText(context, "something wrong", Toast.LENGTH_SHORT).show()
                        }



                }
            } else {
                Toast.makeText(context, "fill all fields ,please", Toast.LENGTH_SHORT).show()
            }


        }
    }




    private fun init(view: View) {
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }


}