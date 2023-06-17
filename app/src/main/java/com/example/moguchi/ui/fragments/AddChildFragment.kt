package com.example.moguchi.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.moguchi.R
import com.example.moguchi.databinding.FragmentAddChildBinding
import com.example.moguchi.domain.models.child.Child
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.UUID

class AddChildFragment : Fragment() {

    private lateinit var binding: FragmentAddChildBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddChildBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.apply {

            saveChildToFirebase()

            isEnabled = true
            setOnClickListener {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_addChildFragment_to_onBoardingFragment)
            }
        }
    }

    private fun saveChildToFirebase() {
        val database =
            FirebaseDatabase.getInstance("https://moguchi-app-default-rtdb.europe-west1.firebasedatabase.app/")
        val parentRef = database.getReference("parents")
        val childId = UUID.randomUUID().toString()

        val childFirstName = binding.firstName.editText?.text.toString().trim { it <= ' ' }
        val childLastName = binding.lastName.editText?.text.toString().trim { it <= ' ' }
        val childYears = binding.years.editText?.text.toString()

        if (childFirstName.isEmpty() || childLastName.isEmpty() || childYears.isEmpty()) {
            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        val parentId = FirebaseAuth.getInstance().currentUser?.uid
        val child = Child(firstName = childFirstName, lastName = childLastName, years = childYears.toInt())
        parentRef.child(parentId!!).child("children").child(childId).setValue(child)
    }
}
