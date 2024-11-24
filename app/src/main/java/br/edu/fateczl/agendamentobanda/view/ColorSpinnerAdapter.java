package br.edu.fateczl.agendamentobanda.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.edu.fateczl.agendamentobanda.R;
import br.edu.fateczl.agendamentobanda.model.Cor;

public class ColorSpinnerAdapter extends ArrayAdapter<Cor> {
    private final LayoutInflater layoutInflater;

    public ColorSpinnerAdapter(Context context, List<Cor> list) {
        super(context, 0, list);
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.color_spinner_bg, parent, false);
        return bindView(view, position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.color_spinner_item, parent, false);
        }
        return bindView(convertView, position);
    }

    private View bindView(View view, int position) {
        Cor cor = getItem(position);
        if (cor == null) {
            return view;
        }

        TextView tvColorNameEnsaio = view.findViewById(R.id.tvColorEnsaio);

        TextView tvColorNameBG = view.findViewById(R.id.tvColorNameBG);
        View vColorBlobBG = view.findViewById(R.id.vColorBlobBG);

        TextView tvColorNameItem = view.findViewById(R.id.tvColorNameItem);
        TextView tvColorHexItem = view.findViewById(R.id.tvColorHexItem);
        View vColorBlobItem = view.findViewById(R.id.vColorBlobItem);


        if (tvColorNameEnsaio != null) {
            tvColorNameEnsaio.setText((cor.getName()));
            tvColorNameEnsaio.setTextColor(Color.parseColor(cor.getContrastHexHash()));
        }

        if (tvColorNameBG != null) {
            tvColorNameBG.setText(cor.getName());
            tvColorNameBG.setTextColor(Color.parseColor(cor.getContrastHexHash()));
        }

        if (vColorBlobBG != null) {
            vColorBlobBG.getBackground().setTint(Color.parseColor(cor.getColorHash()));
        }

        if (tvColorNameItem != null) {
            tvColorNameItem.setText(cor.getName());
        }

        if (tvColorHexItem != null) {
            tvColorHexItem.setText(cor.getHex());
        }

        if (vColorBlobItem != null) {
            vColorBlobItem.getBackground().setTint(Color.parseColor(cor.getColorHash()));
        }


        return view;
    }
}