package com.doodeec.toby.views.shoppingitem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doodeec.toby.appstate.AppState;
import com.doodeec.toby.objectmodel.ShoppingListItem;
import com.doodeec.toby.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dusan.bartos
 */
public class ShoppingItemAdapter extends RecyclerView.Adapter<SLItemViewHolder> {

        List<ShoppingListItem> data;

        public ShoppingItemAdapter() {
            data = new ArrayList<>();
        }

        public void setData(List<ShoppingListItem> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        public void addItem(ShoppingListItem item) {
            this.data.add(item);
            notifyDataSetChanged();
        }

        @Override
        public SLItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater
                    .from(AppState.getAppContext())
                    .inflate(R.layout.list_item_holder, parent, false);
            return new SLItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SLItemViewHolder holder, int position) {
            ShoppingListItem item = data.get(position);

            holder.setName(item.getName());
            holder.setUnitAmount(item.getAmount(), item.getUnit());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
}
