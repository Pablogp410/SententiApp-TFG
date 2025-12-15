package com.example.sententiapptfg.data

import com.example.sententiapptfg.data.models.Date
import com.example.sententiapptfg.data.models.Quote

class SententiAppRepository(private val soapService: SoapService) : ISententiAppRepository {

    override suspend fun getDates(lang: String): List<Date> {
        return soapService.mostrarFechas(lang)
    }

    override suspend fun getCategories(lang: String): List<String> {
        return soapService.mostrarCategorias(lang)
    }

    override suspend fun getDatesByCategory(category: String, lang: String): List<Date> {
        return soapService.mostrarFechasPorCategoria(category, lang)
    }

    override suspend fun getDateDetails(id: Int, lang: String): Date {
        return soapService.mostrarInformacionFecha(id, lang)
    }

    override suspend fun getQuotes(id: Int, lang: String): List<Quote> {
        return soapService.mostrarSentencias(id, lang)
    }

    override suspend fun getQuoteDetails(id: String, lang: String): Quote {
        return soapService.mostrarInformacionSentencia(id, lang)
    }

    /*PROVISIONAL FUNCTIONS*/
    /*suspend fun getDateDetailsCustom(id: Int, lang: String = "es"): Date? {
        return soapService.mostrarInformacionFechaCustom(id, lang)
    }*/

    /*suspend fun getDatesByCategoryCustom(category: String, lang: String = "es"): List<Date> {
        return soapService.mostrarFechasPorCategoriaCustom(category, lang)
    }*/
}