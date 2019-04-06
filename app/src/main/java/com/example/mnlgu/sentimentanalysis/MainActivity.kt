import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.mnlgu.sentimentanalysis.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(),TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var buttonSpeak: Button? = null
    private var editText: EditText? = null

    private var palabrasPositivas = arrayOf("feliz", "bueno", "excelente", "maravilloso", "alegre", "contento", "soleado",
        "agradable", "bonito", "apasionado", "placer", "genial", "gracias", "agradecido", "disfrutando", "disfruto",
        "estupendo", "victoria", "ahuevo", "vamos", "puedes", "éxito", "próspero", "bendecido", "paz", "logré", "felicidad",
        "amor", "positivo", "bailar", "bailo", ":)", ":\'v")

    private var palabrasNegativas = arrayOf("triste", "enojado", "molesto", "incómodo", "estresado", "preocupado", "crudo",
        "sufrir", "sufro", "desamor", "roto", "muerte", "pobre", "desepcionado", "negativo", "odio", "spoilers", "peligro",
        "problema", "depresión", "deprimido", "nadie", "guerra", "infidelidad", "chale", "cerrado", "no", "enfermo", "injusto",
        "sad", "llorar", "lloro",  ":(", ">:v")

    private var palabrasNeutrales = arrayOf("bien", "normal", "tranquilo", "equis", "regular", "a veces", "indiferente",
        "normalmente", "pues bien", "lo de siempre", "lo mismo", "me da igual", "igual", "mas o menos", "70", "ok", "pues",
        "pasé", "", ":|", ":v")

    private var menuPositivo = "menu positivo"

    private var menuNegativo = "menu negativo"

    private var menuNeutral = "menu neutral"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonSpeak = this.button_speak
        editText = this.edittext_input

        buttonSpeak!!.isEnabled = false;
        tts = TextToSpeech(this, this)

        buttonSpeak!!.setOnClickListener { speakOut() }
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            } else {
                buttonSpeak!!.isEnabled = true
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }

    }

    private fun speakOut() {
        val text = editText!!.text.toString()

        val contado = contarPalabras(text)
        var aux : String = "no se tiene un menu"

        if (contado == 1)
            aux = menuPositivo
        else if (contado == 0)
            aux = menuNeutral
        else
            aux = menuNegativo

        tts!!.speak(aux, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

    public fun contarPalabras(cadena : String): Int{
        var contPositivo = 0
        var contNegativo = 0
        var contNeutral = 0

        for (item in palabrasPositivas){
            if (cadena.contains(item))
                contPositivo++
        }
        for (item2 in palabrasNegativas){
            if(cadena.contains(item2))
                contNegativo++
        }
        for (item3 in palabrasNeutrales) {
            if(cadena.contains(item3))
                contNeutral++
        }

        if (contPositivo > contNegativo && contPositivo > contNeutral)
            return 1
        else if (contNegativo > contPositivo && contNegativo > contNeutral)
            return -1
        else if (contNeutral > contPositivo && contNeutral > contNegativo)
            return 0
        else if (contPositivo == contNegativo)
            return 0
        else if (contNeutral == contNegativo && contPositivo > contNegativo)
            return 1
        else if (contNeutral == contPositivo && contNegativo > contPositivo)
            return -1

        return 0
    }

}