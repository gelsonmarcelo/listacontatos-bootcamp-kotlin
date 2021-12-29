package com.everis.listadecontatos.feature.listacontatos.viewmodel

import com.everis.listadecontatos.application.ContatoApplication
import com.everis.listadecontatos.feature.listacontatos.model.ContatosVO

class ListaDeContatosViewModel {
    fun getListaDeContatos(
        busca: String,
        onSucesso: ((List<ContatosVO>)->Unit),
        onError: ((Exception)->Unit)
    ) {
        Thread {
            Thread.sleep(1500)
            var listaFiltrada: List<ContatosVO> = mutableListOf()
            try {
                listaFiltrada = ContatoApplication.instance.helperDB?.buscarContatos(busca) ?: mutableListOf()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            onSucesso(listaFiltrada)
        }.start()
    }
}