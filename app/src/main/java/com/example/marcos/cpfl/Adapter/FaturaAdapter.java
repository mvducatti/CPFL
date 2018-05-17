package com.example.marcos.cpfl.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marcos.cpfl.Models.Faturas;
import com.example.marcos.cpfl.R;

import java.util.ArrayList;

public class FaturaAdapter extends RecyclerView.Adapter<FaturaAdapter.FaturaAdapterViewHolder> {

    private Context context;
    private ArrayList<Faturas> faturasArrayList;
    //creating a listener for the interface with the same name as our interface
    private OnItemClickListener xListener;

    //making our own custom onClickListener
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnClickListener(OnItemClickListener listener){
        //used to simulate the onItemClick of a listview
        xListener = listener;
    }

    public FaturaAdapter(Context context, ArrayList<Faturas> faturasArrayList) {
        this.context = context;
        this.faturasArrayList = faturasArrayList;
    }

    @Override
    public FaturaAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.faturas_list, parent, false);
        return new FaturaAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FaturaAdapterViewHolder holder, int position) {
        Faturas currentFatura = faturasArrayList.get(position);

        String month = String.valueOf(currentFatura.getMonth());
        String year = String.valueOf(currentFatura.getYear());
        String consume = String.valueOf(currentFatura.getConsume());

        holder.txtMonth.setText(month + " /");
        holder.txtYear.setText(year);
        holder.txtConsume.setText("R$" + consume);

    }

    @Override
    public int getItemCount() {
        return faturasArrayList.size();
    }

    public class FaturaAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView txtMonth;
        public TextView txtYear;
        public TextView txtConsume;

        public FaturaAdapterViewHolder(View itemView) {
            super(itemView);
            txtMonth = itemView.findViewById(R.id.txtMonth);
            txtYear = itemView.findViewById(R.id.txtYear);
            txtConsume = itemView.findViewById(R.id.txtConsume);

            //creating the option for when we click on something to work
            //-------------------------WARNING--------------------------//
            /*THIS IS A TRICK TO USE onItemClick AS YOU WOULD USE IN A LIST VIEW
             * IT'S BETTER TO USE HERE (ACCORDING TO THE TUTORIAL THAN USING ON THE *-onBindViewHolder-*)*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (xListener != null) {
                        int position = getAdapterPosition();
                        //noposition to make sure the position is still valid
                        if (position != RecyclerView.NO_POSITION){
                            //onItemclick is the method that we created on the interface
                            xListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
