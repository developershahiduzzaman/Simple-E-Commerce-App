package com.example.ecmmerce;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecmmerce.Model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) { this.orderList = orderList; }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvOrderId.setText("Order #" + order.getId());
        holder.tvOrderTotal.setText("Total: ৳ " + order.getTotalAmount());
        holder.tvOrderStatus.setText(order.getStatus());
        holder.tvOrderDate.setText(order.getCreatedAt());
    }

    @Override
    public int getItemCount() { return orderList.size(); }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderTotal, tvOrderStatus, tvOrderDate;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
        }
    }
}