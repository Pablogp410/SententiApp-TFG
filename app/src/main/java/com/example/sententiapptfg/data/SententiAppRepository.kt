package com.example.sententiapptfg.data

import com.example.sententiapptfg.data.models.Date
import com.example.sententiapptfg.data.models.Quote

class SententiAppRepository(private val soapService: SoapService) {

    suspend fun getDates(lang: String = "es"): List<Date> {
        return soapService.mostrarFechas(lang)
    }

    suspend fun getCategories(lang: String = "es"): List<String> {
        return soapService.mostrarCategorias(lang)
    }

    suspend fun getDatesByCategory(category: String, lang: String = "es"): List<Date> {
        return soapService.mostrarFechasPorCategoria(category, lang)
    }

    suspend fun getDateDetails(id: Int, lang: String = "es"): Date {
        return soapService.mostrarInformacionFecha(id, lang)
    }

    suspend fun getQuotes(id: Int, lang: String = "es"): List<Quote> {
        return soapService.mostrarSentencias(id, lang)
    }

    suspend fun getQuoteDetails(id: String, lang: String = "es"): Quote {
        return soapService.mostrarInformacionSentencia(id, lang)
    }

    /*PROVISIONAL FUNCTIONS*/
    suspend fun getDateDetailsCustom(id: Int, lang: String = "es"): Date? {
        return soapService.mostrarInformacionFechaCustom(id, lang)
    }

    /*suspend fun getDatesByCategoryCustom(category: String, lang: String = "es"): List<Date> {
        return soapService.mostrarFechasPorCategoriaCustom(category, lang)
    }*/
}