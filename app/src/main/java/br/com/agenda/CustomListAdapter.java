package br.com.agenda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<Contato> {

    private int resourceLayout;
    private Context mContext;

    public CustomListAdapter(Context context, int resource, ArrayList<Contato> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Contato c = getItem(position);

            TextView texto = (TextView) v.findViewById(R.id.item_list);
            texto.setText(c.toString2());

        return v;
    }

}