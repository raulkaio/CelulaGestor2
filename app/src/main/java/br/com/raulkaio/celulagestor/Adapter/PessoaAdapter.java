package br.com.raulkaio.celulagestor.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.raulkaio.celulagestor.Classes.Pessoa;
import br.com.raulkaio.celulagestor.R;

/**
 * Created by raulk on 11/05/2018.
 */

public class PessoaAdapter extends RecyclerView.Adapter<PessoaAdapter.ViewHolder> {

    private List<Pessoa> mPessoaList;
    private Context context;
    private DatabaseReference referenciaDatabase;
    private List<Pessoa> pessoas;
    private Pessoa todasPessoas;
    private FirebaseAuth autenticacao;

    public PessoaAdapter(List<Pessoa> l, Context c){
        context = c;
        mPessoaList = l;
    }

    @Override
    public PessoaAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_pessoas, viewGroup, false);
        return new PessoaAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PessoaAdapter.ViewHolder holder, int position) {
        final Pessoa item = mPessoaList.get(position);
        pessoas = new ArrayList<>();
        referenciaDatabase = FirebaseDatabase.getInstance().getReference();
        autenticacao = FirebaseAuth.getInstance();
        referenciaDatabase.child("Pessoa").orderByChild("nome").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pessoas.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    todasPessoas = postSnapshot.getValue(Pessoa.class);
                    pessoas.add(todasPessoas);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.txtNomePessoa.setText(item.getNome());
        holder.txtClassificacao.setText(item.getClassificacao());

        if(item.getEncontro().toString().equals("true")){
            holder.txtEncontro.setText("Sim");
        } else{
            holder.txtEncontro.setText("Não");
        }

        if(item.getBatismo().toString().equals("true")){
            holder.txtBatismo.setText("Sim");
        } else{
            holder.txtBatismo.setText("Não");
        }
        
        holder.txtLetra.setText(item.getNome().substring(0, 1));
    }

    @Override
    public int getItemCount() {
        return mPessoaList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtNomePessoa, txtClassificacao, txtEncontro, txtBatismo, txtLetra;
        protected LinearLayout linearLayoutPessoas;

        public ViewHolder (View itemView){
            super (itemView);

            txtNomePessoa = (TextView) itemView.findViewById(R.id.txtNomePessoa);
            txtClassificacao = (TextView) itemView.findViewById(R.id.txtClassificacao);
            txtEncontro = (TextView) itemView.findViewById(R.id.txtEncontro);
            txtBatismo = (TextView) itemView.findViewById(R.id.txtBatismo);
            linearLayoutPessoas = (LinearLayout) itemView.findViewById(R.id.linearLayoutPessoas);
            txtLetra = (TextView) itemView.findViewById(R.id.icon_text);
        }

    }
}
