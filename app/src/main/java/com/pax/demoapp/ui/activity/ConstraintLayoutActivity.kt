package com.pax.demoapp.ui.activity

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.pax.demoapp.R
import kotlinx.android.synthetic.main.activity_constraint_layout.*

class ConstraintLayoutActivity : AppCompatActivity() {
    private var cls: Array<ConstraintLayout>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_layout)
        cls = arrayOf(cl_show_c, cl_show_e, cl_show_chain)
        btn_a.setOnClickListener {
            showCl(0)
        }

        btn_b.setOnClickListener {
            showCl(1)
        }
        btn_x.setOnClickListener {
            showCl(2)
        }
    }

    private fun showCl(which: Int) {
        for (i in cls?.indices!!) {
            if (i == which) {
                cls!![i].visibility = View.VISIBLE
            } else {
                cls!![i].visibility = View.GONE
            }
        }
    }
}
