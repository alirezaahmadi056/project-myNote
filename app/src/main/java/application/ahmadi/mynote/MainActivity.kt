package application.ahmadi.mynote

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.temp_list.view.*


class MainActivity : AppCompatActivity() {

    private val list = ArrayList<ValuesData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadData("%")

    }

    override fun onResume() {
        super.onResume()
        loadData("%")
    }

    fun loadData(title: String) {
        list.clear()
        val db = DBManager(this)
        val column = arrayOf("ID", "Title", "Description")
        val selectionArgs = arrayOf(title)
        val cursor = db.runQuery(column, "Title Like ?", selectionArgs, "ID")
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Desc = cursor.getString(cursor.getColumnIndex("Description"))
                list.add(ValuesData(id, Title, Desc))
            } while (cursor.moveToNext())
        }
        val adapter = MyAdapter(this, list)
        listView1.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu1, menu)
        val searchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                loadData("%$newText%")
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item != null) {
            when (item.itemId) {
                R.id.itemAdd -> {
                    val intent = Intent(this, Main2Activity::class.java)
                    startActivity(intent)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    inner class MyAdapter(private val context: Context, var categores: List<ValuesData>) : BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val categoreView: View
            val holder: ViewHolder
            if (convertView == null) {
                categoreView = LayoutInflater.from(context).inflate(R.layout.temp_list, null)
                holder = ViewHolder()
                holder.txtTitle = categoreView.findViewById(R.id.txtTitle)
                holder.txtDescription = categoreView.findViewById(R.id.txtDescription)
                categoreView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
                categoreView = convertView
            }
            val categore = categores[position]
            holder.txtTitle?.text = categore.title
            holder.txtDescription?.text = categore.Description
            categoreView.imgDelete.setOnClickListener {
                val dbManager = DBManager(context)
                val selectionArgs = arrayOf(categore.ID.toString())
                dbManager.delete("ID=?", selectionArgs)
                loadData("%")
            }
            categoreView.imgEdit.setOnClickListener {
                GoToEditActivity(categore)
            }
            return categoreView
        }

        override fun getItem(position: Int): Any {
            return categores[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getCount(): Int {
            return categores.count()
        }
    }

    fun GoToEditActivity(note: ValuesData) {
        val intent = Intent(this, Main2Activity::class.java)
        intent.putExtra("ID", note.ID)
        intent.putExtra("Title", note.title)
        intent.putExtra("Description", note.Description)
        startActivity(intent)
    }


}

class ValuesData(val ID: Int, val title: String, val Description: String)

class ViewHolder {
    var txtTitle: TextView? = null
    var txtDescription: TextView? = null
}