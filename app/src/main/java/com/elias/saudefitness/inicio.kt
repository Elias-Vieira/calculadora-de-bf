package com.elias.saudefitness

import android.content.Context
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.elias.saudefitness.databinding.ActivityInicioBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class inicio : AppCompatActivity() {

    private lateinit var binding: ActivityInicioBinding
    private lateinit var intersticial: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this)
        carregarAds()

        // chamada de anuncio admob //
        val adRequest = AdRequest.Builder().build()
        binding.adbanner.loadAd(adRequest)

        val verde = ContextCompat.getColor(this, R.color.verde)
        val branco = ContextCompat.getColor(this, R.color.white)
        var id_navegacao = 0

        binding.iconeHomem.setOnClickListener {

            id_navegacao = 1
            binding.iconeHomem.setImageResource(R.drawable.homem)
            binding.iconeMulher.setImageResource(R.drawable.mulher)
            binding.textHomem.setTextColor(verde)
            binding.textMulher.setTextColor(branco)
            binding.textQuadril.setText("")
            binding.cxQuadril.setHint("XX")
            binding.cxQuadril.isEnabled = false
            zerar()
        }

        binding.iconeMulher.setOnClickListener {

            id_navegacao = 0
            binding.iconeMulher.setImageResource(R.drawable.mulherselec)
            binding.iconeHomem.setImageResource(R.drawable.homemselec)
            binding.textMulher.setTextColor(verde)
            binding.textHomem.setTextColor(branco)
            binding.textQuadril.setText("Quadril em cm")
            binding.cxQuadril.setHint("0")
            binding.cxQuadril.isEnabled = true
            zerar()
        }

        binding.btZerar.setOnClickListener {
            zerar()
            intersticial!!.show(this)
            carregarAds()
        }

        binding.btCalcular.setOnClickListener {

            val altura = binding.cxAutura.text.toString()
            val cintura = binding.cxCintura.text.toString()
            val pescoco = binding.cxPescoco.text.toString()
            val quadril = binding.cxQuadril.text.toString()
            val formatacao = DecimalFormat("#.##")

            if (id_navegacao == 1){

                if (altura.isEmpty() || cintura.isEmpty() || pescoco.isEmpty()){

                    Toast.makeText(this, "Por favor, complete os dados!", Toast.LENGTH_SHORT).show()

                }else{

                    var soma = 85.79 * Math.log10(cintura.toDouble() - pescoco.toDouble()) - 62.56 * Math.log10(altura.toDouble()) + 12.76
                    var soma_formatada = formatacao.format(soma)
                    soma_formatada += "%"
                    binding.porcentagem.setText(soma_formatada)
                    intersticial!!.show(this)
                    carregarAds()

                    }

            }else{

                if (altura.isEmpty() || cintura.isEmpty() || pescoco.isEmpty() || quadril.isEmpty()){

                    Toast.makeText(this, "Por favor, complete os dados!", Toast.LENGTH_SHORT).show()

                } else{

                        var soma2 = 135.10 * Math.log10(quadril.toDouble() + cintura.toDouble() - pescoco.toDouble()) - 97.93 * Math.log10(altura.toDouble()) - 46.65
                        var soma_formatada2 = formatacao.format(soma2)
                        soma_formatada2 += "%"
                        binding.porcentagem.setText(soma_formatada2)
                        intersticial!!.show(this)
                        carregarAds()
                    }



            }

        }

    }

    private fun carregarAds() {

        var adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
        InterstitialAd.load(this, getString(R.string.id_intesticial), adRequest,
            object : InterstitialAdLoadCallback(){

                override fun onAdLoaded(interstitialAd: InterstitialAd) {

                    intersticial = interstitialAd

                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    carregarAds()
                }

            })

    }

    fun zerar (){

        binding.porcentagem.text = "0%"
        binding.cxAutura.text.clear()
        binding.cxCintura.text.clear()
        binding.cxPescoco.text.clear()
        binding.cxQuadril.text.clear()
        fecharTeclado()
    }

    private fun fecharTeclado() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}