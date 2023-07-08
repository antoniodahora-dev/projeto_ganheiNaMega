package co.tiagoaguiar.ganheinamega

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import java.util.*
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    //vamos pre iniciar a variavel
    private lateinit var presf:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContentView(R.layout.activity_main)

        // buscar os objetos e ter referencia deles da activity
        val editText: EditText = findViewById(R.id.edit_number)
        val txtResult: TextView = findViewById(R.id.txt_result)
        val btnGenerate: Button = findViewById(R.id.btn_generate)
        val txtInformation: TextView = findViewById(R.id.txt_information)

        //ira salvar as informações no próprio smartphone (banco de dados)
        presf = getSharedPreferences("dbRifa", Context.MODE_PRIVATE)
        val result = presf.getString("result", null)
//         if (result != null) {
//             txtResult.text = "Última aposta: $result"
//
//         }

        //desta forma podemos utiliza a mesma lógia do if
        result?.let {
        txtResult.text =  "Os últimos números Sorteados:" + "\n" +
                    "$it"
        }


        txtInformation.text = "A Aposta é válida por 10 dias." + "\n" +
                 "Após o vencimento do Prazo a Empresa se " +
                "inseta de qualquer obrigatoriedade de realizar o pagamento do Prêmio."

        //melhor opção para aplicar onclick - para escutar o click de input do usuaŕio
        btnGenerate.setOnClickListener {
            val text = editText.text.toString()
            numberGenerator(text, txtResult)

        }

    }

    //verifica a quantidade de números digitados
    //verifica se atende a condiçao
    //exibe o resultado do sorteio gerado (textView)
    private fun numberGenerator(text: String, txtResult: TextView) {

        // otimizando o código
        if (text.isEmpty()) {
            Toast.makeText(this, "Informe um número de 6 a 15", Toast.LENGTH_LONG).show()
            return
        }

        val qtd = text.toInt()
        if (qtd < 6 || qtd > 15) {
            Toast.makeText(this, "Informe um número de 6 a 15", Toast.LENGTH_LONG).show()
            return
        }

        // aqui o código deu sucesso
        val numbers = mutableSetOf<Int>()
        val random = Random()

        //loop de verificaçao -- loop infinito até que a condicao seja atendida
        while (true) {
            val number = random.nextInt(25)
            numbers.add(number + 1)

            if (numbers.size == qtd) {

                //metodo para esconder o teclado apos clicar no Button
                val service = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                service.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

               // Toast.makeText(this, "Foram Sorteada $text Dezenas! \n " +
                  //      " Até o Próximo Sorteio", Toast.LENGTH_LONG).show()
                break
            }

        }


        // exibir a informaçao dos números sorteados na tela para o usuário
        txtResult.text = numbers.joinToString(" - ")

        //alerte Dialog - Exibe dezenas sorteadas
        AlertDialog.Builder(this)
            .setTitle("Foram Sorteada $text Dezenas! ")
            .setMessage(txtResult.text.toString() + "\n\n"
                    + "Boa Sorte!")
            .setPositiveButton(android.R.string.ok)
            {dialog, which ->}
            .create()
            .show()

//        //objeto para salvar as informações
//        val editor = presf.edit()
//        editor.putString("result", txtResult.text.toString())
//        editor.apply() // funciona de forma assincrona - Mas não notifica ao usuário
//       // Log.i("Teste", "informação salva com sucesso")

        //forma mais curta de escreve a informação acima
        presf.edit().apply {
            putString("result", txtResult.text.toString())
            apply()
        }


}
}