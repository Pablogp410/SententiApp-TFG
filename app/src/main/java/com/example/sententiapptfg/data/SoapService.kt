package com.example.sententiapptfg.data

import android.util.Log
import com.example.sententiapptfg.data.models.Date
import com.example.sententiapptfg.data.models.Quote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

class SoapService {

    private val namespace = "http://tempuri.org/"
    private val url = "https://appstip.iatext.ulpgc.es/ServicioSententiaWCF/ServicioSententia.svc"

    private fun createEnvelope(request: SoapObject): SoapSerializationEnvelope {
        return SoapSerializationEnvelope(SoapEnvelope.VER11).apply {
            dotNet = true
            setOutputSoapObject(request)
        }
    }

    private suspend fun callSoapMethod(method: String, configure: (SoapObject) -> Unit = {}): SoapObject =
        withContext(Dispatchers.IO) {
            val request = SoapObject(namespace, method)
            configure(request)
            val envelope = createEnvelope(request)
            val transport = HttpTransportSE(url)
            val soapAction = "http://tempuri.org/IServicioSententia/$method"

            transport.call(soapAction, envelope)
            envelope.response as SoapObject
        }

    fun SoapObject.getSafeProperty(name: String): String? {
        return try {
            val value = this.getPropertySafelyAsString(name)
            if (value == "anyType{}" || value.isBlank()) null else value
        } catch (e: Exception) {
            null
        }
    }

