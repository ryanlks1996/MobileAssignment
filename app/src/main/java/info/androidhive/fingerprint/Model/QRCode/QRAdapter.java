package info.androidhive.fingerprint.Model.QRCode;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.List;
import info.androidhive.fingerprint.R;
/**
 * Created by RyanLKS on 31/12/2017.
 */




public class QRAdapter extends ArrayAdapter<QRCode> {
    private final List<QRCode> list;
    Activity context;

    public QRAdapter(Activity context, int resource,  List<QRCode> list) {
        super(context, resource, list);
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater  = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.qr_image, parent, false);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageViewQR);

        QRCode imageFile;
        imageFile = getItem(position);



        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //decode base64 string to image
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(imageFile.getQRImage(), Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        imageView.setImageBitmap(decodedImage);
        return rowView;
    }

}