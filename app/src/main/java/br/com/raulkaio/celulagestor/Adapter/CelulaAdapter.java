package br.com.raulkaio.celulagestor.Adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import br.com.raulkaio.celulagestor.Classes.Celula;
import br.com.raulkaio.celulagestor.R;

/**
 * Created by raulk on 11/05/2018.
 */

public class CelulaAdapter extends RecyclerView.Adapter<CelulaAdapter.ViewHolder> {

    private List<Celula> mCelulaList;
    private Context context;
    private DatabaseReference referenciaDatabase;
    private List<Celula> celulas;
    private Celula todasCelulas;
    private FirebaseAuth autenticacao;

    public CelulaAdapter (List<Celula> l, Context c){
        context = c;
        mCelulaList = l;
    }

    @Override
    public CelulaAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_celulas, viewGroup, false);
        return new CelulaAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CelulaAdapter.ViewHolder holder, int position) {
        final Celula item = mCelulaList.get(position);
        celulas = new ArrayList<>();
        referenciaDatabase = FirebaseDatabase.getInstance().getReference();
        autenticacao = FirebaseAuth.getInstance();
        referenciaDatabase.child("Celula").orderByChild("nome").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                celulas.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    todasCelulas = postSnapshot.getValue(Celula.class);
                    celulas.add(todasCelulas);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.txtNomeCelula.setText(item.getNome());

        if(item.getFrequencia().toString().equals("Outros")){
            holder.txtFrequencia.setText("Não informada");
        }
        else if (item.getFrequencia().toString().equals("1")){
            holder.txtFrequencia.setText("Diária");
        }
        else{
            holder.txtFrequencia.setText("A cada "+item.getFrequencia()+" dias");
        }

        holder.txtObservacoes.setText(item.getObservacoes());
        holder.txtLetra.setText(item.getNome().substring(0, 1));
    }

    @Override
    public int getItemCount() {
        return mCelulaList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtNomeCelula, txtFrequencia, txtObservacoes, txtLetra;
        protected LinearLayout linearLayoutCelulas;

        public ViewHolder (View itemView){
            super (itemView);

            txtNomeCelula = (TextView) itemView.findViewById(R.id.txtNomeCelula);
            txtFrequencia = (TextView) itemView.findViewById(R.id.txtFrequencia);
            txtObservacoes = (TextView) itemView.findViewById(R.id.txtObservacoes);
            linearLayoutCelulas = (LinearLayout) itemView.findViewById(R.id.linearLayoutCelulas);
            txtLetra = (TextView) itemView.findViewById(R.id.icon_text);
        }

    }
}
