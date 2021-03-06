package com.example.trendskotlin

import android.content.Context
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.graphics.Canvas
import android.net.ConnectivityManager
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    //Initialise Modes
    val partyMode = (2..5).toList()
    val cpuMode = listOf("Easy", "Normal", "Hard", "Impossible")
    var numPlayersRef = 0
    var difficultyRef = 1

    //Instantiate Buttons
    var partyAddBtn : ImageButton? = null
    var partyRemBtn : ImageButton? = null
    var partyPlayBtn : ImageButton? = null
    var cpuAddBtn : ImageButton? = null
    var cpuRemBtn : ImageButton? = null
    var cpuPlayBtn : ImageButton? = null
    var achieveBtn : Button? = null
    var helpBtn : ImageButton? = null

    //Instantiate Changing Text Views
    var partyText : TextView? = null
    var cpuText : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide() //Removes actionBar for this activity only. Could also have changed styles.

        reportFullyDrawn()

        val pathView = findViewById<PathView>(R.id.path)
        pathView.init()

        // Initialise Buttons
        partyAddBtn = findViewById(R.id.partyAddBtn)
        partyRemBtn = findViewById(R.id.partyRemBtn)
        partyPlayBtn = findViewById(R.id.partyPlayBtn)
        cpuAddBtn = findViewById(R.id.cpuAddBtn)
        cpuRemBtn = findViewById(R.id.cpuRemBtn)
        cpuPlayBtn = findViewById(R.id.cpuPlayBtn)
        achieveBtn = findViewById(R.id.achieveBtn)
        helpBtn = findViewById(R.id.helpBtn)

        //Initialise Text Views
        partyText = findViewById(R.id.partyTextView)
        cpuText = findViewById(R.id.cpuTextView)

        //Set onClick Listeners
        //Party Mode
        partyAddBtn?.setOnClickListener {
            if (numPlayersRef < 3) numPlayersRef++
            nextMode(partyText, partyMode[numPlayersRef].toString())
        }
        partyRemBtn?.setOnClickListener {
            if (numPlayersRef > 0) numPlayersRef--
            prevMode(partyText, partyMode[numPlayersRef].toString())
        }
        partyPlayBtn?.setOnClickListener {
            pushThemesActivity("Party Mode", partyMode[numPlayersRef].toString())
        }

        //CPU Mode
        cpuAddBtn?.setOnClickListener {
            if (difficultyRef < 3) difficultyRef++
            nextMode(cpuText, cpuMode[difficultyRef])
        }
        cpuRemBtn?.setOnClickListener {
            if (difficultyRef > 0) difficultyRef--
            prevMode(cpuText, cpuMode[difficultyRef])
        }
        cpuPlayBtn?.setOnClickListener {
            pushThemesActivity("CPU Mode", cpuMode[numPlayersRef])
        }

        //Achievements Page
        achieveBtn?.setOnClickListener {
            val intent = Intent(this, AchievementsActivity::class.java)
            this.startActivity(intent)
        }

        //About Page
        helpBtn?.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            this.startActivity(intent)
        }
    }

    fun nextMode(textView: TextView?, text: String) {
        textView?.text = text
    }

    fun prevMode(textView: TextView?, text: String) {
        textView?.text = text
    }

    fun pushThemesActivity(mode: String, secondaryChoice: String) {
        if (isOnline()) {
            val intent = Intent(this, ThemesActivity::class.java)
            intent.putExtra("mode", mode)
            intent.putExtra("secondaryChoice", secondaryChoice)
            this.startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        } else {
            this.shortToast("Internet Connection Required")
        }
    }

    fun Context.shortToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
}