    suspend fun mostrarFechas(lang: String = "es"): List<Date> {
        return try {
            val result = callSoapMethod("MostrarFechas") {
                it.addProperty("lang", lang)
            }
            (0 until result.propertyCount).map { i ->
                val obj = result.getProperty(i) as SoapObject
                Date(
                    id = obj.getSafeProperty("id")?.toIntOrNull() ?: 0,
                    tag = obj.getSafeProperty("etiqueta") ?: "",
                    day = obj.getSafeProperty("dia") ?: "",
                    image = obj.getSafeProperty("imagen") ?: "",
                    details = obj.getSafeProperty("detalles") ?: ""
                )
            } .filter { it.id != 1 }
        } catch (e: Exception) {
            Log.e("SoapService", "Error en mostrarFechas: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun mostrarCategorias(lang: String = "es"): List<String> {
        return try {
            val result = callSoapMethod("MostrarCategorias") {
                it.addProperty("lang", lang)
            }
            (0 until result.propertyCount).map { i ->
                result.getProperty(i).toString()
            }
        } catch (e: Exception) {
            Log.e("SoapService", "Error en mostrarCategorias: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun mostrarFechasPorCategoria(categoria: String, lang: String = "es"): List<Date> {
        return try {
            val result = callSoapMethod("MostrarFechasPorCategoria") {
                it.addProperty("Categoria", categoria)
                it.addProperty("lang", lang)
            }
            /*for (i in 0 until result.propertyCount) {
                val propName = result.getPropertyInfo(i).name
                val propValue = result.getProperty(i)
                Log.d("SoapResponse", "$propName: $propValue")
            }*/
            (0 until result.propertyCount).map { i ->
                val obj = result.getProperty(i) as SoapObject
                Date(
                    id = obj.getSafeProperty("id")?.toIntOrNull() ?: 0,
                    tag = obj.getSafeProperty("etiqueta") ?: "",
                    day = obj.getSafeProperty("dia") ?: "",
                    image = obj.getSafeProperty("imagen") ?: "",
                    details = obj.getSafeProperty("detalles") ?: ""
                )
            }
        } catch (e: Exception) {
            Log.e("SoapService", "Error en mostrarFechasPorCategoria: ${e.message}", e)
            emptyList()
        } .filter { it.id != 1 }
    }

    suspend fun mostrarInformacionFecha(id: Int, lang: String = "es"): Date {
        return try {
            val result = callSoapMethod("MostrarInformacionFecha") {
                it.addProperty("ID", id)
                it.addProperty("lang", lang)
            }
            /*for (i in 0 until result.propertyCount) {
                val propName = result.getPropertyInfo(i).name
                val propValue = result.getProperty(i)
                Log.d("SoapResponse", "$propName: $propValue")
            }*/
            Date(
                id = result.getSafeProperty("id")?.toIntOrNull() ?: 0,
                tag = result.getSafeProperty("etiqueta") ?: "",
                day = result.getSafeProperty("dia") ?: "",
                image = result.getSafeProperty("imagen") ?: "",
                details = result.getSafeProperty("detalles") ?: ""
            )
        } catch (e: Exception) {
            Log.e("SoapService", "Error en mostrarInformacionFecha: ${e.message}", e)
            Date(0, "", "", "", "")
        }
    }


    suspend fun mostrarSentencias(id: Int, lang: String = "es"): List<Quote> {
        return try {
            val result = callSoapMethod("MostrarSentencias") {
                it.addProperty("ID", id)
                it.addProperty("lang", lang)
            }
            (0 until result.propertyCount).map { i ->
                val obj = result.getProperty(i) as SoapObject
                Quote(
                    id = obj.getSafeProperty("id") ?: "0",
                    author = obj.getSafeProperty("autor") ?: "",
                    article = obj.getSafeProperty("obra") ?: "",
                    category = obj.getSafeProperty("categoria") ?: "",
                    latinQuote = obj.getSafeProperty("extractolatino") ?: "",
                    spanishQuote = obj.getSafeProperty("extractoespanol") ?: "",
                    englishQuote = obj.getSafeProperty("extractoingles") ?: ""
                    /*id = obj.getProperty("id").toString(),
                    author = obj.getProperty("autor").toString(),
                    article = obj.getProperty("obra").toString(),
                    category = obj.getProperty("categoria").toString(),
                    latinQuote = obj.getProperty("extractolatino").toString(),
                    spanishQuote = obj.getProperty("extractoespanol").toString(),
                    englishQuote = obj.getProperty("extractoingles").toString()*/
                )
            }
        } catch (e: Exception) {
            Log.e("SoapService", "Error en mostrarSentencias: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun mostrarInformacionSentencia(id: String, lang: String = "es"): Quote {
        return try {
            val result = callSoapMethod("MostrarInformacionSentencia") {
                it.addProperty("ID", id)
                it.addProperty("lang", lang)
            }
            /*for (i in 0 until result.propertyCount) {
                val propName = result.getPropertyInfo(i).name
                val propValue = result.getProperty(i)
                Log.d("SoapResponse", "$propName: $propValue")
            }*/
            Quote(
                //id = obj.getProperty("id").toString().toInt(),
                id = result.getSafeProperty("id") ?: "0",
                author = result.getSafeProperty("autor") ?: "",
                article = result.getSafeProperty("obra") ?: "",
                category = result.getSafeProperty("categoria") ?: "",
                latinQuote = result.getSafeProperty("extractolatino") ?: "",
                spanishQuote = result.getSafeProperty("extractoespanol") ?: "",
                englishQuote = result.getSafeProperty("extractoingles") ?: ""
            )
        } catch (e: Exception) {
            Log.e("SoapService", "Error en mostrarInformacionSentencia: ${e.message}", e)
            //Quote(0, "", "", "", "", "", "")
            Quote("0", "", "", "", "", "", "")
        }
    }

    /*PROVISIONAL FUNCTIONS*/
    suspend fun mostrarInformacionFechaCustom(id: Int, lang: String = "es"): Date?{
        return try {
            val allDates = mostrarFechas(lang)
            allDates.find { it.id == id }
        } catch (e: Exception) {
            Log.e("SoapService", "Error en getDateDetails (fallback): ${e.message}", e)
            Date(0, "", "", "", "")
        }
    }

    /*suspend fun mostrarFechasPorCategoriaCustom(category: String, lang: String = "es"): List<Date>{
        return try {
            val allDates = mostrarFechas(lang)
            allDates.filter { it.tag.equals(category, ignoreCase = true) } //NO ES EL TAG, NO TIENE PARAMETRO CATEGORIA
        } catch (e: Exception) {
            Log.e("SoapService", "Error en getDatesByCategory: ${e.message}", e)
            emptyList()
        }
    }*/
}
