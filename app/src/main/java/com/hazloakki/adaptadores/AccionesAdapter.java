package com.hazloakki.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hazloakki.modelos.AccionesDto;
import com.hazloakki.modelos.AccionesItem;
import com.hazloakki.modelos.Footer;
import com.hazloakki.modelos.Header;
import com.hazloakki.modelos.RecyclerViewItem;
import com.hazloakki.R;
import java.util.List;


public class AccionesAdapter extends RecyclerView.Adapter {
    //Declare List of Recyclerview Items
    List<RecyclerViewItem> recyclerViewItems;
    //Header Item Type
    private static final int HEADER_ITEM = 0;
    //Footer Item Type
    private static final int FOOTER_ITEM = 1;
    ////Food Item Type
    private static final int FOOD_ITEM = 2;
    private Context mContext;
    private List<AccionesDto> listaAcciones;

    public AccionesAdapter(List<RecyclerViewItem> recyclerViewItems, Context mContext,List<AccionesDto> listaAcciones) {
        this.recyclerViewItems = recyclerViewItems;
        this.mContext = mContext;
        this.listaAcciones = listaAcciones;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row;
        //Check fot view Type inflate layout according to it
        if (viewType == HEADER_ITEM) {
            row = inflater.inflate(R.layout.custom_row_header, parent, false);
            return new HeaderHolder(row);
        } else if (viewType == FOOTER_ITEM) {
            row = inflater.inflate(R.layout.custom_row_footer, parent, false);
            return new FooterHolder(row);
        } else if (viewType == FOOD_ITEM) {
            row = inflater.inflate(R.layout.custom_row_food, parent, false);
            return new FoodItemHolder(row);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerViewItem recyclerViewItem = recyclerViewItems.get(position);
        //Check holder instance to populate data  according to it
        if (holder instanceof HeaderHolder) {
            HeaderHolder headerHolder = (HeaderHolder) holder;
            Header header = (Header) recyclerViewItem;
            //set data
            headerHolder.texViewHeaderText.setText(header.getHeaderText());
            headerHolder.textViewCategory.setText(header.getCategory());
            Glide.with(mContext).load(header.getImageUrl()).into(headerHolder.imageViewHeader);

        } else if (holder instanceof FooterHolder) {
            FooterHolder footerHolder = (FooterHolder) holder;
            Footer footer = (Footer) recyclerViewItem;
            //set data
            footerHolder.texViewQuote.setText(footer.getQuote());
            footerHolder.textViewAuthor.setText(footer.getAuthor());
            Glide.with(mContext).load(footer.getImageUrl()).into(footerHolder.imageViewFooter);

        } else if (holder instanceof FoodItemHolder) {
            FoodItemHolder foodItemHolder = (FoodItemHolder) holder;
            AccionesItem accionesItem = (AccionesItem) recyclerViewItem;
            //set data
            foodItemHolder.texViewFoodTitle.setText(accionesItem.getTitulo());
            foodItemHolder.texViewDescription.setText(accionesItem.getDescripcion());
           // foodItemHolder.textViewPrice.setText(accionesItem.getPrice());
            Glide.with(mContext).load(accionesItem.getUrlImagen()).into(foodItemHolder.imageViewFood);
            //check food item is hot or not to set visibility of hot text on image
           /* if (accionesItem.isHot())
                foodItemHolder.textViewIsHot.setVisibility(View.VISIBLE);
            else
                foodItemHolder.textViewIsHot.setVisibility(View.GONE);

                */

        }

    }

    @Override
    public int getItemViewType(int position) {
        //here we can set view type
        RecyclerViewItem recyclerViewItem = recyclerViewItems.get(position);
        //if its header then return header item
        if (recyclerViewItem instanceof Header)
            return HEADER_ITEM;
            //if its Footer then return Footer item
        else if (recyclerViewItem instanceof Footer)
            return FOOTER_ITEM;
        //if its AccionesItem then return Food item
        else if (recyclerViewItem instanceof AccionesItem)
            return FOOD_ITEM;
        else
            return super.getItemViewType(position);

    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }
    //Food item holder
    private class FoodItemHolder extends RecyclerView.ViewHolder {
        TextView texViewFoodTitle, texViewDescription, textViewPrice, textViewIsHot;
        ImageView imageViewFood;

        FoodItemHolder(View itemView) {
            super(itemView);
            texViewFoodTitle = itemView.findViewById(R.id.texViewTituloAccion);
            texViewDescription = itemView.findViewById(R.id.texViewDescripcionAccion);
            imageViewFood = itemView.findViewById(R.id.imagenAccion);
            //textViewPrice = itemView.findViewById(R.id.textViewNumeroOfertasPublicadas);

        }
    }
    //header holder
    private class HeaderHolder extends RecyclerView.ViewHolder {
        TextView texViewHeaderText, textViewCategory;
        ImageView imageViewHeader;

        HeaderHolder(View itemView) {
            super(itemView);
            texViewHeaderText = itemView.findViewById(R.id.texViewHeaderText);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            imageViewHeader = itemView.findViewById(R.id.imageViewHeader);
        }
    }
    //footer holder
    private class FooterHolder extends RecyclerView.ViewHolder {
        TextView texViewQuote, textViewAuthor;
        ImageView imageViewFooter;

        FooterHolder(View itemView) {
            super(itemView);
            texViewQuote = itemView.findViewById(R.id.texViewQuote);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            imageViewFooter = itemView.findViewById(R.id.imageViewFooter);
        }
    }

    public List<AccionesDto> getItems() {
        return listaAcciones;
    }

    public void setListaAcciones(List<AccionesDto> listaAcciones) {
        this.listaAcciones = listaAcciones;
    }
}
