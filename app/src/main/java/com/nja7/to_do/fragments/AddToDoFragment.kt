package com.nja7.to_do.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import com.google.android.material.animation.AnimatableView.Listener
import com.google.android.material.textfield.TextInputEditText
import com.nja7.to_do.R
import com.nja7.to_do.databinding.FragmentAddToDoBinding
import com.nja7.to_do.utils.ToDoData

class AddToDoFragment : DialogFragment() {
    private lateinit var binding: FragmentAddToDoBinding
    private lateinit var listener: DiaglogNextButton
    private var toDoData: ToDoData? = null


    companion object{
        const val TAG = "AddToDoFragment"

        @JvmStatic
        fun newInstance(taskId: String, task: String) = AddToDoFragment().apply {
            arguments = Bundle().apply {
                putString("taskId" , taskId)
                putString("task", task)

            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View  {
        // Inflate the layout for this fragment
        binding = FragmentAddToDoBinding.inflate(inflater, container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.todoEt.requestFocus()


        if (arguments != null){
            toDoData =  ToDoData(
                arguments?.getString("taskId").toString(),
                arguments?.getString("task").toString())

            binding.todoEt.setText(toDoData?.task)
        }




        registerEvents()


    }

    private fun registerEvents() {
        binding.todoNextBtn.setOnClickListener{

            val todoTask = binding.todoEt.text.toString()
            if (todoTask.isNotEmpty()){

                if (toDoData == null){
                    listener.onSaveTask(todoTask, binding.todoEt)
                }else{
                    toDoData?.task = todoTask
                    listener.onUpdateTask(
                        toDoData!!, binding.todoEt)
                }
            }else{
                Toast.makeText(context, " Please Type your task??", Toast.LENGTH_SHORT).show()
            }
        }
        binding.todoClose.setOnClickListener {
            dismiss()
        }
    }

    interface DiaglogNextButton{
        fun onSaveTask( todo : String,  todoEt : TextInputEditText)
        fun onUpdateTask( toDoData : ToDoData ,  todoEt : TextInputEditText)
    }


    fun setListener(listener: DiaglogNextButton ) {
        this.listener = listener
    }
}