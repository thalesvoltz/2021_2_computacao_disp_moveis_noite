package com.thales.apppersist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ListView lvProdutos;
    private ArrayAdapter adapter;
    private List<Produto> listaDeProdutos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         setContentView(R.layout.activity_main);

         lvProdutos = findViewById(R.id.lvProdutos);

         carregarProdutos();

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FormularioActivity.class);
                intent.putExtra("acao", "inserir");
                startActivity(intent);
            }
        });

        lvProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idProduto = listaDeProdutos.get( position ).getId();
                Intent intent = new Intent(MainActivity.this, FormularioActivity.class);
                intent.putExtra("acao", "editar");
                intent.putExtra("idProduto",idProduto);
                startActivity(intent);
            }
        });

        lvProdutos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                excluir(position);
                return true;
            }
        });
        }

    private void excluir(int posicao) {
        Produto prod = listaDeProdutos.get ( posicao );
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("Excluir...");
        alerta.setIcon(android.R.drawable.ic_delete);
        alerta.setMessage("Confirma a exclusão do produto" + prod.getNome() + "?");
        alerta.setNeutralButton("Cancelar", null);

        alerta.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ProdutoDAO.excluir(MainActivity.this, prod.getId());
                carregarProdutos();
            }
        });
        alerta.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        carregarProdutos();
    }

    private void carregarProdutos() {

        listaDeProdutos = ProdutoDAO.getProdutos(this);
        if(listaDeProdutos.size() == 0) {
            Produto fake = new Produto("Lista Vazia", "");
            listaDeProdutos.add(fake);
            lvProdutos.setEnabled(false);
        } else {
            lvProdutos.setEnabled(true);
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaDeProdutos);
        lvProdutos.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}