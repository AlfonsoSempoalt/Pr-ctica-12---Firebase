package com.example.digimind.ui.home

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.digimind.R
import com.example.digimind.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private var adaptador: AdaptadorTareas? = null
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var storage: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    companion object {
        var tasks = ArrayList<Task>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.storage = FirebaseFirestore.getInstance()
        this.auth = FirebaseAuth.getInstance()

        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        fillTasks(root.context)
        return root
    }

    fun fillTasks(context: Context) {
        this.storage.collection("actividades")
            .whereEqualTo("email", auth.currentUser.email)
            .get()
            .addOnSuccessListener { it ->
                it.forEach {
                    var dias = ArrayList<String>()

                    if (it.getBoolean("lu") == true) {
                        dias.add("Monday")
                    }
                    if (it.getBoolean("ma") == true) {
                        dias.add("Monday")
                    }
                    if (it.getBoolean("mi") == true) {
                        dias.add("Wednesday")
                    }

                    if (it.getBoolean("ju") == true) {
                        dias.add("Thursday")
                    }

                    if (it.getBoolean("vi") == true) {
                        dias.add("Friday")
                    }

                    if (it.getBoolean("sa") == true) {
                        dias.add("Saturday")
                    }

                    if (it.getBoolean("do") == true) {
                        dias.add("Sunday")
                    }
                    tasks!!.add(Task(it.getString("actividad")!!, dias, it.getString("tiempo")!!))
                }
                adaptador = AdaptadorTareas(context, tasks)
                gridview.adapter = adaptador
            }
    }


    private class AdaptadorTareas : BaseAdapter {
        var tasks = ArrayList<Task>()
        var context: Context? = null

        constructor(contexto: Context, tasks: ArrayList<Task>) {
            this.context = contexto
            this.tasks = tasks
        }

        override fun getCount(): Int {
            return tasks.size
        }

        override fun getItem(p0: Int): Any {
            return tasks[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var task = tasks[p0]
            var inflador = LayoutInflater.from(this.context)
            var vista = inflador.inflate(R.layout.task_view, null)

            val titulo: TextView = vista.findViewById(R.id.tv_title)
            val tiempo: TextView = vista.findViewById(R.id.tv_time)
            val dias: TextView = vista.findViewById(R.id.tv_days)

            titulo.setText(task.title)
            tiempo.setText(task.time)
            dias.setText(task.days.toString())

            return vista
        }
    }

}
