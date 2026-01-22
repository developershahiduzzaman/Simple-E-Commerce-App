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

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecmmerce.Model.AddToCartRequest;
import com.example.ecmmerce.Model.Product;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;

    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvName.setText(product.getName());
        holder.tvPrice.setText("৳ " + product.getPrice());

        if (product.getCategory() != null) {
            holder.tvCategory.setText("Category: " + product.getCategory().getName());
        } else {
            holder.tvCategory.setText("Category: N/A");
        }

        // Load image with Glide
        if (product.getImage() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(product.getImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(R.drawable.ic_launcher_background);
        }

        // Add to Cart click
        holder.btnAddToCart.setOnClickListener(v -> {
            android.util.Log.d("CART_DEBUG", "Button Clicked for: " + product.getName());
            Context context = v.getContext();
            SharedPreferences sp = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);


            String savedToken = sp.getString("token", "");


            if (savedToken.isEmpty()) {
                Toast.makeText(context, "Please Login First!", Toast.LENGTH_SHORT).show();
                return;
            }

            String token = "Bearer " + savedToken;


            android.util.Log.d("CART_DEBUG", "Token: " + token);

            AddToCartRequest request = new AddToCartRequest(product.getId(), 1);
            ApiService apiService = RetrofitClient.getApiService();

            apiService.addToCart(token, request).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        Toast.makeText(context, product.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
                    } else {

                        try {

                            String errorBody = response.errorBody().string();
                            android.util.Log.e("CART_ERROR", "Code: " + response.code() + " Body: " + errorBody);
                            Toast.makeText(context, "Failed: " + response.code(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPrice, tvCategory;
        ImageView imgProduct;
        Button btnAddToCart;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
