package com.vaishnavi.kandoi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.vaishnavi.kandoi.ui.theme.VaishnaviKandoi_COMP304_FinalExam_F24Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val stockDatabase = StockInfoDatabase.getDatabase(applicationContext)
        val stockRepository = StockRepository(stockDatabase.stockInfoDao())
        val stockModelFactory = StockViewModelFactory(stockRepository)
        val stockViewModel = ViewModelProvider(this, stockModelFactory)[StockViewModel::class.java]


        setContent {
            VaishnaviKandoi_COMP304_FinalExam_F24Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StockApp(stockViewModel, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockApp(stockViewModel: StockViewModel, modifier: Modifier) {
    val allStocks = stockViewModel.dbStockInfo
    var stockSymbol by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var stockQuote by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = stockSymbol,
            onValueChange = { stockSymbol = it },
            label = { Text("Stock Symbol") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = companyName,
            onValueChange = { companyName = it },
            label = { Text("Company Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = stockQuote,
            onValueChange = { stockQuote = it },
            label = { Text("Stock Quote") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                stockViewModel.insertStock(
                    StockInfo(
                        stockSymbol,
                        companyName,
                        stockQuote.toDoubleOrNull() ?: 0.0
                    )
                )
                stockSymbol = ""
                companyName = ""
                stockQuote = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Stock")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Available Stocks", style = MaterialTheme.typography.headlineMedium)
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(allStocks.size) { index ->
                var stock = allStocks[index]
                StockItem(stock = stock, onStockClick = { selectedStock ->
                    val intent = Intent(context, DisplayActivity::class.java).apply {
                        putExtra("stockSymbol", selectedStock.stockSymbol)
                    }
                    context.startActivity(intent)
                })
            }
        }
    }
}

@Composable
fun StockItem(stock: StockInfo, onStockClick: (StockInfo) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onStockClick(stock) },
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(text = stock.stockSymbol, style = MaterialTheme.typography.bodyMedium)
        }
    }
}