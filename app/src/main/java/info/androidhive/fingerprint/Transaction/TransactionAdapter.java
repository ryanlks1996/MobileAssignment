package info.androidhive.fingerprint.Transaction;

/**
 * Created by RyanLKS on 30/12/2017.
 */
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

import info.androidhive.fingerprint.Model.Transaction.Transaction;
import info.androidhive.fingerprint.R;


public class TransactionAdapter extends ArrayAdapter<Transaction> {


    public TransactionAdapter(Activity context, int resource, List<Transaction> list) {
        super(context, resource, list);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Transaction transaction = getItem(position);

        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.transaction_history, parent, false);

        TextView tvTransactionDate, tvAmount, tvType;

        tvTransactionDate = (TextView)rowView.findViewById(R.id.tvTransactionDate);
        tvAmount = (TextView)rowView.findViewById(R.id.tvAmount);
        tvType = (TextView)rowView.findViewById(R.id.tvType);

        tvTransactionDate.setText(tvTransactionDate.getText() +" " + transaction.getTransactionDate());
        tvAmount.setText(tvAmount.getText() + String.valueOf(transaction.getAmount()));
        tvType.setText(tvType.getText() + " "+ transaction.getType() );
        return rowView;
    }
}