package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebase.adapter.LivrosAdapter;
import com.example.firebase.model.Livro;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public MenuItem btn_add, btnSave, btn_undo;
    public EditText edt_titulo, edt_autor, edt_editora;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private LivrosAdapter myAdapter;
    private ArrayList<Livro> livros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        livros = new ArrayList<Livro>();
        edt_autor = (EditText) findViewById(R.id.add_autor);
        edt_editora = (EditText) findViewById(R.id.add_editora);
        edt_titulo = (EditText) findViewById(R.id.add_titulo);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        inicializarFirebase();

        listarDados();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);

        databaseReference = firebaseDatabase.getReference();
    }

    private void listarDados() {
        databaseReference.child("Livros").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot objSnapshot: dataSnapshot.getChildren()){
                    Livro livro = objSnapshot.getValue(Livro.class);
                    livros.add(livro);
                }

                myAdapter = new LivrosAdapter(livros);
                recyclerView.setAdapter(myAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        btnSave = menu.findItem(R.id.btn_save_alterar);
        btn_add = menu.findItem(R.id.add_btn_cadastrar);
        btn_undo = menu.findItem(R.id.btn_undo_toolbar);
        btnSave.setVisible(false);
        btn_undo.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Livro liv;
        switch (id){
            case R.id.add_btn_cadastrar:
                liv = new Livro(
                        edt_titulo.getText().toString(),
                        edt_autor.getText().toString(),
                        edt_editora.getText().toString());
                liv.setId(UUID.randomUUID().toString());

                databaseReference.child("Livros").child(liv.getId()).setValue(liv);
                Toast.makeText(MainActivity.this, "Created", Toast.LENGTH_SHORT).show();

                break;
            case R.id.btn_save_alterar:
                liv = new Livro(
                        edt_titulo.getText().toString(),
                        edt_autor.getText().toString(),
                        edt_editora.getText().toString());
                liv.setId(UUID.randomUUID().toString());
                databaseReference.child("Livros").child(liv.getId()).setValue(liv);
                btn_undo.setVisible(false);
                btn_add.setVisible(true);
                btnSave.setVisible(false);
                break;
            case R.id.btn_undo_toolbar:
                clearFields();
                btn_undo.setVisible(false);
                btn_add.setVisible(true);
                btnSave.setVisible(false);
            break;
            case R.id.btn_delete:
//                databaseReference.child("Livros").child(liv.getId())
                break;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clearFields(){
        edt_titulo.setText("");
        edt_editora.setText("");
        edt_autor.setText("");
    }

}
