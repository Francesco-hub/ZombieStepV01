package com.example.zombiestepv01.Activities

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.zombiestepv01.Data.IStoreDao
import com.example.zombiestepv01.Data.IUserDao
import com.example.zombiestepv01.Data.StoreDao_Impl
import com.example.zombiestepv01.Data.UserDao_Impl
import com.example.zombiestepv01.Model.BEItem
import com.example.zombiestepv01.Model.BEUser
import com.example.zombiestepv01.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main_window.*
import kotlinx.android.synthetic.main.activity_store.*
import kotlinx.android.synthetic.main.activity_store.drawerLayout
import kotlinx.android.synthetic.main.activity_store.imageButton
import kotlinx.android.synthetic.main.content_main.*

class Store : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    private lateinit var user : BEUser
    private lateinit var storeItems : List<BEItem>
    private lateinit var storeRepo: IStoreDao
    private lateinit var userRepo: IUserDao
    private var posToBuy = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        var extras: Bundle = intent.extras!!
        user = extras.getSerializable("loggedUser") as BEUser
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.open,R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        imageButton.setOnClickListener{v-> drawerLayout.open()}
        var navigationView : NavigationView = findViewById(R.id.nav_menu)
        val headerView = navigationView.getHeaderView(0)
        val txt_stepcoins = headerView.findViewById(R.id.stepcoins) as TextView
        txt_stepcoins.text = "Stepcoins: ${user.stepCoins}"
        navigationView.setNavigationItemSelectedListener(this)

        storeRepo = StoreDao_Impl(this)
        userRepo = UserDao_Impl(this)
        storeItems = storeRepo.getStore(user.id)
        lst_storeItems.adapter = StoreAdapter(this, storeItems.toTypedArray(), user)
        lst_storeItems.setOnItemClickListener { parent, view, position, id -> onListItemClick(parent as ListView, view, position) }
        btn_buy.isVisible = false
        btn_buy.setOnClickListener{v -> onClickBuy()}

    }

    private fun onClickBuy() {
        if(checkBalance(posToBuy)){
            user.stepCoins-=storeItems[posToBuy].itemPrice
            if(storeItems[posToBuy].itemType == 1){
                user.weaponsLvl++
                updateStore(storeItems[posToBuy])
            }
            else if(storeItems[posToBuy].itemType == 2){
                user.wallLvl++
                updateStore(storeItems[posToBuy])
            }
            else{
                user.multiplier+= 0.25
                updateStore(storeItems[posToBuy])
            }
            checkFortressUpdate()
        }
        refreshStore()
    }

    private fun refreshStore() {
        storeItems = storeRepo.getStore(user.id)
        lst_storeItems.adapter = StoreAdapter(this, storeItems.toTypedArray(), user)
        var navigationView : NavigationView = findViewById(R.id.nav_menu)
        val headerView = navigationView.getHeaderView(0)
        val txt_stepcoins = headerView.findViewById(R.id.stepcoins) as TextView
        txt_stepcoins.text = "Stepcoins: ${user.stepCoins}"
        btn_buy.isVisible = false
    }

    fun onListItemClick(parent: ListView?, v: View?, position: Int) {
        val selectedItem = storeItems[position]
        btn_buy.isVisible = user.stepCoins>=selectedItem.itemPrice && selectedItem.itemLevel<7
        setItemSelection(position)
        posToBuy=position
    }

    private fun checkFortressUpdate() {
        if((user.wallLvl > user.fortressLvl) && (user.weaponsLvl > user.fortressLvl)){
            user.fortressLvl++
        }
        userRepo.updateUser(user)
    }

    private fun updateStore(item: BEItem) {
        if(item.itemType==1){
            if(item.itemLevel==1){
                item.itemLevel++
                item.itemName = "Knife"
                item.itemPrice = 2000
            }
            else if(item.itemLevel==2){
                item.itemLevel++
                item.itemName = "Gun"
                item.itemPrice = 5000
            }
            else if(item.itemLevel==3){
                item.itemLevel++
                item.itemName = "Shotgun"
                item.itemPrice = 10000
            }
            else if(item.itemLevel==4){
                item.itemLevel++
                item.itemName = "Ak-47"
                item.itemPrice = 20000
            }
            else if(item.itemLevel==5){
                item.itemLevel++
                item.itemName = "Famas F1"
                item.itemPrice = 500000
            }
            else item.itemLevel++

        }
        else if(item.itemType==2){
            if(item.itemLevel==1){
                item.itemLevel++
                item.itemName = "Wood Reinforcement 2"
                item.itemPrice = 2000
            }
            else if(item.itemLevel==2){
                item.itemLevel++
                item.itemName = "Wired Reinforcement"
                item.itemPrice = 5000
            }
            else if(item.itemLevel==3){
                item.itemLevel++
                item.itemName = "Wired Reinforcement 2"
                item.itemPrice = 10000
            }
            else if(item.itemLevel==4){
                item.itemLevel++
                item.itemName = "Steel Reinforcement"
                item.itemPrice = 20000
            }
            else if(item.itemLevel==5){
                item.itemLevel++
                item.itemName = "Steel Reinforcement 2"
                item.itemPrice = 500000
            }
            else item.itemLevel++
        }
        else{
            if(item.itemLevel==1){
                item.itemLevel++
                item.itemPrice = 2000
                item.itemName = "Morgan"
            }
            else if(item.itemLevel==2){
                item.itemLevel++
                item.itemPrice = 5000
                item.itemName = "Michonne"
            }
            else if(item.itemLevel==3){
                item.itemLevel++
                item.itemPrice = 10000
                item.itemName = "Carol"
            }
            else if(item.itemLevel==4){
                item.itemLevel++
                item.itemPrice = 20000
                item.itemName = "Daryl"
            }
            else if(item.itemLevel==5){
                item.itemLevel++
                item.itemPrice = 50000
                item.itemName = "Negan"
            }
            else item.itemLevel++
        }
        storeRepo.updateStore(item)
    }

    private fun checkBalance(position: Int): Boolean {
        if(user.stepCoins >= storeItems[position].itemPrice) return true
        return false
    }

    private fun setItemSelection(position: Int) {
        lst_storeItems.getChildAt(0).setBackgroundColor(Color.parseColor("#556F44"))
        lst_storeItems.getChildAt(0).findViewById<TextView>(R.id.txt_itemName).setTypeface(null, Typeface.NORMAL)
        lst_storeItems.getChildAt(0).findViewById<TextView>(R.id.txt_itemPrice).setTypeface(null, Typeface.NORMAL)
        lst_storeItems.getChildAt(0).findViewById<TextView>(R.id.txt_itemDescription).setTypeface(null, Typeface.NORMAL)
        lst_storeItems.getChildAt(1).setBackgroundColor(Color.parseColor("#697A21"))
        lst_storeItems.getChildAt(1).findViewById<TextView>(R.id.txt_itemName).setTypeface(null, Typeface.NORMAL)
        lst_storeItems.getChildAt(1).findViewById<TextView>(R.id.txt_itemPrice).setTypeface(null, Typeface.NORMAL)
        lst_storeItems.getChildAt(1).findViewById<TextView>(R.id.txt_itemDescription).setTypeface(null, Typeface.NORMAL)
        lst_storeItems.getChildAt(2).setBackgroundColor(Color.parseColor("#556F44"))
        lst_storeItems.getChildAt(2).findViewById<TextView>(R.id.txt_itemName).setTypeface(null, Typeface.NORMAL)
        lst_storeItems.getChildAt(2).findViewById<TextView>(R.id.txt_itemPrice).setTypeface(null, Typeface.NORMAL)
        lst_storeItems.getChildAt(2).findViewById<TextView>(R.id.txt_itemDescription).setTypeface(null, Typeface.NORMAL)
        lst_storeItems.getChildAt(position).setBackgroundColor(Color.parseColor("#000000"))
        lst_storeItems.getChildAt(position).findViewById<TextView>(R.id.txt_itemName).setTypeface(null, Typeface.BOLD)
        lst_storeItems.getChildAt(position).findViewById<TextView>(R.id.txt_itemPrice).setTypeface(null, Typeface.BOLD)
        lst_storeItems.getChildAt(position).findViewById<TextView>(R.id.txt_itemDescription).setTypeface(null, Typeface.BOLD)
    }

    internal class StoreAdapter(context: Context, private val items: Array<BEItem>, var user : BEUser) : ArrayAdapter<BEItem>(context, 0, items) {
        private val colours = intArrayOf(
                Color.parseColor("#556F44"),
                Color.parseColor("#697A21")
        )


        override fun getView(position: Int, v: View?, parent: ViewGroup): View {
            var v1: View? = v
            if (v1 == null) {
                val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                        as LayoutInflater
                v1 = li.inflate(R.layout.store_item, null)
            }
            val resView: View = v1!!
            resView.setBackgroundColor(colours[position % colours.size])
            val item = items[position]
            val itemName = resView.findViewById<TextView>(R.id.txt_itemName)
            val itemPrice = resView.findViewById<TextView>(R.id.txt_itemPrice)
            if(item.itemPrice > user.stepCoins){
                itemPrice.setTextColor(Color.parseColor("#ff0000"))
            }
            val itemDescription = resView.findViewById<TextView>(R.id.txt_itemDescription)
            val itemPicture = resView.findViewById<ImageView>(R.id.img_itemPicture)
            itemName.text = item.itemName
            itemPrice.text = (item.itemPrice.toString() + " Stepcoins")
            itemDescription.text = item.itemDescription
            if(item.itemType==1){
                if(item.itemLevel==1) itemPicture.setImageResource(R.drawable.wp1)
                else if(item.itemLevel==2) itemPicture.setImageResource(R.drawable.wp2)
                else if(item.itemLevel==3) itemPicture.setImageResource(R.drawable.wp3)
                else if(item.itemLevel==4) itemPicture.setImageResource(R.drawable.wp4)
                else if(item.itemLevel==5) itemPicture.setImageResource(R.drawable.wp5)
                else if (item.itemLevel==6) itemPicture.setImageResource(R.drawable.wp6)
                else{
                    itemPicture.setImageResource(R.drawable.wp6)
                    itemName.text = "MAX LEVEL"
                    itemPrice.text = ""
                    itemDescription.text = ""
                    itemName.setTextColor(Color.parseColor("#61de2a"))
                }
            }
            else if(item.itemType==2){
                if(item.itemLevel==1) itemPicture.setImageResource(R.drawable.wl1)
                else if(item.itemLevel==2) itemPicture.setImageResource(R.drawable.wl1)
                else if(item.itemLevel==3) itemPicture.setImageResource(R.drawable.wl3)
                else if(item.itemLevel==4) itemPicture.setImageResource(R.drawable.wl3)
                else if(item.itemLevel==5) itemPicture.setImageResource(R.drawable.wl5)
                else if(item.itemLevel==6) itemPicture.setImageResource(R.drawable.wl5)
                else{
                    itemPicture.setImageResource(R.drawable.wl5)
                    itemName.text = "MAX LEVEL"
                    itemPrice.text = ""
                    itemDescription.text = ""
                    itemName.setTextColor(Color.parseColor("#61de2a"))
                }
            }
            else{
                if(item.itemLevel==1) itemPicture.setImageResource(R.drawable.sv1)
                else if(item.itemLevel==2) itemPicture.setImageResource(R.drawable.sv2)
                else if(item.itemLevel==3) itemPicture.setImageResource(R.drawable.sv3)
                else if(item.itemLevel==4) itemPicture.setImageResource(R.drawable.sv4)
                else if(item.itemLevel==5) itemPicture.setImageResource(R.drawable.sv5)
                else if(item.itemLevel==6) itemPicture.setImageResource(R.drawable.sv6)
                else{
                    itemPicture.setImageResource(R.drawable.sv6)
                    itemName.text = "MAX LEVEL"
                    itemPrice.text = ""
                    itemDescription.text = ""
                    itemName.setTextColor(Color.parseColor("#61de2a"))
                }
            }
            return resView
        }
    }

    override fun onBackPressed() {

    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean = when (item.itemId){
        R.id.main_window-> {
            val intent = Intent(this, MainWindow::class.java)
            intent.putExtra("loggedUser",user)
            startActivity(intent)
            drawerLayout.close()
            finish()
            true
        }
        R.id.activity_profile -> {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("loggedUser", user)
            startActivity(intent)
            drawerLayout.close()
            finish()
            true
        }
        R.id.activity_logIn -> {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            drawerLayout.close()
            finish()
            true
        }
        else -> {
            true
        }
    }

}