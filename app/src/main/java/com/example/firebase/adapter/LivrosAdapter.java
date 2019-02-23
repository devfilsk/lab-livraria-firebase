package com.example.firebase.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebase.MainActivity;
import com.example.firebase.R;
import com.example.firebase.model.Livro;

import java.util.ArrayList;

public class LivrosAdapter extends RecyclerView.Adapter<LivrosAdapter.LivrosViewHolder> {

    private final ArrayList<Livro> livros;

    public LivrosAdapter(ArrayList<Livro> livros){
        this.livros = livros;
    }

    public void add(Livro livro) {
        livros.add(livro);
        notifyItemInserted(livros.size());
    }
    @NonNull
    @Override
    public LivrosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.livro_row, viewGroup, false);
        LivrosViewHolder pvh = new LivrosViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull LivrosViewHolder livrosViewHolder, int i) {
        livrosViewHolder.bind(livros.get(i));
    }

    @Override
    public int getItemCount() {
        return livros.size();
    }

    public class LivrosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View view;
        TextView titulo, autor, editora;
        ImageView prodImage;
        Livro livro;
        public LivrosViewHolder(View itemView){
            super(itemView);
            view = itemView;
            view.setOnClickListener(this);

            titulo = (TextView) view.findViewById(R.id.livro_title);
            autor = (TextView) view.findViewById(R.id.livro_autor);
            editora = (TextView) view.findViewById(R.id.livro_editora);

        }

        public void bind(Livro livro) {
            this.livro = livro;
            titulo.setText(livro.getTitulo());
            autor.setText(livro.getAutor());
            editora.setText(livro.getEditora());
        }

        public void bindForm(Livro livro){
            this.livro.setAutor(livro.getTitulo());
            this.livro.setEditora(livro.getEditora());
            this.livro.setAutor(livro.getAutor());
        }

        @Override
        public void onClick(View v) {
            MainActivity context = (MainActivity) v.getContext();
            context.edt_autor.setText(livro.getAutor());
            context.edt_editora.setText(livro.getEditora());
            context.edt_titulo.setText(livro.getTitulo());

            //Altera a visibilidade dos botões
            context.btn_add.setVisible(false);
            context.btnSave.setVisible(true);
            context.btn_undo.setVisible(true);

            bindForm(livro);

        }
    }


    public void removeItem(int position) {
        livros.remove(position);
        notifyItemRemoved(position);
    }

    public void addLivro(Livro item){
        livros.add(item);
    }

    public void restoreItem(Livro item, int position) {
        livros.add(position, item);
        notifyItemInserted(position);
    }

    public ArrayList<Livro> getData() {
        return livros;
    }

}
