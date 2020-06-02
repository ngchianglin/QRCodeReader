package sg.nighthour.qrcodereader

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    /* The request code for zxing IntentIntegrator */
    val REQUEST_CODE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Setup the onClick Listener for the launch browser button */
        btnLaunch.setOnClickListener {
            val urlstring = tvResult.text.toString()
            if (urlstring.contentEquals("Simple QR Reader")){
                Toast.makeText(this, "No url scanned", Toast.LENGTH_LONG).show()
            }
            else if(urlstring.startsWith("http://") || urlstring.startsWith("https://") )
            {/* Check to make sure that the URL starts with http:// or https:// */

                /* Launch an external browser using an implicit intent */
                Intent(Intent.ACTION_VIEW).also {
                    it.setData(Uri.parse(urlstring))
                    startActivity(it)
                }
            }
            else
            {
                Toast.makeText(this, "URL doesn't start with http:// or https://", Toast.LENGTH_LONG).show()
            }
        }

        /* Create a Zxing IntentIntegrator and start the QR code scan */
        val integrator = IntentIntegrator(this)
        integrator.setRequestCode(REQUEST_CODE)
        integrator.setOrientationLocked(false)
        integrator.initiateScan()

    }

    /* Process the result from the zxing QR code scan */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode != REQUEST_CODE) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }

        val result = IntentIntegrator.parseActivityResult(resultCode, data)
        if(result != null) {

            if(result.getContents() == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show()
            }
            else
            {   /* Update the textview with the scanned URL result */
                tvResult.setText(result.getContents())
                Toast.makeText(this, "Content: ${result.getContents()}",Toast.LENGTH_LONG ).show()
            }

        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }


}