package com.example.ecmmerce;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecmmerce.Model.CartItem;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context;
    List<CartItem> cartList;
    ApiService apiService;
    String token;

    public CartAdapter(Context context, List<CartItem> cartList) {
        this.context = context;
        this.cartList = cartList;
        this.apiService = RetrofitClient.getApiService();

        // টোকেন "MyAppPrefs" থেকে নেওয়া হচ্ছে
        SharedPreferences sp = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        this.token = "Bearer " + sp.getString("token", "");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartList.get(position);

        holder.tvName.setText(item.getProduct().getName());
        holder.tvPrice.setText("৳ " + item.getProduct().getPrice());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        // Image Load
        Glide.with(context).load(item.getProduct().getImage()).placeholder(R.drawable.ic_launcher_background).into(holder.imgProduct);

        // --- PLUS BUTTON ---
        holder.btnPlus.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            updateQuantity(item.getId(), newQuantity, holder, position);
        });

        // --- MINUS BUTTON ---
        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                int newQuantity = item.getQuantity() - 1;
                updateQuantity(item.getId(), newQuantity, holder, position);
            } else {
                Toast.makeText(context, "Minimum quantity is 1", Toast.LENGTH_SHORT).show();
            }
        });

        // --- DELETE BUTTON ---
        holder.btnDelete.setOnClickListener(v -> {
            deleteItem(item.getId(), position);
        });
    }

    private void updateQuantity(int cartId, int quantity, ViewHolder holder, int position) {
        apiService.updateQuantity(token, cartId, quantity).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Update Local List
                    cartList.get(position).setQuantity(quantity);
                    holder.tvQuantity.setText(String.valueOf(quantity));

                    // Update Total Price in Activity
                    ((CartActivity) context).calculateTotalPrice();
                } else {
                    Toast.makeText(context, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteItem(int cartId, int position) {
        apiService.deleteCartItem(token, cartId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    cartList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, cartList.size());

                    // Update Total Price
                    ((CartActivity) context).calculateTotalPrice();

                    Toast.makeText(context, "Item Removed", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity;
        ImageView imgProduct, btnDelete;
        Button btnPlus, btnMinus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCartName);
            tvPrice = itemView.findViewById(R.id.tvCartPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            imgProduct = itemView.findViewById(R.id.imgCartProduct);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }
    }
}