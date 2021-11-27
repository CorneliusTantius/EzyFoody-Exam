package com.corneliustantius.ezyfoody.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.corneliustantius.ezyfoody.R;
import com.corneliustantius.ezyfoody.databinding.ActivityCartBinding;
import com.corneliustantius.ezyfoody.helpers.DatabaseHelper;
import com.corneliustantius.ezyfoody.models.CartSelectionModel;

import java.util.List;

public class AdapterFinishItem extends RecyclerView.Adapter<AdapterFinishItem.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv1;
        public TextView tv2;
        public TextView tv3;
        public ViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.cart_name);
            tv2 = (TextView) itemView.findViewById(R.id.cart_qty);
            tv3 = (TextView) itemView.findViewById(R.id.cart_price);
        }
    }

    private List<CartSelectionModel> cartItems;
    private Context context;
    public AdapterFinishItem(List<CartSelectionModel> cartItems, Context context) {
        this.cartItems = cartItems;
        this.context = context;
    }

    @Override
    public AdapterFinishItem.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.holder_finishitem, parent, false);

        AdapterFinishItem.ViewHolder viewHolder = new AdapterFinishItem.ViewHolder(contactView);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(AdapterFinishItem.ViewHolder holder, int position) {
        CartSelectionModel contact = cartItems.get(position);

        Integer price = contact.getPrice();
        String sPrice = String.format("%,d", price);
        TextView textView = holder.tv1;
        String tv1Text = contact.getName() + "\nRp. " + sPrice;
        textView.setText(tv1Text);

        Integer qty = contact.getQty();
        TextView textView2 = holder.tv2;
        String tv2Text = "Quantity: " + qty.toString();
        textView2.setText(tv2Text);

        TextView textView3 = holder.tv3;
        Integer tot = qty*price;
        String sTot = "Sub Total: " +  String.format("%,d", tot);
        textView3.setText(sTot);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}
