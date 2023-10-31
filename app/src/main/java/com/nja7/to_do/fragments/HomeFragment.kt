package com.nja7.to_do.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nja7.to_do.R
import com.nja7.to_do.databinding.FragmentHomeBinding
import com.nja7.to_do.utils.ToDoAdapter
import com.nja7.to_do.utils.ToDoData

class HomeFragment : Fragment(), AddToDoFragment.DiaglogNextButton,
    ToDoAdapter.ToDoAdapterClicksInterface {

    private lateinit var autho : FirebaseAuth
    private lateinit var dataBaseRef: DatabaseReference
    private lateinit var navControl: NavController
    private lateinit var binding : FragmentHomeBinding
    private  var popupFragment: AddToDoFragment? = null
    private lateinit var adapter : ToDoAdapter
    private lateinit var mutableList: MutableList<ToDoData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        getDataFromFireBase()
        registerEvents()

    }

    private fun registerEvents() {
        binding.addToDo.setOnClickListener {
            if (popupFragment != null)
                childFragmentManager.beginTransaction().remove(popupFragment!!).commit()
            popupFragment= AddToDoFragment()
            popupFragment!!.setListener(this)
            popupFragment!!.show(
                childFragmentManager,
                AddToDoFragment.TAG
            )
        }
        binding.logout.setOnClickListener {
            autho.signOut()
            navControl.navigate(R.id.action_homeFragment_to_splashFragment)
        }
    }

    private fun init(view: View) {
        navControl = Navigation.findNavController(view)
        autho = FirebaseAuth.getInstance()
        mutableList = mutableListOf()
        adapter = ToDoAdapter(mutableList)

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager= LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        adapter.setListener(this)

        dataBaseRef = FirebaseDatabase.getInstance()
            .reference.child("Tasks").child(autho.currentUser?.uid.toString())


    }


    fun getDataFromFireBase(){
        dataBaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mutableList.clear()
                for (taskSnapshot in snapshot.children){
                    val todoTask = taskSnapshot.key?.let{
                        ToDoData(it, taskSnapshot.value.toString())
                    }
                    if( todoTask != null){
                        mutableList.add(todoTask)


                    }
                }

                mutableList.reverse()

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onSaveTask(todo: String, todoEt: TextInputEditText) {
        dataBaseRef.push().setValue(todo).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context, "successful saving", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
            todoEt.text = null
            popupFragment!!.dismiss()
        }


    }

    override fun onUpdateTask(toDoData:  ToDoData, todoEt: TextInputEditText) {
        val map = HashMap<String, Any>()
        map[toDoData.taskId] = toDoData.task
        dataBaseRef.updateChildren(map).addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            todoEt.text = null
            popupFragment!!.dismiss()
        }
    }

    override fun onDeleteTaskBtnClicked(toDoData: ToDoData) {

        dataBaseRef.child(toDoData.taskId).removeValue().addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, it.exception?.message.toString(), Toast.LENGTH_SHORT).show()

            }
        }

    }

    override fun onEditTaskBtnClicked(toDoData: ToDoData) {

        if (popupFragment != null)
            childFragmentManager.beginTransaction().remove(popupFragment!!).commit()
        popupFragment = AddToDoFragment.newInstance(toDoData.taskId, toDoData.task)
        popupFragment!!.setListener(this)
        popupFragment!!.show(childFragmentManager, AddToDoFragment.TAG)

    }


}