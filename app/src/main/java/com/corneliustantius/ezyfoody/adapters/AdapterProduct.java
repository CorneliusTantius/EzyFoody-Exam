package com.corneliustantius.ezyfoody.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.corneliustantius.ezyfoody.OrderActivity;
import com.corneliustantius.ezyfoody.ProductsActivity;
import com.corneliustantius.ezyfoody.R;
import com.corneliustantius.ezyfoody.helpers.DatabaseHelper;
import com.corneliustantius.ezyfoody.models.CartItemModel;
import com.corneliustantius.ezyfoody.models.ProductModel;

import java.util.List;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv1;
        public TextView tv2;
        public Button btn;
        public ViewHolder(View itemView) {
            super(itemView);

            tv1 = (TextView) itemView.findViewById(R.id.product_name);
            tv2 = (TextView) itemView.findViewById(R.id.product_price);
            btn = (Button) itemView.findViewById(R.id.add_button);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemPos = getAdapterPosition();
                    ProductModel thisEntry = products.get(itemPos);
                    goToOrder(thisEntry.getId().toString(), thisEntry.getName());
                }
            });
        }
    }
    private List<ProductModel> products;
    private Context context;
    public AdapterProduct(List<ProductModel> products, Context context) {
        this.products = products;
        this.context = context;
    }
    public static String EXTRA_PID = "PRODUCT_ID";
    public static String EXTRA_PN = "PRODUCT_NAME";
    private void goToOrder(String id, String name){
        Intent intent = new Intent(context, OrderActivity.class);
        intent.putExtra(EXTRA_PID, id);
        intent.putExtra(EXTRA_PN, name);
        context.startActivity(intent);
    }
    @Override
    public AdapterProduct.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.holder_product, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(AdapterProduct.ViewHolder holder, int position) {
        ProductModel contact = products.get(position);
        TextView textView = holder.tv1;
        textView.setText(contact.getName());

        String sPrice = String.format("%,d", contact.getPrice());
        TextView textView2 = holder.tv2;
        textView2.setText("Rp. "+sPrice);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
