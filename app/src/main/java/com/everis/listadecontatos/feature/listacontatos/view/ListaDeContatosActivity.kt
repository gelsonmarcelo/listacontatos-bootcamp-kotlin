package com.everis.listadecontatos.feature.listacontatos.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.everis.listadecontatos.R
import com.everis.listadecontatos.application.ContatoApplication
import com.everis.listadecontatos.bases.BaseActivity
import com.everis.listadecontatos.feature.contato.ContatoActivity
import com.everis.listadecontatos.feature.listacontatos.adapter.ContatoAdapter
import com.everis.listadecontatos.feature.listacontatos.model.ContatosVO
import com.everis.listadecontatos.feature.listacontatos.viewmodel.ListaDeContatosViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception


class ListaDeContatosActivity : BaseActivity() {

    private var adapter: ContatoAdapter? = null

    var viewModel: ListaDeContatosViewModel? =
        null //Deixa nullable para retirar a obrigação de a view precisar de um model, assim facilita fazer mock e testes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolBar(toolBar, "Lista de contatos", false)
        setupListView()
        setupOnClicks()
    }

    private fun setupOnClicks() {
        fab.setOnClickListener { onClickAdd() }
        ivBuscar.setOnClickListener { onClickBuscar() }
    }

    private fun setupListView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        onClickBuscar()
    }

    private fun onClickAdd() {
        val intent = Intent(this, ContatoActivity::class.java)
        startActivity(intent)
    }

    private fun onClickItemRecyclerView(index: Int) {
        val intent = Intent(this, ContatoActivity::class.java)
        intent.putExtra("index", index)
        startActivity(intent)
    }

    private fun onClickBuscar() {
        val busca = etBuscar.text.toString()
        viewModel?.getListaDeContatos(
            busca,
            onSucesso = { listaFiltrada ->
                runOnUiThread {
                    adapter = ContatoAdapter(this, listaFiltrada) { onClickItemRecyclerView(it) }
                    recyclerView.adapter = adapter
                    progress.visibility = View.GONE
                    Toast.makeText(this, "Buscando por $busca", Toast.LENGTH_SHORT).show()
                }
            },
            onError = { ex ->
                runOnUiThread {
                    //Aqui no onError faz as tratativas de erro de view ou lógica
                    AlertDialog.Builder(this)
                        .setTitle("Atenção")
                        .setMessage("Não foi possível completar sua solicitação!")
                        .setPositiveButton("OK") { alert, i ->
                            alert.dismiss()
                        }
                }
            }
        )
        progress.visibility = View.VISIBLE
        Thread {
            Thread.sleep(1500)
            var listaFiltrada: List<ContatosVO> = mutableListOf()
            try {
                listaFiltrada = ContatoApplication.instance.helperDB?.buscarContatos(busca) ?: mutableListOf()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }.start()
    }

}
