package info.androidhive.fingerprint.Model.Transaction;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import info.androidhive.fingerprint.R;

/**
 * Created by Family on 27/12/2017.
 */

public class TransactionAdapter extends ArrayAdapter<Transaction>{
    private final List<Transaction> list;
    Activity context;

    public TransactionAdapter(Activity context, int resource, List<Transaction> list) {
        super(context, resource, list);
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Transaction transaction = getItem(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.content_home, parent, false);

        //

        return rowView;
    }
}
