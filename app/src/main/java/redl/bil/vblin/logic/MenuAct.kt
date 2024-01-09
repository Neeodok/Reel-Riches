package redl.bil.vblin.logic

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import redl.bil.vblin.databinding.MenuScreenBinding


class MenuAct : AppCompatActivity() {

    private val binding by lazy { MenuScreenBinding.inflate(layoutInflater) }
    private val btnGame by lazy {binding.btnStart  }
    private val btnSettings by lazy {binding.btnSettings  }
    private val btnBack by lazy {binding.exit  }
    private val btnPolina by lazy {binding.btnPoli  }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        btnGame.setOnClickListener {
            val intent = Intent(this@MenuAct, GameLogi::class.java)
            startActivity(intent)
        }

        btnSettings.setOnClickListener {
            val intent = Intent(this@MenuAct, SettingsAct::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            showExitDialog(this@MenuAct)
        }
        btnPolina.setOnClickListener {
            opPolic("https://sites.google.com/view/privacy-policy-reel-riches/home")
        }
    }
    private fun opPolic(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No web browser found", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showExitDialog(context: Context) {
        AlertDialog.Builder(context)
            .setMessage("Close Application?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                (context as? AppCompatActivity)?.finishAndRemoveTask()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
