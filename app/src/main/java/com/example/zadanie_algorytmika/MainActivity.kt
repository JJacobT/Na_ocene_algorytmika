package com.example.zadanie_algorytmika

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlin.math.pow
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val Sprawdz = findViewById<Button>(R.id.button_sprawdz)
        val PodajWzorzec = findViewById<EditText>(R.id.Wzorzec)
        val IloscZnakow = findViewById<EditText>(R.id.Il_znakow)
        val BruteForceCzas = findViewById<TextView>(R.id.BruteForceCzas)
        val KMPCzas = findViewById<TextView>(R.id.KMPCzas)
        val BmCzas = findViewById<TextView>(R.id.BmCzas)
        val RkCzas = findViewById<TextView>(R.id.RkCzas)

        Sprawdz.setOnClickListener {
            val tekst = wylosujTekst(IloscZnakow.text.toString().toInt())

            var czas = measureTimeMillis {
                bruteforce_txt(tekst, PodajWzorzec.text.toString())
            }
            BruteForceCzas.text = String.format("%s ms", czas)

            czas = measureTimeMillis {
                KMP(tekst, PodajWzorzec.text.toString())
            }
            KMPCzas.text = String.format("%s ms", czas)

            czas = measureTimeMillis {
                bm(tekst, PodajWzorzec.text.toString())
            }
            BmCzas.text = String.format("%s ms", czas)

            czas = measureTimeMillis {
                RK(tekst, PodajWzorzec.text.toString())
            }
            RkCzas.text = String.format("%s ms", czas)
        }
    }
    //bruteforce
    fun bruteforce_txt(tekst: String, wzorzec: String): List<Int> {
        val miejsce = mutableListOf<Int>()
        val n = tekst.length
        val m = wzorzec.length

        for (i in 0..n - m) {
            var j = 0
            while (j < m && tekst[i + j] == wzorzec[j]) {
                j++
            }
            if (j == m) {
                miejsce.add(i)
            }
        }

        return miejsce
    }

    //algorytm KMP
    fun KMP(tekst: String, wzorzec: String) {
        val miejsce = mutableListOf<Int>()
        val n = tekst.length
        val m = wzorzec.length
        val lps = buildLPS(wzorzec)
        var i = 0
        var j = 0
        while (j < n) {
            if (wzorzec[i] == tekst[j]) {
                i++
                j++
                if (i == m) {
                    miejsce.add(j - m)
                    i = lps[i - 1]
                }
            } else if (i > 0) {
                i = lps[i - 1]
            } else {
                j++
            }
        }
    }

    // tworzenie prefikso-sufiksów
    private fun buildLPS(wzorzec: String): IntArray {
        val m = wzorzec.length
        val lps = IntArray(m)
        var len = 0
        var i = 1
        while (i < m) {
            if (wzorzec[i] == wzorzec[len]) {
                len++
                lps[i] = len
                i++
            } else if (len > 0) {
                len = lps[len - 1]
            } else {
                lps[i] = 0
                i++
            }
        }
        return lps
    }

    // algorytm B-M
    fun bm(tekst: String, wzorzec: String): Int {
        val n = tekst.length
        val m = wzorzec.length

        val skip = IntArray(256) { m }

        for (i in 0 until m - 1) {
            skip[wzorzec[i].toInt()] = m - i - 1
        }

        var i = m - 1

        while (i < n) {
            var j = m - 1
            while (tekst[i] == wzorzec[j]) {
                if (j == 0) {
                    return i
                }
                i--
                j--
            }
            i += maxOf(skip[tekst[i].toInt()], m - j)
        }
        return -1
    }

    //algorytm R-K
    fun RK(tekst: String, wzorzec: String) {
        val prime = 101
        val m = wzorzec.length
        val n = tekst.length
        val results = mutableListOf<Int>()
        val pHash = wzorzec.hashCode()

        var tHash = tekst.substring(0, m)
            .hashCode()
        if (tHash == pHash && tekst.substring(0, m) == wzorzec) {
            results.add(0)
        }

        val power = prime.toDouble().pow(m - 1).toInt()

        for (i in 1..n - m) {
            tHash = prime * (tHash - tekst[i - 1].hashCode() * power) + tekst[i + m - 1].hashCode()

            if (tHash == pHash && tekst.substring(i, i + m) == wzorzec) {
                results.add(i)
            }
        }
    }


    fun wylosujTekst(ilosc: Int): String {
        val charPool: List<Char> =
            ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..ilosc)
            .map {
                Random.nextInt(
                    0,
                    charPool.size
                )
            }
            .map(charPool::get)
            .joinToString("")
    }
}





