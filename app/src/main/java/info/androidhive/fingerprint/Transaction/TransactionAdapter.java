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
import java.util.Random;
import info.androidhive.fingerprint.LoginActivity;
import info.androidhive.fingerprint.Model.Customer.Customer;
import info.androidhive.fingerprint.Model.Transaction.Transaction;
import info.androidhive.fingerprint.R;


public class TransactionAdapter extends ArrayAdapter<Transaction> {


    public TransactionAdapter(Activity context, int resource, List<Transaction> list) {
        super(context, resource, list);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Transaction transaction = getItem(position);
        String cID;
        String idPrefix = "C";
        Random rand = new Random();
        int  n = rand.nextInt(4) + 1001;
        cID = idPrefix + String.format("%4d", n);
        LayoutInflater inflater  = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.transaction_history, parent, false);
        TextView tvTransactionDate, tvAmount, tvType,textViewCharges, textViewTarget;
        tvTransactionDate = (TextView)rowView.findViewById(R.id.tvTransactionDate);
        tvAmount = (TextView)rowView.findViewById(R.id.tvAmount);
        tvType = (TextView)rowView.findViewById(R.id.tvType);
        textViewCharges = (TextView)rowView.findViewById(R.id.textViewCharges);
        textViewTarget = (TextView)rowView.findViewById(R.id.textViewTarget);
        tvTransactionDate.setText(tvTransactionDate.getText() +" " + transaction.getTransactionDate());
        tvAmount.setText(tvAmount.getText() +" " + String.valueOf(transaction.getAmount()));
        tvType.setText(tvType.getText() + " "+ transaction.getType() );
        textViewCharges.setText(textViewCharges.getText()+ " "+ String.valueOf(transaction.getCharges()));
        if(transaction.getType().equals("Transferred")){
            textViewTarget.setText("To : " + cID);
        }else if(transaction.getType().equals("Received")){
            textViewTarget.setText("From : " + cID);
        }

        return rowView;
    }
}