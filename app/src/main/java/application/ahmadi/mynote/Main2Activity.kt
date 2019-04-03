package application.ahmadi.mynote

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        var id = 0

        if (intent.extras != null) {
            val bundle: Bundle = intent.extras
            id = bundle.getInt("ID")
            edtTitle.setText(bundle.getString("Title"))
            edtDescription.setText(bundle.getString("Description"))
        }

        btnAdd.setOnClickListener {

            if (edtTitle.text.toString() == "" || edtDescription.text.toString() == "") {

                Toast.makeText(this, "لطفا مقادیر را وارد کنید", Toast.LENGTH_LONG).show()

            } else {
                val dbManager = DBManager(this)
                val values = ContentValues()
                values.put("Title", edtTitle.text.toString())
                values.put("Description", edtDescription.text.toString())
                if (id == 0) {
                    val ID = dbManager.insert(values)
                    if (ID > 0)
                        Toast.makeText(this, "یادداشت ذخیره شد", Toast.LENGTH_LONG).show()
                    else
                        Toast.makeText(this, "مشکل در ذخیره سازی", Toast.LENGTH_LONG).show()
                } else {
                    val selectionArgs = arrayOf(id.toString())
                    val ID = dbManager.update(values, "ID=?", selectionArgs)
                    id = 0
                    if (ID > 0)
                        Toast.makeText(this, "یادداشت بروزرسانی شد", Toast.LENGTH_LONG).show()
                    else
                        Toast.makeText(this, "مشکل در بروزرسانی", Toast.LENGTH_LONG).show()
                }
            }

        }

    }
}