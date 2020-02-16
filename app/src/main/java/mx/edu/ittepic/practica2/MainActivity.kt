package mx.edu.ittepic.practica2

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var archivo = ""
        var radioSeleccionado = "N"

        radio.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                var radioSel = radio.text
                if(radioSel.equals("Archivo memoria INTERNA")){
                    radioSeleccionado = "I"
                }else{
                    radioSeleccionado = "S"
                }

                if(ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    //si entra entonces aún no se otorgaron los permiros
                    //el siguiente código los solicita
                    ActivityCompat.requestPermissions(this,
                        arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE),0)
                }


            })

        button.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            //si entra entonces aún no se otorgaron los permiros
            //el siguiente código los solicita
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE),0)
        }


            archivo = editText2.text.toString()

            if(radioSeleccionado.equals("N")) {
                mensaje("No elegiste Almacenamiento")
            }
            if(radioSeleccionado.equals("S")){
                guardarArchivoExterno(archivo)
            }
             if(radioSeleccionado.equals("I")){
                guardarArchivoInterno(archivo)
            }
        }

        button2.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

                ActivityCompat.requestPermissions(this,
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE),0)
            }

            archivo = editText2.text.toString()

            if(radioSeleccionado.equals("N")) {
                mensaje("No elegiste Almacenamiento")
            }
            if(radioSeleccionado.equals("S")){
                leerArchivoExterno(archivo)
            }
            if(radioSeleccionado.equals("I")){
                leerArchivoInterno(archivo)
            }
        }
    }



    fun guardarArchivoInterno(nombreArchivo:String){
        try{
            var flujoSalida = OutputStreamWriter(openFileOutput(nombreArchivo, Context.MODE_PRIVATE))
            var data = editText.text.toString()
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
            mensaje("Se guardo correctamente")
            editText.setText("")
            editText2.setText("")
        }catch (error : IOException){
            mensaje(error.message.toString())
        }
    }

    private fun leerArchivoInterno(arch: String){
        try{
            var flujoEntrada = BufferedReader(InputStreamReader(openFileInput(arch)))
            var data = flujoEntrada.readLine()
            editText.setText(data)
            flujoEntrada.close()

        }catch (error: IOException){
            mensaje(error.message.toString())
        }
    }

    fun mensaje(m : String){
        AlertDialog.Builder(this)
            .setTitle("Atención")
            .setMessage(m)
            .setPositiveButton("OK"){d,i->}
            .show()
    }


    fun guardarArchivoExterno(arch: String){
        if(noSD()){
            mensaje("No hay memoria externa")
            return
        }
        try {
            val rutaSD = Environment.getExternalStorageDirectory()
            var data = editText.text.toString()
            val flujo = File(rutaSD.absolutePath, arch)
            val flujoSalida = OutputStreamWriter(FileOutputStream(flujo))
            flujoSalida.write(data)
            flujoSalida.close()
            mensaje("Se guardo correctamente")
            editText.setText("")
            editText2.setText("")
        } catch (error: Exception) {
            mensaje(error.message.toString())
        }
    }

    fun leerArchivoExterno(arch: String){
        if(noSD()){
            mensaje("No hay memoria externa")
            return
        }
        try {
            val rutaSD = Environment.getExternalStorageDirectory()
            val flujo = File(rutaSD.absolutePath, arch)
            val flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(flujo)))
            val data = flujoEntrada.readLine()
            editText.setText(data)
            flujoEntrada.close()
        } catch (error: Exception) {
            mensaje(error.message.toString())
        }
    }

    fun noSD() : Boolean{
        var estado = Environment.getExternalStorageState()
        if(estado != Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }
}

