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

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.ViewHolder>{
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv1;
        public TextView tv2;
        public TextView tv3;
        public Button btn;

        public ViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.cart_name);
            tv2 = (TextView) itemView.findViewById(R.id.cart_qty);
            tv3 = (TextView) itemView.findViewById(R.id.cart_price);
            btn = (Button) itemView.findViewById(R.id.remove_btn);

        }
    }

    private List<CartSelectionModel> cartItems;
    private Context context;
    private ActivityCartBinding binding;
    private Toast toast;
    public AdapterCart(List<CartSelectionModel> cartItems, Context context, ActivityCartBinding binding) {
        this.cartItems = cartItems;
        this.context = context;
        this.binding = binding;
    }
    public void removeAt(int position) {
        cartItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cartItems.size());
    }

    @Override
    public AdapterCart.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.holder_cart, parent, false);

        AdapterCart.ViewHolder viewHolder = new AdapterCart.ViewHolder(contactView);
        viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPos = viewHolder.getAdapterPosition();
                CartSelectionModel thisItem = cartItems.get(itemPos);
                removeAt(itemPos);
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
                SQLiteDatabase db = databaseHelper.getReadableDatabase();
                databaseHelper.deleteCartItem(thisItem.getId());

                Integer total = 0;
                for(CartSelectionModel it:cartItems){
                    total += (it.getQty() * it.getPrice());
                }
                String sTotal = String.format("%,d", total);
                binding.grandTotal.setText(sTotal);
                if(toast != null)
                    toast.cancel();
                toast = Toast.makeText(v.getContext(), "Removed: " + thisItem.getName(), Toast.LENGTH_SHORT);
                toast.show();

            }
        });
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(AdapterCart.ViewHolder holder, int position) {
        CartSelectionModel contact = cartItems.get(position);

        Integer price = contact.getPrice();
        String sPrice = String.format("%,d", price);
        TextView textView = holder.tv1;
        String tv1Text = contact.getName() + " - Rp. " + sPrice;
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
