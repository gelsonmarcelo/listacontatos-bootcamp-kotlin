package com.everis.listadecontatos.feature.listacontatos.viewmodel

import com.everis.listadecontatos.application.ContatoApplication
import com.everis.listadecontatos.feature.listacontatos.model.ContatosVO
import com.everis.listadecontatos.feature.listacontatos.repository.ListaDeContatosRepository

class ListaDeContatosViewModel(
    var repository: ListaDeContatosRepository? = null
) {
    fun getListaDeContatos(
        busca: String,
        onSucesso: ((List<ContatosVO>) -> Unit),
        onError: ((Exception) -> Unit)
    ) {
        Thread {
            Thread.sleep(1500)
            repository?.requestListaDeContatos(
                busca,
                onSucesso = { lista -> /*Aqui teria alguma regra de restrição de exibição de contatos se existir*/
                    onSucesso(lista)
                },
                onError = { ex ->
                    onError(ex)
                }
            )
        }.start()
    }
}