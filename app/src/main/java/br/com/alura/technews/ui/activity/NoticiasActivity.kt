package br.com.alura.technews.ui.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import br.com.alura.technews.R
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.ui.activity.extensions.transacaoFragment
import br.com.alura.technews.ui.fragment.ListaNoticiasFragment
import br.com.alura.technews.ui.fragment.VisualizaNoticiaFragment
import kotlinx.android.synthetic.main.activity_noticias.*


class NoticiasActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticias)
        if (savedInstanceState == null) {
            transacaoFragment {
                replace(R.id.activity_noticias_container_primario, ListaNoticiasFragment())
            }
        } else {
            supportFragmentManager.findFragmentByTag("visualizaNoticia")?.let {
                val argumentos = it.arguments
                val novoFragment = VisualizaNoticiaFragment()
                novoFragment.arguments = argumentos

                transacaoFragment {
                    remove(it)
                }
                supportFragmentManager.popBackStack()

                transacaoFragment {
                    val container = configuraContainerVisualizaNoticia()
                    replace(container, novoFragment, "visualizaNoticia")
                }
            }
            }
        }


    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
         when (fragment){
            is ListaNoticiasFragment -> {
                fragment.quandoNoticiaSelecionada = { noticia ->
                    abreVisualizadorNoticia(noticia)
                }
                fragment.quandoFabSalvaNoticiaClicado = this::abreFormularioModoCriacao
            }

            is VisualizaNoticiaFragment -> {
                fragment.quandoSelecionaMenuEdicao = {noticia ->
                    abreFormularioEdicao(noticia)
                }
                fragment.quandoFinaliza = {
                    supportFragmentManager.findFragmentByTag("visualizaNoticia")?.let {

                        transacaoFragment {
                            remove(it)
                        }
                        supportFragmentManager.popBackStack()
                    }
                    }
                }
            }
         }



     private fun abreFormularioModoCriacao() {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        startActivity(intent)
    }

     private fun abreVisualizadorNoticia(noticia: Noticia) {
         val fragment = VisualizaNoticiaFragment()
         val dados = Bundle()
         dados.putLong(NOTICIA_ID_CHAVE, noticia.id)
         fragment.arguments = dados
         transacaoFragment {
             val container = configuraContainerVisualizaNoticia()
             replace(container, fragment, "visualizaNoticia")
         }
     }

    private fun FragmentTransaction.configuraContainerVisualizaNoticia(): Int {
        if (activity_noticias_container_secundario != null)
           return  R.id.activity_noticias_container_secundario
            addToBackStack(null)
            return R.id.activity_noticias_container_primario
        }



    private fun abreFormularioEdicao(noticia: Noticia) {
        val intent = Intent(this, FormularioNoticiaActivity::class.java)
        intent.putExtra(NOTICIA_ID_CHAVE, noticia.id)
        startActivity(intent)
    }
}
